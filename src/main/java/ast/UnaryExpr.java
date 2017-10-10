package ast;

/** A representation of a unary numerical expression that evaluates to an integer. */
public class UnaryExpr extends AbstractNode implements Expr
{
	/** The type of this unary expression. */
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
				sb.append("-(" + exp.toString() + ")");
				break;
			case SENSORVAL:
				//if this UnaryExpr has the type of SENSORVAL, then the class type of exp should be Sensor
				sb.append(exp.toString());
				break;
			default:
				break;
		}
		return sb;
	}
	
	@Override
	public int size()
	{
		if(type == ExprType.CONSTANT)
			return 1;
		return 1 + exp.size();
	}
	@Override
	public int evaluate()
	{
		throw new UnsupportedOperationException();
	}
	
	/** An enumeration of all the possible unary expression types. */
	public enum ExprType
	{
		CONSTANT, MEMORYVAL, EXPRESSION, NEGATION, SENSORVAL;
	}
}
