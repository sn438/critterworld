package simulation;

public class SmellValue {
	/**
	 * Distance from the root to this hex
	 */
	public int totalDist = Integer.MAX_VALUE;
	
	/**
	 * Direction in which Dijkstra reached this hex
	 */
	public int orientation;
	
	/**
	 * Orientation of first hex in the sequence of hexes Dijkstra traversed to get to this hex
	 */
	public int origin;
	
	/**
	 * Number of steps Dijkstra took to get to this hex
	 */
	public int numSteps;
}
