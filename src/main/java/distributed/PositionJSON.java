package distributed;

public class PositionJSON {

	private Integer row;
	private Integer col;

	public PositionJSON(int col, int row) {
		this.row = row;
		this.col = col;
	}

	public Integer getRow(){
		return this.row;
	}
	
	public Integer getColumn() {
		return this.col;
	}
	
}
