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
	private IntegerProperty crittersAlive;
	private IntegerProperty stepsTaken;
	private IntegerProperty simulationSpeed;
	
	/** Creates a new random world. */
	public void createNewWorld()
	{
		world = new World();
		crittersAlive = new SimpleIntegerProperty(0);
		stepsTaken = new SimpleIntegerProperty(0);
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
	
	/** Advances one time step. */
	public void advanceTime()
	{
		world.advanceOneTimeStep();
		stepsTaken.set(stepsTaken.get() + 1);
		crittersAlive.set(world.numRemainingCritters());
	}
}