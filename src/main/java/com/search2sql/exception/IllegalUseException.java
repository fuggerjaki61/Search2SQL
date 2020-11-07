package com.search2sql.exception;

public class IllegalUseException extends RuntimeException {

    public IllegalUseException(String message) {
        super(message);
    }

    public IllegalUseException(String message, Throwable cause) {
        super(message, cause);
    }
}
