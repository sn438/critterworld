package ast;

/** A representation of a relational comparison between two numerical expressions. */
public class Relation extends AbstractNode
{
<<<<<<< HEAD
	/** */
	public Relation(Expr l, RelOp o, Expr r)
	{
		
=======
	/** The left child of this node. */
	private Expr left;
	/** The operation to be performed on the two children. */
	private RelOp op;
	/** The right child of this node. */
	private Expr right;
	
	/** */
	public Relation(Expr l, RelOp o, Expr r)
	{
		this.left = l;
		this.op = o;
		this.right = r;
>>>>>>> bcbac9a71a41a75cc4a622474fdf2d9fa785a8ad
	}
	
	public Relation(Condition c)
	{
		
	}
	
<<<<<<< HEAD
=======
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		return null;
	}
	
>>>>>>> bcbac9a71a41a75cc4a622474fdf2d9fa785a8ad
	/** An enumeration of all the accepted mathematical relational operators. */
	public enum RelOp
	{
		LESS, LESSOREQ, GREATER, GREATEROREQ, EQUAL, NOTEQUAL;
	}
}