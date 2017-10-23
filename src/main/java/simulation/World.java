package simulation;

import java.util.HashMap;
import java.util.LinkedList;

/** A class to simulate the world state. */
public class World implements SimpleWorld
{
	/** Stores all the tiles in the world. */
	private HashMap<Hex, WorldObject> tiles;
	/** Stores all the critters present in the world, in the order in which they were created. */
	private LinkedList<Critter> critterList;
	
	public World()
	{
		
	}
	
	public void notifyObservers()
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