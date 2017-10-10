package ast;

/** A representation of a critter rule, which is composed of a condition and a command. */
public class Rule extends AbstractNode
{
	private Condition cond;
	private Command comm;
	
	public Rule(Condition c1, Command c2)
	{
		cond = c1;
		comm = c2;
	}
	
	@Override
	public int size()
	{
		return 1 + cond.size() + comm.size();
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
		sb.append(cond.toString() + " --> " + comm.toString() + ";\n");
		return sb;
	}
}