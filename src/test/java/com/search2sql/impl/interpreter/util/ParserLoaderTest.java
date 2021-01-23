package com.search2sql.impl.interpreter.util;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ParserLoaderTest {

    @Test
    void getParser() {
        ParserLoader.loadParser("text#'test test', 11, true");
    }
}