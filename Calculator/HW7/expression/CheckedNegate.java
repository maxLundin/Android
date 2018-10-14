package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class CheckedNegate implements TripleExpression {
    TripleExpression first;

    public CheckedNegate(TripleExpression first) {
        this.first = first;
    }

    private void check(int res) throws OverflowException {
        if (res == Integer.MIN_VALUE) {
            throw new OverflowException("Overflow with " + Integer.MAX_VALUE);
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws DivisionByZeroException, OverflowException {
        int res = first.evaluate(x, y, z);
        check(res);
        return -res;
    }
}
