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
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public String toString()
	{
		return prettyPrint(new StringBuilder()).toString();
	}
}
