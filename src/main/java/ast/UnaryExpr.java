package ast;

public class UnaryExpr extends AbstractNode implements Expr
{
	
	private Expr exp;
	private UnaryExprType type;
	
	public UnaryExpr(Expr e, UnaryExprType t)
	{
		this.exp = e;
		this.type = t;
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
	
	public enum UnaryExprType
	{
		CONSTANT, MEMORYVAL, EXPRESSION, NEGATION, SENSORVAL
	}
}
