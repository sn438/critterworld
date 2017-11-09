package gui;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import simulation.SimpleWorld;
import simulation.World;

public class WorldModel
{
	private boolean isRunning;
	private SimpleWorld world;
	IntegerProperty numCritters;
	IntegerProperty time;
	private IntegerProperty simulationSpeed;
	
	public WorldModel()
	{
		numCritters = new SimpleIntegerProperty(0);
		time = new SimpleIntegerProperty(0);
	}
	
	/** Creates a new random world. */
	public void createNewWorld()
	{
		world = new World();
	}
	
	/**
	 * Loads in a world file.
	 * @param worldfile
	 * @throws FileNotFoundException if the file could not be found or is somehow invalid
	 * @throws IllegalArgumentException if the constants.txt file could not be read
	 */
	public void loadWorld(File worldfile) throws FileNotFoundException, IllegalArgumentException
	{
		world = new World(worldfile);
	}
	
	public boolean isReady()
	{
		return world != null;
	}
	
	/** Returns the number of columns in the world. */
	public int getColumns()
	{
		return world.getColumns();
	}
	
	/** Returns the number of rows in the world. */
	public int getRows()
	{
		return world.getRows();
	}
	
	/** Advances one time step. */
	public void advanceTime()
	{
		world.advanceOneTimeStep();
		time.set(time.get() + 1);
		numCritters.set(world.numRemainingCritters());
	}
}