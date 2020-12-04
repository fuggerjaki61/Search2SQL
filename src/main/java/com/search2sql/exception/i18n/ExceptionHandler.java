package com.search2sql.exception.i18n;

import com.search2sql.exception.InvalidSearchException;

public abstract class ExceptionHandler {

    public abstract String handle(int errorCode);

    public String handle(String hex) {
        return handle((int) Long.parseLong(hex, 16));
    }

    public String handle(InvalidSearchException exception) {
        return handle(exception.getErrorCode());
    }

}
