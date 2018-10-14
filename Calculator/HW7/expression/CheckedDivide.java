package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

public class CheckedDivide extends AbstractOperation {
	public CheckedDivide(TripleExpression first, TripleExpression second) {
		super(first, second);
	}

	void check(int a,int b) throws DivisionByZeroException, OverflowException {
		if (b==0){
			throw new DivisionByZeroException("DivisionByZeroException");
		}
		if (a == Integer.MIN_VALUE && b==-1){
			throw new OverflowException("OverflowException");
		}
	}

	int operation(int a, int b) {
		return a / b;
	}

}
