package interpret;

import simulation.Critter;

public class UpdateOutcome implements Outcome
{
	private int memIndex;
	private int value;
	
	public UpdateOutcome(int memInd, int val)
	{
		this.memIndex = memInd;
		this.value = val;
	}
	
	@Override
	public boolean applyOutcome(Critter c)
	{
		// TODO Auto-generated method stub
		return false;
	}
}