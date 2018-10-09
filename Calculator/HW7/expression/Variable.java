package expression;

public class Variable implements TripleExpression {
	public String name;

	public Variable(String curr) {
		name = curr;
	}

	public int evaluate(int x) {
		return x;
	}

	public double evaluate(double x) {
		return x;
	}

	@Override
	public int evaluate(int x, int y, int z) {
		switch(name){
			case "x":
				return x;
			case "y":
				return y;
			case "z":
				return z;
		}
		return 0;
	}
}
