package ast;

/** A node in the abstract syntax tree of a program. */
public interface Node
{
	/** Returns a parent pointer for this node. */
	Node getParent();
	
	/** Sets the parent for this node. */
	void setParent(Node par);
	
	/**
	 * The number of nodes in the AST rooted at this node, including this node
	 * @return The size of the AST rooted at this node
	 */
	int size();

	/**
	 * Returns the node at {@code index} in the AST rooted at this node. Indices
	 * are defined such that:<br>
	 * 1. Indices are in the range {@code [0, size())}<br>
	 * 2. {@code this.nodeAt(0) == this} for all nodes in the AST <br>
	 * 3. All nodes in the AST rooted at {@code this} must be reachable by a call
	 * to {@code this.nodeAt(i)} with an appropriate index {@code i}
	 * 
	 * @param index - The index of the node to retrieve
	 * @return The node at {@code index}
	 * @throws IndexOutOfBoundsException if {@code index} is not in the range of valid indices
	 */
	Node nodeAt(int index);

	/** Returns a deep copy of this node. */
	Node clone();
	
	/**
	 * Accepts a mutation to this node.
	 * @param m the type of mutation that this node will accept
	 * @return whether or not this node can accept Mutation {@code m}
	 */
	boolean acceptMutation(Mutation m);
	
	/**
	 * Replaces one child of this node with another node.<br>
	 * Precondition: {@code child} is a child of {@code this}, and {@code replacement} must be a valid child of {@code this}
	 * @param child the child of this node to be replaced
	 * @param replacement the node that will replace {@code child}
	 * @return whether or not this node has children to be replaced
	 */
	boolean replaceChild(Node child, Node replacement);
	
	/**
	 * Searches and returns a direct child of this node that is of a similar type.
	 * Not supported for nodes of type Program, Rule, Command, Update, Action, or Relation.
	 * @return the child of a similar type, {@code null} if none found or this node has no children
	 */
	Node searchChildrenForSimilarType();
	
	/**
	 * Appends the program represented by this node prettily to the given StringBuilder.
	 * <p>
	 * The output of this method must be consistent with both the critter grammar
	 * and itself; that is:<br>
	 * 1. It must be possible to put the result of this method into a valid
	 * critter program<br>
	 * 2. Placing the result of this method into a valid critter program then
	 * parsing the program must yield an AST which contains a subtree identical
	 * to the one rooted at {@code this}
	 * 
	 * @param sb - The {@code StringBuilder} to which the program will be appended
	 * @return The {@code StringBuilder} to which this program was appended
	 */
	StringBuilder prettyPrint(StringBuilder sb);

	/**
	 * Returns the pretty-print of the abstract syntax subtree rooted at this
	 * node.
	 * <p>
	 * This method returns the same result as {@code prettyPrint(...).toString()}
	 * 
	 * @return The pretty-print of the AST rooted at this node.
	 */
	@Override
	String toString();
	
	/** An enumeration of all the node types. */
	public enum NodeType
	{
		PROGRAM, RULE, BINARYCONDITION, COMMAND, UPDATE, ACTION, RELATION, BINARYEXPR, UNARYEXPR, SENSOR;
	}
	
	/** Returns the type of this node. */
	public NodeType getType();
}