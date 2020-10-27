package com.search2sql.impl.interpreter.util;

import com.search2sql.exception.InvalidParserIdException;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import org.reflections8.Reflections;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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
        if (id == null) {
            throw new InvalidParserIdException("Id cannot be null.", new NullPointerException());
        }

        Class<?> parser;
        String name = null;
        String params = null;

        Matcher matcher = Pattern.compile("(?<id>[^#]+)(?:#|)(?<params>.*)").matcher(id);

        if (matcher.find()) {
            name = matcher.group("id");
            params = matcher.group("params");
        }

        for (Class<?> clazz : parsers) {
            if (clazz.getSimpleName().equalsIgnoreCase(name)) {
                parser = clazz;
                break;
            }
        }

        System.out.println(Arrays.deepToString(resolveParameters(params)));

        return null;
    }

    private static Object[] resolveParameters(String params) {
        ArrayList<Object> objects = new ArrayList<>();
        StringBuilder param = new StringBuilder();
        boolean inString = false;

        for (char c : params.toCharArray()) {
            if (c != ',' || inString) {
                param.append(c);

                if (c == '\'') {
                    if (inString) {
                        objects.add(resolveParameter(param.toString().trim()));

                        param = new StringBuilder();
                    }

                    inString = !inString;
                }
            } else if (param.length() > 0) {
                objects.add(resolveParameter(param.toString().trim()));

                param = new StringBuilder();
            }
        }

        if (param.length() > 0) {
            objects.add(resolveParameter(param.toString().trim()));
        }

        return objects.toArray();
    }

    private static Object resolveParameter(String param) {
        System.out.println(param);

        if (param.matches("'[^']*'")) {
            return param.substring(1, param.length() - 1);
        } else if (param.equals("true")) {
            return true;
        } else if (param.equals("false")) {
            return false;
        } else if (param.matches("(?:\\+|-|)\\d+")) {
            return Integer.parseInt(param);
        } else {
            throw new UnsupportedOperationException();
        }
    }
}















