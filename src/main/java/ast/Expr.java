package ast;

import interpret.Interpreter;

/** A critter program expression that has an integer value. */
public interface Expr extends Node
{
	/** Accepts an evaluation from Interpreter i. */
	public int acceptEvaluation(Interpreter i);
	
	/** Returns a deep copy of this expression. */
	public Expr clone();
}