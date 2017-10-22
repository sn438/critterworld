package simulation;

import interpret.Outcome;
import ast.Program;

public class Critter
{
	/** The set of rules for this critter. */
	private Program prog;
	/** The memory of this critter, which stores important attributes of the critter. */
	private int[] memory;
	
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
	
	/** Applies the effects of an outcome to this critter. */
	public boolean acceptOutcome(Outcome o)
	{
		boolean result = o.applyOutcome(this);
		return result;
	}
	
	public class WorldObserver
	{
		void update()
		{
			
		}
	}
}