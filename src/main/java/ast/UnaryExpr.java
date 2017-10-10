package ast;

public class UnaryExpr extends AbstractNode implements Expr
{

	private ExprType type;
	private Expr exp;
	private int value;
	
	public UnaryExpr(Expr e, ExprType t)
	{
		this.exp = e;
		this.type = t;
	}

	public UnaryExpr(int value)
	{
		this.value = value;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int evaluateNode()
	{
		throw new UnsupportedOperationException();
	}
	
	public enum ExprType
	{
		CONSTANT, MEMORYVAL, EXPRESSION, NEGATION, SENSORVAL;
	}
}
