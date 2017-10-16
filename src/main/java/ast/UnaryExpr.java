package ast;

import ast.Node.NodeType;

/** A representation of a unary numerical expression that evaluates to an integer. */
public class UnaryExpr extends AbstractNode implements Expr
{
	/** The type of this unary expression. */
	private ExprType type;
	/** The subexpression that this unary expression is based off of. May be null. */
	private Expr exp;
	private int value;
	
	/** 
	 * Creates a unary expression based on the given expression and ExprType.
	 * @param e - The subexpression that this unary expression is based off of
	 * @param t - The type of this unary expression. May not be CONSTANT.
	 */
	public UnaryExpr(Expr e, ExprType t)
	{
		this.exp = e;
		this.type = t;
		exp.setParent(this);
	}
	
	/** 
	 * Creates a unary expression of type CONSTANT. 
	 * @param val - the constant integer value of this unary expression.
	 */
	public UnaryExpr(int val)
	{
		this.value = val;
		this.exp = null;
		this.type = ExprType.CONSTANT;
	}
	
	@Override
	public int size()
	{
		if(type == ExprType.CONSTANT)
			return 1;
		return 1 + exp.size();
	}
	
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1)
			throw new IndexOutOfBoundsException();
		return exp.nodeAt(index - 1);
	}
	@Override
	public UnaryExpr clone()
	{
		if(type == ExprType.CONSTANT)
			return new UnaryExpr(value);
		return new UnaryExpr(exp.clone(), type);
	}
	
	@Override
	public boolean acceptMutation(Mutation m)
	{
		try
		{
			boolean result = m.mutate(this);
			return result;
		}
		catch(UnsupportedOperationException u)
		{
			return false;
		}
	}
	
	@Override
	public boolean replaceChild(Node child, Node replacement)
	{
		if(type == ExprType.CONSTANT)
			return false;
		exp = (Expr) replacement;
		exp.setParent(this);
		return false;
	}
	

	@Override
	public Node searchChildrenForSimilarType()
	{
		if(type != ExprType.CONSTANT && (exp.getType() == NodeType.BINARYEXPR || exp.getType() == NodeType.UNARYEXPR
																			  || exp.getType() == NodeType.SENSOR))
			return exp;
		return null;
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
			case SENSORVAL:
				//if this UnaryExpr has the type of SENSORVAL, then the class type of exp should be Sensor
				sb.append(exp.toString());
				break;
		}
		return sb;
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

	@Override
	public NodeType getType()
	{
		return NodeType.UNARYEXPR;
	}
}