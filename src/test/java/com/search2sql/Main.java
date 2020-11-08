package com.search2sql;

import com.search2sql.exception.InvalidSearchException;
import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.BasicTranslator;
import com.search2sql.impl.translator.FileTranslator;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;
import com.search2sql.translator.Translator;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
        Translator translator = new FileTranslator();

        // configure the table
        TableConfig tableConfig = new TableConfig("table", new Column("text", "text#false"), new Column("int", "int"));

        // print out help
        System.out.println("Type a search query, enter 'exit' to exit or set the table config with 'config [[\"name\", \"parserId\"], ...]'.");

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
            } else if (input.matches("^config\\s+\\[.*]$")) {
                tableConfig.setColumns(new ArrayList<>());

                input = input.substring(6).trim();
                input = input.substring(1, input.length() - 1);

                Matcher m = Pattern.compile("\\[[^,]+,\\s*[^\\]]+]")
                        .matcher(input);

                while (m.find()) {
                    String match = m.group();

                    Matcher matcher = Pattern.compile("\\[(?<name>[^,]+),\\s*(?<parser>[^]]+)]").matcher(match);

                    if (matcher.find()) {
                        tableConfig.getColumns().add(new Column(matcher.group("name"), matcher.group("parser")));
                    }
                }
            } else {
                // gets current time in millis
                long time = System.currentTimeMillis();

                // interprets&translates input
                String sql = null;

                try {
                    sql = translator.translate(new BasicInterpreter().interpret(input, tableConfig));
                } catch (InvalidSearchException e) {
                    e.printStackTrace();
                }

                // gets time used for generating query
                time = System.currentTimeMillis() - time;

                // outputs input, time taken and the generated sql query
                System.out.format("\tInput: %s\n\tTime: %dms\n\tSQL: %s", input, time, sql);
            }

            // prints new line
            System.out.println();
        }
    }
}
