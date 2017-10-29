package interpret;

import ast.BinaryCondition;
import ast.Relation;
import ast.BinaryExpr;
import ast.UnaryExpr;
import ast.Sensor;

/**
 * An example interface for interpreting a critter program. This is just a starting
 * point and may be changed as much as you like.
 */
public interface Interpreter
{
	/** Executes the results of one critter turn. */
	public void simulateCritterTurn();

    /**
     * Evaluates the given binary condition.
     * @param c
     * @return a boolean that results from evaluating c.
     */
    boolean eval(BinaryCondition c);
    
    /**
     * Evaluates the given relation.
     * @param c
     * @return a boolean that results from evaluating c.
     */
    boolean eval(Relation c);

    /**
     * Evaluates the given binary expression.
     * @param e
     * @return an integer that results from evaluating e.
     */
    int eval(BinaryExpr e);
    
    /**
     * Evaluates the given unary expression.
     * @param e
     * @return an integer that results from evaluating e.
     */
    int eval(UnaryExpr e);
    
    /**
     * Evaluates the given sensor.
     * @param e
     * @return an integer that results from evaluating e.
     */
    int eval(Sensor s);
}
