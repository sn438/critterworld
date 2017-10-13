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
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		sb.append(left.toString());
		sb.append(op == Operator.OR ? "or" : "and");
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