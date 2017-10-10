package ast;

public class Action extends AbstractNode implements CommandComponent
{

	/** The type of action that this node stores. */
	private ActType act;
	private Expr val;
	
	/** Creates an Action node that doesn't involve an Expr node. */
	public Action(ActType a)
	{
		act = a;
		val = null;
	}
	
	/** Creates an Action node that is linked with an Expr node. */
	public Action(ActType a, Expr e)
	{
		act = a;
		val = e;
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
			default:
				break;
		}
		return sb;
	}
	
	@Override
	public int size()
	{
		if(act == ActType.TAG || act == ActType.SERVE)
			return 1 + val.size();
		return 1;
	}
	
	/** An enumeration of all the possible action types. */
	public enum ActType
	{
		WAIT, FORWARD, BACKWARD, LEFT, RIGHT, EAT, ATTACK, GROW, BUD, MATE, TAG, SERVE;
	}
}