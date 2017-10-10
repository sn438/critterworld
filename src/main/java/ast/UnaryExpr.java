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

	public UnaryExpr(int val)
	{
		this.value = val;
		this.exp = null;
		this.type = ExprType.CONSTANT;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		switch(type)
		{
			case CONSTANT:
				sb.append(value);
				break;
			case MEMORYVAL:
				sb.append("mem[" + exp.toString() + "]");
				break;
			case EXPRESSION:
				sb.append("(" + exp.toString() + ")");
				break;
			case NEGATION:
				sb.append("-" + exp.toString());
				break;
			case SENSORVAL: //TODO fix
				break;
			default:
				break;
		}
		return sb;
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
