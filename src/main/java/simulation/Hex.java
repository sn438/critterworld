package simulation;

/** Stores the information for one hex in the world grid. */
public class Hex
{
	/** The row index of this Hex. */
	private int rowIndex;
	/** The column index of this Hex. */
	private int colIndex;
	
	/** Creates a new Hex object with the specified rowIndex and colIndex. */
	public Hex(int r, int c)
	{
		rowIndex = r;
		colIndex = c;
	}
}
