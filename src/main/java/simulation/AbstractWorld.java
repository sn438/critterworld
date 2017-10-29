package simulation;

import java.util.HashMap;
import java.util.LinkedList;

import interpret.Interpreter;
import interpret.InterpreterImpl;

public abstract class AbstractWorld implements SimpleWorld
{
	/** A compilation of all the constants needed for world creation. */
	protected HashMap<String, Double> CONSTANTS;
	/** Stores all the critters present in the world, in the order in which they were created. */
	protected LinkedList<SimpleCritter> critterList;
	/** The number of time steps passed since this world's genesis. */
	protected int timePassed;
	
	@Override
	public int getMinMemory()
	{
		return CONSTANTS.get("MIN_MEMORY").intValue();
	}

	@Override
	public int getMaxRules()
	{
		return CONSTANTS.get("MAX_RULES_PER_TURN").intValue();
	}

	@Override
	public int numRemainingCritters()
	{
		return critterList.size();
	}
	
	@Override
	public int getTimePassed()
	{
		return timePassed;
	}
	
	@Override
	public void advanceOneTimeStep()
	{
		LinkedList<SimpleCritter> clone = (LinkedList<SimpleCritter>) critterList.clone();
		for(int i = 0; i < clone.size(); i++)
		{
			SimpleCritter sc = clone.get(i);
			Interpreter im = new InterpreterImpl(sc, this);
			im.simulateCritterTurn();
			System.out.println("\n" + this.numRemainingCritters());
		}
	}

	@Override
	public abstract void loadCritters(String filename, int n, int direction);
	
	@Override
	public abstract StringBuilder printGrid();
	

	public abstract int searchNearby(SimpleCritter sc, int dir);

	public abstract int searchAhead(SimpleCritter sc, int index);

	public abstract void moveCritter(SimpleCritter sc, boolean forward);

	public abstract void critterEat(SimpleCritter sc);

	public abstract void growCritter(SimpleCritter sc);

	public abstract void critterBattle(SimpleCritter attacker);
	
	/** 
	 * Executes the result of one critter attempting to bud. The newly created offspring will appear directly behind
	 * the parent critter, unless there is something already in that hex (in which case no reproduction will occur).
	 * @param sc
	 */
	public abstract void critterBud(SimpleCritter sc);
	
	/**
	 * 
	 * @param sc
	 */
	public abstract void critterMate(SimpleCritter sc);
	
	public abstract void critterTag(SimpleCritter sc, int index);
	
	public abstract void critterServe(SimpleCritter sc, int index);
	
	public abstract void critterSoakEnergy(SimpleCritter sc);
}