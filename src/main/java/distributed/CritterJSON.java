package distributed;

public class CritterJSON {

	private String species_id;
	private String program;
	private int[] mem;
	private PositionJSON[] positions;
	private Integer num;

	public CritterJSON(String species_id, String program, int[] mem, PositionJSON[] positions) {
		this.species_id = species_id;
		this.program = program;
		this.mem = mem;
		this.positions = positions;
	}

	public CritterJSON(String species_id, String program, int[] mem, Integer num) {
		this.species_id = species_id;
		this.program = program;
		this.mem = mem;
		this.num = num;
	}

	public String getSpeciesId() {
		return this.species_id;
	}

	public String getProgram() {
		return this.program;
	}

	public int[] getMem() {
		return this.mem;
	}

	public PositionJSON[] getPositions() {
		return this.positions;
	}

	public Integer getNum() {
		return this.num;
	}
}