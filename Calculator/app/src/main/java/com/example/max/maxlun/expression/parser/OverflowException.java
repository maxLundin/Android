package com.example.max.maxlun.expression.parser;

public class OverflowException extends Exception {
    public OverflowException(String message) {
        super(message);
    }

    public OverflowException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
