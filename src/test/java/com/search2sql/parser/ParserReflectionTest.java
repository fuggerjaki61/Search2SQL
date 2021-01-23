package com.search2sql.parser;

import org.junit.jupiter.api.Test;
import org.reflections8.Reflections;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ParserReflectionTest {

    @Test
    void findParsers() {
        // checks if there is an invalid parser
        boolean failed = false;

        // save start time
        long time = System.currentTimeMillis();

        // finds all classes annotated by a specific annotation
        Set<Class<?>> annotatedClasses = new Reflections().getTypesAnnotatedWith(SearchParser.class);

        // prints how many parsers exists and how long it took to find them
        System.out.format("Found %d classes declaring the @SearchParser annotation in %dms.\n\n", annotatedClasses.size(),
                System.currentTimeMillis() - time); // calculate delta time

        // iterates over all classes that were found
        for (Class<?> clazz : annotatedClasses) {
             // gets annotation from current class
             SearchParser annotation = clazz.getAnnotation(SearchParser.class);

             // checks if the class is a subclass of the Parser class
             if (!Parser.class.isAssignableFrom(clazz)) {
                 // class is not a (in)direct children of the Parser class

                 // print error
                 System.err.format("\tThe class '%s' declared the @SearchParser annotation but did NOT extend the necessary Parser class!\n",
                         clazz.getName());

                 // set failed to true so the test will fail
                 if (!failed) {
                     failed = true;
                 }
             } else {
                 // class is a (in)direct children of the Parser class

                 // print info
                 System.out.format("\tThe class '%s' with the parser id '%s' was found.\n", clazz.getName(), annotation.value());
             }
        }

        if (failed) {
            // one or more parsers were invalid so test will fail

            // print help text for user
            System.err.println("\nThere was an invalid parser(s). Please check if all classes that declare the @SearchParser annotation also extend the Parser class.");

            // fail test
            fail();
        }
    }
}
