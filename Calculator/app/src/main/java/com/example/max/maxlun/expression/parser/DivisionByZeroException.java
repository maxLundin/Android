package com.example.max.maxlun.expression.parser;

public class DivisionByZeroException extends Exception{
    public DivisionByZeroException(String message) {
        super(message);
    }

    public DivisionByZeroException(
            String message,
            Throwable cause
    ) {
        super(message, cause);
    }
}
