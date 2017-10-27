package simulation;

import ast.Program;

/** An interface containing the basic functions of the world. */
public interface SimpleWorld
{
	/** Returns the minimum critter memory size for this world. Cannot be less than 8. */
	int getMinMemory();
	
	/** Returns the maximum number of rules that may be executed per turn for this world. */
	int getMaxRules();
	
	/**
	 * Loads critters of following a set pattern into this world.
	 * @param filename the file containing the critter information
	 * @param n the number of critters to load
	 * @param c the column index of the critter
	 * @param r the row index of the critter
	 * @param direction the orientation of the critter
	 */
	public void loadCritters(String filename, int n, int c, int r, int direction);
	
	int searchNearby(SimpleCritter c, int index);
	
	int searchAhead(SimpleCritter c, int index);
	
	boolean moveCritter(SimpleCritter c, boolean forward);
	
	void printGrid();
}