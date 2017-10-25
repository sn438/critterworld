package simulation;

/** A piece of food that can be present on one world hex. */
public class Food implements WorldObject
{
	/** The amount of food stored in this food object. */
	private int amount;
	
	/** Creates a new Food object with the specified amount of food. */
	public Food(int amt)
	{
		amount = amt;
	}
}
