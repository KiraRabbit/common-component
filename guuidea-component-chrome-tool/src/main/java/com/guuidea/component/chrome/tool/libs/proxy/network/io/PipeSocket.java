
package com.guuidea.component.chrome.tool.libs.proxy.network.io;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.List;
import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.guuidea.component.chrome.tool.common.Constant;
import com.guuidea.component.chrome.tool.libs.proxy.cryto.CryptFactory;
import com.guuidea.component.chrome.tool.libs.proxy.cryto.ICrypt;
import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.IProxy;
import com.guuidea.component.chrome.tool.libs.proxy.network.proxy.ProxyFactory;
import com.guuidea.component.chrome.tool.libs.proxy.utils.Util;
import com.guuidea.component.chrome.tool.model.ProxyAccount;


/**
 * Pipe local and remote sockets while server is running under blocking mode.
 */
public class PipeSocket implements Runnable {
    private Logger logger = LoggerFactory.getLogger(PipeSocket.class.getName());

    private final int TIMEOUT = 10000; // 10s
    private ByteArrayOutputStream _remoteOutStream;
    private ByteArrayOutputStream _localOutStream;
    private Socket _remote;
    private Socket _local;
    private IProxy _proxy;
    private ICrypt _crypt;
    private boolean _isClosed;
    private Executor _executor;
    private ProxyAccount proxyAccount;

    public PipeSocket(Executor executor, Socket socket, ProxyAccount proxyAccount) throws IOException {
        _executor = executor;
        _local = socket;
        _local.setSoTimeout(TIMEOUT);
        this.proxyAccount = proxyAccount;
        _crypt = CryptFactory.get(this.proxyAccount.getMethod(), this.proxyAccount.getPassword());
        _proxy = ProxyFactory.get(this.proxyAccount.getProxyType());
        _remoteOutStream = new ByteArrayOutputStream(Constant.BUFFER_SIZE);
        _localOutStream = new ByteArrayOutputStream(Constant.BUFFER_SIZE);
    }

    @Override
    public void run() {
        try {
            _remote = initRemote(proxyAccount);
            _remote.setSoTimeout(TIMEOUT);
        } catch (IOException e) {
            close();
            logger.warn(Util.getErrorMessage(e));
            return;
        }

        _executor.execute(getLocalWorker());
        _executor.execute(getRemoteWorker());
    }

    private Socket initRemote(ProxyAccount proxyAccount) throws IOException {
        return new Socket(proxyAccount.getRemoteIpAddress(), proxyAccount.getRemotePort());
    }

    private Runnable getLocalWorker() {
        return new Runnable() {
            @Override
            public void run() {
                BufferedInputStream stream;
                byte[] dataBuffer = new byte[Constant.BUFFER_SIZE];
                byte[] buffer;
                int readCount;
                List<byte[]> sendData = null;

                // prepare local stream
                try {
                    stream = new BufferedInputStream(_local.getInputStream());
                } catch (IOException e) {
                    logger.info(e.toString());
                    return;
                }

                // start to process data from local socket
                while (true) {
                    try {
                         // read data
                        readCount = stream.read(dataBuffer);
                        if (readCount == -1) {
                            throw new IOException("Local socket closed (Read)!");
                        }

                        // initialize proxy
                        if (!_proxy.isReady()) {
                            byte[] temp;
                            buffer = new byte[readCount];

                            // dup dataBuffer to use in later
                            System.arraycopy(dataBuffer, 0, buffer, 0, readCount);

                            temp = _proxy.getResponse(buffer);
                            if ((temp != null) && (!_sendLocal(temp, temp.length))) {
                                throw new IOException("Local socket closed (proxy-Write)!");
                            }
                            // packet for remote socket
                            sendData = _proxy.getRemoteResponse(buffer);
                            if (sendData == null) {
                                continue;
                            }
                            logger.info("Connected to: " + Util.getRequestedHostInfo(sendData.get(0)));
                        }
                        else {
                            sendData.clear();
                            sendData.add(dataBuffer);
                        }

                        for (byte[] bytes : sendData) {
                            // send data to remote socket
                            if (!sendRemote(bytes, bytes.length)) {
                                throw new IOException("Remote socket closed (Write)!");
                            }
                        }
                    } catch (SocketTimeoutException e) {
                        continue;
                    } catch (IOException e) {
                        logger.error(e.toString());
                        break;
                    }
                }
                close();
                logger.info(String.format("localWorker exit, Local=%s, Remote=%s", _local, _remote));
            }
        };
    }

    private Runnable getRemoteWorker() {
        return new Runnable() {
            @Override
            public void run() {
                BufferedInputStream stream;
                int readCount;
                byte[] dataBuffer = new byte[4096];

                // prepare remote stream
                try {
                    //stream = _remote.getInputStream();
                    stream = new BufferedInputStream (_remote.getInputStream());
                } catch (IOException e) {
                    logger.info(e.toString());
                    return;
                }

                // start to process data from remote socket
                while (true) {
                    try {
                        readCount = stream.read(dataBuffer);
                        if (readCount == -1) {
                            throw new IOException("Remote socket closed (Read)!");
                        }

                        // send data to local socket
                        if (!sendLocal(dataBuffer, readCount)) {
                            throw new IOException("Local socket closed (Write)!");
                        }
                    } catch (SocketTimeoutException e) {
                        continue;
                    } catch (IOException e) {
                        logger.warn(e.toString());
                        break;
                    }

                }
                close();
                logger.warn(String.format("remoteWorker exit, Local=%s, Remote=%s", _local, _remote));
            }
        };
    }

    public void close() {
        if (_isClosed) {
            return;
        }
        _isClosed = true;

        try {
            _local.shutdownInput();
            _local.shutdownOutput();
            _local.close();
        } catch (IOException e) {
            logger.warn("PipeSocket failed to close local socket (I/O exception)!");
        }
        try {
            if (_remote != null) {
                _remote.shutdownInput();
                _remote.shutdownOutput();
                _remote.close();
            }
        } catch (IOException e) {
            logger.warn("PipeSocket failed to close remote socket (I/O exception)!");
        }
    }

    private boolean sendRemote(byte[] data, int length) {
        _crypt.encrypt(data, length, _remoteOutStream);
        byte[] sendData = _remoteOutStream.toByteArray();

        return _sendRemote(sendData, sendData.length);
    }

    private boolean _sendRemote(byte[] data, int length) {
        try {
            if (length > 0) {
                OutputStream outStream = _remote.getOutputStream();
                outStream.write(data, 0, length);
            }
            else {
                logger.info("Nothing to sendRemote!\n");
            }
        } catch (IOException e) {
            logger.info(Util.getErrorMessage(e));
            return false;
        }

        return true;
    }

    private boolean sendLocal(byte[] data, int length) {
        _crypt.decrypt(data, length, _localOutStream);
        byte[] sendData = _localOutStream.toByteArray();

        return _sendLocal(sendData, sendData.length);
    }

    private boolean _sendLocal(byte[] data, int length) {
        try {
            OutputStream outStream = _local.getOutputStream();
            outStream.write(data, 0, length);
        } catch (IOException e) {
            logger.info(Util.getErrorMessage(e));
            return false;
        }
        return true;
    }
}
