
package com.guuidea.component.chrome.tool.libs.proxy.network;


import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.common.Constant;
import com.guuidea.component.chrome.tool.libs.proxy.cryto.CryptFactory;
import com.guuidea.component.chrome.tool.libs.proxy.network.io.PipeSocket;
import com.guuidea.component.chrome.tool.libs.proxy.utils.Util;
import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * Blocking local server for shadowsocks
 */
public class LocalServer implements IServer {
    private Logger logger = LoggerFactory.getLogger(LocalServer.class);
    private ProxyAccount proxyAccount;
    private ServerSocket _serverSocket;
    private Executor _executor;
    private List<PipeSocket> _pipes;

    public LocalServer(ProxyAccount proxyAccount) throws IOException, InvalidAlgorithmParameterException {
        if (!CryptFactory.isCipherExisted(proxyAccount.getMethod())) {
            throw new InvalidAlgorithmParameterException(proxyAccount.getMethod());
        }
        this.proxyAccount = proxyAccount;
        _serverSocket = new ServerSocket(proxyAccount.getLocalPort(), 128);
        _executor = Executors.newCachedThreadPool();
        _pipes = new ArrayList<>();

        // print server info
        logger.info("Shadowsocks-Java v" + Constant.VERSION);
        logger.info(proxyAccount.getProxyType() + " Proxy Server starts at port: " + proxyAccount.getLocalPort());
    }

    @Override
    public void run() {
        while (true) {
            try {
                Socket localSocket = _serverSocket.accept();
                PipeSocket pipe = new PipeSocket(_executor, localSocket, proxyAccount);
                _pipes.add(pipe);
                _executor.execute(pipe);
            } catch (IOException e) {
                logger.warn(Util.getErrorMessage(e));
            }
        }
    }

    public void close() {
        try {
            for (PipeSocket p : _pipes) {
                p.close();
            }
            _pipes.clear();
            _serverSocket.close();
        } catch (IOException e) {
            logger.warn(Util.getErrorMessage(e));
        }
    }

}
