package com.search2sql.parser;

import com.search2sql.impl.interpreter.util.ParserLoader;
import org.junit.jupiter.api.Test;

class ParserTest {

    @Test
    void test() {
        long time = System.currentTimeMillis();

//        ParserLoader.initialize( true);
//
//        System.out.println(System.currentTimeMillis() - time);

        time = System.currentTimeMillis();

        System.out.println(ParserLoader.loadParser("default.text"));

        System.out.println(System.currentTimeMillis() - time);
    }
}
