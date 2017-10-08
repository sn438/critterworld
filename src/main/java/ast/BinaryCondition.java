package ast;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends AbstractNode implements Condition
{

	private Condition left;
	private Operator op;
	private Condition right;
	
	/**
	 * Create an AST representation of l op r.
	 * @param l
	 * @param op
	 * @param r
	 */
	public BinaryCondition(Condition l, Operator o, Condition r)
	{
		this.left = l;
		this.op = o;
		this.right = r;
	}

	@Override
	public int size()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Node nodeAt(int index)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		// TODO Auto-generated method stub
		return null;
	}

	/** An enumeration of all possible binary condition operators. */
	public enum Operator
	{
		OR, AND;
	}
}
