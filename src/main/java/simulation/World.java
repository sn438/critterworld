package simulation;

import java.util.HashMap;
import java.util.LinkedList;

import ast.Program;

/** A class to simulate the world state. */
public class World implements SimpleWorld
{
	/** Contains the hex grid of the world. */
	private Hex[][] grid;
	/** Maps each critter to a location in the world */
	private HashMap<Critter, Hex> critterLocations;
	/** Stores all the critters present in the world, in the order in which they were created. */
	private LinkedList<Critter> critterList;
	
	/** Loads a world based on a saved file. */
	public World(String filename)
	{
		
	}
	
	/** Generates a random world. */
	public World()
	{
		
	}

	@Override
	public int getMinMemory()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void loadCritters(String name, int[] mem, Program p, int n)
	{
		// TODO Auto-generated method stub
		
	}
	
	/** Advances the world state by a single time step. */
	public void advanceTimeStep()
	{
		
	}
	
	@Override
	public int searchNearby(Critter c, int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int searchAhead(Critter c, int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}
}