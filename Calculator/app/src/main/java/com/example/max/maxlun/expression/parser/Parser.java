package com.example.max.maxlun.expression.parser;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface Parser {
    double parse(String expression) throws ParserException, OverflowException, DivisionByZeroException;
}
