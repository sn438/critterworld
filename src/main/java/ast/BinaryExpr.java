package ast;

/** A representation of a binary numerical expression that evaluates to an integer. */
public class BinaryExpr extends AbstractNode implements Expr
{
	/** The left child of this node. */
	private Expr left;
	/** The operation to be performed on the two children. */
	private MathOp operator;
	/** The right child of this node. */
	private Expr right;
	
	/** Creates a BinaryExpr with the given operands and mathematical operator, representing left op right. */
	public BinaryExpr(Expr l, MathOp op, Expr r)
	{
		this.left = l;
		this.operator = op;
		this.right = r;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		switch(operator)
		{
			case ADD:
				sb.append(left.toString() + " + " + right.toString());
				break;
			case SUBTRACT:
				sb.append(left.toString() + " - " + right.toString());
				break;
			case MULTIPLY:
				sb.append(left.toString() + " * " + right.toString());
				break;
			case DIVIDE:
				sb.append(left.toString() + " / " + right.toString());
				break;
			case MOD:
				sb.append(left.toString() + " mod " + right.toString());
				break;
		}
		return sb;
	}
	
	@Override
	public int size()
	{
		return 1 + left.size() + right.size();
	}
	
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		if(index < left.size())
			return left.nodeAt(index);
		else
			return right.nodeAt(index - left.size());
	}
	
	@Override
	public int evaluate()
	{
		throw new UnsupportedOperationException();
	}

	/** Enumerates all the accepted binary mathematical operations. */
	public enum MathOp
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD;
	}
}