package simulation;

/** Stores the information for one hex in the world grid. */
public class Hex
{
	/** The row index of this Hex. */
	private int rowIndex;
	/** The column index of this Hex. */
	private int colIndex;
	/** What this hex contains. May be null if there is nothing on this hex. */
	private WorldObject content;
	
	/** Creates a new empty Hex object with the specified rowIndex and colIndex. */
	public Hex(int r, int c)
	{
		rowIndex = r;
		colIndex = c;
		content = null;
	}
	
	public boolean isEmpty()
	{
		return content == null;
	}
	
	@Override
	public String toString()
	{
		if(content == null)
			return "-";
		return content.toString();
	}
}