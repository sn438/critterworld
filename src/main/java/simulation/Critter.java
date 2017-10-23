package simulation;

import interpret.*;
import ast.Program;

public class Critter implements WorldObject
{
	/** The set of rules for this critter. */
	private Program prog;
	/** The memory of this critter, which stores important attributes of the critter. */
	private int[] memory;
	/** The length of this critter's memory. Must be at least 8. */
	private int memLength;
	
	/**
	 * 
	 * @param p
	 * @param mem
	 */
	public Critter(Program p, int[] mem)
	{
		this.prog = p;
		this.memory = mem;
		this.memLength = mem[0];
	}
	
	/** Returns the memory size of this critter. */
	public int getMemLength()
	{
		return memLength;
	}
	
	/** 
	 * Returns the value of {@code memory[index]}.
	 * @param index
	 * @return the value of this critter's memory at the specified index, or -1 if the index lies out-of-bounds
	 */
	public int readMemory(int index)
	{
		if(index < 0 || index >= memLength)
			return -1;
		return memory[index];
	}
	
	/**
	 * Sets the memory at index to val. Does nothing if {@code index} is an out-of-bounds or unassignable index
	 * or if {@code val} is not within the restrictions of that array index.
	 * @param val
	 * @param index
	 * @return Whether or not the memory array was actually altered
	 */
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
	
	/*
	/** Applies the effects of an update outcome to this critter.
	public boolean acceptOutcome(UpdateOutcome uo)
	{
		boolean result = setMemory(uo.getValue(), uo.getMemIndex());
		return result;
	}
	*/
	
	public class WorldObserver
	{
		void update()
		{
			
		}
	}
}