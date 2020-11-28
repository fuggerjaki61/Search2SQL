package com.search2sql.impl.interpreter.util;

import com.search2sql.exception.IllegalUseException;
import com.search2sql.parser.Parser;
import com.search2sql.parser.SearchParser;
import org.reflections.Reflections;
import org.reflections.scanners.FieldAnnotationsScanner;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * <code>ParserLoader</code> is an utility class that loads the parser specified by an id.
 * <br><br>
 * This class may be used by any implementation of {@link com.search2sql.interpreter.Interpreter Interpreter}. The main
 * purpose of this class is to load and construct an object of the {@link Parser} class. The list of classes that define
 * the {@link SearchParser @SearchParser} annotation is loaded in the <code>static</code> block of this class. To trigger
 * this <code>static</code> block you can use:<br><br>
 * <code>Class.forName("com.search2sql.impl.interpreter.util.ParserLoader");</code>
 * <br><br>
 * Executing this statement before the first use of the API, reduces the first call time (first call time without this:
 * <code>~ 1000ms</code>, average call time: <code>~ 1ms</code>) to the average call time.
 * <br><br>
 * Another feature that was added in version <i>1.0-echo</i> are <i>parameterized parsers</i>. These parameters are added
 * to the parser id after a <code>#</code> (hashtag). The constructor that will create a new instance is chosen based on
 * these parameters. This could be an example for a parser id with parameters:
 * <br><br>
 * <code>text#false</code>
 *
 * @author fuggerjaki61
 * @since 1.0-echo
 */
public class ParserLoader {

    private static Set<Class<?>> parsers;

    /**
     * This is the main method of this class. This method loads and constructs an object of the {@link Parser} based
     * on the parser id parameter. For more information see {@link ParserLoader}.
     *
     * @param id parser id
     * @return a new instance of the specific Parser class based on the id
     */
    public static Parser getParser(String id) {
        if (parsers == null) {
            Reflections reflections = new Reflections("",
                    new TypeAnnotationsScanner(),
                    new SubTypesScanner(),
                    new FieldAnnotationsScanner());

            parsers = reflections.getTypesAnnotatedWith(SearchParser.class);
        }

        // id can't be null
        if (id == null) {
            // misuse of the api
            throw new IllegalUseException("Parser id cannot be null.");
        }

        // class of the parser
        Class<?> parser = null;

        // id of the parser
        String name;

        // parameters
        String params;

        // regex to split id and parameters
        Matcher matcher = Pattern.compile("(?<id>[^#]+)(?:#|)(?<params>.*)").matcher(id);

        // checks if id is valid
        if (matcher.find()) {
            // gets id group
            name = matcher.group("id");
            // gets params group
            params = matcher.group("params");
        } else {
            // misuse of the api
            throw new IllegalUseException(String.format("Parser id '%s' isn't valid!", id));
        }

        // iterate over every class that defines the @SearchParser annotation
        for (Class<?> clazz : parsers) {
            // get annotation from class
            SearchParser annotation = clazz.getAnnotation(SearchParser.class);

            // checks if id of the parser matches
            if (annotation.value().equalsIgnoreCase(name)) {
                // sets parser
                parser = clazz;

                // found parser can exit now
                break;
            }
        }

        if (parser == null) {
            throw new IllegalUseException(String.format("Parser with id '%s' couldn't be found!", name));
        }

        // resolve parameters
        Object[] param = resolveParameters(params);

        // create array with classes of the objects to get the constructor based on them
        Class<?>[] paramTypes = new Class<?>[param.length];

        // iterate over every parameter
        for (int i = 0; i < param.length; i++) {
            // adds class responding to the object to the array
            paramTypes[i] = param[i].getClass();
        }

        try {
            // get constructor based on values' classes
            Constructor<?> constructor = parser.getConstructor(paramTypes);

            // create new instance of that parser
            return (Parser) constructor.newInstance(param);
        } catch (Exception e) {
            // wrap exception and forward it
            throw new IllegalUseException(String.format("A problem occurred while instantiating the parser with the id '%s'.", id), e);
        }
    }

    private static Object[] resolveParameters(String params) {
        // create new list of objects
        ArrayList<Object> objects = new ArrayList<>();

        // create new builder
        StringBuilder param = new StringBuilder();

        // checks if is in a quote currently
        boolean inString = false;

        // iterate over every char
        for (char c : params.toCharArray()) {
            // checks if there's a coma or is in a string currently
            if (c != ',' || inString) {
                // adds the char to the string
                param.append(c);

                // checks if is a char is a quotation character
                if (c == '\'') {
                    // checks if in a quote currently
                    if (inString) {
                        // quote is ending so parameter is finished and should be added

                        // add the object
                        objects.add(resolveParameter(param.toString().trim()));

                        // clear the string
                        param = new StringBuilder();
                    }

                    // inverts flag
                    inString = !inString;
                }
            } else if (param.length() > 0) {    // param mustn't be empty
                // add the parameter
                objects.add(resolveParameter(param.toString().trim()));

                // clear the string
                param = new StringBuilder();
            }
        }

        // last parameter isn't added, if it's not empty it should be added
        if (param.length() > 0) {
            // add it
            objects.add(resolveParameter(param.toString().trim()));
        }

        // return array
        return objects.toArray();
    }

    private static Object resolveParameter(String param) {
        // checks if parameter is a quote
        if (param.matches("'[^']*'")) {
            // return the string without the quotes
            return param.substring(1, param.length() - 1);
        } else if (param.equals("true")) {  // checks if string is a boolean with the value true
            // return a boolean
            return true;
        } else if (param.equals("false")) { // checks if string is a boolean with the value false
            // return a boolean
            return false;
        } else if (param.matches("(?:\\+|-|)\\d+")) {   // checks if string is a (negative) number
            // parse that integer
            return Integer.parseInt(param);
        } else {
            // parameter couldn't be resolved, it isn't supported (yet)
            throw new IllegalUseException(String.format("The parameter type '%s' is not supported.", param));
        }
    }
}















