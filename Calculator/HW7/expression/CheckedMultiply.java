package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class CheckedMultiply extends AbstractOperation {
	public CheckedMultiply(TripleExpression first, TripleExpression second) {
		super(first, second);
	}

	int operation(int a, int b) {
		return a * b;
	}

	@Override
	void check(int a, int b) throws OverflowException {
		if (a > 0 && b > 0 && Integer.MAX_VALUE / a < b) {
			throw new OverflowException("OverflowException");
		}
		if (a > 0 && b < 0 && Integer.MIN_VALUE / a > b) {
			throw new OverflowException("OverflowException");
		}
		if (a < 0 && b > 0 && Integer.MIN_VALUE / b > a) {
			throw new OverflowException("OverflowException");
		}
		if (a < 0 && b < 0 && Integer.MAX_VALUE / a > b) {
			throw new OverflowException("OverflowException");
		}
	}

	double operation(double a, double b) {
		return a * b;
	}
}
