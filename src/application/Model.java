package application;

import java.util.Random;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Date;
import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Model {
	private int sizeX;
	private int sizeY;
	private int numberOfBombs;
	private Tile[][] board;
	private int hiddenFields;
	private boolean gameOver;
	private int numOfMoves = 0;
	private int totalFieldsToClear;
	private boolean isGameLost = false;
	private boolean isGameWon = false;
	private View view;

	public Model(View view, int sizeY, int sizeX, int numberOfBombs) {

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
		this.view.update(board, gameOver, isGameWon, isGameLost);
		this.view.updateBombsLeft(numberOfBombs, true);
	}

	// Saves the game state into a text file. Can be loaded with
	// loadFameFromFile("[path]")
	public void saveGameToFile() throws IOException {
		String difficultyLevel;;

		String fileName = new SimpleDateFormat("'Minesweeper save 'HH,mm,ss dd-MM-yyyy'.txt'").format(new Date());
		File saveGameFile = new File(fileName);
		FileWriter writer = new FileWriter(fileName);
		String boardLine = "";
		
		if (this.board.length == 8) {
			//Adding space to make it readable in the saved file.
			difficultyLevel = "easy ";
		}else if(this.board.length == 13){
			difficultyLevel = "medium ";
		}else if (this.board.length == 16) {
			difficultyLevel = "hard ";
		}else {
			difficultyLevel = "errorInDiffivulty\n";
		}
		

		for (int i = 0; i < this.board.length; i++) {
			for (int j = 0; j < this.board[0].length; j++) {
				boardLine += this.board[i][j].toString();
			}
			boardLine += "\n";
		}
		writer.write(difficultyLevel);
		writer.write(this.numOfMoves+"\n");
		writer.write(boardLine);
		writer.close();
	}

	// Loads a saved file created by the saveGameToFile() method.
	public Tile[][] loadGameFromFile(String filePath) {
		String tileInfo;
		String difficultyLevel = "unknown";
		File gameFile = new File(filePath);
		int rowCounter = 0;
		int columnCounter = 0;
		
		try {
			Scanner fileScanner = new Scanner(gameFile);
			difficultyLevel = fileScanner.next();
			this.numOfMoves = fileScanner.nextInt();
			fileScanner.nextLine();
			
			//Sets board to correct size of difficulty level
			if (difficultyLevel.equals("easy")) {
				this.board = new Tile[8][8];
			}else if(difficultyLevel.equals("medium")) {
				this.board = new Tile[13][15];
			}else if (difficultyLevel.equals("hard")) {
				this.board = new Tile[16][30];
			}
			
			//Initializes board
			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					this.board[i][j] = new Tile();
				}
			}
			
			while (fileScanner.hasNextLine()) {
				Scanner line = new Scanner(fileScanner.nextLine());
				while (line.hasNext()) {
					tileInfo = line.next();
					if (tileInfo.contains("B")) {
						board[rowCounter][columnCounter].setBomb();
					}
					if (tileInfo.contains("F")) {
						board[rowCounter][columnCounter].setFlag();
					}
					if (tileInfo.contains("C")) {
						board[rowCounter][columnCounter].clearField();
					}
					
					columnCounter++;
				}
				rowCounter++;
				columnCounter = 0;
				
			}
			this.numOfMoves=1;
			countNeighborBombs(this.board);
			view.setNewDimensions(board);
			view.update(board, gameOver, isGameWon, isGameLost);
			
		} catch (FileNotFoundException e) {
			System.out.print("Error while loading game from file.");
		}
		return this.board;
		
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
		
		countNeighborBombs(this.board);


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
			this.view.smileyFaceSetter("WinSmiley");
		}
	}

	public void update(int y, int x) {
		if (!board[y][x].hasFlag()) {
			if (!gameOver) {
				if (numOfMoves == 0) {
					FirstMove(x, y);
					view.firstMove();
				} else {
					pressField(y, x);
				}
				this.numOfMoves++;
				view.update(board, gameOver, isGameWon, isGameLost);
			}
		}
	}
	
	public void countNeighborBombs(Tile[][] board) {
		// Calculate amount of neighbor bombs for each field. Can be read by board[(x
				// pos)][(y pos)].getNeighborBombs();

				// These two for-loops go through the entire board.
				for (int i = 0; i < board.length; i++) {
					for (int j = 0; j < board[0].length; j++) {
						// Checks if the tile in question has a bomb
						if (board[i][j].hasBomb()) {
							// Inspects the tiles around the tile in question
							for (int k = -1; k < 2; k++) {
								// Checks if the tile in question is within horizontal bounds of the array
								if (i + k >= 0 && i + k < board.length) {
									// Does the same thing for the vertical direction
									for (int m = -1; m < 2; m++) {
										if (j + m >= 0 && j + m < board[0].length) {
											// If we're not inspectng the center field
											if (!(k == 0 && m == 0)) {
												// incremnt the amount of neighborBombs for the inspected field
												this.board[i + k][j + m].incNeighborBombs();
											}
										}
									}
								}
							}
						}
					}
				}
	}

	// Clears fields in relation to the selected field that are not in proximity to
	// bombs (neigborBombs == 0)
	// The field used as input must NOT be cleared, otherwise the function will not
	// run recursively!

	public void clearNonProximity(int y, int x) {
		if (!board[y][x].isCleared() && !board[y][x].hasFlag()) {
			// Examines the given field to figure out if it's eligible to be "autocleared"
			if (board[y][x].getNeighborBombs() == 0) {
				this.board[y][x].clearField();
				// Examines all surrounding fields by determining if they're within bounds. If
				// so,
				// the runction runs recursively on the given tile.

				// Variable to determine bounds for each tile
				int pointX;
				int pointY;
				for (int i = -1; i < 2; i++) {
					for (int j = -1; j < 2; j++) {
						if (!(j == 0 && i == 0)) {
							pointY = y + i;
							pointX = x + j;
							if ((pointX >= 0) && (pointY >= 0) && (pointY < board.length)
									&& (pointX < board[0].length)) {
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

	public void reset() {
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				this.board[i][j] = new Tile();
			}
		}
		this.gameOver = false;
		this.isGameLost = false;
		this.isGameWon = false;
		this.numOfMoves = 0;
		this.hiddenFields = sizeX * sizeY - numberOfBombs;
		this.totalFieldsToClear = hiddenFields;
		this.view.update(board, gameOver, isGameWon, isGameLost);
		view.updateBombsLeft(numberOfBombs, true);
	}

	public void setFlag(int y, int x) {
		if (!gameOver) {
			if (!board[y][x].isCleared()) {
				this.board[y][x].setFlag();
			}
			view.updateFlagOrPressedTile(board, y, x);

			int count = numberOfBombs;

			for (int i = 0; i < board.length; i++) {
				for (int j = 0; j < board[0].length; j++) {
					if (board[i][j].hasFlag()) {
						count--;
					}
				}
			}
			view.updateBombsLeft(count, true);
		}
	}

	public void setPressedButton(int y, int x, boolean mousePressed) {
		if (!gameOver) {
			if (!board[y][x].isCleared()) {
				if (!board[y][x].isPressedButton() && mousePressed) {
					this.board[y][x].setPressedButton();
				} else if (board[y][x].isPressedButton() && !mousePressed) {
					this.board[y][x].setPressedButton();
				}
			}
			this.view.updateFlagOrPressedTile(board, y, x);
		}
	}

	public boolean isGameOver() {
		return this.gameOver;
	}

	// This method
	// 1. Presses the button on the given coordinates.
	// 2. Counts how many fields left before player has won, updates fieldstoclear
	// 3. Auto-clears surrounding fields with clearNonProximity().
	public void pressField(int y, int x) {

		if (board[y][x].hasBomb()) {
			board[y][x].setPressedBomb();
			this.gameOver = true;
			this.isGameLost = true;
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
				this.isGameWon = true;
			}
		}
	}
}
