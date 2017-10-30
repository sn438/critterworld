package simulation;

public interface WorldObject
{
	/** Returns the ASCII art representation of this object. */
	public String toString();
	
	/** 
	 * Returns an integer value based on this world object. This value depends on the type of this object:
	 * 		   <ul><li>If this object is a critter, the value will be a positive integer equal to the critter's
	 * 				   appearance, as determined by {@code SimpleCritter.getAppearance()}.</li>
	 * 		   <li>If this object is a rock, the value will be -1.</li>
	 * 		   <li>If the hex contains food, the value will be {@code -1 * ([food calories] + 1)}.</li.</ul>
	 */
	public int getAppearance();
}