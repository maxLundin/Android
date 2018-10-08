package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class CheckedPow implements TripleExpression {
    TripleExpression first;

    public CheckedPow(TripleExpression first) {
        this.first = first;
    }

    private void check(int res) throws OverflowException {
        if (res < 0 || res > 9) {
            throw new OverflowException("Too big value for exponent. Expected exponent from 0, to 9, but got " + res);
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws DivisionByZeroException, OverflowException {
        int res = first.evaluate(x, y, z);
        check(res);
        int power = 1;
        for (int i = 0; i < res; i++) {
            power *= 10;
        }
        return power;
    }
}
