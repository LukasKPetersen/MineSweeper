
package src.model;

import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;

import src.gui.View;

public class Model {
	private int sizeX;
	private int sizeY;
	private int numberOfBombs;
	private Tile[][] board;
	private int hiddenFields;
	private boolean gameOver;
	private int numOfMoves = 0;
	private int totalFieldsToClear;

	private src.gui.View view;

	public Model(src.gui.View view, int sizeY, int sizeX, int numberOfBombs) {

		this.view = view;

		// Variables
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.numberOfBombs = numberOfBombs;
		this.gameOver = false;
		this.hiddenFields = sizeX * sizeY - numberOfBombs;
		this.totalFieldsToClear = hiddenFields;

		// Throws exception if more bombs than possible
		if (numberOfBombs - 1 > sizeX * sizeY) {
			throw new IllegalArgumentException("Too many bombs!");
		}

		// Creates the board array
		this.board = new Tile[sizeY][sizeX];

		// Fills the board with tiles
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = new Tile();
			}
		}
		this.view.update(board, gameOver);
	}

	public void FirstMove(int x, int y) {
		// Creates arraylist to keep track of possible bomb locations
		ArrayList<Point> bombLocations = new ArrayList<Point>();

		// Fills the array with all possible positions, size will be sizeX*sizeY.
		for (int j = 0; j < sizeY; j++) {
			for (int k = 0; k < sizeX; k++) {
				bombLocations.add(new Point(j, k));
			}
		}

		// Game initialization
		// Gets first input from user to determine first field pressed

		Point playerAction = new Point(y, x);

		// Removes the chosen starting-tile from possible bomb locations
		for (int i = 0; i < bombLocations.size(); i++) {
			if (playerAction.equals(bombLocations.get(i))) {
				bombLocations.remove(i);
			}
		}

		// Places random bombs using bombLocations array
		Random r = new Random();
		for (int i = 0; i < numberOfBombs; i++) {
			int randomInt = r.nextInt(bombLocations.size());
			Point chosenOne = bombLocations.get(randomInt);
			bombLocations.remove(randomInt);
			this.board[(int) chosenOne.getX()][(int) chosenOne.getY()].setBomb();
		}

		// Calculate amount of neighbor bombs for each field. Can be read by board[(x
		// pos)][(y pos)].getNeighborBombs();
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j].hasBomb()) {
					for (int k = -1; k < 2; k++) {
						if (i + k >= 0 && i + k < board.length) {
							for (int m = -1; m < 2; m++) {
								if (j + m >= 0 && j + m < board[0].length) {
									if (!(k == 0 && m == 0)) {
										this.board[i + k][j + m].incNeighborBombs();
									}
								}
							}
						}
					}
				}
			}
		}

		// Clears fields after bombs have been placed
		// The first fields selected must not be cleared before using this function, as
		// the field not being cleared is used as a starting condition.
		clearNonProximity(y, x);
		// If clearNonProximity hasn't cleared the first tile chosen by the player,
		// the tile gets cleared
		this.board[(int) playerAction.getX()][(int) playerAction.getY()].clearField();
		int fieldstoclear = 0;
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j].isCleared()) {
					fieldstoclear++;
				}
			}
		}
		this.hiddenFields = totalFieldsToClear - fieldstoclear;
		if (hiddenFields == 0) {
			this.gameOver = true;
		}
	}

	public void update(int y, int x) {
		if (!gameOver) {
			if (numOfMoves == 0) {
				FirstMove(x, y);
			} else {
				pressField(y, x);
			}
			this.numOfMoves++;
			view.update(board, gameOver);
		}
	}

	// Clears fields in relation to the selected field that are not in proximity to
	// bombs (neigborBombs == 0)
	// The field used as input must NOT be cleared, otherwise the function will not
	// run recursively!

	public void clearNonProximity(int y, int x) {
		if (!board[y][x].isCleared()) {
			// Examines the given field to figure out if it's eligible to be "autocleared"
			if (board[y][x].getNeighborBombs() == 0) {
				this.board[y][x].clearField();
				// Examines all surrounding fields by determining if they're within bounds. If
				// so,
				// the runction runs recursively on the given tile.

				// Variable to determine bounds for each tile
				int pointX;
				int pointY;
				Tile chosenTile;
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (!(j == 0 && i == 0)) {
							pointY = y + i;
							pointX = x + j;
							if ((pointX >= 0) && (pointY >= 0) && (pointY < board.length)
									&& (pointX < board[0].length)) {
								chosenTile = board[pointY][pointX];
								clearNonProximity(pointY, pointX);
							}
						}
					}
				}
			} else {
				this.board[y][x].clearField();
			}
		}
	}

	// Counts and returns the amount of fields left to be cleared before the player
	// has won.

	public void setFlag(int y, int x) {
		if (!board[y][x].isCleared()) {
			this.board[y][x].setFlag();
		}
		view.update(board, gameOver);

	}

	public void setPressedButton(int y, int x) {
		this.board[y][x].setPressedButton();
		view.update(board, gameOver);
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	public void pressField(int y, int x) {

		// 2nd place holds info about how many fields were cleared

		// 1st place holds info about the game status. 1 if game is over, 0 if game
		// continues.
		if (board[y][x].hasBomb()) {
			board[y][x].setPressedBomb();
			this.gameOver = true;
		} else {
			// It is important to use clearNonProximity() before the selected field is
			// cleared, as
			// the function will not run if the selected fields has been cleared beforehand.
			clearNonProximity(y, x);
			int fieldstoclear = 0;
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j].isCleared()) {
						fieldstoclear++;
					}
				}
			}
			this.hiddenFields = totalFieldsToClear - fieldstoclear;
			if (hiddenFields == 0) {
				this.gameOver = true;
			}
		}
	}
}
