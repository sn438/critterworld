package ast;

/** A representation of a relational comparison between two numerical expressions. */
public class Relation extends AbstractNode
{
	/** The left child of this node. */
	private Expr left;
	/** The operation to be performed on the two children. */
	private RelOp op;
	/** The right child of this node. */
	private Expr right;
	
	private boolean isCond;
	private Condition cond;
	
	/** */
	public Relation(Expr l, RelOp o, Expr r)
	{
		this.left = l;
		this.op = o;
		this.right = r;
		this.isCond = false;
		this.cond = null;
	}
	
	public Relation(Condition c)
	{
		this.left = null;
		this.op = null;
		this.right = null;
		this.isCond = true;
		this.cond = c;
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		if(isCond)
		{
			sb.append("{" + cond.toString() + "}");
			return sb;
		}
		
		sb.append(left.toString());
		switch(op)
		{
			case LESS:
				sb.append(" < ");
				break;
			case LESSOREQ:
				sb.append(" <= ");
				break;
			case GREATER:
				sb.append(" > ");
				break;
			case GREATEROREQ:
				sb.append(" >= ");
				break;
			case EQUAL:
				sb.append(" = ");
				break;
			case NOTEQUAL:
				sb.append(" != ");
				break;
			default:
				break;
		}
		sb.append(right.toString());
		return sb;
	}

	/** An enumeration of all the accepted mathematical relational operators. */
	public enum RelOp
	{
		LESS, LESSOREQ, GREATER, GREATEROREQ, EQUAL, NOTEQUAL;
	}
}