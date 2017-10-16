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
	
	/** 
	 * Mutates a single program node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(ProgramImpl p);
	
	/** 
	 * Mutates a single rule node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(Rule r);
	
	/** 
	 * Mutates a single binary condition node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(BinaryCondition c);
	
	/** 
	 * Mutates a single command node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(Command c);
	
	/** 
	 * Mutates a single update node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(Update u);
	
	/** 
	 * Mutates a single action node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(Action a);
	
	/** 
	 * Mutates a single relaion node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(Relation r);
	
	/** 
	 * Mutates a single binary expression node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(BinaryExpr be);
	
	/** 
	 * Mutates a single unary expression node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(UnaryExpr ue);
	
	/** 
	 * Mutates a single sensor node.
	 * @param p the ProgramImpl node to mutate
	 * @return whether this mutation is valid for the specified node
	 */
	boolean mutate(Sensor s);
}