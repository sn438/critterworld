package gui;

import java.io.File;

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
	
	public void createNewWorld()
	{
		world = new World();
		crittersAlive = new SimpleIntegerProperty(0);
		stepsTaken = new SimpleIntegerProperty(0);
	}
	
	public void loadWorld(File worldfile)
	{
		
	}
	
	public void advanceTime()
	{
		world.advanceOneTimeStep();
		stepsTaken.set(stepsTaken.get() + 1);
	}
}