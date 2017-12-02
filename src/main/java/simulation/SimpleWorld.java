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
	
	/** Returns the name of this world. */
	String getWorldName();
	
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
	
	/**
	 * Loads one critter species into the world at random locations.
	 * @param sc
	 * @param n
	 * @param sessionID
	 * @return 
	 */
	int[] loadCritters(SimpleCritter sc, int n, int sessionID);
	
	/**
	 * 
	 * @param file
	 * @param c
	 * @param r
	 */
	void loadCritterAtLocation(File file, int c, int r);
	
	/**
	 * Loads a single critter into the world at the specified coordinates, if
	 * possible. Does nothing if the hex is not within the world boundaries, or if
	 * there is something already present at the hex.
	 * 
	 * @param sc The critter to add
	 * @param c The column index of the hex where the critter will be added
	 * @param r The row index of the hex where the critter will be added
	 * @return The ID of the newly created critter
	 */
	int loadOneCritter(SimpleCritter sc, int c, int r, int sessionID);
	
	/**
	 * Loads a single non-critter world object into the world at the specified
	 * coordinates, if possible. Does nothing if the hex is not within the world
	 * boundaries or if there is something already present at the hex. This method
	 * cannot be used to add critters into the world. Use the method
	 * {@code loadCritter(SimpleCritter sc, int c, int r)} instead.
	 * 
	 * @param wo The object to add
	 * @param c The column index of the hex where the object will be added
	 * @param r The row index of the hex where the object will be added
	 */
	void addNonCritterObject(WorldObject wo, int c, int r);
	
	/** Gets a list of the living critters in the world. */
	LinkedList<SimpleCritter> getCritterList();
	
	/** Retrieves a list of all critters that have died in this world. */
	LinkedList<SimpleCritter> collectCritterCorpses();
	
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
	
	/** Removes a critter from the world. */
	void removeCritter(SimpleCritter sc);
	
	/**
	 * Returns information about a hex.
	 * @param c
	 * @param r
	 * @return
	 */
	int analyzeHex(int c, int r);
	
	/**
	 * Returns the critter on a hex, for analysis.
	 * @param c The column index of the hex on which the critter is located
	 * @param r The row index of the hex on which the critter is located
	 * @return A pointer to the critter, or {@code null} if there is no critter there
	 * 		   (or if the provided column-row pair is not on the world grid).
	 */
	SimpleCritter analyzeCritter(int c, int r);
	
	/**
	 * 
	 * @param sc
	 * @return
	 */
	int getCritterID(SimpleCritter sc);
	
	SimpleCritter getCritterFromID(int id);
	
	int getCritterCreatorID(SimpleCritter sc);
	
	/**
	 * Retrieves the contents of a hex.
	 * @param c The column index of the desired hex
	 * @param r The row index of the desired hex
	 * @return The world object at that hex, or {@code null} if it is empty.
	 * 		   Also returns {@code null} if the provided column-row pair is not on the world grid.
	 */
	WorldObject getHexContent(int c, int r);

	/** Returns a StringBuilder containing the printed version of the world grid. */
	StringBuilder printGrid();
}