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
	
	boolean mutate(ProgramImpl n);
	
	boolean mutate(Rule r);
	
	boolean mutate(BinaryCondition c);
	
	boolean mutate(Command c);
	
	boolean mutate(Update u);
	
	boolean mutate(Action a);
	
	boolean mutate(Relation r);
	
	boolean mutate(BinaryExpr be);
	
	boolean mutate(UnaryExpr ue);
	
	boolean mutate(Sensor s);
}