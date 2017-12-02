package distributed;

public class LoadCritterResponseJSON {
	private String species_id;
	private int[] ids;
	
	public LoadCritterResponseJSON(String species, int[] IDList) {
		species_id = species;
		ids = IDList;
	}
}