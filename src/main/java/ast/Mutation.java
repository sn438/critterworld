package ast;

/** A mutation to the AST. */
public interface Mutation
{
	/**
	 * Compares the type of this mutation to {@code m}
	 * @param m - The mutation to compare with
	 * @return Whether this mutation is the same type as {@code m}
	 */
	boolean equals(Mutation m);
	
	void mutate(ProgramImpl p);
	
	void mutate(Rule r);
	
	void mutate(BinaryCondition c);
	
	void mutate(Command comm);
	
	void mutate(Update u);
	
	void mutate(Action a);
	
	void mutate(Relation r);
	
	void mutate(BinaryExpr be);
	
	void mutate(UnaryExpr ue);
	
	void mutate(Sensor s);
}