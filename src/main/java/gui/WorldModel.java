package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import distributed.WorldStateJSON;
import simulation.Hex;
import simulation.SimpleCritter;
import simulation.SimpleWorld;
import simulation.World;
import simulation.WorldObject;

/**
 * The model that stores world and critter states. It also serves as an
 * abstraction barrier between the world and the modules that interact with the
 * world.
 */
public class WorldModel {

	/** An instance of the world. */
	private SimpleWorld world;
	/** The number of critters. */
	private int numCritters;
	/** The number of time steps taken. */
	private int time;

	/** Creates a new blank world model. */
	public WorldModel() {
		numCritters = 0;
		time = 0;
	}

	/**
	 * Creates a new random world.
	 * 
	 * @throws UnsupportedOperationException
	 *             if the constants.txt file could not be read
	 */
	public void createNewWorld() throws UnsupportedOperationException {
		world = new World();
		time = 0;
		numCritters = world.numRemainingCritters();
	}

	/**
	 * Loads in a world based on a description.
	 * 
	 * @param desc
	 * @throws IllegalArgumentException
	 *             if the description is invalid
	 * @throws UnsupportedOperationException
	 *             if the constants.txt file could not be read
	 */
	public void loadWorld(WorldStateJSON w) throws IllegalArgumentException, UnsupportedOperationException {
		world = new World(w);
		time = world.getTimePassed();
		numCritters = world.numRemainingCritters();
	}

	/**
	 * Loads in a world file.
	 * 
	 * @param worldfile
	 * @throws FileNotFoundException
	 *             if the file could not be found or is somehow invalid
	 * @throws UnsupportedOperationException
	 *             if the constants.txt file could not be read
	 */
	public void loadWorld(File worldfile) throws FileNotFoundException, UnsupportedOperationException {
		world = new World(worldfile);
		time = 0;
		numCritters = world.numRemainingCritters();
	}

	public boolean isReady() {
		return world != null;
	}

	/** Returns the number of columns in the world. */
	public int getColumns() {
		return world.getColumns();
	}

	/** Returns the number of rows in the world. */
	public int getRows() {
		return world.getRows();
	}

	/** Returns the number of living critters in the world. */
	public int getNumCritters() {
		return numCritters;
	}

	/** Returns the current time step of the world. */
	public int getCurrentTimeStep() {
		return time;
	}

	/**
	 * Returns a number giving information about a hex.
	 * 
	 * @param c
	 * @param r
	 * @return
	 */
	public int hexInfo(int c, int r) {
		return world.analyzeHex(c, r);
	}

	/**
	 * Returns a pointer to a critter.
	 * @param c The column index of the critter
	 * @param r The row index of the critter
	 */
	public SimpleCritter getCritter(int c, int r) {
		return world.analyzeCritter(c, r);
	}

	/**
	 * 
	 * @return
	 */
	public Set<Map.Entry<SimpleCritter, Hex>> getCritterMap() {
		return world.getCritterMap();
	}

	/**
	 * 
	 * @return
	 */
	public synchronized Set<Map.Entry<WorldObject, Hex>> getObjectMap() {
		return world.getObjectMap();
	}

	/** Advances one time step. */
	public synchronized void advanceTime() {
		world.advanceOneTimeStep();
		time++;
		numCritters = world.numRemainingCritters();
	}

	/**
	 * Loads critters at random locations.
	 * 
	 * @param f The file specifying the critter to load
	 * @param n The number of critters to load
	 */
	public synchronized void loadRandomCritters(File f, int n) {
		world.loadCritters(f, n, -1);
		numCritters = world.numRemainingCritters();
	}

	public synchronized void loadCritterAtLocation(File f, int c, int r) {
		world.loadCritterAtLocation(f, c, r);
		numCritters = world.numRemainingCritters();
	}
}