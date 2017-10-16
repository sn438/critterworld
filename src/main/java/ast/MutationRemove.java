package ast;

public class MutationRemove implements Mutation
{
	public boolean equals(Mutation m)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mutate(Rule r)
	{
		Node parent = r.getParent();
		parent.replaceChild(r, null);
		return true;
	}
	
	public boolean mutate(BinaryCondition c)
	{
		Node parent = c.getParent();
		Node replacement = c.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(c, replacement);
		return true;
	}
	
	public boolean mutate(Update u)
	{
		Command parent = (Command) u.getParent();
		if(parent.getUpdateList().size() == 0)
			return false;
		parent.replaceChild(u, null);
		return true;
	}

	public boolean mutate(Action a)
	{
		Command parent = (Command) a.getParent();
		if(parent.getUpdateList().size() == 0)
			return false;
		parent.replaceChild(a, null);
		return true;
	}
	
	public boolean mutate(Relation r)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mutate(BinaryExpr be)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(UnaryExpr ue)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(Sensor s)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	//Unsupported methods
	public boolean mutate(ProgramImpl p)
	{
		// TODO Auto-generated method stub
		return false;
	}
	public boolean mutate(Command comm)
	{
		// TODO Auto-generated method stub
		return false;
	}
}