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
		sb.append(left.toString());
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
		sb.append(right.toString());
		return sb;
	}
	
	@Override
	public int evaluateNode()
	{
		throw new UnsupportedOperationException();
	}

	
	/** Enumerates all the accepted binary mathematical operations. */
	public enum MathOp
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD;
	}
}
