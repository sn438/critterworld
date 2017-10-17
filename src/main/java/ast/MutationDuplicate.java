package ast;

public class MutationDuplicate extends AbstractMutation
{	
	public MutationDuplicate(boolean p)
	{
		super(p);
	}
	
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationDuplicate);
	}
	
	@Override
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
		if(printMutationDetail)
			System.out.println("Duplicated the Rule node\n" + copy.toString() + "\n");
		return true;
	}
	
	@Override
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
		if(printMutationDetail)
			System.out.println("Duplicated the Update node\n" + copy.toString() + "\n");
		comm.getUpdateList().add(copy);
		return true;
	}
	
	//Unsupported methods, which return false by default
	@Override
	public boolean mutate(Rule r)
	{
		return false;
	}
	@Override
	public boolean mutate(BinaryCondition c)
	{
		return false;
	}
	@Override
	public boolean mutate(Update u)
	{
		return false;
	}
	@Override
	public boolean mutate(Action a)
	{
		return false;
	}
	@Override
	public boolean mutate(Relation r)
	{
		return false;
	}
	@Override
	public boolean mutate(BinaryExpr be)
	{
		return false;
	}
	@Override
	public boolean mutate(UnaryExpr ue)
	{
		return false;
	}
	@Override
	public boolean mutate(Sensor s)
	{
		return false;
	}
}