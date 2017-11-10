package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;

import simulation.SimpleCritter;
import simulation.SimpleWorld;
import simulation.World;
import simulation.WorldObject;
import simulation.Hex;

public class WorldModel {
	private SimpleWorld world;
	int numCritters;
	int time;
	private int simulationSpeed;
	
	public WorldModel()
	{
		numCritters = 0;
		time = 0;
	}

	/** Creates a new random world. */
	public void createNewWorld() {
		world = new World();
	}

	/**
	 * Loads in a world file.
	 * 
	 * @param worldfile
	 * @throws FileNotFoundException
	 *             if the file could not be found or is somehow invalid
	 * @throws IllegalArgumentException
	 *             if the constants.txt file could not be read
	 */
	public void loadWorld(File worldfile) throws FileNotFoundException, IllegalArgumentException {
		world = new World(worldfile);
	}
	
	public boolean isReady()
	{
		return world != null;
	}
	
	/** Returns the number of columns in the world. */
	public synchronized int getColumns()
	{
		return world.getColumns();
	}
	
	/** Returns the number of rows in the world. */
	public synchronized int getRows()
	{
		return world.getRows();
	}
	
	public synchronized int hexContent(int c, int r)
	{
		return world.analyzeHex(c, r);		
	}
	
	public synchronized SimpleCritter getCritter(int c, int r)
	{
		return world.analyzeCritter(c, r);
	}
	
	public synchronized Set<Map.Entry<SimpleCritter, Hex>> getCritterMap()
	{
		return world.getCritterMap();
	}
	
	public synchronized Set<Map.Entry<WorldObject, Hex>> getObjectMap()
	{
		return world.getObjectMap();
	}
	
	/** Advances one time step. */
	public synchronized void advanceTime() {
		world.advanceOneTimeStep();
		time++;
		numCritters = world.numRemainingCritters();
	}
}