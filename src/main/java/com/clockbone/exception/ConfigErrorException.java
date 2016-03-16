package com.clockbone.exception;

/**
 * Created by qinjun on 2016/3/15.
 */
public class ConfigErrorException extends RuntimeException {
    public ConfigErrorException() {
        super();
    }

    public ConfigErrorException(String message) {
        super(message);
    }

    public ConfigErrorException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigErrorException(Throwable cause) {
        super(cause);
    }

    protected ConfigErrorException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
