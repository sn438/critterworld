package ast;

import java.util.LinkedList;

/** A representation of a critter command. */
public class Command extends AbstractNode
{
	/** A LinkedList of all the possible updates stored in this Command node. */
	private LinkedList<Update> UpdateList;
	
	/** Points to the last node stored in this Command node, which can be either
	 *  an update or an action. */
	private CommandComponent last;
	
	/** Creates a new command with the given list of updates and one final CommandComponent. */
	public Command(LinkedList<Update> list, CommandComponent cc)
	{
		UpdateList = list;
		last = cc;
		for(Update u : UpdateList)
			u.setParent(this);
		last.setParent(this);
	}
	public LinkedList<Update> getUpdateList()
	{
		return UpdateList;
	}
	@Override
	public int size()
	{
		int result = 1;
		for(Update u : getUpdateList())
			result += u.size();
		result += last.size();
		return result;
	}
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		for(Update u : getUpdateList())
		{
			if(index > u.size())
				index -= u.size();
			else
				return u.nodeAt(index - 1);
		}
		return last.nodeAt(index - 1);
	}
	
	@Override
	public Command clone()
	{
		LinkedList<Update> tempUL = new LinkedList<Update>();
		for(Update u : getUpdateList())
			tempUL.add(u.clone());
		CommandComponent tempLast = last.clone();
		return new Command(tempUL, tempLast);
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
		for(int i = 0; i < UpdateList.size(); i++)
			if(child == UpdateList.get(i))
			{
				if(replacement == null)
					UpdateList.remove(i);
				else
				{
					UpdateList.set(i, (Update) replacement);
					UpdateList.get(i).setParent(this);
				}
				return true;
			}
		if(child == last)
		{
			if(replacement == null)
			{
				last = UpdateList.removeLast();
				last.setParent(this);
				return true;
			}
			else
			{
				last = (CommandComponent) replacement;
				last.setParent(this);
			}
		}
		System.out.println("You messed up RCW in Command"); //TODO remove when done testing
		return false;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		return null;
	}
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		for(Update u : getUpdateList())
			sb.append(u.toString() + "\n");
		sb.append(last.toString());
		return sb;
	}
	@Override
	public NodeType getType()
	{
		return NodeType.COMMAND;
	}
}