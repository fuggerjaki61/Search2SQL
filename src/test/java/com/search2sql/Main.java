package com.search2sql;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.exception.i18n.LocalizedExceptionHandler;
import com.search2sql.exception.i18n.ExceptionHandler;
import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.FileTranslator;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import com.search2sql.translator.Translator;

import java.util.*;

/**
 * This is a basic test class that contains a main method that can be run to type in a query that will be translated to
 * sql.
 * <br>
 * The first iteration will always take 1-2s while every subsequent will take not more than 1ms.
 */
public class Main {

    /**
     * This will run a simple command line application.
     */
    public static void main(String[] args) {
        // instantiate a new scanner
        Scanner scanner = new Scanner(System.in);

        // define a translator implementation
        Translator translator = new FileTranslator("some.properties");

        // configure the table
        TableConfig tableConfig = new TableConfig("table", new Column("text", "text"), new Column("int", "int"));

        // print out help
        System.out.println("Type a search query or enter 'exit' to exit.");

        // runs forever
        while (true) {
            // prints arrow in front of input
            System.out.print("~> ");

            // gets complete input and removes whitespaces from beginning and end
            String input = scanner.nextLine().trim();

            // checks if user wants to exit
            if ("exit".equals(input)) {
                // closes scanner
                scanner.close();

                // exits JVM and loop is stopped
                System.exit(0);
            } else {
                // gets current time in millis
                long time = System.currentTimeMillis();

                try {
                    // translate it to sql
                    String sql = translator.translate(new BasicInterpreter().interpret(input, tableConfig));

                    // gets time used for generating query
                    time = System.currentTimeMillis() - time;

                    // outputs input, time taken and the generated sql query
                    System.out.format("\tInput: %s\n\tTime: %dms\n\tSQL: %s", input, time, sql);
                } catch (InvalidSearchException e) {
                    // get an exception handler
                    ExceptionHandler handler = new LocalizedExceptionHandler();

                    // gets the message for the exception
                    String msg = handler.handle(e);

                    // gets time used for generating query
                    time = System.currentTimeMillis() - time;

                    // output the input, time taken and the error message
                    System.out.format("\tInput: %s\n\tTime: %dms\n\tError: %s", input, time, msg);
                }
            }

            // prints new line
            System.out.println();
        }
    }
}
