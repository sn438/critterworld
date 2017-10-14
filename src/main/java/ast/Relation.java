package ast;

/** A representation of a relational comparison between two numerical expressions. */
public class Relation extends AbstractNode implements Condition
{
	/** The left child of this node. */
	private Expr left;
	/** The operation to be performed on the two children. */
	private RelOp op;
	/** The right child of this node. */
	private Expr right;
	
	private Condition cond;
	
	/** Creates a relational comparison between two numerical expressions, representing l o r.*/
	public Relation(Expr l, RelOp o, Expr r)
	{
		this.left = l;
		this.op = o;
		this.right = r;
		this.cond = null;
	}
	
	/** Creates a Relation based on a condition. */
	public Relation(Condition c)
	{
		this.left = null;
		this.op = RelOp.ISCOND;
		this.right = null;
		this.cond = c;
	}
	
	@Override
	public int size()
	{
		if(op == RelOp.ISCOND)
			return 1 + cond.size();
		return 1 + left.size() + right.size();
	}
	
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		if(op == RelOp.ISCOND)
			return cond.nodeAt(index - 1);
		else
		{
			if(index <= left.size())
				return left.nodeAt(index - 1);
			else
				return right.nodeAt(index - left.size() - 1);
		}
	}
	
	@Override
	public Relation clone()
	{
		if(op == RelOp.ISCOND)
			return new Relation(cond.clone());
		Expr tempLeft = left.clone();
		Expr tempRight = right.clone();
		return new Relation(tempLeft, op, tempRight);
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{	
		switch(op)
		{
			case LESS:
				sb.append(left.toString() + " < " + right.toString());
				break;
			case LESSOREQ:
				sb.append(left.toString() + " <= " + right.toString());
				break;
			case GREATER:
				sb.append(left.toString() + " > " + right.toString());
				break;
			case GREATEROREQ:
				sb.append(left.toString() + " >= " + right.toString());
				break;
			case EQUAL:
				sb.append(left.toString() + " = " + right.toString());
				break;
			case NOTEQUAL:
				sb.append(left.toString() + " != " + right.toString());
				break;
			case ISCOND:
				sb.append("{" + cond.toString() + "}");
		}
		return sb;
	}
	@Override
	public boolean evaluate()
	{
		throw new UnsupportedOperationException();
	}
	/** An enumeration of all the accepted mathematical relational operators. */
	public enum RelOp
	{
		LESS, LESSOREQ, GREATER, GREATEROREQ, EQUAL, NOTEQUAL, ISCOND;
	}
}