package com.search2sql.exception;

/**
 * This exception should only be thrown by an <code>Interpreter</code>.
 * This exception shows the programmer that a specific parser isn't valid.
 */
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
