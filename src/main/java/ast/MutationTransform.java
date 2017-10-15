package ast;

public class MutationTransform implements Mutation
{
	public boolean equals(Mutation m)
	{
		// TODO Auto-generated method stub
		return false;
	}	
	
	public boolean mutate(Node n)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(ProgramImpl p)
	{
		// TODO Auto-generated method stub
		return false;
	}

	public boolean mutate(Rule r)
	{
		Node parent = r.getParent();
		return false;
	}
	
	public boolean mutate(BinaryCondition c)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(Command comm)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	public boolean mutate(Update u)
	{
		// TODO Auto-generated method stub
		return false;
	}

	
	public boolean mutate(Action a)
	{
		// TODO Auto-generated method stub
		return false;
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
}