package ast;

/** A critter program expression that has an integer value. */
public interface Expr extends Node
{
	
	/** Returns the integer value of this expression. */
	public int evaluateNode(); // to be done in A5
}
