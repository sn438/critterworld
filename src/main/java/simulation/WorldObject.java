package simulation;

public interface WorldObject
{
	/** Returns the ASCII art representation of this object. */
	public String toString();
	
	/** How this object appears to critter sensors. */
	public int getAppearance();
}