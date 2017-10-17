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

	@Override
	public boolean mutate(Rule r)
	{
		Node parent = r.getParent();
		parent.replaceChild(r, null);
		if(printMutationDetail)
			System.out.println("Removed the Rule node " + r.toString());
		return true;
	}
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		Node parent = c.getParent();
		Node replacement = c.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(c, replacement);
		if(printMutationDetail)
			System.out.println("Removed the binary condition node " + c.toString());
		return true;
	}
	
	@Override
	public boolean mutate(Update u)
	{
		Command parent = (Command) u.getParent();
		if(parent.getUpdateList().size() == 0)
			return false;
		parent.replaceChild(u, null);
		if(printMutationDetail)
			System.out.println("Removed the Update node " + u.toString());
		return true;
	}

	@Override
	public boolean mutate(Action a)
	{
		Command parent = (Command) a.getParent();
		if(parent.getUpdateList().size() == 0)
			return false;
		parent.replaceChild(a, null);
		if(printMutationDetail)
			System.out.println("Removed the Action node " + a.toString());
		return true;
	}
	
	@Override
	public boolean mutate(Relation r)
	{
		Node parent = r.getParent();
		Node replacement = r.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(r, replacement);
		if(printMutationDetail)
			System.out.println("Removed the Relation node " + r.toString());
		return true;
	}
	
	@Override
	public boolean mutate(BinaryExpr be)
	{
		Node parent = be.getParent();
		Node replacement = be.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(be, replacement);
		if(printMutationDetail)
			System.out.println("Removed the binary expression node " + be.toString());
		return true;
	}
	
	@Override
	public boolean mutate(UnaryExpr ue)
	{
		Node parent = ue.getParent();
		Node replacement = ue.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(ue, replacement);
		if(printMutationDetail)
			System.out.println("Removed the unary expression node " + ue.toString());
		return true;}
	
	@Override
	public boolean mutate(Sensor s)
	{
		Node parent = s.getParent();
		Node replacement = s.searchChildrenForSimilarType();
		if(replacement == null)
			return false;
		parent.replaceChild(s, replacement);
		if(printMutationDetail)
			System.out.println("Removed the Sensor node " + s.toString());
		return true;
	}
	
	//Unsupported methods
	@Override
	public boolean mutate(ProgramImpl p)
	{
		return false;
	}
	@Override
	public boolean mutate(Command comm)
	{
		return false;
	}
}