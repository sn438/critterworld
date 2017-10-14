package ast;

/** A representation of a critter rule, which is composed of a condition and a command. */
public class Rule extends AbstractNode
{
	/** The first part of a rule: the condition that, if met, leads to the execution of the command {@code comm}. */
	private Condition cond;
	/** The second part of a rule: the command that is executed if {@code cond} is met. */
	private Command comm;
	
	/** Creates a new rule with the given condition and command. */
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
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		if(index <= cond.size())
			return cond.nodeAt(index - 1);
		else
			return comm.nodeAt(index - cond.size() - 1);
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		sb.append(cond.toString() + " --> " + comm.toString() + ";\n\n");
		return sb;
	}
	
	@Override
	public Rule clone()
	{
		Condition tempCond = cond.clone();
		Command tempComm = comm.clone();
		return new Rule(tempCond, tempComm);
	}
}