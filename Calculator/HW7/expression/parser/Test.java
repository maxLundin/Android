package expression.parser;


public class Test {
	public static void main(String args[]) throws ParserException, DivisionByZeroException, OverflowException {
		ExpressionParser parser = new ExpressionParser();
		System.out.println((parser.parse("log10 1").evaluate(0,0,-1249535276)));
	}
}