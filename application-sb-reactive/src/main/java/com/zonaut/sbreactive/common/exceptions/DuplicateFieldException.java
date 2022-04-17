package com.zonaut.sbreactive.common.exceptions;

public class DuplicateFieldException extends RuntimeException {

    private final String field;
    private final String error;

    public DuplicateFieldException(String field, String error) {
        this.field = field;
        this.error = error;
    }

    public String getField() {
        return field;
    }

    public String getError() {
        return error;
    }
}
