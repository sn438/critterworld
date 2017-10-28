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
	
	/** Returns the row index of this Hex. */
	public int getRowIndex()
	{
		return rowIndex;
	}
	
	/** Returns the column index of this Hex. */
	public int getColumnIndex()
	{
		return colIndex;
	}
	
	/**
	 * Adds a world object to this hex, if it is empty.
	 * @param wo the object to add
	 * @return whether or not {@code wo} was able to be added to this hex.
	 */
	public boolean addContent(WorldObject wo)
	{
		if(isEmpty())
		{
			content = wo;
			return true;
		}
		return false;
	}
	
	/** Removes the content on this hex. */
	public void removeContent()
	{
		content = null;
	}
	
	public WorldObject getContent()
	{
		return content;
	}
	/** Returns {@code true} if and only if {@code content == null}. */
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