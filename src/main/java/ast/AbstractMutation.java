package ast;

public abstract class AbstractMutation implements Mutation
{
	protected boolean printMutationDetail;
	
	public AbstractMutation(boolean p)
	{
		printMutationDetail = p;
	}
	@Override
	public abstract boolean equals(Mutation m);

	@Override
	public abstract boolean mutate(ProgramImpl p);

	@Override
	public abstract boolean mutate(Rule r);

	@Override
	public abstract boolean mutate(BinaryCondition c);

	@Override
	public abstract boolean mutate(Command c);

	@Override
	public abstract boolean mutate(Update u);

	@Override
	public abstract boolean mutate(Action a);

	@Override
	public abstract boolean mutate(Relation r);

	@Override
	public abstract boolean mutate(BinaryExpr be);

	@Override
	public abstract boolean mutate(UnaryExpr ue);

	@Override
	public abstract boolean mutate(Sensor s);
}