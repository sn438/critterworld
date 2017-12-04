package ast;

import interpret.Interpreter;

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
	/** Returns the left child of this binary expression. */
	public Expr getLeft()
	{
		return left;
	}
	/** Sets the value of {@code left} to {@code newLeft}. */
	public void setLeft(Expr newLeft)
	{
		left = newLeft;
		left.setParent(this);
	}
	/** Returns the right child of this binary expression. */
	public Expr getRight()
	{
		return right;
	}
	/** Sets the value of {@code right} to {@code newRight}. */
	public void setRight(Expr newRight)
	{
		right = newRight;
		right.setParent(this);
	}
	/** Returns the operator type of this binary expression. */
	public MathOp getOperator()
	{
		return operator;
	}
	/** Sets the value of {@code operator} to {@code op}. */
	public void setOperator(MathOp op)
	{
		this.operator = op;
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
		boolean result = m.mutate(this);
		return result;
	}
	
	@Override
	public boolean replaceChild(Node child, Node replacement)
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
		return false;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		if(left.getType() == NodeType.BINARYEXPR || left.getType() == NodeType.UNARYEXPR || left.getType() == NodeType.SENSOR)
			return left;
		else if(right.getType() == NodeType.BINARYCONDITION || right.getType() == NodeType.RELATION || right.getType() == NodeType.SENSOR)
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
	public int acceptEvaluation(Interpreter i)
	{
		return i.eval(this);
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