package ast;

public class MutationSwap extends AbstractMutation
{
	public MutationSwap(boolean p)
	{
		super(p);
	}
	
	@Override
	public boolean equals(Mutation m)
	{
		return (m instanceof MutationSwap);
	}
	
	@Override
	public boolean mutate(ProgramImpl p)
	{
		if(p.getRulesList().size() == 0)
			return false;
		int firstIndex = (int) Math.random() * p.getRulesList().size();
		int secondIndex = (int) Math.random() * p.getRulesList().size();
		
		Rule temp = p.getRulesList().get(firstIndex);
		p.getRulesList().add(secondIndex, temp);
		temp = p.getRulesList().remove(secondIndex + 1);
		p.getRulesList().add(firstIndex, temp);
		p.getRulesList().remove(firstIndex + 1);
		if(printMutationDetail)
			System.out.println("Swapped for the ProgramImpl node\n" + p + "\n");
		return true;
	}
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		Condition temp = c.getLeft();
		c.setLeft(c.getRight());
		c.setRight(temp);
		if(printMutationDetail)
			System.out.println("Swapped for the BinaryCondition node\n" + c + "\n");
		return true;
	}

	@Override
	public boolean mutate(Command comm)
	{
		if(comm.getUpdateList().size() == 0)
			return false;
		
		int firstIndex = (int) (Math.random() * comm.getUpdateList().size());
		int secondIndex = (int) (Math.random() * comm.getUpdateList().size());
		Update temp = comm.getUpdateList().get(firstIndex);
		comm.getUpdateList().add(secondIndex, temp);
		temp = comm.getUpdateList().remove(secondIndex + 1);
		comm.getUpdateList().add(firstIndex, temp);
		comm.getUpdateList().remove(firstIndex + 1);
		if(printMutationDetail)
			System.out.println("Swapped for the Command node\n" + comm + "\n");
		return true;
	}
	
	@Override
	public boolean mutate(Update u)
	{
		Expr temp = u.getMemIndex();
		u.setMemIndex(u.getValue());
		u.setValue(temp);
		if(printMutationDetail)
			System.out.println("Swapped for the Update node\n" + u + "\n");
		return true;
	}

	@Override
	public boolean mutate(Relation r)
	{
		if(r.isCond())
			return false;
		Expr temp = r.getLeft();
		r.setLeft(r.getRight());
		r.setRight(temp);
		if(printMutationDetail)
			System.out.println("Swapped the Relation node\n" + r + "\n");
		return true;
	}

	@Override
	public boolean mutate(BinaryExpr be)
	{
		Expr temp = be.getLeft();
		be.setLeft(be.getRight());
		be.setRight(temp);
		if(printMutationDetail)
			System.out.println("Swapped the BinaryExpr node\n" + be + "\n");
		return true;
	}

	//Unsupported methods, which defaultly return false
	@Override
	public boolean mutate(Rule r)
	{
		return false;
	}
	@Override
	public boolean mutate(Action a)
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