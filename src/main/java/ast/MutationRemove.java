package ast;

public class MutationRemove extends AbstractMutation
{
	public MutationRemove(boolean p)
	{
		super(p);
	}
	
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationRemove);
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
		Node parent = r.getParent();
		Node replacement = r.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(r, replacement);
		return true;
	}

	public boolean mutate(BinaryExpr be)
	{
		Node parent = be.getParent();
		Node replacement = be.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(be, replacement);
		return true;
	}
	
	public boolean mutate(UnaryExpr ue)
	{
		Node parent = ue.getParent();
		Node replacement = ue.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(ue, replacement);
		return true;
	}
	
	public boolean mutate(Sensor s)
	{
		Node parent = s.getParent();
		Node replacement = s.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(s, replacement);
		return true;
	}
	
	//Unsupported methods
	public boolean mutate(ProgramImpl p)
	{
		return false;
	}
	public boolean mutate(Command comm)
	{
		return false;
	}
}