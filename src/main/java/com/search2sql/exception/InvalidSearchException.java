package com.search2sql.exception;

public class InvalidSearchException extends Exception {

    private final long errorCode;
    private final String exception;

    public InvalidSearchException(long errorCode, String exception) {
        this.errorCode = errorCode;
        this.exception = exception;
    }

    public long getErrorCode() {
        return errorCode;
    }

    public String getException() {
        return exception;
    }
}
