package com.guuidea.component.docker.exception;

/**
 * Docker APi调用异常信息.
 *
 * @author hzchendou
 * @date 2020/2/17
 * @since 1.0
 */
public class GuuideaDockerApiException extends RuntimeException {

    /**
     * 构造函数
     *
     * @param message
     */
    public GuuideaDockerApiException(String message) {
        super(message);
    }
}
