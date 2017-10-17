package ast;

/** A class that stores implementations of some of the shared methods of all the node subclasses. */
public abstract class AbstractNode implements Node
{	
	/** A pointer to the parent node of this node. May be null.*/
	protected Node parent;
	
	@Override
	public Node getParent()
	{
		return parent;
	}
	
	@Override
	public void setParent(Node par)
	{
		parent = par;
	}
	@Override
	public abstract int size();
	
	@Override
	public abstract Node nodeAt(int index);

	@Override
	public abstract StringBuilder prettyPrint(StringBuilder sb);
	
	@Override
	public abstract Node clone();
	
	@Override
	public String toString()
	{
		return prettyPrint(new StringBuilder()).toString();
	}
}
