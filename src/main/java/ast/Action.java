package ast;

import ast.Action.ActType;

/** An AST representation of a critter action. */
public class Action extends AbstractNode implements CommandComponent
{

	/** The type of action that this node stores. */
	private ActType act;
	private Expr val;
	
	/** Creates an Action node that doesn't involve an Expr node. Not compatible with ActTypes TAG and SERVE. */
	public Action(ActType a)
	{
		act = a;
		val = null;
	}
	
	/** Creates an Action node that is linked with an Expr node. Compatible ActTypes: TAG and SERVE. */
	public Action(ActType a, Expr e)
	{
		act = a;
		val = e;
		val.setParent(this);
	}
	
	@Override
	public int size()
	{
		if(act == ActType.TAG || act == ActType.SERVE)
			return 1 + val.size();
		return 1;
	}
	
	@Override
	public Node nodeAt(int index)
	{
		if(index == 0)
			return this;
		if(index > size() - 1 || index < 0)
			throw new IndexOutOfBoundsException();
		return val.nodeAt(index - 1);
	}
	
	@Override
	public Action clone()
	{
		if(act == ActType.TAG || act == ActType.SERVE)
		{
			Expr tempVal = val.clone();
			return new Action(act, tempVal);
		}
		return new Action(act);
	}
	@Override
	public boolean acceptMutation(Mutation m)
	{
		try
		{
			boolean result = m.mutate(this);
			return result;
		}
		catch(UnsupportedOperationException u)
		{
			return false;
		}
	}
	
	@Override
	public boolean replaceChild(Node child, Node replacement)
	{
		if(act == ActType.TAG || act == ActType.SERVE)
			return false;
		val = (Expr) replacement;
		val.setParent(this);
		return true;
	}
	
	@Override
	public Node searchChildrenForSimilarType()
	{
		return null;
	}
	
	@Override
	public StringBuilder prettyPrint(StringBuilder sb)
	{
		switch(act)
		{
			case WAIT:
				sb.append("wait");
				break;
			case FORWARD:
				sb.append("forward");
				break;
			case BACKWARD:
				sb.append("backward");
				break;
			case LEFT:
				sb.append("left");
				break;
			case RIGHT:
				sb.append("right");
				break;
			case EAT:
				sb.append("eat");
				break;
			case ATTACK:
				sb.append("attack");
				break;
			case GROW:
				sb.append("grow");
				break;
			case BUD:
				sb.append("bud");
				break;
			case MATE:
				sb.append("mate");
				break;
			case TAG:
				sb.append("tag[" + val.toString() + "]");
				break;
			case SERVE: 
				sb.append("serve[" + val.toString() + "]");
				break;
		}
		return sb;
	}
	
	public void setActType(ActType at) {
		if (!(at.equals(ActType.TAG) || at.equals(ActType.SERVE))){
			this.val = null;
		}
		this.act = at;
	}
	/** An enumeration of all the possible action types. */
	public enum ActType
	{
		WAIT, FORWARD, BACKWARD, LEFT, RIGHT, EAT, ATTACK, GROW, BUD, MATE, TAG, SERVE;
	}

	@Override
	public NodeType getType()
	{
		return NodeType.ACTION;
	}
}