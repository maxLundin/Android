package expression;

public abstract class Operation implements SuperExpression {
    private SuperExpression oper1;
    private SuperExpression oper2;

    Operation(SuperExpression oper1, SuperExpression oper2) {
        assert oper1 != null;
        assert oper2 != null;
        this.oper1 = oper1;
        this.oper2 = oper2;
    }

    @Override
    public double evaluate(double x) {
        return operation(oper1.evaluate(x), oper2.evaluate(x));
    }

    abstract double operation(double a, double b);

    @Override
    public int evaluate(int x) {
        return operation(oper1.evaluate(x), oper2.evaluate(x));
    }

    abstract int operation(int a, int b);
}
