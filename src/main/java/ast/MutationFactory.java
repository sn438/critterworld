package ast;

/** A factory that produces the public static Mutation objects corresponding to each mutation. */
public class MutationFactory
{
	public static Mutation getRemove(boolean b)
	{
		return new MutationRemove(b);
	}

	public static Mutation getSwap(boolean b)
	{
		return new MutationSwap(b);
	}

	public static Mutation getReplace(boolean b)
	{
		return new MutationReplace(b);
	}

	public static Mutation getTransform(boolean b)
	{
		return new MutationTransform(b);
	}

	public static Mutation getInsert(boolean b)
	{
		return new MutationInsert(b);
	}

	public static Mutation getDuplicate(boolean b)
	{
		return new MutationDuplicate(b);
	}
}