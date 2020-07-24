package com.guuidea.component.docker;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.concurrent.TimeUnit;

import org.apache.commons.lang3.StringUtils;

import com.guuidea.component.docker.exception.GuuideaDockerApiException;
import com.guuidea.component.docker.interceptor.AuthenticationInterceptor;
import com.guuidea.component.utils.IOUtils;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

/**
 * APi客户端工厂类.
 *
 * @author chendou
 * @date 2020/2/17
 * @since 1.0
 */
public class ApiClientFactory {

    private static final Converter.Factory CONVERTER_FACTORY = JacksonConverterFactory.create();

    /**
     * 创建共享客户端
     *
     * @return
     */
    public static OkHttpClient createClient(String p12, String password) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder().pingInterval(30, TimeUnit.SECONDS);
        if (StringUtils.isNotBlank(p12)) {
            InputStream stream = IOUtils.toStream(p12);
            clientBuilder.socketFactory(getSSLSocketFactory(stream, password));
        }
        return clientBuilder.build();
    }

    public static SSLSocketFactory getSSLSocketFactory(InputStream stream, String password) {
        SSLContext sslContext = null;
        try {
            sslContext = SSLContext.getInstance("TLS");
            KeyManagerFactory keyManagerFactory = getKeyManagerFactory(stream, password);
            TrustManager trustManager = getTrustManager();
            sslContext.init(keyManagerFactory.getKeyManagers(), new TrustManager[] {trustManager}, null);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        }
        return sslContext != null ? sslContext.getSocketFactory() : null;
    }

    /**
     * server端
     *
     * @return
     */
    public static TrustManager getTrustManager() {
        TrustManager tm = new X509TrustManager() {
            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }

            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            }
        };
        return tm;
    }


    /**
     * client端
     *
     * @return
     */
    public static KeyManagerFactory getKeyManagerFactory(InputStream stream, String password) {
        KeyStore keyStore = null;
        KeyManagerFactory keyManagerFactory = null;
        try {
            keyStore = KeyStore.getInstance("PKCS12");
            keyStore.load(stream, password.toCharArray());
            keyManagerFactory = KeyManagerFactory.getInstance("SunX509");
            keyManagerFactory.init(keyStore, password.toCharArray());
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (UnrecoverableKeyException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return keyManagerFactory;
    }

    /**
     * 创建服务类
     *
     * @param serviceClass
     * @param serverHost
     * @param <S>
     * @return
     */
    public static <S> S createService(Class<S> serviceClass, String serverHost, String p12, String password) {
        Retrofit.Builder retrofitBuilder =
                new Retrofit.Builder().baseUrl(serverHost).addConverterFactory(CONVERTER_FACTORY);
        OkHttpClient client = createClient(p12, password);
        AuthenticationInterceptor interceptor = new AuthenticationInterceptor();
        OkHttpClient adaptedClient = client.newBuilder().addInterceptor(interceptor).build();
        retrofitBuilder.client(adaptedClient);
        Retrofit retrofit = retrofitBuilder.build();
        return retrofit.create(serviceClass);
    }

    /**
     * Execute a REST call and block until the response is received.
     *
     * @param call
     * @param <T>
     * @return
     */
    public static <T> T executeSync(Call<T> call) {
        try {
            Response<T> response = call.execute();
            if (response.isSuccessful()) {
                return response.body();
            }
            throw new GuuideaDockerApiException(response.errorBody().string());
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuuideaDockerApiException(e.getMessage());
        }
    }

    /**
     * Execute a REST call and block until the response is received.
     *
     * @param call
     * @return
     */
    public static String executeStringSync(Call<ResponseBody> call) {
        try {
            Response<ResponseBody> response = call.execute();
            if (response.isSuccessful()) {
                if (response.body() != null) {
                    return response.body().string();
                }
                return null;
            }
            String errMsg = "调用失败";
            if (response.errorBody() != null) {
                errMsg = response.errorBody().string();
            }
            throw new GuuideaDockerApiException(errMsg);
        } catch (IOException e) {
            e.printStackTrace();
            throw new GuuideaDockerApiException(e.getMessage());
        }
    }

}
