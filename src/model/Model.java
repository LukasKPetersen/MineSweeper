package src.model;

import java.util.Random;
import java.util.ArrayList;
import java.awt.Point;
import java.util.Scanner;

public class Model {

	public static void main(String[] args) {

		Scanner consol = new Scanner(System.in);
		Random r = new Random();

		// Variables
		int sizeX = 8;
		int sizeY = 8;
		int numberOfBombs = 10;
		boolean gameOver = false;
		final int totalFieldsToClear = sizeX * sizeY - numberOfBombs;
		int hiddenFields = totalFieldsToClear;

		// Throws exception if more bombs than possible
		if (numberOfBombs - 1 > sizeX * sizeY) {
			consol.close();
			throw new IllegalArgumentException("Too many bombs!");
		}

		// Creates the board array
		Tile[][] board = new Tile[sizeX][sizeY];

		// Fills the board with tiles
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new Tile();
			}
		}

		// Creates arraylist to keep track of possible bomb locations
		ArrayList<Point> bombLocations = new ArrayList<Point>();

		// Fills the array with all possible positions, size will be sizeX*sizeY.
		for (int j = 0; j < sizeX; j++) {
			for (int k = 0; k < sizeY; k++) {
				bombLocations.add(new Point(j, k));
			}
		}

		// Game initialization
		// Gets first input from user to determine first field pressed
		printArray(board);
		Point playerAction = getPlayerInput(consol, board);

		// Removes the chosen starting-tile from possible bomb locations
		for (int i = 0; i < bombLocations.size(); i++) {
			if (playerAction.equals(bombLocations.get(i))) {
				bombLocations.remove(i);
			}
		}

		// Places random bombs using bombLocations array
		for (int i = 0; i < numberOfBombs; i++) {
			int randomInt = r.nextInt(bombLocations.size());
			Point chosenOne = bombLocations.get(randomInt);
			bombLocations.remove(randomInt);
			board[(int) chosenOne.getX()][(int) chosenOne.getY()].setBomb();

		}

		// Calculate amount of neighbor bombs for each field. Can be read by board[(x
		// pos)][(y pos)].getNeighborBombs();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {

				if (board[i][j].hasBomb()) {
					// Upper left
					if ((i - 1 >= 0) && (j - 1 >= 0)) {
						board[i - 1][j - 1].incNeighborBombs();
					}
					// Upper center
					if (i - 1 >= 0) {
						board[i - 1][j].incNeighborBombs();
					}
					// Upper right
					if ((i - 1 >= 0) && (j + 1 < board[0].length)) {
						board[i - 1][j + 1].incNeighborBombs();
					}
					// Center left
					if (j - 1 >= 0) {
						board[i][j - 1].incNeighborBombs();
					}
					// Center right
					if (j + 1 < board[0].length) {
						board[i][j + 1].incNeighborBombs();
					}
					// Bottom left
					if ((i + 1 < board.length) && (j - 1 >= 0)) {
						board[i + 1][j - 1].incNeighborBombs();
					}
					// Bottom center
					if (i + 1 < board.length) {
						board[i + 1][j].incNeighborBombs();
					}
					// Bottom right
					if ((i + 1 < board.length) && (j + 1 < board[0].length)) {
						board[i + 1][j + 1].incNeighborBombs();
					}
				}

			}
		}

		// Clears fields after bombs have been placed
		//The first fields selected must not be cleared before using this function, as
		//the field not being cleared is used as a starting condition.
		clearNonProximity(playerAction, board);
		// If clearNonProximity hasn't cleared the first tile chosen by the player,
		// the tile gets cleared
		board[(int) playerAction.getX()][(int) playerAction.getY()].clearField();
		hiddenFields = fieldsLeft(board, totalFieldsToClear);
		
		//The game is now ready to start gameloop.
		// Gameloop
		while (true) {
			
			//Win-lose conditions. Put these first to make sure it's the first thing being evaluated
			// in the gameloop.
			if (gameOver) {
				System.out.print("Oh no, you hit a bomb!\nGame over :(");
				break;
			} else if (hiddenFields == 0) {
				System.out.print("You won, Poul is proud! :) <3");
				break;
			}
			
			//Show the board
			printArray(board);
			//Recieve player action
			playerAction = getPlayerInput(consol, board);
			//Use action to make move on chosen tile.
			//pressFields also clears tiles with clearNonProximity()
			gameOver = pressField(playerAction, board);
			//hiddenFields must be updated after every move as some tiles
			//might have been cleared using clearNonProximity()
			hiddenFields = fieldsLeft(board, totalFieldsToClear);
			
			System.out.println();
			System.out.print("******************");
			System.out.println();

		}

		consol.close();

	}

	// Clears fields in relation to the selected field that are not in proximity to
	// bombs (neigborBombs == 0)
	//The field used as input must NOT be cleared, otherwise the function will not run recursively!
	
	public static void clearNonProximity(Point p, Tile[][] board) {
		
		//Examines the given field to figure out if it's eligible to be "autocleared"
		if (board[(int) p.getX()][(int) p.getY()].getNeighborBombs() == 0
				&& !board[(int) p.getX()][(int) p.getY()].isCleared()) {
			
			board[(int) p.getX()][(int) p.getY()].clearField();
			
			
			//Examines all surrounding fields by determining if they're within bounds. If so,
			//the runction runs recursively on the given tile.
			
			//Variable to determine bounds for each tile
			int pointX;
			int pointY;
			Tile chosenTile;
			boolean withinBounds;
			
			
			//Tiles are now inspected------------------------------------------------------------|

			// Upper left tile (x-1,y-1)
			
			//[X][ ][ ]
			//[ ][C][ ]
			//[ ][ ][ ]
			
			pointX = (int) p.getX() - 1;
			pointY = (int) p.getY() - 1;
			withinBounds = (pointX >= 0) && (pointY >= 0);

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {

					clearNonProximity(new Point(pointX, pointY), board);
				}
			}

			// Upper center tile (x-1,y)
			
			//[ ][X][ ]
			//[ ][C][ ]
			//[ ][ ][ ]
			
			pointX = (int) p.getX() - 1;
			pointY = (int) p.getY();
			withinBounds = pointX >= 0;

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {

					clearNonProximity(new Point(pointX, pointY), board);

				}
			}

			// Upper right (x-1,y+1)
			
			//[ ][ ][X]
			//[ ][C][ ]
			//[ ][ ][ ]

			pointX = (int) p.getX() - 1;
			pointY = (int) p.getY() + 1;
			withinBounds = (pointX >= 0) && (pointY < board[0].length);

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board);

				}
			}

			// Center left (x,y-1)
			
			//[ ][ ][ ]
			//[X][C][ ]
			//[ ][ ][ ]

			pointX = (int) p.getX();
			pointY = (int) p.getY() - 1;
			withinBounds = pointY >= 0;

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board);

				}
			}

			// Center right (x,y+1)
			
			//[ ][ ][ ]
			//[ ][C][X]
			//[ ][ ][ ]

			pointX = (int) p.getX();
			pointY = (int) p.getY() + 1;
			withinBounds = pointY < board[0].length;

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board);

				}
			}

			// Bottom left (x+1,y-1)
			
			//[ ][ ][ ]
			//[ ][C][ ]
			//[X][ ][ ]
			
			pointX = (int) p.getX() + 1;
			pointY = (int) p.getY() - 1;
			withinBounds = (pointX < board.length) && (pointY >= 0);

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board);

				}
			}

			// Bottom center (x+1,y)
			
			//[ ][ ][ ]
			//[ ][C][ ]
			//[ ][X][ ]

			pointX = (int) p.getX() + 1;
			pointY = (int) p.getY();
			withinBounds = pointX < board.length;

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board);

				}
			}

			// Bottom right (x+1,y+1)
			
			//[ ][ ][ ]
			//[ ][C][ ]
			//[ ][ ][X]

			pointX = (int) p.getX() + 1;
			pointY = (int) p.getY() + 1;
			withinBounds = (pointX < board.length) && (pointY < board[0].length);

			if (withinBounds) {
				chosenTile = board[pointX][pointY];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board);

				}
			}
			
			//Inspection is done------------------------------------------------------------|

		}

	}

	// Counts and returns the amount of fields left to be cleared before the player
	// has won.
	public static int fieldsLeft(Tile[][] board, int totalFieldsToClear) {
		int clearedFields = 0;
		// Fills the board with tiles
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				if (board[i][j].isCleared() && !board[i][j].hasBomb()) {
					clearedFields++;
				}
			}
		}

		return totalFieldsToClear - clearedFields;

	}

	public static Point getPlayerInput(Scanner consol, Tile[][] board) {

		System.out.print("Enter x and y coordinates for the field to press: ");
		Point input = new Point(consol.nextInt(), consol.nextInt());
		while (board[(int) input.getX()][(int) input.getY()].isCleared()) {
			System.out.print("The selected fields has already been cleared. Choose another field");
			input = new Point(consol.nextInt(), consol.nextInt());
		}

		return input;

	}

	public static void setFlag(Tile[][] board, Point p) {
		board[(int) p.getX()][(int) p.getY()].setFlag();
	}

	public static boolean pressField(Point p, Tile[][] board) {

		// 2nd place holds info about how many fields were cleared

		// 1st place holds info about the game status. 1 if game is over, 0 if game
		// continues.
		if (board[(int) p.getX()][(int) p.getY()].hasBomb()) {
			return true;
		} else {
			//It is important to use clearNonProximity() before the selected field is cleared, as
			//the function will not run if the selected fields has been cleared beforehand.
			clearNonProximity(p, board);
			board[(int) p.getX()][(int) p.getY()].clearField();
			return false;
		}

	}

	public static void printNeighbor(Tile[][] a) {
		String output = "";

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				output += a[i][j].getNeighborBombs();
			}
			output += "\n";
		}

		System.out.print(output);
	}

	public static void printArray(Tile[][] a) {
		String output = "";

		for (int i = 0; i < a.length; i++) {
			for (int j = 0; j < a[0].length; j++) {
				output += a[i][j].toString();
			}
			output += "\n";
		}

		System.out.print(output);
	}

}




