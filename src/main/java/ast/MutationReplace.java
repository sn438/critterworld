package ast;

import ast.Node.NodeType;

public class MutationReplace extends AbstractMutation
{
	public MutationReplace(boolean p)
	{
		super(p);
	}
	
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationReplace);
	}	

	@Override
	public boolean mutate(Rule r)
	{
		Node root = findRoot(r);
		Rule copy = null;

		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == r.getType())
			{
				copy = (Rule) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = r.getParent();
		parent.replaceChild(r, copy);
		return true;
	}
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		Node root = findRoot(c);
		Condition copy = null;
		
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == c.getType() || root.nodeAt(index).getType() == NodeType.RELATION)
			{
				copy = (Condition) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = c.getParent();
		parent.replaceChild(c, copy);
		return true;
	}
	
	@Override
	public boolean mutate(Command comm)
	{
		Node root = findRoot(comm);
		Command copy = null;
		
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == comm.getType())
			{
				copy = (Command) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = comm.getParent();
		parent.replaceChild(comm, copy);
		return true;
	}
	
	@Override
	public boolean mutate(Update u)
	{
		Node root = findRoot(u);
		Update copy = null;

		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == u.getType())
			{
				copy = (Update) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = u.getParent();
		parent.replaceChild(u, copy);
		return true;
	}
	
	@Override
	public boolean mutate(Action a)
	{
		Node root = findRoot(a);
		Action copy = null;
		
		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			//System.out.println(index + " " + rand + " " + i); //TODO remove when done testing
			if(root.nodeAt(index).getType() == a.getType())
			{
				copy = (Action) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = a.getParent();
		parent.replaceChild(a, copy);
		return true;
	}
	
	@Override
	public boolean mutate(Relation r)
	{
		Node root = findRoot(r);
		Condition copy = null;

		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == r.getType() || root.nodeAt(index).getType() == NodeType.BINARYCONDITION)
			{
				copy = (Condition) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = r.getParent();
		parent.replaceChild(r, copy);
		return true;
	}

	@Override
	public boolean mutate(BinaryExpr be)
	{
		Node root = findRoot(be);
		Expr copy = null;

		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == be.getType() || root.nodeAt(index).getType() == NodeType.UNARYEXPR 
															|| root.nodeAt(index).getType() == NodeType.SENSOR)
			{
				copy = (Expr) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = be.getParent();
		parent.replaceChild(be, copy);
		return true;
	}
	
	@Override
	public boolean mutate(UnaryExpr ue)
	{
		Node root = findRoot(ue);
		Expr copy = null;

		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == ue.getType() || root.nodeAt(index).getType() == NodeType.BINARYEXPR 
															|| root.nodeAt(index).getType() == NodeType.SENSOR)
			{
				copy = (Expr) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = ue.getParent();
		parent.replaceChild(ue, copy);
		return true;
	}
	
	@Override
	public boolean mutate(Sensor s)
	{
		Node root = findRoot(s);
		Expr copy = null;

		int size = root.size();
		int rand = (int) (Math.random() * size);
		int index;
		for(int i = 0; i < size; i++)
		{
			index = i + rand < size ? i + rand : i + rand - size;
			if(root.nodeAt(index).getType() == s.getType() || root.nodeAt(index).getType() == NodeType.UNARYEXPR 
															|| root.nodeAt(index).getType() == NodeType.BINARYEXPR)
			{
				copy = (Expr) (root.nodeAt(index)).clone();
				break;
			}
		}
		if(copy == null)
			return false;
		Node parent = s.getParent();
		parent.replaceChild(s, copy);
		return true;
	}
	
	//Unsupported method, which returns false by default
	@Override
	public boolean mutate(ProgramImpl p)
	{
		// TODO Auto-generated method stub
		return false;
	}
}