package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class BitAnd extends AbstractOperation {
    public BitAnd(TripleExpression first, TripleExpression second) {
        super(first, second);
    }

    int operation(int a, int b) {
        return a & b;
    }

    @Override
    void check(int a, int b) throws DivisionByZeroException, OverflowException {

    }

}
