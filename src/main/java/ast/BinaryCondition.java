package ast;

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
	}
	
	public Condition getLeft()
	{
		return left;
	}
	public void setLeft(Condition newLeft)
	{
		left = newLeft;
	}
	public Condition getRight()
	{
		return right;
	}
	public void setRight(Condition newRight)
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
	public BinaryCondition clone()
	{
		Condition tempLeft = left.clone();
		Condition tempRight = right.clone();
		return new BinaryCondition(tempLeft, op, tempRight);
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
	public boolean evaluate()
	{
		throw new UnsupportedOperationException();
	}

	/** An enumeration of all possible binary condition operators. */
	public enum Operator
	{
		OR, AND;
	}
}