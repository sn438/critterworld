package simulation;

/** A piece of food that can be present on one world hex. */
public class Food implements WorldObject
{
	/** The caloric value of this piece of food. */
	private int calories;
	
	/** Creates a new Food object with the specified amount of calories. */
	public Food(int amt)
	{
		calories = amt;
	}
	
	/** Returns the amount of calories in this piece of food. */
	public int getCalories()
	{
		return calories;
	}
	
	public String toString()
	{
		return "F";
	}
}