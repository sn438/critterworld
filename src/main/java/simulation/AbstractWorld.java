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
	@SuppressWarnings("unchecked")
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
	
	/**
	 * Looks at the contents of a hex adjacent to a critter.
	 * @param sc : the critter who is observing its surroundings
	 * @param dir : the direction in which to look
	 * @return an integer value based on the contents of the observed hex. This value depends on the content of the hex:
	 * 		   <ul><li>If the hex contains a critter, the value will be a positive integer equal to the critter's
	 * 				   appearance, as determined by {@code SimpleCritter.getAppearance()}.</li>
	 * 		   <li>If the hex contains nothing, the value will be 0.</li>
	 * 		   <li>If the hex contains a rock or the hex is out of the world bounds, the value will be -1.</li>
	 * 		   <li>If the hex contains food, the value will be {@code -1 * ([food calories] + 1)}.</li.</ul>
	 */
	public abstract int searchNearby(SimpleCritter sc, int dir);

	/**
	 * Looks at the contents of a hex ahead of a critter.
	 * @param sc : the critter who is observing its surroundings
	 * @param index : how far ahead to look
	 * @return an integer value based on the contents of the observed hex, following the same scheme as {@code searchNearby}.
	 */
	public abstract int searchAhead(SimpleCritter sc, int index);

	/**
	 * Moves a critter, if there is nothing in the way and critter will not move off the world bounds.
	 * @param sc : the moving critter
	 * @param forward : whether or not the critter is moving forward or backward
	 */
	public abstract void moveCritter(SimpleCritter sc, boolean forward);
	
	/**
	 * Turns a critter.
	 * @param sc : the turning critter
	 * @param forward : whether or not the critter is turning clockwise or counterclockwise
	 */
	public abstract void turnCritter(SimpleCritter sc, boolean clockwise);

	/**
	 * Forces a critter to eat, if there is food in the hex directly in front of it.
	 * @param sc : the feasting critter
	 */
	public abstract void critterEat(SimpleCritter sc);

	/**
	 * 
	 * @param sc
	 */
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