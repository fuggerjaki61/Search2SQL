package com.search2sql;

import com.search2sql.impl.interpreter.BasicInterpreter;
import com.search2sql.impl.translator.BasicTranslator;
import com.search2sql.table.Column;
import com.search2sql.table.TableConfig;

import java.util.Scanner;

public class MainTest {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        BasicTranslator translator = new BasicTranslator();
        TableConfig tableConfig = new TableConfig("table", new Column("text", "text"), new Column("int", "int"));

        boolean exit = false;

        System.out.println("Type a search query or enter 'exit' to exit");

        while (!exit) {
            System.out.print("~> ");
            String input = scanner.nextLine().trim();

            if ("exit".equals(input)) {
                exit = true;
            } else {
                long time = System.currentTimeMillis();

                String sql = translator.translate(new BasicInterpreter().interpret(input, tableConfig));

                time = System.currentTimeMillis() - time;

                System.out.format("\tInput: '%s'\n\tTime: '%d'\n\tSQL: '%s'", input, time, sql);
            }

            System.out.println();
        }

        scanner.close();
    }
}
