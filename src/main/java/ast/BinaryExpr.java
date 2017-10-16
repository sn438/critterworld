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
		
		left.setParent(this);
		right.setParent(this);
	}
	
	public Expr getLeft()
	{
		return left;
	}
	public void setLeft(Expr newLeft)
	{
		left = newLeft;
	}
	public Expr getRight()
	{
		return right;
	}
	public void setRight(Expr newRight)
	{
		right = newRight;
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
		if(index <= left.size())
			return left.nodeAt(index - 1);
		else
			return right.nodeAt(index - left.size() - 1);
	}
	
	@Override
	public BinaryExpr clone()
	{
		Expr tempLeft = left.clone();
		Expr tempRight = right.clone();
		return new BinaryExpr(tempLeft, operator, tempRight);
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
	public boolean replaceChildWith(Node child, Node replacement)
	{
		if(child == this.left)
		{
			this.left = (Expr) replacement;
			left.setParent(this);
			return true;
		}
		else if(child == this.right)
		{
			this.right = (Expr) replacement;
			right.setParent(this);
			return true;
		}
		System.out.println("You messed up RCW in BinaryExpr."); //TODO remove when done testing
		return false;
	}
	
	@Override
	public Node searchChildrenForType(Node model)
	{
		if(left.getType() == model.getType())
			return left;
		else if(right.getType() == model.getType())
			return right;
		return null;
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
	public int evaluate()
	{
		throw new UnsupportedOperationException();
	}

	/** Enumerates all the accepted binary mathematical operations. */
	public enum MathOp
	{
		ADD, SUBTRACT, MULTIPLY, DIVIDE, MOD;
	}

	@Override
	public NodeType getType()
	{
		return NodeType.BINARYEXPR;
	}
}