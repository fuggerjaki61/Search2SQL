package com.search2sql.impl.translator.util;

import java.io.IOException;
import java.util.Properties;

/**
 * This is an utility class used by the {@link com.search2sql.impl.translator.FileTranslator FileTranslator} for loading
 * the <code>sql.properties</code> file.<br />
 * The only purpose of this class is to load the <code>sql.properties</code> file from the projects resources.
 */
public class SqlPropertiesLoader {

    private final Properties properties;

    /**
     * Basic Constructor that initializes and loads the values.
     */
    public SqlPropertiesLoader() {
        // initialize properties
        properties = new Properties();

        try {
            //loads properties from file
            properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("sql.properties"));
        } catch (IOException e) {
            // TODO exception handling
            e.printStackTrace();
        }
    }

    /**
     * This method returns the loaded Properties from the <code>sql.properties</code> file.
     *
     * @return fully loaded properties
     */
    public Properties getProperties() {
        return properties;
    }
}
