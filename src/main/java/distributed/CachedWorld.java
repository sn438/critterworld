package distributed;

public class CachedWorld {
	
	private int columns;
	private int rows;
	
	
	public CachedWorld(WorldStateJSON state) {
		columns = state.getCols();
		rows = state.getRows();
	}
}