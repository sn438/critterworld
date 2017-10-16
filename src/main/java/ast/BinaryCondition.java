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
		
		left.setParent(this);
		right.setParent(this);
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
		System.out.println("You messed up RCW in BinaryCondition."); //TODO remove when done testing
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
	public boolean evaluate()
	{
		throw new UnsupportedOperationException();
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