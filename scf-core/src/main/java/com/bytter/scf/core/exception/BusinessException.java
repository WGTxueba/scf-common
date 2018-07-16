package com.bytter.scf.core.exception;

/**
 * 业务异常类
 * created by zhoul
 */
public class BusinessException extends RuntimeException implements BytterException {

    public BusinessException() {
        super();
    }


    public BusinessException(String message) {
        super(message);
    }


    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
