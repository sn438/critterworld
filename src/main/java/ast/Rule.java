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
		cond.setParent(this);
		comm.setParent(this);
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
	public Rule clone()
	{
		Condition tempCond = cond.clone();
		Command tempComm = comm.clone();
		return new Rule(tempCond, tempComm);
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
		if(child == this.cond)
		{
			this.cond = (Condition) replacement;
			cond.setParent(this);
			return true;
		}
		else if(child == this.comm)
		{
			this.comm = (Command) replacement;
			comm.setParent(this);
			return true;
		}
		System.out.println("You messed up RCW in Rule."); //TODO remove when done testing
		return false;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		//Since no child of a rule node will be of type Rule, this method is not supported by this class.
		return null;
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		sb.append(cond.toString() + " --> " + comm.toString() + ";\n\n");
		return sb;
	}

	@Override
	public NodeType getType()
	{
		return NodeType.RULE;
	}
}