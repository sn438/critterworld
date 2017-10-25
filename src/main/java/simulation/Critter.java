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
	/** The name of this critter, used for identification purposes. */
	private String name;
	
	/**
	 * 
	 * @param p
	 * @param mem
	 * @param dir
	 * @param s
	 */
	public Critter(Program p, int[] mem, Direction dir, String s)
	{
		this.prog = p;
		this.memory = mem;
		this.memLength = mem[0];
		this.orientation = dir;
		this.name = s;
	}
	
	/**
	 * 
	 * @param p
	 * @param mem
	 * @param s
	 */
	public Critter(Program p, int[] mem, String s)
	{
		this.prog = p;
		this.memory = mem;
		this.memLength = mem[0];
		this.name = s;
		
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
	
	/*
	/** Applies the effects of an update outcome to this critter.
	public boolean acceptOutcome(UpdateOutcome uo)
	{
		boolean result = setMemory(uo.getValue(), uo.getMemIndex());
		return result;
	}
	*/
	
	public enum Direction
	{
		NORTH, SOUTH, NORTHEAST, NORTHWEST, SOUTHEAST, SOUTHWEST;
	}
}