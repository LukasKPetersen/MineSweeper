package application;
public class Tile {

	private boolean bomb;
	private boolean flag;
	private boolean cleared;
	private int neighborBombs;
	private boolean pressedBomb;
	private boolean pressedButton;

	// Standard constructor
	public Tile() {
		// Initialize to neutral field.
		this.bomb = false;
		this.flag = false;
		this.cleared = false;
		this.pressedBomb=false;
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
	public boolean isPressedButton() {
		return this.pressedButton;
	}

	public boolean hasFlag() {
		return this.flag;
	}
	public boolean isPressedBomb() {
		return pressedBomb;
	}
	
	public boolean isCleared() {
		return this.cleared;
	}
	
	public int getNeighborBombs() {
		return this.neighborBombs;
	}

	// Set-methods
	public void setPressedButton() {
		this.pressedButton = !pressedButton;
	}
	
	public void setPressedBomb() {
		this.pressedBomb=true;
	}

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
	
}