package ast;

import interpret.Interpreter;

/** An interface representing a Boolean condition in a critter program. */
public interface Condition extends Node
{
	/** Accepts an evaluation from an Interpreter. */
	public boolean acceptEvaluation(Interpreter i);
	
	/** Returns a deep copy of this condition. */
	public Condition clone();
}