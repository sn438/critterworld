package ast;

public class MutationDuplicate implements Mutation
{
	public boolean equals(Mutation m)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(ProgramImpl p)
	{
		Rule copy = null;
		int size = p.size();
		int rand = (int) (Math.random() * size) + 1;
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(p.nodeAt(index) instanceof Rule)
			{
				copy = (Rule) (p.nodeAt(index)).clone();
				break;
			}
				
		}
		if(copy == null)
			return false;
		p.getRulesList().add(copy);
		return true;
	}
	public boolean mutate(Command comm)
	{
		Node root = comm.getParent();
		Update copy = null;
		while(root.getParent() != null)
			root = root.getParent();
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index) instanceof Update)
			{
				copy = (Update) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		comm.getUpdateList().add(copy);
		return true;
	}
	
	//Unsupported methods, which return false by default
	public boolean mutate(Rule r)
	{
		return false;
	}
	public boolean mutate(BinaryCondition c)
	{
		return false;
	}
	public boolean mutate(Update u)
	{
		return false;
	}
	public boolean mutate(Action a)
	{
		return false;
	}
	public boolean mutate(Relation r)
	{
		return false;
	}
	public boolean mutate(BinaryExpr be)
	{
		return false;
	}
	public boolean mutate(UnaryExpr ue)
	{
		return false;
	}
	public boolean mutate(Sensor s)
	{
		return false;
	}
}