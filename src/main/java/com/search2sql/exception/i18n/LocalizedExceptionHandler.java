package com.search2sql.exception.i18n;

import com.search2sql.exception.IllegalUseException;

import java.io.IOException;
import java.util.*;

/**
 * The <code>LocalizedExceptionHandler</code> is an implementation of the {@link ExceptionHandler}. This implementation
 * targets to get message with a localized value.
 * <br><br>
 * This <code>ExceptionHandler</code> tries to load the message with a given locale. If this locale doesn't exist it takes
 * the next one as a fallback. If the fallback doesn't exists, it takes the next fallback and so on.
 * <br><br>
 * This <code>ExceptionHandler</code> loads the messages from the <code>.properties</code> files located under
 * <code>src/main/resources/i18n/exceptions_{locale}.properties</code>.
 * <br><br>
 * The most confusing about this class might be the different constructors. This list should explain the usage of each:
 * <ul>
 *     <li>
 *         {@link LocalizedExceptionHandler#LocalizedExceptionHandler()}  (no args)<br>
 *         This constructor sets the system's locale as the default and sets english as the only fallback
 *     </li>
 *     <li>
 *         {@link LocalizedExceptionHandler#LocalizedExceptionHandler(Locale)}<br>
 *         This constructor just sets the locale as the default without any fallbacks.
 *     </li>
 *     <li>
 *         {@link LocalizedExceptionHandler#LocalizedExceptionHandler(Locale, Locale)}<br>
 *         This constructor sets the first locale as the default and the next one as the only fallback.
 *     </li>
 *     <li>
 *         {@link LocalizedExceptionHandler#LocalizedExceptionHandler(Locale...)}<br>
 *         This constructor sets the first locale as the default and the following ones as fallbacks. The fallbacks are
 *         used in the order they were added.
 *     </li>
 * </ul>
 */
public class LocalizedExceptionHandler extends ExceptionHandler {

    private int currentLocale;
    private final LinkedList<Locale> locales;

    /**
     * This constructor sets the system's locale as the default and sets english as the only fallback.
     */
    public LocalizedExceptionHandler() {
        this(Locale.getDefault(), Locale.ENGLISH);
    }

    /**
     * This constructor just sets the locale as the default without any fallbacks.
     *
     * @param locale default locale
     */
    public LocalizedExceptionHandler(Locale locale) {
        this(Collections.singletonList(locale));
    }

    /**
     * This constructor sets the first locale as the default and the next one as the only fallback.
     *
     * @param locale default locale
     * @param fallback fallback locale
     */
    public LocalizedExceptionHandler(Locale locale, Locale fallback) {
        this(Arrays.asList(locale, fallback));
    }

    /**
     * This constructor sets the first locale as the default and the following ones as fallbacks. The fallbacks are
     * used in the order they were added.
     * @param locales default locale and all fallbacks
     */
    public LocalizedExceptionHandler(Locale... locales) {
        this(Arrays.asList(locales));
    }

    private LocalizedExceptionHandler(List<Locale> languages) {
        this.locales = new LinkedList<>(languages);
    }

    /**
     * This method tries to load the message for the given error code in the desired language. If the message wasn't translated
     * for the desired language, a fallback is used (if specified). If the message still wasn't translated for the fallback,
     * the next fallback is used (if specified) and then so on. If the message wasn't translated in any specified language, the error code
     * <code>0</code> message is loaded in the desired language. If it wasn't added in the desired language, the fallbacks are
     * used. Finally, if nothing was found <code>null</code> is returned.
     *
     * @param errorCode code that identifies the message
     * @return localized message for the given code
     */
    @Override
    public String handle(int errorCode) {
        // reset the current locale
        currentLocale = 0;

        // try to load the message for the given error code and the desired language
        String msg = handle(errorCode, locales.getFirst());

        // check if the message is translated in any language
        if (msg == null) {
            // it isn't so the message for the error code 0 is loaded in the desired language
            msg = handle(0, locales.getFirst());

            // if the message is still null, null will be returned because nothing else can be done
        }

        // return the loaded message
        return msg;
    }

    private String handle(int errorCode, Locale locale) {
        // initialize the properties
        Properties properties = new Properties();

        try {
            // construct the path
            String location = "i18n/exceptions_" + locale.getLanguage() + ".properties";

            // try to load the file for the generated path
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(location));
        } catch (IOException e) {
            // throw an IllegalUseException
            throw new IllegalUseException(String.format("The locale '%s' was specified even though no file with the" +
                    "name 'exceptions_%s.properties' exists in the 'i18n' folder in the resources.", locale.getLanguage(), locale.getLanguage()), e);
        }

        // get the message for the error code
        String msg = properties.getProperty(String.valueOf(errorCode));

        // already get the next fallback
        Locale next = nextLocale();

        if (msg == null && next != null) {
            // the message is still null and the next fallback is used
            return handle(errorCode, next);
        } else {
            /*
             * the message still might be null but so is the next locale
             * nothing can be done anymore so must null be returned if no value was found
             */
            return msg;
        }
    }

    private Locale nextLocale() {
        // increment to get the next locale
        currentLocale++;

        // check if there is a next locale
        if (currentLocale >= locales.size()) {
            // the end of the list was reached and null is returned
            return null;
        }

        // there is still a next locale so it is returned
        return locales.get(currentLocale);
    }
}
