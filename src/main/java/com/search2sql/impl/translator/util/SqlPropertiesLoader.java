package com.search2sql.impl.translator.util;

import java.io.IOException;
import java.util.Properties;

/**
 * This is an utility class used by the {@link com.search2sql.impl.translator.FileTranslator FileTranslator} for loading
 * any <code>.properties</code> file.
 * <br /><br />
 * This class provides a static method {@link SqlPropertiesLoader#getProperties(String)} to load the properties out of any
 * <code>.properties</code> file located in the <code>resources</code> of your application.
 */
public class SqlPropertiesLoader {

    /**
     * This method loads data from a specified <code>.properties</code> file into a {@link Properties} object.<br />
     * The file must be located in the resources of the application that loads it. This may be a custom (user) application
     * or this library (<code>resources/sql.properties</code>).
     *
     * @param name name of the file
     * @return loaded Properties object
     */
    public static Properties getProperties(String name) {
        // initialize properties
        Properties properties = new Properties();

        try {
            //loads properties from file located in the resources
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(name));
        } catch (IOException e) {
            // TODO exception handling
            e.printStackTrace();
        }

        // return loaded properties
        return properties;
    }
}
