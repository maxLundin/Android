package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class CheckedSubtract extends AbstractOperation {
	public CheckedSubtract(TripleExpression first, TripleExpression second) {
		super(first, second);
	}

	int operation(int a, int b) {
		return a - b;
	}

	@Override
	void check(int a, int b) throws DivisionByZeroException, OverflowException {
		if (b < 0 && Integer.MAX_VALUE + b < a || b > 0 && Integer.MIN_VALUE + b > a) {
			throw new OverflowException("Overflow");
		}
	}

	double operation(double a, double b) {
		return a - b;
	}
}
