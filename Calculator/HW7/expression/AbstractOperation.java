package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public abstract class AbstractOperation implements TripleExpression {
    TripleExpression first;
    TripleExpression second;

    AbstractOperation(TripleExpression first, TripleExpression second) {
        assert first != null && second != null;
        this.first = first;
        this.second = second;
    }


    abstract int operation(int a, int b);

    abstract void check(int a, int b) throws DivisionByZeroException, OverflowException;


//	public int evaluate(int x) {
//		return operation(first.evaluate(x), second.evaluate(x));
//	}
//
//	public double evaluate(double x) {
//		return operation(first.evaluate(x), second.evaluate(x));
//	}

    @Override
    public int evaluate(int x, int y, int z) throws DivisionByZeroException, OverflowException {
        int fir = first.evaluate(x, y, z);
        int sec = second.evaluate(x, y, z);
        check(fir, sec);
        return operation(fir, sec);
    }
}
