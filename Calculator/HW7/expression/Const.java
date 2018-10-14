package expression;

public class Const implements TripleExpression {
	public Number value;

	public Const(int a) {
		value = a;
	}

	public Const(double a) {
		value = a;
	}

	public int evaluate(int x) {
		return this.value.intValue();
	}

	public double evaluate(double x) {
		return this.value.doubleValue();
	}

	@Override
	public int evaluate(int x, int y, int z) {
		return this.value.intValue();
	}
}
