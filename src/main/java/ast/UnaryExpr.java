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
	@Override
	public int evaluateNode()
	{
		/*
		int val = exp.evaluateNode();
		switch(type)
		{
			case CONSTANT:
				return val;
			case MEMORYVAL:
				return mem[val];
			case EXPRESSION:
				return val;
			case NEGATION:
				return -1 * val;
			case SENSORVAL:
				
			default:
				return 0;
		}
		*/
		throw new UnsupportedOperationException();

	}
	
	public enum Factor{
		CONSTANT, MEMORY, EXPRESSION, NEGATE, SENSOR
	}
}
