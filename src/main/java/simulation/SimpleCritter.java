package simulation;

public interface SimpleCritter extends WorldObject
{
	/** 
	 * Returns the value of {@code memory[index]}.
	 * @param index
	 * @return the value of this critter's memory at the specified index, or {@code Integer.MIN_VALUE} if the index lies out-of-bounds
	 */
	public int readMemory(int index);
	
	/**
	 * Sets the memory at index to val. Does nothing if {@code index} is an out-of-bounds or unassignable index
	 * or if {@code val} is not within the restrictions of that array index.
	 * @param val
	 * @param index
	 * @return Whether or not the memory array was actually altered
	 */
	public boolean setMemory(int val, int index);
	
	/** Increments the pass number of this critter (memory index 5) by one, if it is less than 999. */
	public void incrementPass();
}