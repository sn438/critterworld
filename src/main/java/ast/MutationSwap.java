package ast;

public class MutationSwap implements Mutation
{

	@Override
	public boolean equals(Mutation m)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void mutate(ProgramImpl p)
	{
		int firstIndex = (int) Math.random() * p.getRulesList().size();
		int secondIndex = (int) Math.random() * p.getRulesList().size();
		
		Rule temp = p.getRulesList().get(firstIndex);
		p.getRulesList().add(secondIndex, temp);
		temp = p.getRulesList().remove(secondIndex + 1);
		p.getRulesList().add(firstIndex, temp);
		p.getRulesList().remove(firstIndex + 1);
	}

	@Override
	public void mutate(BinaryCondition c)
	{
		Condition temp = c.getLeft();
		c.setLeft(c.getRight());
		c.setRight(temp);
	}

	@Override
	public void mutate(Command comm)
	{
		int firstIndex = (int) Math.random() * comm.getUpdateList().size();
		int secondIndex = (int) Math.random() * comm.getUpdateList().size();
		
		Update temp = comm.getUpdateList().get(firstIndex);
		comm.getUpdateList().add(secondIndex, temp);
		temp = comm.getUpdateList().remove(secondIndex + 1);
		comm.getUpdateList().add(firstIndex, temp);
		comm.getUpdateList().remove(firstIndex + 1);
	}

	@Override
	public void mutate(Update u)
	{
		Expr temp = u.getMemIndex();
		u.setMemIndex(u.getValue());
		u.setValue(temp);
	}

	@Override
	public void mutate(Relation r)
	{
		if(r.isCond())
			throw new UnsupportedOperationException();
		Expr temp = r.getLeft();
		r.setLeft(r.getRight());
		r.setRight(temp);
	}

	@Override
	public void mutate(BinaryExpr be)
	{
		Expr temp = be.getLeft();
		be.setLeft(be.getRight());
		be.setRight(temp);
	}

	@Override
	public void mutate(Rule r)
	{
		throw new UnsupportedOperationException();
	}
	@Override
	public void mutate(Action a)
	{
		throw new UnsupportedOperationException();
	}
	@Override
	public void mutate(UnaryExpr ue)
	{
		throw new UnsupportedOperationException();
	}
	@Override
	public void mutate(Sensor s)
	{
		throw new UnsupportedOperationException();
	}
}