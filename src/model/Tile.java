package src.model;

public class Tile {

	private boolean bomb;
	private boolean flag;
	private boolean cleared;
	private int neighborBombs;

	// Standard constructor
	public Tile() {
		// Initialize to neutral field.
		this.bomb = false;
		this.flag = false;
		this.cleared = false;
	}

	// Specific field construction
	public Tile(boolean bomb, boolean flag, boolean cleared) {
		this.bomb = bomb;
		this.flag = flag;
		this.cleared = cleared;
	}

	// Get-methods
	public boolean hasBomb() {
		return this.bomb;
	}

	public boolean hasFlag() {
		return this.flag;
	}

	public boolean isCleared() {
		return this.cleared;
	}
	
	public int getNeighborBombs() {
		return this.neighborBombs;
	}

	// Set-methods
	public void setBomb() {
		this.bomb = true;
	}

	public void setFlag() {
		this.flag = !this.flag;
	}

	public void clearField() {
		this.cleared = true;
	}
	public void incNeighborBombs() {
		this.neighborBombs++;
	}

	public String toString() {
		if (this.bomb && this.flag) {
			return "FB";
		} else if (this.bomb) {
			return "B";
		} else if (this.flag) {
			return "F";
		} else if(this.cleared){
			return "O";
		}else {
			return "-";
		}

	}
}

