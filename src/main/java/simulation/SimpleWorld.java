package simulation;

import ast.Program;

/** An interface containing the basic functions of the world. */
public interface SimpleWorld
{
	/** Returns the minimum critter memory size for this world. */
	int getMinMemory();
	
	/** Returns the maximum number of rules that may be executed per turn for this world. */
	int getMaxRules();
	
	/**
	 * Loads critters of following a set pattern into the 
	 * @param name the name of the critters to be loaded
	 * @param mem the memory of the critters
	 * @param p the ruleset of the critters
	 * @param n the number of critters to load
	 */
	void loadCritters(String name, int[] mem, Program p, int n);
	
	int searchNearby(SimpleCritter c, int index);
	
	int searchAhead(SimpleCritter c, int index);
	
	boolean moveCritter(SimpleCritter c, boolean forward);
	
	void printGrid();
}