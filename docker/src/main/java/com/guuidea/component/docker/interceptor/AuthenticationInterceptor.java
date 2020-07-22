package com.guuidea.component.docker.interceptor;

import java.io.IOException;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 构建校验参数拦截器
 *
 * @Author: hzchendou
 * @Date: 2020-02-17 11:22
 * @since: 1.0
 * @Copyright (C), 杭州古点科技有限公司
 */
public class AuthenticationInterceptor implements Interceptor {

    private final String uuid;

    /**
     * 构造函数
     *
     */
    public AuthenticationInterceptor() {
        uuid = UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 拦截请求构造校验参数
     *
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request origin = chain.request();
        Request.Builder authRequestBuilder = origin.newBuilder();
        String apiPath = origin.url().uri().getPath();
//        authRequestBuilder.addHeader(RequestParamConstants.SEQ_NUM_NAME, seqNum);
        Request authRequest = authRequestBuilder.build();
        return chain.proceed(authRequest);
    }
}
