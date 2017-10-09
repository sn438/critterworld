package ast;

public abstract class AbstractNode implements Node
{
	/** The number of nodes rooted at a node, not including itself. */
	protected int size;
	
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
	public abstract StringBuilder prettyPrint(StringBuilder sb);
	
	@Override
	public String toString()
	{
		return prettyPrint(new StringBuilder()).toString();
	}
}
