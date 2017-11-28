package simulation;

import java.io.File;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

/** An interface containing the basic functions of the world. */
public interface SimpleWorld
{
	/** Returns the number of columns in this world. */
	int getColumns();
	
	/** Returns the number of rows in this world. */
	int getRows();
	
	/** Returns the minimum critter memory size for this world. Cannot be less than 8. */
	int getMinMemory();
	
	/** Returns the maximum number of rules that may be executed per turn for this world. */
	int getMaxRules();
	
	/** Returns the number of living critters currently in the simulation. */
	int numRemainingCritters();
	
	/** Returns the amount of time passed since this world's genesis. */
	int getTimePassed();
	
	/**
	 * Loads critters of following a set pattern into this world.
	 * @param filename the file containing the critter information
	 * @param n the number of critters to load
	 * @param direction the orientation of the critter. If this value is less than 0, a critter orientation
	 * 					will be chosen at random.
	 */
	void loadCritters(String filename, int n, int direction);
	
	/**
	 * Loads critters of following a set pattern into this world.
	 * @param file the file containing the critter information
	 * @param n the number of critters to load
	 * @param direction the orientation of the critter. If this value is less than 0, a critter orientation
	 * 					will be chosen at random.
	 */
	void loadCritters(File file, int n, int direction);
	
	void loadCritterAtLocation(File file, int c, int r);
	
	/** Advances the world state by a single time step. */
	void advanceOneTimeStep();
	
	/** Retrieves the list of updated hexes from the most recent time steps, and then empties the list. */ 
	LinkedList<Hex> getAndResetUpdatedHexes();
	
	/** Determines whether or not a hex with column index {@code c} and row index {@code r} is on the world grid. */
	boolean isValidHex(int c, int r);
	
	/** Returns an entry set mapping the world critters to hex locations. */
	Set<Map.Entry<SimpleCritter, Hex>> getCritterMap();
	
	/** Returns an entry set mapping the world objects to hex locations. */
	Set<Map.Entry<WorldObject, Hex>> getObjectMap();
	
	/**
	 * Returns information about a hex.
	 * @param c
	 * @param r
	 * @return
	 */
	int analyzeHex(int c, int r);
	
	/**
	 * Returns the critter on a hex, for analysis.
	 * @param c
	 * @param r
	 * @return
	 */
	SimpleCritter analyzeCritter(int c, int r);
	
	/**
	 * 
	 * @param c
	 * @param r
	 * @return
	 */
	WorldObject getHexContent(int c, int r);

	/** Returns a StringBuilder containing the printed version of the world grid. */
	StringBuilder printGrid();
}