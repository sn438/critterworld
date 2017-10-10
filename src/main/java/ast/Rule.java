package ast;

/** A representation of a critter rule. */
public class Rule extends AbstractNode
{
	private Condition condition;
	
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
	
	public void setCondition(Condition condition) {
		this.condition = condition;
	}
}