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
	
	public void createNewWorld()
	{
		world = new World();
		crittersAlive = new SimpleIntegerProperty(0);
		stepsTaken = new SimpleIntegerProperty(0);
	}
	
	public void loadWorld(File worldfile) throws FileNotFoundException, IllegalArgumentException
	{
		world = new World(worldfile);
	}
	
	public void advanceTime()
	{
		world.advanceOneTimeStep();
		stepsTaken.set(stepsTaken.get() + 1);
		crittersAlive.set(world.numRemainingCritters());
	}
}