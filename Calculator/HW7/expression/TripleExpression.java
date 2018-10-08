package expression;

import expression.parser.DivisionByZeroException;
import expression.parser.OverflowException;

/**
 * @author Georgiy Korneev (kgeorgiy@kgeorgiy.info)
 */
public interface TripleExpression {
    int evaluate(int x, int y, int z) throws DivisionByZeroException, OverflowException;
}
