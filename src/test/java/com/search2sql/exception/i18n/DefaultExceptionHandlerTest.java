package com.search2sql.exception.i18n;

import org.junit.jupiter.api.Test;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

public class DefaultExceptionHandlerTest {

    @Test
    void handle() {
        ExceptionHandler handler = new DefaultExceptionHandler();

        String i18n = handler.handle(0);

        System.out.println(i18n);

        //fail();
    }
}