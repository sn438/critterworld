package ast;

/** An interface representing a Boolean condition in a critter program. */
public interface Condition extends Node
{
	/** Returns the boolean value of this condition. */
	public boolean evaluate(); // to be done in A5
}
