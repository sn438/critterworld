package interpret;

import simulation.Critter;
import ast.Action;

public class ActionOutcome implements Outcome
{
	private Action act;
	
	public ActionOutcome(Action a)
	{
		act = a;
	}
	@Override
	public boolean applyOutcome(Critter c)
	{
		switch(act.getActType())
		{
			case
		}
	}
}