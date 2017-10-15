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
	public boolean mutate(Node n)
	{
		return false;
	}
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

	public boolean mutate(BinaryCondition c)
	{
		Condition temp = c.getLeft();
		c.setLeft(c.getRight());
		c.setRight(temp);
		return true;
	}

	public boolean mutate(Command comm)
	{
		int firstIndex = (int) Math.random() * comm.getUpdateList().size();
		int secondIndex = (int) Math.random() * comm.getUpdateList().size();
		
		Update temp = comm.getUpdateList().get(firstIndex);
		comm.getUpdateList().add(secondIndex, temp);
		temp = comm.getUpdateList().remove(secondIndex + 1);
		comm.getUpdateList().add(firstIndex, temp);
		comm.getUpdateList().remove(firstIndex + 1);
		return true;
	}

	public boolean mutate(Update u)
	{
		Expr temp = u.getMemIndex();
		u.setMemIndex(u.getValue());
		u.setValue(temp);
		return true;
	}

	public boolean mutate(Relation r)
	{
		if(r.isCond())
			throw new UnsupportedOperationException();
		Expr temp = r.getLeft();
		r.setLeft(r.getRight());
		r.setRight(temp);
		return true;
	}

	public boolean mutate(BinaryExpr be)
	{
		Expr temp = be.getLeft();
		be.setLeft(be.getRight());
		be.setRight(temp);
		return true;
	}

	public boolean mutate(Rule r)
	{
		throw new UnsupportedOperationException();
	}
	public boolean mutate(Action a)
	{
		throw new UnsupportedOperationException();
	}
	public boolean mutate(UnaryExpr ue)
	{
		throw new UnsupportedOperationException();
	}
	public boolean mutate(Sensor s)
	{
		throw new UnsupportedOperationException();
	}
}