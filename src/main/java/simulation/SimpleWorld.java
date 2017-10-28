package simulation;

/** An interface containing the basic functions of the world. */
public interface SimpleWorld
{
	/** Returns the minimum critter memory size for this world. Cannot be less than 8. */
	int getMinMemory();
	
	/** Returns the maximum number of rules that may be executed per turn for this world. */
	int getMaxRules();
	
	/** Returns the number of living critters currently in the simulation. */
	int numRemainingCritters();
	
	/** Returns the amount of time passed since this world's genesis. */
	public int getTimePassed();
	
	/**
	 * Loads critters of following a set pattern into this world.
	 * @param filename the file containing the critter information
	 * @param n the number of critters to load
	 * @param direction the orientation of the critter. If this value is less than 0, a critter orientation
	 * 					will be chosen at random.
	 */
	public void loadCritters(String filename, int n, int direction);
	
	/** Advances the world state by a single time step. */
	void advanceOneTimeStep();
	
	/** Returns a StringBuilder containing the printed version of the world grid. */
	StringBuilder printGrid();
}