package distributed;

/** A class to provide JSON representations of world objects (food, rocks, critters, nothing). */
public class JSONWorldObject {
	/** The column index of the object. */
	private Integer col;
	/** The row index of the object. */
	private Integer row;
	/** The type of the object, as a string. */
	private String type;
	
	/** Food objects only: the number of calories. */
	private Integer value;
	
	/** Critter objects only: the critter ID. */
	private Integer id;
	private String species_id;
	private Integer direction;
	private int[] mem;
	private String program;
	private Integer recently_executed_rule;
	
	
	public JSONWorldObject(String typ, int c, int r) {
		type = typ;
		col = c;
		row = r;
	}
	
	public JSONWorldObject(String typ, int c, int r, int calories) {
		type = typ;
		col = c;
		row = r;
		value = calories;
	}
	
	public JSONWorldObject(String typ, int c, int r, int dir, int critterID, String species, String program, int[] memory, int ruleIndex) {
		type = typ;
		col = c;
		row = r;
		direction = dir;
		id = critterID;
		species_id = species;
		mem = memory;
		recently_executed_rule = ruleIndex;
	}
	
	public JSONWorldObject(String typ, int c, int r, int dir, int critterID, String species, int[] memory) {
		type = typ;
		col = c;
		row = r;
		direction = dir;
		id = critterID;
		species_id = species;
		mem = memory;
	}
	
	
}