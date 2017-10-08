package ast;

public class UnaryExpr extends AbstractNode implements Expr
{
	
	private Factor factor;
	private Expr expression;
	
	public UnaryExpr(Factor factor, Expr expression) {
		this.factor = factor;
		this.expression = expression;
	}
	
	@Override
	public int evaluateNode()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	public enum Factor{
		CONSTANT, MEMORY, EXPRESSION, NEGATE, SENSOR
	}
}
