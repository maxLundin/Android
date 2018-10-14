package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class BitOr extends AbstractOperation {
    public BitOr(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    int operation(int a, int b) {
        return a | b;
    }

    @Override
    void check(int a, int b) throws DivisionByZeroException, OverflowException {

    }

}
