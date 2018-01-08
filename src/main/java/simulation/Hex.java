package simulation;

/** Stores the information for one hex in the world grid. */
public class Hex {
	/** The column index of this Hex. */
	private int colIndex;
	/** The row index of this Hex. */
	private int rowIndex;
	/** What this hex contains. May be null if there is nothing on this hex. */
	private WorldObject content;

	/** Creates a new empty Hex object with the specified rowIndex and colIndex. */
	public Hex(int c, int r) {
		colIndex = c;
		rowIndex = r;
		content = null;
	}

	/** Returns the column index of this Hex. */
	public int getColumnIndex() {
		return colIndex;
	}

	/** Returns the row index of this Hex. */
	public int getRowIndex() {
		return rowIndex;
	}

	/**
	 * Adds a world object to this hex, if it is empty.
	 *
	 * @param wo
	 *            the object to add
	 * @return whether or not {@code wo} was able to be added to this hex.
	 */
	public boolean addContent(WorldObject wo) {
		if (isEmpty()) {
			content = wo;
			return true;
		}
		return false;
	}

	/** Removes the content on this hex. */
	public void removeContent() {
		content = null;
	}

	public WorldObject getContent() {
		return content;
	}

	/** Returns {@code true} if and only if {@code content == null}. */
	public boolean isEmpty() {
		return content == null;
	}

	/**
	 * Returns an integer representing how this hex appears to a critter. If this
	 * hex is empty, this integer is 0, but otherwise, this value is determined by
	 * {@code content.getAppearance()}, which follows the numbering scheme described
	 * in the interface {@code WorldObject}.
	 */
	public int hexAppearance() {
		return isEmpty() ? 0 : content.getAppearance();
	}

	@Override
	public String toString() {
		if (content == null)
			return "-";
		return content.toString();
	}

	@Override
	public boolean equals(Object o) {
		Hex secondHex = (Hex) o;
		if (this.getColumnIndex() == secondHex.getColumnIndex() && this.getRowIndex() == secondHex.getRowIndex())
			return true;
		return false;
	}
}
