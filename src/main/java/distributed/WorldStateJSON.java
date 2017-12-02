package distributed;

/** A JSON representation of the state of a world. */
public class WorldStateJSON {
	private Integer current_timestep;
	private Integer current_version_number;
	private Integer update_since;
	private Float rate;
	private String name;
	private Integer population;
	private Integer cols;
	private Integer rows;
	private int[] dead_critters;
	private JSONWorldObject[] state;
	
	/** Creates a new WorldStateJSON object. */
	public WorldStateJSON(int time, int version, int updateSince, float runrate, String worldname,
						  int numCritters, int c, int r, int[] deadList, JSONWorldObject[] objectList) {
		current_timestep = time;
		current_version_number = version;
		update_since = updateSince;
		rate = runrate;
		name = worldname;
		population = numCritters;
		cols = c;
		rows = r;
		dead_critters = deadList;
		state = objectList;
	}
	
	public Integer getCurrentTime() {
		return current_timestep;
	}
	
	public Integer getCurrentVersion() {
		return current_version_number;
	}
	
	public Float getRate() {
		return rate;
	}
	
	public Integer getCols() {
		return cols;
	}
	
	public Integer getRows() {
		return rows;
	}
	
	public Integer getUpdateSince() {
		return update_since;
	}
	
	public Integer getPopulation() {
		return population;
	}
	
	public String getName() {
		return name;
	}
	
	public int[] getDeadCritters() {
		return dead_critters;
	}
	
	public JSONWorldObject[] getWorldObjects() {
		return state;
	}
}