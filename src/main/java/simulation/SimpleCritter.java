package simulation;

import ast.Program;

public interface SimpleCritter extends WorldObject
{
	/** Returns the ID of this critter, or 0 if it hasn't been assigned one. */
	public int getID();
	
	/** Sets the ID of this critter. */
	public void setID(int id);
	
	/** Returns the ruleset of this critter. */
	public Program getProgram();
	
	/** Returns the size of this critter. */
	public int size();
	
	/** Returns the memory length of this critter. */
	public int getMemLength();
	
	/** Returns the orientation of this critter. */
	public int getOrientation();
	
	/** Randomizes the orientation of this critter. */
	public void randomizeOrientation();
	
	/** Returns the current energy level of this critter. */
	public int getEnergy();
	
	/** Updates the critter's energy by the amount specified. */
	public void updateEnergy(int amount, int maxEnergyPerSize);
	
	/** Updates the critter's tag number to the value specified, if that value is in [0, 99]. */
	public void setTag(int newVal);
	
	/** Increments the pass number of this critter (memory index 5) by one, if it is less than {@code maxRules}. */
	public void incrementPass(int maxRules);
	
	/** Resets the pass number of this critter (memory index 5) to zero. */
	public void resetPass();
	
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
	
	@Override
	public int getAppearance();
	
	/** Returns a copy of the memory. */
	public int[] getMemoryCopy();
	
	/** Returns the species name of this critter. */
	public String getName();
	
	/** Returns a string representation of the last rule executed by this critter. */
	public String getLastRuleString();
	
	/** Returns the index of the last rule executed by this critter, or -1 if none have been executed yet. */
	public int getLastRuleIndex();
	
	/** Sets the last rule executed. */
	public void setLastRuleString(String s);
	
	/** Sets the index of the last rule executed by this critter. */
	public void setLastRuleIndex(int i);
	
	/**
	 * Turns this critter in the direction specified.
	 * @param counterclockwise
	 */
	public void turn(boolean clockwise);
	
	/**
	 * Returns an array of length 2 that symbolizes the change in position of a critter if it moves one hex in the specified direction.
	 * @param forward : whether or not the hex will be accessed by moving forward or backward
	 * @param dir : the direction the hex is in
	 */
	public int[] changeInPosition(boolean forward, int dir);
	
	/** Whether or not this critter wants to mate. */
	public boolean wantsToMate();
	
	/** Turns mating signals of this critter on or off. */
	public void toggleMatingPheromones(boolean b);
	
	/** Returns an integer value representing this critter's complexity. */
	public int complexity(int ruleCost, int abilityCost);
}