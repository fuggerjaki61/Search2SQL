package com.search2sql.impl.interpreter.util;

import com.search2sql.exception.InvalidParserException;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import org.reflections.Reflections;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/**
 * This class contains static utility methods for the usage in an <code>Interpreter</code>.
 * This class may be used by all implementation and just should help reduce code size and complexity.
 */
public class ParserUtils {

    private static final Set<Class<?>> annotatedClasses;

    static {
        annotatedClasses = new Reflections().getTypesAnnotatedWith(SearchParser.class);
    }

    /**
     * This method is one of the most important utility methods in this class. This method is for loading all allowed
     * parser for the specified table. This method will return a list of parsers that can be used by the interpreter to parse
     * the split sub-queries.
     *
     * @param config meta-information about the table
     * @return all valid parser specified by the TableConfig
     */
    public static HashMap<String, Parser> loadParser(TableConfig config) {
        // all valid parser will be added here
        HashMap<String, Parser> parsers = new HashMap<>();

        // finds parsers that were allowed in the TableConfig
        HashMap<String, String> allowedParsers = findAllowedParser(config);

        // iterates over all classes that were found
        for (Class<?> clazz : annotatedClasses) {
            // gets annotation from current class
            SearchParser annotation = clazz.getAnnotation(SearchParser.class);

            // checks if class is a child of Parser or any of its subclasses
            if (Parser.class.isAssignableFrom(clazz)) {
                // checks if parser is allowed
                if (allowedParsers.containsValue(annotation.value())) {
                    // parser is allowed so it will be instantiate and added to the list

                    try {
                        Constructor<?> clazzConstructor = clazz.getConstructor();

                        for (Map.Entry<String, String> entry : allowedParsers.entrySet()) {
                            if (entry.getValue().equals(annotation.value())) {
                                parsers.put(entry.getKey() ,(Parser) clazzConstructor.newInstance());
                            }
                        }
                    } catch (Exception e) {
                        throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation" +
                                "but it couldn't been initialized. Please check the attached error message.", clazz.getName()), e);
                    }
                }
            } else {
                // class defines @SearchParser annotation but it does not extend the Parser class
                throw new InvalidParserException(String.format("The class '%s' defines the @SearchParser annotation but does" +
                        "not extend the 'com.searchflow.parser.Parser' or any subclasses of it.", clazz.getName()));
            }
        }

        // return valid parsers
        return parsers;
    }

    /**
     * This method takes the TableConfig and iterates over every Column of it. It collects all column names and the
     * associated parser id in a map.
     *
     * @param config meta-information about the table
     * @return all allowed parser with column names for this table
     */
    public static HashMap<String, String> findAllowedParser(TableConfig config) {
        // instantiate new HashMap
        HashMap<String, String> allowed = new HashMap<>();

        // iterates over every column
        for (Column column : config.getColumns()) {
            // adds name and allowed id to it
            allowed.put(column.getName(), column.getParserId());
        }

        // all allowed parsers with column names
        return allowed;
    }
}
