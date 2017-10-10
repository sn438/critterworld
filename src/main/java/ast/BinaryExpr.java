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
	
	/** Creates a BinaryExpr node, an AST representation of left op right. */
	public BinaryExpr(Expr l, MathOp op, Expr r)
	{
		this.left = l;
		this.operator = op;
		this.right = r;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
<<<<<<< HEAD
		return null;
	}
	
	@Override
	public int evaluateNode()
	{
		/*
		int leftValue = left.evaluateNode();
		int rightValue = right.evaluateNode();
=======
		sb.append(left.toString());
>>>>>>> bcbac9a71a41a75cc4a622474fdf2d9fa785a8ad
		switch(operator)
		{
			case ADD:
				sb.append(" + ");
				break;
			case SUBTRACT:
				sb.append(" - ");
				break;
			case MULTIPLY:
				sb.append(" * ");
				break;
			case DIVIDE:
				sb.append(" / ");
				break;
			case MOD:
				sb.append(" % ");
				break;
			default:
				break;
		}
<<<<<<< HEAD
		*/
		throw new UnsupportedOperationException();
	}
=======
		return sb;
	}
	
	@Override
	public int evaluateNode()
	{
		throw new UnsupportedOperationException();
	}
>>>>>>> bcbac9a71a41a75cc4a622474fdf2d9fa785a8ad

	
	/** Enumerates all the accepted binary mathematical operations. */
	public enum MathOp
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD;
	}
}
