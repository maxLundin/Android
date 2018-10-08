package com.example.max.maxlun.expression.parser;

public class ExpressionParser implements Parser {
    enum Tokens {
        Operation, Number, Variable, TheEnd, Error
    }

    private int i;
    private int balance;
    private Tokens ct;
    private char curr;
    private StringBuilder sb;
    private boolean end;
    private boolean lastTime;
    private boolean unary;
    private boolean fl;

    private Tokens get_token(String expression) throws ParserException {

        while (i != expression.length() && Character.isWhitespace(expression.charAt(i))) {
            i++;
        }
        if (i == expression.length()) {
            end = true;
            return Tokens.TheEnd;
        }
        curr = expression.charAt(i);
        switch (curr) {
            case '(':
                unary = false;
                balance++;
                i++;
                lastTime = true;
                return Tokens.Operation;
            case ')':
                unary = false;
                i++;
                lastTime = false;
                if (balance == 0) {
                    throw new ParserException("Wrong count of brackets. Expected (, got )" + "at " + "pos: " + (i - 1));
                }
                balance--;
                return Tokens.Operation;
            case '+':
            case '-':
            case '*':
            case '/':
            case '%':
                unary = false;
                i++;
                lastTime = true;
                return Tokens.Operation;
            default:
                if (Character.isDigit(curr)) {
                    if (!unary) {
                        if (!lastTime) {
                            throw new ParserException("Wrong place for number. Expected operation, got --> " + curr + " <<-- at " + "pos: " + (i - 1));
                        }
                    }
                    sb = new StringBuilder();
                    while (i != expression.length() && (Character.isDigit(expression.charAt(i)) || expression.charAt(i) == ',' || expression.charAt(i) == '.'
                            || expression.charAt(i) == 'E' || expression.charAt(i) == 'e')) {
                        if (expression.charAt(i) == ',') {
                            sb.append('.');
                        } else {
                            sb.append(expression.charAt(i));
                        }
                        i++;
                    }
                    if (i == expression.length()) {
                        end = true;
                    }
                    lastTime = false;
                    return Tokens.Number;
                }
                unary = false;
                if (!lastTime) {
                    if (!Character.isAlphabetic(curr)) {
                        throw new ParserException("Unexpected symbol-->> " + curr + " <<-- at " + "pos: " + (i - 1));
                    }
                    throw new ParserException("Expected to get unary operation, but got -->> " + curr + " <<-- at " + "pos: " + (i - 1));
                }
                throw new ParserException("Unexpected symbol-->> " + curr + " <<-- at " + "pos: " + (i - 1));

        }
    }

    private double highPriority(String expression, boolean flag) throws ParserException, OverflowException, DivisionByZeroException {
        if (flag && !end) {
            ct = get_token(expression);
        }
        switch (ct) {
            case Number:
                ct = get_token(expression);
                double const1;
                if (unary) {
                    unary = false;
                    fl = true;

                    try {
                        const1 = Double.parseDouble("-" + sb.toString());
                    } catch (NumberFormatException e) {
                        throw new OverflowException("OverflowException with " + "-" + sb.toString() + "at " + "pos: " + (i - 1));
                    }
                    return const1;
                } else {
                    try {
                        const1 = Double.parseDouble(sb.toString());
                    } catch (NumberFormatException e) {
                        throw new OverflowException("OverflowException with " + sb.toString() + "at " + "pos: " + (i - 1));
                    }
                    return const1;
                }
            case Operation:
                if (curr == '(') {
                    double te = lowestPriority(expression, true);
                    ct = get_token(expression);
                    return te;
                } else {
                    if (curr == '-') {
                        unary = true;
                        fl = false;
                        double tr = highPriority(expression, true);
                        if (fl) {
                            fl = false;
                            return tr;
                        }
                        return -tr;
                    } else {
                        //System.err.println("4");
                        throw new ParserException("Expected to get expression, but got an operation " + curr + " at " + "pos: " + (i - 1));
                    }
                }
            default:
                // System.err.println("2");
                throw new ParserException("No arguments " + "at " + "pos: " + (i - 1));
        }
    }

    private double midPriority(String expression, boolean flag) throws ParserException, DivisionByZeroException, OverflowException {
        double first = highPriority(expression, flag);
        while (true) {
            switch (curr) {
                case '*':
                    first = first * highPriority(expression, true);
                    continue;
                case '/':
                    double result = highPriority(expression, true);
                    if (result == 0) {
                        throw new DivisionByZeroException("/0");
                    }
                    first = first / result;
                    continue;
                default:
                    return first;
            }

        }
    }

    private double lowPriority(String expression, boolean flag) throws ParserException, DivisionByZeroException, OverflowException {
        double first = midPriority(expression, flag);
        while (true) {
            switch (curr) {
                case '+':
                    first = first + midPriority(expression, true);
                    continue;
                case '-':
                    first = first - midPriority(expression, true);
                    continue;
                default:
                    return first;
            }
        }
    }

    private double lowestPriority(String expression, boolean flag) throws ParserException, DivisionByZeroException, OverflowException {
        double first = lowPriority(expression, flag);
        while (true) {
            switch (curr) {
                case '%':
                    first = first % lowPriority(expression, true);
                    continue;
                default:
                    return first;
            }
        }
    }


    public double parse(String expression) throws ParserException, OverflowException, DivisionByZeroException {
        lastTime = true;
        balance = 0;
        i = 0;
        end = false;
        fl = false;
        ct = get_token(expression);
        unary = false;
        if (ct.equals(Tokens.TheEnd)) {
            return 0;
        }
        double te = lowestPriority(expression, false);
        if (balance != 0) {
            throw new ParserException("Wrong count of brackets. Expected (, got )" + "at " + "pos: " + (i - 1));
        }
        return te;
    }
}