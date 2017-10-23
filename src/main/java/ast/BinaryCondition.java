package ast;

import interpret.Interpreter;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends AbstractNode implements Condition
{
	/** The left child of this node. */
	private Condition left;
	/** The operation to be performed on the two children. */
	private Operator op;
	/** The right child of this node. */
	private Condition right;
	
	/** Creates a BinaryCondition with the specified operands and boolean operator, representing l op r. */
	public BinaryCondition(Condition l, Operator o, Condition r)
	{
		this.left = l;
		this.op = o;
		this.right = r;
		
		left.setParent(this);
		right.setParent(this);
	}
	/** Returns the left child of this binary condition. */
	public Condition getLeft()
	{
		return left;
	}
	
	/** Sets the value of {@code left} to {@code newleft}. */
	public void setLeft(Condition newLeft)
	{
		left = newLeft;
		left.setParent(this);
	}
	
	/** Returns the right child of this binary condition. */
	public Condition getRight()
	{
		return right;
	}
	
	/** Sets the value of {@code right} to {@code newRight}. */
	public void setRight(Condition newRight)
	{
		right = newRight;
		right.setParent(this);
	}
	
	/** Returns the operator type of this binary condition. */
	public Operator getOp()
	{
		return op;
	}
	
	/** Sets the value of {@code op} to {@code o}. */
	public void setOp(Operator o)
	{
		this.op = o;
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
	public BinaryCondition clone()
	{
		Condition tempLeft = left.clone();
		Condition tempRight = right.clone();
		return new BinaryCondition(tempLeft, op, tempRight);
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
			this.left = (Condition) replacement;
			left.setParent(this);
			return true;
		}
		else if(child == this.right)
		{
			this.right = (Condition) replacement;
			right.setParent(this);
			return true;
		}
		return false;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		if(left.getType() == NodeType.BINARYCONDITION || left.getType() == NodeType.RELATION)
			return left;
		else if(right.getType() == NodeType.BINARYCONDITION || right.getType() == NodeType.RELATION)
			return right;
		return null;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		sb.append(left.toString());
		sb.append(op == Operator.OR ? " or " : " and ");
		sb.append(right.toString());
		return sb;
	}

	@Override
	public boolean acceptEvaluation(Interpreter i)
	{
		return i.eval(this);
	}

	/** An enumeration of all possible binary condition operators. */
	public enum Operator
	{
		OR, AND;
	}

	@Override
	public NodeType getType()
	{
		return NodeType.BINARYCONDITION;
	}
}