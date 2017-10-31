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
		timePassed++;
	}

	@Override
	public abstract void loadCritters(String filename, int n, int direction);
	
	@Override
	public abstract StringBuilder printGrid();
	
	/* ========================================= */
	/* ----------- Critter Sensors ------------- */
	/* ========================================= */
	
	/**
	 * Looks at the contents of a hex adjacent to a critter.
	 * @param sc : the critter who is observing its surroundings
	 * @param dir : the direction in which to look
	 * @return an integer value based on the contents of the observed hex. If the hex is empty, this
	 * @param sc the critter who is observing its surroundings
	 * @param dir the direction in which to look
	 * @return an integer value based on the contents of the observed hex
	 */
	public abstract int searchNearby(SimpleCritter sc, int dir);

	/**
	 * Looks at the contents of a hex ahead of a critter.
	 * @param sc : the critter who is observing its surroundings
	 * @param index : how far ahead to look
	 * @return an integer value based on the contents of the observed hex, following the same scheme as {@code searchNearby}.
	 */
	public abstract int searchAhead(SimpleCritter sc, int index);

	/* ========================================= */
	/* ----------- Critter Actions ------------- */
	/* ========================================= */
	
	/* 
	  A NOTE ON ACTIONS: all critter actions except WAIT expend energy. Most of them expend the same amount of energy whether
	  they succeed or not. If a critter tries to execute an action that requires more energy than it currently has, it will 
	  die without executing that action. If a critter has just enough energy to perform an action (i.e. the energy required
	  to perform that action is equal to the energy it currently has, it will execute that action and promptly die afterwards.
	*/
	
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
	 * Forces a critter to eat, if there is food in the hex directly in front of it. This action replenishes energy
	 * equal to the caloric content of the food eaten.
	 * @param sc : the feasting critter
	 */
	public abstract void critterEat(SimpleCritter sc);

	/**
	 * Grows a critter by one size.
	 * @param sc : the growing critter
	 */
	public abstract void growCritter(SimpleCritter sc);

	/** 
	 * Simulates the results of one critter attacking another.
	 * @param attacker
	 */
	public abstract void critterBattle(SimpleCritter attacker);
	
	
	/** 
	 * Executes the result of one critter attempting to bud. The newly created offspring will appear directly behind
	 * the parent critter, unless there is something already in that hex (in which case no reproduction will occur).
	 * @param sc : the budding critter
	 */
	public abstract void critterBud(SimpleCritter sc);
	
	/**
	 * Executes the result of one critter attempting to mate with another critter. The other critter must be directly in
	 * front of the first critter and facing the first critter. The newly created offspring will appear directly behind
	 * one of the parent critters, unless there is something already in that hex (in which case no reproduction will occur).
	 * @param sc
	 */
	public abstract void critterMate(SimpleCritter sc);
	
	/**
	 * One critter "tags" another critter by setting its "tag" attribute in memory equal to the value of {@code index}.
	 * @param sc
	 * @param index
	 */
	public abstract void critterTag(SimpleCritter sc, int index);
	
	/**
	 * One critter severs a part of its soul (its energy) and places it on the hex in front of it in the form of food. It is
	 * possible for the critter to serve all of its remaining energy, killing it in the process
	 * @param sc : the overly generous critter
	 * @param index : the amount of food to serve
	 */
	public abstract void critterServe(SimpleCritter sc, int index);
	
	/**
	 * A critter does nothing but sit in the sun. It replenishes some energy in the process.
	 * @param sc
	 */
	public abstract void critterSoakEnergy(SimpleCritter sc);
}