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
		int firstIndex = (int) Math.random() * p.getRulesList().size();
		int secondIndex = (int) Math.random() * p.getRulesList().size();
		
		Rule temp = p.getRulesList().get(firstIndex);
		p.getRulesList().add(secondIndex, temp);
		temp = p.getRulesList().remove(secondIndex + 1);
		p.getRulesList().add(firstIndex, temp);
		p.getRulesList().remove(firstIndex + 1);
		return true;
	}
	
	@Override
	public boolean mutate(BinaryCondition c)
	{
		Condition temp = c.getLeft();
		c.setLeft(c.getRight());
		c.setRight(temp);
		return true;
	}

	@Override
	public boolean mutate(Command comm)
	{
		int firstIndex = (int) (Math.random() * comm.getUpdateList().size());
		int secondIndex = (int) (Math.random() * comm.getUpdateList().size());
		Update temp = comm.getUpdateList().get(firstIndex);
		comm.getUpdateList().add(secondIndex, temp);
		temp = comm.getUpdateList().remove(secondIndex + 1);
		comm.getUpdateList().add(firstIndex, temp);
		comm.getUpdateList().remove(firstIndex + 1);
		return true;
	}
	
	@Override
	public boolean mutate(Update u)
	{
		Expr temp = u.getMemIndex();
		u.setMemIndex(u.getValue());
		u.setValue(temp);
		return true;
	}

	@Override
	public boolean mutate(Relation r)
	{
		if(r.isCond())
			throw new UnsupportedOperationException();
		Expr temp = r.getLeft();
		r.setLeft(r.getRight());
		r.setRight(temp);
		return true;
	}

	@Override
	public boolean mutate(BinaryExpr be)
	{
		Expr temp = be.getLeft();
		be.setLeft(be.getRight());
		be.setRight(temp);
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