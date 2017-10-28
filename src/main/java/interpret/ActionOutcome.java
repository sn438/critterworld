package interpret;

import simulation.SimpleCritter;
import simulation.SimpleWorld;
import ast.Action;
import ast.Action.ActType;

public class ActionOutcome implements Outcome
{
	private ActType act;
	
	public ActionOutcome(ActType a)
	{
		act = a;
	}
	
	public ActionOutcome(ActType a, int val)
	{
		
	}
	
	@Override
	public void applyOutcome(SimpleCritter c, SimpleWorld w)
	{
		
		/*switch(act)
		{
			case FORWARD:
				w.moveCritter(c, true);
				break;
			case BACKWARD:
				w.moveCritter(c, false);
				break;
			case LEFT:
				c.turn(true);
				break;
			case RIGHT:
				c.turn(false);
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
		}*/
	}
}