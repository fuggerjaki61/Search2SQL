package com.search2sql.exception.i18n;

import com.search2sql.exception.InvalidSearchException;

/**
 * The <code>ExceptionHandler</code> is the main part of the I18N utility for exceptions. This class is a template for own
 * implementations or implementations provided by the library.
 * <br><br>
 * The goal of this class is to get the error message corresponding the given error code of the exception in one of this
 * three forms:<br>
 * <ul>
 *     <li>int</li>
 *     <li>hex (<code>String</code>)</li>
 *     <li><code>InvalidSearchException</code></li>
 * </ul>
 * The handler will return the localized string for the specific error code. The localization is the task of each implementation
 * and thus is handled differently by each implementation.
 * <br><br>
 * <b>Note</b><br>
 * When creating an own <code>ExceptionHandler</code> only the {@link ExceptionHandler#handle(int)} has to be overridden.
 *
 * @author fuggerjaki61
 * @since 1.1-bravo
 */
public abstract class ExceptionHandler {

    /**
     * This is the main part of the <code>ExceptionHandler</code>. Only this method should vary from implementation to
     * implementation.+
     * <br><br>
     * The task of this method to get the message that corresponds to the given error code. Localization is only optional but
     * normally an implementation should handle this.
     * <br><br>
     * <b>Note</b><br>
     * If there is no message for the given code, the message with the code <code>0</code> <i>"No Error Description Added!"</i>
     * should be returned.<br>
     * A list with all error codes can be found in the documentation.
     *
     * @param errorCode code that identifies the message
     * @return message for the given code
     */
    public abstract String handle(int errorCode);

    /**
     * See {@link ExceptionHandler#handle(int)} for more specific information.<br>
     * This method is already implemented and shouldn't be overridden.
     * <br><br>
     * This method overloads the <code>handle(int)</code> method and takes the error code as a hexadecimal number saved
     * in string. The method parses the hex value, gets the message by the integer value and returns it.
     *
     * @param hex code that identifies the message in hexadecimal form
     * @return message for the given code
     */
    public String handle(String hex) {
        // parse the hexadecimal string and return the message
        return handle((int) Long.parseLong(hex, 16));
    }

    /**
     * See {@link ExceptionHandler#handle(int)} for more specific information.<br>
     * This method is already implemented and shouldn't be overridden.
     * <br><br>
     * This method overloads the <code>handle(int)</code> method and takes the thrown <code>InvalidSearchException</code>
     * as a parameter. The method gets the error code from the exception, gets the message by the error code and returns
     * the message.
     *
     * @param exception thrown exception which message is loaded
     * @return message for the given code
     */
    public String handle(InvalidSearchException exception) {
        return handle(exception.getErrorCode());
    }
}
