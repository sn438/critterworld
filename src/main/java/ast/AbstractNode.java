package ast;

/** A class that stores implementations of some of the shared methods of all the node subclasses. */
public abstract class AbstractNode implements Node
{	
	@Override
	public abstract int size();
	
	@Override
	public abstract Node nodeAt(int index);

	@Override
	public abstract StringBuilder prettyPrint(StringBuilder sb);
	
	@Override
	public String toString()
	{
		return prettyPrint(new StringBuilder()).toString();
	}
}
