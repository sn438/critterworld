package ast;

/** One possible component of a Command Node. */
public interface CommandComponent extends Node
{
	/** Returns a deep copy of this command component. */
	public CommandComponent clone();
}