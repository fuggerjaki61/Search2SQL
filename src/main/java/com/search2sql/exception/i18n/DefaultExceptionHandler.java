package com.search2sql.exception.i18n;

import com.search2sql.exception.IllegalUseException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class DefaultExceptionHandler extends ExceptionHandler {

    private final LinkedList<Locale> languages;

    public DefaultExceptionHandler() {
        this(Locale.getDefault(), Locale.ENGLISH);
    }

    public DefaultExceptionHandler(Locale locale) {
        this(Collections.singletonList(locale));
    }

    public DefaultExceptionHandler(Locale locale, Locale defaultLocale) {
        this(Arrays.asList(locale, defaultLocale));
    }

    public DefaultExceptionHandler(Locale... locales) {
        this(Arrays.asList(locales));
    }

    public DefaultExceptionHandler(List<Locale> languages) {
        this.languages = new LinkedList<>(languages);
    }

    @Override
    public String handle(int errorCode) {
        File libraryFolder = new File(Thread.currentThread().getContextClassLoader().getResource("i18n").getFile());

        for (Locale locale : languages) {
            File[] files = libraryFolder.listFiles((dir, name) -> name.matches("(?i).*" + locale.getLanguage() + "\\.properties$"));

            if (files != null && files.length != 0) {
                if (files.length > 1) {
                    throw new IllegalUseException(String.format("There are more than one file for the locale %s.", locale.getLanguage()));
                }

                Properties properties = new Properties();

                try {
                    properties.load(new FileInputStream(files[0]));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return properties.getProperty(Integer.toHexString(errorCode));
            }
        }

        return null;
    }
}
