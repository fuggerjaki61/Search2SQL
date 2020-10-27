package com.search2sql.exception;

public class InvalidParserIdException extends RuntimeException {

    public InvalidParserIdException() {
    }

    public InvalidParserIdException(String message) {
        super(message);
    }

    public InvalidParserIdException(String message, Throwable cause) {
        super(message, cause);
    }
}
