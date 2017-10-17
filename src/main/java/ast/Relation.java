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
		
		left.setParent(this);
		right.setParent(this);
	}
	
	/** Creates a Relation based on a condition. */
	public Relation(Condition c)
	{
		this.left = null;
		this.op = RelOp.ISCOND;
		this.right = null;
		this.cond = c;
		
		cond.setParent(this);
	}

	public Expr getLeft()
	{
		return left;
	}
	public void setLeft(Expr newLeft)
	{
		left = newLeft;
		left.setParent(this);
	}
	public Expr getRight()
	{
		return right;
	}
	public void setRight(Expr newRight)
	{
		right = newRight;
		right.setParent(this);
	}
	public boolean isCond()
	{
		return op == RelOp.ISCOND;
	}
	
	public void setRelOp(RelOp ro)
	{
		if (this.op.equals(RelOp.ISCOND))
			return;
		if (ro.equals(RelOp.ISCOND))
		{
			if (this.cond == null)
			{
				Condition c = new Relation(this.left, this.op, this.right);
				this.cond = c;
				this.op = RelOp.ISCOND;
			}
			else if (this.cond != null)
				this.op = RelOp.ISCOND;
		}
		else
			this.op = ro;
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
	public boolean acceptMutation(Mutation m)
	{
		boolean result = m.mutate(this);
		return result;
	}
	@Override
	public boolean replaceChild(Node child, Node replacement)
	{
		if(op == RelOp.ISCOND)
		{
			if(child == this.cond)
			{
				cond = (Condition) replacement;
				cond.setParent(this);
				return true;
			}
			else
				return false;
		}
		
		if(child == this.left)
		{
			this.left = (Expr) replacement;
			left.setParent(this);
			return true;
		}
		else if(child == this.right)
		{
			this.right = (Expr) replacement;
			right.setParent(this);
			return true;
		}
		return false;
	}
	@Override
	public Node searchChildrenForSimilarType()
	{
		// Since no child of a relation node will be of type relation, this method is unsupported.
		return null;
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
	@Override
	public NodeType getType()
	{
		return NodeType.RELATION;
	}
}