package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class CheckedLog implements TripleExpression {
    TripleExpression first;

    public CheckedLog(TripleExpression first) {
        this.first = first;
    }

    private void check(int res) throws OverflowException {
        if (res <= 0) {
            throw new OverflowException("Logarithm of negative value " + res);
        }
    }

    @Override
    public int evaluate(int x, int y, int z) throws DivisionByZeroException, OverflowException {
        int res = first.evaluate(x, y, z);
        check(res);
        int log = 0;
        while (res > 9) {
            log++;
            res /= 10;
        }
        return log;
    }
}
