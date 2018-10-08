package expression.parser;

import expression.*;

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
            case 'l':
                if (!expression.substring(i, i + 5).equals("log10")) {
                    throw new ParserException("Bad operation. Expected log10, got -->" + expression.substring(i, i + 5) + "<<-- at " + "pos: " + (i - 1));
                }
            case 'p':
                if (curr != 'l' && !expression.substring(i, i + 5).equals("pow10")) {
                    throw new ParserException("Bad operation. Expected pow10, got -->" + expression.substring(i, i + 5) + "<<-- at " + "pos: " + (i - 1));
                }
            case '+':
            case '-':
            case '*':
            case '/':
                unary = false;
                i++;
                lastTime = true;
                return Tokens.Operation;
            default:
                if (Character.isDigit(curr)) {
                    if (!unary) {
                        if (!lastTime) {
                            //System.err.println("6");
                            throw new ParserException("Wrong place for number. Expected operation, got --> " + curr + " <<-- at " + "pos: " + (i - 1));
                        }
                    }
                    sb = new StringBuilder();
                    while (i != expression.length() && Character.isDigit((expression.charAt(i)))) {
                        sb.append(expression.charAt(i));
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
                        //System.err.println("7");
                        throw new ParserException("Unexpected symbol-->> " + curr + " <<-- at " + "pos: " + (i - 1));
                    }
                    throw new ParserException("Expected to get unary operation, but got -->> " + curr + " <<-- at " + "pos: " + (i - 1));
                }
                if (Character.isAlphabetic(curr)) {
                    sb = new StringBuilder();
                    sb.append(curr);
                    ++i;
                    if (i == expression.length()) {
                        end = true;
                    }
                    lastTime = false;
                    return Tokens.Variable;
                } else {
                    System.err.println("5");
                    throw new ParserException("Unexpected symbol-->> " + curr + " <<-- at " + "pos: " + (i - 1));
                }
        }
    }

    private TripleExpression highPriority(String expression, boolean flag) throws ParserException, OverflowException {
        if (flag && !end) {
            ct = get_token(expression);
        }
        switch (ct) {
            case Number:
                ct = get_token(expression);
                TripleExpression const1;
                if (unary) {
                    unary = false;
                    fl = true;

                    try {
                        const1 = new Const(Integer.parseInt("-" + sb.toString()));
                    } catch (NumberFormatException e) {
                        throw new OverflowException("OverflowException with " + "-" + sb.toString() + "at " + "pos: " + (i - 1));
                    }
                    return const1;
                } else {
                    try {
                        const1 = new Const(Integer.parseInt(sb.toString()));
                    } catch (NumberFormatException e) {
                        throw new OverflowException("OverflowException with " + sb.toString() + "at " + "pos: " + (i - 1));
                    }
                    return const1;
                }
            case Variable:
                ct = get_token(expression);
                return new Variable(sb.toString());
            case Operation:
                if (curr == '(') {
                    TripleExpression te = lowPriority(expression, true);
                    ct = get_token(expression);
                    return te;
                } else {
                    if (curr == 'l') {
                        i += 4;
                        return new CheckedLog(highPriority(expression, true));
                    }
                    if (curr == 'p') {
                        i += 4;
                        return new CheckedPow(highPriority(expression, true));
                    }
                    if (curr == '-') {
                        unary = true;
                        fl = false;
                        TripleExpression tr = highPriority(expression, true);
                        if (fl) {
                            fl = false;
                            return tr;
                        }
                        return new CheckedNegate(tr);
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

    private TripleExpression midPriority(String expression, boolean flag) throws ParserException, OverflowException {
        TripleExpression first = highPriority(expression, flag);
        while (true) {
            switch (curr) {
                case '*':
                    first = new CheckedMultiply(first, highPriority(expression, true));
                    continue;
                case '/':
                    first = new CheckedDivide(first, highPriority(expression, true));
                    continue;
                default:
                    return first;
            }

        }
    }

    private TripleExpression lowPriority(String expression, boolean flag) throws ParserException, OverflowException {
        TripleExpression first = midPriority(expression, flag);
        while (true) {
            switch (curr) {
                case '+':
                    first = new CheckedAdd(first, midPriority(expression, true));
                    continue;
                case '-':
                    first = new CheckedSubtract(first, midPriority(expression, true));
                    continue;
                default:
                    return first;
            }
        }
    }


    public TripleExpression parse(String expression) throws ParserException, OverflowException {
        //  System.err.println(expression);
        lastTime = true;
        balance = 0;
        i = 0;
        end = false;
        fl = false;
        ct = get_token(expression);
        unary = false;
        if (ct.equals(Tokens.TheEnd)) {
            return new Const(0);
        }
        TripleExpression te = lowPriority(expression, false);
        if (balance != 0) {
            throw new ParserException("Wrong count of brackets. Expected (, got )" + "at " + "pos: " + (i - 1));
        }
        return te;
    }
}