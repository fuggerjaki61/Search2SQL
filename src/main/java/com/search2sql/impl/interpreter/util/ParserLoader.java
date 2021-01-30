package com.search2sql.impl.interpreter.util;

import com.search2sql.exception.IllegalUseException;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import org.reflections8.Reflections;
import org.reflections8.scanners.FieldAnnotationsScanner;
import org.reflections8.scanners.SubTypesScanner;
import org.reflections8.scanners.TypeAnnotationsScanner;

import java.io.*;
import java.lang.reflect.Constructor;
import java.util.*;

public class ParserLoader {

    private static Map<String, Class<?>> parsers;

    public static void initialize() {
        initialize(false);
    }

    public static void initialize(boolean forceUpdate) {
        String path = Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResource("")).getPath() + "persisted";
        File file = new File(path);

        if (file.exists() && !forceUpdate) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream(file));

                Object o = in.readObject();

                parsers = Collections.unmodifiableMap((Map<String, Class<?>>) o);
            } catch (IOException | ClassNotFoundException e) {
                throw new IllegalUseException(String.format("An error occurred while trying to deserialize the persisted " +
                        "parser from the file '%s'.", file.getAbsolutePath()), e);
            } catch (ClassCastException e) {
                initialize(true);
            }
        } else {
            Reflections reflections = new Reflections(
                    new TypeAnnotationsScanner(),
                    new SubTypesScanner(),
                    new FieldAnnotationsScanner());

            Set<Class<?>> classes = reflections.getTypesAnnotatedWith(SearchParser.class);

            Map<String, Class<?>> temp = new HashMap<>();

            for (Class<?> parser : classes) {
                SearchParser annotation = parser.getAnnotation(SearchParser.class);

                if (!annotation.value().isEmpty()) {
                    if (annotation.value().trim().matches("^\\S+$")) {
                        if (Parser.class.isAssignableFrom(parser)) {
                            if (!temp.containsKey(annotation.value())) {
                                temp.put(annotation.value(), parser);
                            } else {
                                throw new IllegalUseException(String.format("'%s' and '%s' specify the same id '%s'. You can't have duplicate parser ids.",
                                        temp.get(annotation.value()).getName(), parser.getName(), annotation.value()));
                            }
                        } else {
                            throw new IllegalUseException(String.format("The parser '%s' defines the @SearchParser annotation " +
                                    "but doesn't inherit the 'com.parser.Parser' class.", parser.getName()));
                        }
                    } else {
                        throw new IllegalUseException(String.format("The parser '%s' defines the id '%s' that contains a whitespace. That's invalid!",
                                parser.getName(), annotation.value()));
                    }
                } else {
                    throw new IllegalUseException(String.format("The parser '%s' defines an empty string as its id. That's invalid!",
                            parser.getName()));
                }
            }

            parsers = Collections.unmodifiableMap(temp);

            try {
                ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(file));

                out.writeObject(parsers);

                out.flush();

                out.close();
            } catch (IOException e) {
                throw new IllegalUseException(String.format("An error occurred while persisting the set of parsers" +
                        "in the file '%s'.", file.getAbsolutePath()), e);
            }
        }
    }

    public static Parser loadParser(String id) {
        if (parsers == null || parsers.isEmpty()) {
            initialize();
        }

        ParserId parserId = ParserId.valueOf(id);

        Class<?> parserClass = parsers.get(parserId.getId());

        if (parserClass != null) {
            try {
                if (parserId.getParametersTypes() == null) {
                    Constructor<?> constructor = parserClass.getConstructor();

                    return (Parser) constructor.newInstance();
                } else {
                    Constructor<?> constructor = parserClass.getConstructor(parserId.getParametersTypes().toArray(new Class<?>[]{}));

                    return (Parser) constructor.newInstance(parserId.getParameters());
                }
            } catch (Exception e) {
                throw new IllegalUseException(String.format("An exception occurred while instantiating the parser '%s'.",
                        parserClass.getName()), e);
            }
        } else {
            throw new IllegalUseException(String.format("Couldn't find a parser with id '%s'.", parserId.getId()));
        }
    }
}
