package ast;

/** A representation of a binary numerical expression that evaluates to an integer. */
public class BinaryExpr extends AbstractNode implements Expr
{
	/** The left child of this node. */
	private Expr left;
	/** The operation to be performed on the two children. */
	private MathOp op;
	/** The right child of this node. */
	private Expr right;
	
	/** Creates a BinaryExpr node. */
	public BinaryExpr(Expr l, MathOp operator, Expr r)
	{
		this.left = l;
		this.op = operator;
		this.right = r;
	}
	
	/*public BinaryExpr(Expr l)
	{
		this.left = l;
		this.op = null;
		this.right = null;
	}*/
	
	@Override
	//TODO implement
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		return null;
	}
	
	/* To be done in A5
	@Override
	public int evaluateNode()
	{
		int leftValue = left.evaluateNode();
		int rightValue = right.evaluateNode();
		switch(op)
		{
			case ADD:
				return leftValue + rightValue;
			case SUBTRACT:
				return leftValue - rightValue;
			case MULTIPLY:
				return leftValue * rightValue;
			case DIVIDE:
				return leftValue / rightValue;
			case MOD:
				return leftValue % rightValue;
			default:
				return 0;
		}
	}
	*/
	
	/** Enumerates all the accepted binary mathematical operations. */
	public enum MathOp
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD;
	}
}
