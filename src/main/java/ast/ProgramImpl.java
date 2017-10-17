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
			if(index > r.size())
				index -= r.size();
			else
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
	public boolean replaceChild(Node child, Node replacement)
	{
		for(int i = 0; i < RulesList.size(); i++)
			if(child == RulesList.get(i))
			{
				if(replacement == null)
					RulesList.remove(i);
				else
				{
					RulesList.set(i, (Rule) replacement);
					RulesList.get(i).setParent(this);
				}
				return true;
			}
		//System.out.println("You messed up RCW in Command"); //TODO remove when done testing
		return false;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		//Since no child of a Program node will be another Program node, this method is unsupported.
		return null;
	}
	
	@Override
	public Program mutate()
	{
		int randIndex = (int) (Math.random() * this.size());
		Mutation m = null;
		Program valid = null;
		while(valid == null)
		{
			int rand = (int) (Math.random() * 6);
			switch(rand)
			{
				case 0:
					m = MutationFactory.getRemove(true);
					break;
				case 1:
					m = MutationFactory.getSwap(true);
					break;
				case 2:
					m = MutationFactory.getReplace(true);
					break;
				case 3:
					m = MutationFactory.getTransform(true);
					break;
				case 4:
					m = MutationFactory.getInsert(true);
					break;
				case 5:
					m = MutationFactory.getDuplicate(true);
					break;
			}
			valid = mutate(randIndex, m);
		}
		return valid;
	}

	@Override
	public Program mutate(int index, Mutation m)
	{
		ProgramImpl copy = this.clone();
		if(copy.nodeAt(index).acceptMutation(m))
			return copy;
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
}