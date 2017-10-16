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
		for(Rule r : RulesList)
			r.setParent(this);
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
	public boolean replaceChildWith(Node child, Node replacement)
	{
		for(int i = 0; i < RulesList.size(); i++)
		{
			if(child == RulesList.get(i))
			{
				RulesList.set(i, (Rule) replacement);
				RulesList.get(i).setParent(this);
				return true;
			}
		}
		System.out.println("You messed up RCW in Command"); //TODO remove when done testing
		return false;
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

	@Override
	public NodeType getType()
	{
		return NodeType.PROGRAM;
	}

	@Override
	public Node searchChildrenForType(Node model) {
		// TODO Auto-generated method stub
		return null;
	}
}