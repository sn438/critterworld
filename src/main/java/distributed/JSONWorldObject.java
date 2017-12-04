package distributed;

import java.util.Arrays;

import simulation.Food;
import simulation.Rock;
import simulation.SimpleCritter;
import simulation.WorldObject;

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
	private Integer amount;
	/** Critter objects only: the critter ID. */
	private Integer id;
	/** Critter objects only: the species name. */
	private String species_id;
	/** Critter objects only: the direction of the critter. */
	private Integer direction;
	/** Critter objects only: the critter memory. */
	private int[] mem;
	/** Critter objects only: the string representation of the memory. */
	private String program;
	/** Critter objects only: the index of the most recently executed rule. */
	private Integer recently_executed_rule;
	
	
	
	


	/**
	 * Creates a new JSON object of type "rock" or "nothing".
	 * @param typ The type of the object (must be either "rock" or "nothing")
	 * @param c The column index of the object
	 * @param r The row index of the object
	 */
	public JSONWorldObject(String typ, int c, int r) {
		type = typ;
		col = c;
		row = r;
	}

	/**
	 * Creates a JSON object of type food.
	 * @param typ The type of the object (must be "food")
	 * @param c The column index of the object
	 * @param r The row index of the object
	 * @param calories The number of calories in the food
	 */
	public JSONWorldObject(String typ, int c, int r, int calories) {
		type = typ;
		col = c;
		row = r;
		value = calories;
	}

	/**
	 * Creates a JSON critter object for which the user has full permissions
	 * @param typ The type of the object (must be "critter")
	 * @param c The column index of the object
	 * @param r The row index of the object
	 * @param dir The direction the critter is facing
	 * @param critterID The unique ID of the critter
	 * @param species The species name of the critter
	 * @param program The String representation of the critter's program
	 * @param memory The memory of the critter
	 * @param ruleIndex The index of the critter's last executed rule
	 */
	public JSONWorldObject(String typ, int c, int r, int dir, int critterID, String species, String prog, int[] memory, int ruleIndex) {
		type = typ;
		col = c;
		row = r;
		direction = dir;
		id = critterID;
		species_id = species;
		program = prog;
		mem = memory;
		recently_executed_rule = ruleIndex;

	}
	
	
	public JSONWorldObject(int col, int row, String type, int amount) {
		this.row = row;
		this.col = col;
		this.type = type;
		this.amount = amount;
	}
	
	/**
	 * Creates a JSON critter object for which the user does not have full permissions
	 * @param typ The type of the object (must be "critter")
	 * @param c The column index of the object
	 * @param r The row index of the object
	 * @param dir The direction the critter is facing
	 * @param critterID The unique ID of the critter
	 * @param species The species name of the critter
	 * @param memory The memory of the critter
	 */
	public JSONWorldObject(String typ, int c, int r, int dir, int critterID, String species, int[] memory) {
		type = typ;
		col = c;
		row = r;
		direction = dir;
		id = critterID;
		species_id = species;
		mem = memory;
	}

	/**
	 *
	 * @param wo
	 * @param c
	 * @param r
	 */
	public JSONWorldObject(WorldObject wo, int c, int r) {
		col = c;
		row = r;

		if(wo instanceof Rock) {
			type = "rock";
		} else if(wo instanceof Food) {
			type = "food";
			value = ((Food) wo).getCalories();
		} else if(wo == null) {
			type = "nothing";
		}
	}

	/**
	 *
	 * @param sc
	 * @param c
	 * @param r
	 * @param fullPermissions
	 */
	public JSONWorldObject(SimpleCritter sc, int c, int r, int critterID, boolean fullPermissions) {
		type = "critter";
		col = c;
		row = r;
		direction = sc.getOrientation();
		id = critterID;
		species_id = sc.getName();
		mem = sc.getMemoryCopy();
		if(fullPermissions) {
			program = sc.getProgram().toString();
			recently_executed_rule = sc.getLastRuleIndex();
		}
	}

	/** Returns the type of this object. */
	public String getType() {
		return type;
	}

	/** Returns the column index of this object. */
	public Integer getCol() {
		return col;
	}

	/** Returns the row index of this object. */
	public Integer getRow() {
		return row;
	}

	/** Returns the caloric content of this object, if it is food. */
	public Integer getCalories() {
		return value;
	}

	public Integer getCritterID() {
		return id;
	}

	public String getSpeciesName() {
		return species_id;
	}

	public Integer getOrientation() {
		return direction;
	}

	public int[] getMemory() {
		return mem;
	}

	public String getProgram() {
		return program;
	}

	public Integer getLastRuleIndex() {
		return recently_executed_rule;
	}
	
	public Integer getAmount() {
		return this.amount;
	}
	@Override
	public int hashCode() {
		String s = "";
		if(type.equals("critter"))
			s += type + col + row + id + species_id + direction + Arrays.toString(mem) + program + recently_executed_rule;
		else if(type.equals("food"))
			s += type + col + row + value;
		else
			s += type + col + row;
		return s.hashCode();
	}
}