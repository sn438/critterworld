package simulation;

import interpret.*;
import ast.Program;

public class Critter implements SimpleCritter
{
	/** The set of rules for this critter. */
	private Program prog;
	/** The memory of this critter, which stores important attributes of the critter. */
	private int[] memory;
	/** The length of this critter's memory. Must be at least 8. */
	private int memLength;
	/** The direction this critter is facing. */
	private Direction orientation;
	/** Whether or not this critter wants to mate. */
	private boolean readyToMingle;
	/** The name of this critter, used for identification purposes. */
	private String name;
	
	private Outcome lastActionCompleted;
	
	/**
	 * Creates a new Critter with a specified ruleset, memory, orientation, and name.
	 * @param p
	 * @param mem
	 * @param dir
	 * @param s
	 */
	public Critter(Program p, int[] mem, String s, int dir)
	{
		prog = p;
		memory = mem;
		memLength = mem[0];
		name = s;
		readyToMingle = false;
		
		switch(dir)
		{
			case 0:
				orientation = Direction.NORTH;
				break;
			case 1:
				orientation = Direction.NORTHWEST;
				break;
			case 2:
				orientation = Direction.SOUTHWEST;
				break;
			case 3:
				orientation = Direction.SOUTH;
				break;
			case 4:
				orientation = Direction.SOUTHEAST;
				break;
			case 5:
				orientation = Direction.NORTHEAST;
				break;
		}
	}
	
	/**
	 * Creates a new Critter with a specified ruleset, memory, name, and a random orientation.
	 * @param p
	 * @param mem
	 * @param s
	 */
	public Critter(Program p, int[] mem, String s)
	{
		prog = p;
		memory = mem;
		memLength = mem[0];
		name = s;
		readyToMingle = false;
		
		int rand = (int) (Math.random() * 6);
		switch(rand)
		{
			case 0:
				orientation = Direction.NORTH;
				break;
			case 1:
				orientation = Direction.SOUTH;
				break;
			case 2:
				orientation = Direction.NORTHEAST;
				break;
			case 3:
				orientation = Direction.NORTHWEST;
				break;
			case 4:
				orientation = Direction.SOUTHEAST;
				break;
			case 5:
				orientation = Direction.SOUTHWEST;
				break;
		}
	}
	
	/** Returns the memory size of this critter. */
	public int getMemLength()
	{
		return memLength;
	}
	
	@Override
	public int size()
	{
		return memory[3];
	}
	
	@Override
	public Program getProgram()
	{
		return prog;
	}
	
	/** Returns the orientation of this critter. */
	public Direction getOrientation()
	{
		return orientation;
	}
	
	@Override
	public int readMemory(int index)
	{
		if(index < 0 || index >= memLength)
			return Integer.MIN_VALUE;
		return memory[index];
	}
	
	@Override
	public boolean setMemory(int val, int index)
	{
		//this method does nothing if it tries to alter an index of less than 7, or if the index is out of memory's bounds
		if(index < 7 || index >= memLength)
			return false;
		//the value of mem[7] must be in the range [0, 99] so attempting to set it to something else also has no effect
		if(index == 7 && !(val <= 99 && val >= 0))
			return false;
		
		memory[index] = val;
		return true;
	}
	
	@Override
	public void incrementPass()
	{
		if(memory[5] < 999)
			memory[5]++;
	}
	
	@Override
	public void turn(boolean counterclockwise)
	{
		//if
	}
	
	@Override
	public String toString() //TODO fix when done testing
	{
		//String result = name + Arrays.toString(memory) + "\n" + prog.toString();
		//return result;
		return "" + orientation.getValue();
	}
	
	@Override
	public int getEnergy()
	{
		return memory[4];
	}
	
	@Override
	public boolean wantsToMate()
	{
		return readyToMingle;
	}
	
	@Override
	public void toggleMatingPheromones(boolean b)
	{
		readyToMingle = b;
	}
	
	@Override
	public int complexity(int ruleCost, int abilityCost)
	{
		return prog.getRulesList().size() * ruleCost + (memory[1] + memory[2]) * abilityCost;
	}
	
	@Override
	public void updateEnergy(int amount, int maxEnergyPerSize)
	{
		// TODO Auto-generated method stub
		memory[4] += amount;
		if(memory[4] > maxEnergyPerSize * size())
			memory[4] = maxEnergyPerSize * size();
	}

	@Override
	public int[] changeInPosition(boolean forward)
	{
		int[] result = new int[2];
		switch(orientation)
		{
			case NORTH:
				result[0] = 0;
				result[1] = 1;
				break;
			case NORTHWEST:
				result[0] = -1;
				result[1] = 0;
				break;
			case SOUTHWEST:
				result[0] = -1;
				result[1] = -1;
				break;
			case SOUTH:
				result[0] = 0;
				result[1] = -1;
				break;
			case SOUTHEAST:
				result[0] = 1;
				result[1] = 0;
				break;
			case NORTHEAST:
				result[0] = 1;
				result[1] = 1;
				break;
		}
		if(!forward)
		{
			result[0] *= -1;
			result[1] *= -1;
		}
		return result;
	}
	
	/** An enumeration of all the possible directions a critter can be facing. */
	public enum Direction
	{
		NORTH, NORTHWEST, SOUTHWEST, SOUTH, SOUTHEAST, NORTHEAST;
		
		public int getValue()
		{
			int result = 0;
			switch(this)
			{
				case NORTH:
					result = 0;
					break;
				case NORTHWEST:
					result = 1;
					break;
				case SOUTHWEST:
					result = 2;
					break;
				case SOUTH:
					result = 3;
					break;
				case SOUTHEAST:
					result = 4;
					break;
				case NORTHEAST:
					result = 5;
					break;
			}
			return result;
		}		
	}
}