package com.search2sql.exception;

/**
 * <code>IllegalUseException</code> is thrown if any error occurred that affect the user of this library. This exception
 * extends the {@link RuntimeException}.
 * <br /><br />
 * This exception may be thrown by any class and in any phase. This exception should not reach the end-user - displaying the
 * stacktrace/specific cause should be avoided, instead display a generic error message (e.g. in web development return a 500 code).
 * <br /><br />
 * <i>In case the end-user can fix the exception</i> the {@link InvalidSearchException} should be used.
 *
 * @author fuggerjaki61
 * @since 1.0-echo
 */
public class IllegalUseException extends RuntimeException {

    /**
     * This is a basic constructor inherited from the {@link RuntimeException}.
     * <br /><br />
     * This constructor should be used if the error was caused by this library.
     * <br /><br />
     * <b>See Also</b><br />
     * If you want to forward a cause, use {@link IllegalUseException#IllegalUseException(String, Exception)}.
     *
     * @param message describing the error (and how to fix it)
     */
    public IllegalUseException(String message) {
        super(message);
    }

    /**
     * This is a basic constructor inherited from the {@link RuntimeException}.
     * <br /><br />
     * This constructor should be used if the error was caused by something else that threw an exception initially. In this
     * case this class should function as wrapper for it.
     * <br /><br />
     * <b>See Also</b><br />
     * If there is no direct cause, use {@link IllegalUseException#IllegalUseException(String)}.
     *
     * @param message describing the error (and how to fix it)
     * @param cause initial Exception thrown
     */
    public IllegalUseException(String message, Exception cause) {
        super(message, cause);
    }
}
