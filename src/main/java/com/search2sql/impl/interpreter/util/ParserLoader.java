package com.search2sql.impl.interpreter.util;

import com.search2sql.exception.IllegalUseException;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import org.reflections8.Reflections;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            throw new IllegalUseException("Parser id cannot be null.");
        }

        Class<?> parser = null;
        String name = null;
        String params = null;

        Matcher matcher = Pattern.compile("(?<id>[^#]+)(?:#|)(?<params>.*)").matcher(id);

        if (matcher.find()) {
            name = matcher.group("id");
            params = matcher.group("params");
        }

        for (Class<?> clazz : parsers) {
            if (clazz.getSimpleName().replaceAll("(?i)parser", "").equalsIgnoreCase(name)) {
                parser = clazz;
                break;
            }
        }

        Object[] param = resolveParameters(params);
        Class<?>[] paramTypes = new Class<?>[param.length];

        for (int i = 0; i < param.length; i++) {
            paramTypes[i] = param[i].getClass();
        }

        try {
            Constructor<?> constructor = parser.getConstructor(paramTypes);

            return (Parser) constructor.newInstance(param);
        } catch (Exception e) {
            e.printStackTrace();
        }

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















