package com.search2sql.exception;

public class InvalidParserException extends RuntimeException {

    public InvalidParserException() {
    }

    public InvalidParserException(String message) {
        super(message);
    }

    public InvalidParserException(String message, Throwable cause) {
        super(message, cause);
    }
}
