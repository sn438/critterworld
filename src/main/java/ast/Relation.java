package ast;

/** A representation of a relational comparison between two numerical expressions. */
public class Relation extends AbstractNode
{
	/** */
	public Relation(Expr l, RelOp o, Expr r)
	{
		
	}
	
	public Relation(Condition c)
	{
		
	}
	
	/** An enumeration of all the accepted mathematical relational operators. */
	public enum RelOp
	{
		LESS, LESSOREQ, GREATER, GREATEROREQ, EQUAL, NOTEQUAL;
	}
}