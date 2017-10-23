package ast;

import interpret.Interpreter;

/** A representation of a relational comparison between two numerical expressions. */
public class Relation extends AbstractNode implements Condition
{
	/** The left child of this node. */
	private Expr left;
	/** The operation to be performed on the two children. */
	private RelOp op;
	/** The right child of this node. */
	private Expr right;
	/** The condition contained in this relation, if there is one. */
	private Condition cond;
	
	/** Creates a relational comparison between two numerical expressions, representing l op r.*/
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

	/** 
	 * Returns the left child of this relation. 
	 * Precondition: {@code this.op != ISCOND}
	 */
	public Expr getLeft()
	{
		return left;
	}
	
	/** Sets the value of {@code left} to {@code newLeft}. */
	public void setLeft(Expr newLeft)
	{
		left = newLeft;
		left.setParent(this);
	}
	
	/** 
	 * Returns the right child of this relation. 
	 * Precondition: {@code this.op != ISCOND}
	 */
	public Expr getRight()
	{
		return right;
	}
	
	/** Sets the value of {@code right} to {@code newRight}. */
	public void setRight(Expr newRight)
	{
		right = newRight;
		right.setParent(this);
	}
	
	/** Returns a boolean based on whether or not this relation node contains a condition. */
	public boolean isCond()
	{
		return op == RelOp.ISCOND;
	}
	
	/**
	 * Returns the condition this relation contains.
	 * Precondition: {@code this.op == ISCOND}
	 */
	public Condition getCond()
	{
		return cond;
	}
	
	/** Returns the type of this Relation. */
	public RelOp getRelOp()
	{
		return op;
	}
	/** Sets the value of {@code op} to {@code ro}, if {@code op} is not equal to ISCOND. */
	public void setRelOp(RelOp ro)
	{
		if (isCond())
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
		if(isCond())
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
		if(isCond())
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
		if(isCond())
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
		if(isCond())
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
	public boolean acceptEvaluation(Interpreter i)
	{
		return i.eval(this);
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