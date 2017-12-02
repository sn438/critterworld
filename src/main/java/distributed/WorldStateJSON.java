package distributed;

/** A JSON representation of the state of a world. */
public class WorldStateJSON {
	private Integer current_timestep;
	private Integer current_version_number;
	private Integer update_since;
	private Double rate;
	private String name;
	private Integer population;
	private Integer rows;
	private Integer cols;
	private int[] dead_critters;
	private JSONWorldObject[] state;
	
	
}