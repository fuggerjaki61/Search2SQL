package com.search2sql.exception;

/**
 * <code>InvalidSearchException</code> is thrown if any error occurred that affects the end-user.
 * <br /><br />
 * This exception should only be thrown by {@link com.search2sql.parser.Parser Parsers} or
 * {@link com.search2sql.interpreter.Interpreter Interpreters}. This exception should be caught and shown to the end-user.
 * The end-user should see a summary and/or detailed description of the problem and <i>how to solve it</i>.<br />
 * This exception provides an unique error code and an (not yet i18n) string that is provided by the library.
 * <br /><br />
 * <i>In case the end-user can't fix the exception</i> the {@link IllegalUseException} should be used.
 *
 * @author fuggerjaki61
 * @since 1.0-echo
 */
public class InvalidSearchException extends Exception {

    private final long errorCode;

    // TODO i18n
    private final String exception = null;

    /**
     * This is a basic constructor initializing values.
     *
     * @param errorCode unique error code
     */
    public InvalidSearchException(long errorCode) {
        this.errorCode = errorCode;
    }

    /**
     * This method returns the error code.
     *
     * @return long containing the error code
     */
    public long getErrorCode() {
        return errorCode;
    }

    /**
     * <b>NOT WORKING</b><br />
     * The i18n string isn't supported yet.
     *
     * @return null
     */
    public String getException() {
        return null;
    }
}
