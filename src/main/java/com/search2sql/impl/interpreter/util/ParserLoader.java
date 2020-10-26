package com.search2sql.impl.interpreter.util;

import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import org.reflections8.Reflections;

import java.util.Set;

/**
 * Utility class that loads the classes that define the {@link SearchParser} annotation.
 */
public class ParserLoader {

    private static Set<Class<?>> parsers;

    static {
        Reflections reflections = new Reflections();

        parsers = reflections.getTypesAnnotatedWith(SearchParser.class);
    }

    public static Parser getParser(String id) {
        

        return null;
    }
}
