package com.search2sql.exception.i18n;

import org.junit.jupiter.api.Test;

public class LocalizedExceptionHandlerTest {

    @Test
    void handle() {
        ExceptionHandler handler = new LocalizedExceptionHandler();

        String i18n = handler.handle(0);

        System.out.println(i18n);

        //fail();
    }
}