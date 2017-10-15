package ast;

import java.util.LinkedList;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program
{
	/** RulesList will contain all the rules detailed in the program. */
	private LinkedList<Rule> RulesList;

	/** Creates a new Program node with the given list of rules. */
	public ProgramImpl(LinkedList<Rule> rl)
	{
		RulesList = rl;
	}
	
	public LinkedList<Rule> getRulesList()
	{
		return RulesList;
	}

	@Override
	public int size()
	{
		int result = 1;
		for(Rule r : RulesList)
			result += r.size();
		return result;
	}

	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		for(Rule r : RulesList)
		{
			if(index >= r.size())
			{
				index -= r.size();
				break;
			}
			return r.nodeAt(index - 1);
		}
		throw new IndexOutOfBoundsException();
	}

	@Override
	public ProgramImpl clone()
	{
		LinkedList<Rule> tempRL = new LinkedList<Rule>();
		for(Rule r : RulesList)
			tempRL.add(r.clone());
		return new ProgramImpl(tempRL);
	}
	
	@Override
	public Program mutate()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Program mutate(int index, Mutation m)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		for(Rule r : RulesList)
			sb.append(r.toString());
		return sb;
	}
}