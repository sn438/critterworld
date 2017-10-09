package ast;

public class UnaryExpr extends AbstractNode implements Expr
{
	/**
	 * factor is the enum that indicates what kind of factor is stored in the AST.
	 * expression is the 
	 */
	private Factor factor;
	private Expr expression;
	private int value;
	
	public UnaryExpr(Factor factor, Expr expression) {
		this.factor = factor;
		this.expression = expression;
	}
	
	public UnaryExpr(int value) {
		this.value = value;
	}
	
	public enum Factor{
		CONSTANT, MEMORY, EXPRESSION, NEGATE, SENSOR
	}
}
