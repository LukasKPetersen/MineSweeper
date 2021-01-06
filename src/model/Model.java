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
		int sizeX = 20;
		int sizeY = 20;
		int numberOfBombs = 20;
		boolean gameOver = false;
		int hiddenFields = sizeX * sizeY - numberOfBombs;

		if (numberOfBombs - 1 > sizeX * sizeY) {
			throw new IllegalArgumentException("Too many bombs!");
		}

		Tile[][] board = new Tile[sizeX][sizeY];

		// Fills the array with tiles
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {
				board[i][j] = new Tile();
			}
		}

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
		Point firstPlayerAction = getPlayerInput(consol, board);

		// Removes the chosen tile from possible bomb locations
		for (int i = 0; i < bombLocations.size(); i++) {
			if (firstPlayerAction.equals(bombLocations.get(i))) {
				bombLocations.remove(i);
			}
		}

		// Places random bombs
		for (int i = 0; i < numberOfBombs; i++) {
			int randomInt = r.nextInt(bombLocations.size());
			Point chosenOne = bombLocations.get(randomInt);
			bombLocations.remove(randomInt);
			board[(int) chosenOne.getX()][(int) chosenOne.getY()].setBomb();

		}

		// Calculate amount of neighbor bombs for each field

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[0].length; j++) {

				if (board[i][j].hasBomb()) {
					// Upper left
					if (i - 1 >= 0 && j - 1 >= 0) {
						board[i - 1][j - 1].incNeighborBombs();
					}
					// Upper center
					if (i - 1 >= 0) {
						board[i - 1][j].incNeighborBombs();
					}
					// Upper right
					if (i - 1 >= 0 && j + 1 < board[0].length) {
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
					if (i + 1 < 0 && j - 1 <= board[0].length) {
						board[i + 1][j - 1].incNeighborBombs();
					}
					// Bottom center
					if (i + 1 < board.length) {
						board[i + 1][j].incNeighborBombs();
					}
					// Bottom right
					if (i + 1 < board.length && j + 1 < board[0].length) {
						board[i + 1][j + 1].incNeighborBombs();
					}
				}

			}
		}

		// Clears fields after bombs have been placed
		clearNonProximity(firstPlayerAction,board,hiddenFields);
		board[(int)firstPlayerAction.getX()][(int)firstPlayerAction.getY()].clearField();
		hiddenFields--;

		// Gameloop
		while (true) {
			
			if (gameOver) {
				System.out.print("Game over! :(");
				break;
			} else if (hiddenFields == 0) {
				System.out.print("You won, Poul is proud! <3");
				break;
			}
			printArray(board);
			System.out.println(hiddenFields);
			gameOver = pressField(getPlayerInput(consol, board), board, hiddenFields, false);
			hiddenFields -= gameOver == false ? 1 : 0;
			System.out.println();
			System.out.print("******************");
			System.out.println();

			
		}

		consol.close();

	}

	// Clears fields in relation to the selected field that are not in proximity to
	// bombs (neigborBombs == 0)
	public static void clearNonProximity(Point p, Tile[][] board, int hiddenFields) {
		if (board[(int) p.getX()][(int) p.getY()].getNeighborBombs() == 0
				&& !board[(int) p.getX()][(int) p.getY()].isCleared()) {

			board[(int) p.getX()][(int) p.getY()].clearField();
			hiddenFields--;

			int pointX;
			int pointY;
			Tile chosenTile;
			boolean withinBounds;

			// Upper left tile (x-1,y-1)
			pointX = (int) p.getX() - 1;
			pointY = (int) p.getY() - 1;
			withinBounds = pointX >= 0 && pointY >= 0;

			if (withinBounds) {
				chosenTile = board[(int) p.getX() - 1][(int) p.getY() - 1];
				if (chosenTile.getNeighborBombs() == 0) {

					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Upper center (x-1,y)

			pointX = (int) p.getX() - 1;
			pointY = (int) p.getY();
			withinBounds = pointX - 1 >= 0;

			if (withinBounds) {
				chosenTile = board[(int) p.getX() - 1][(int) p.getY()];
				if (chosenTile.getNeighborBombs() == 0) {

					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Upper right (x-1,y+1)

			pointX = (int) p.getX() - 1;
			pointY = (int) p.getY() + 1;
			withinBounds = pointX - 1 >= 0 && pointY + 1 < board[0].length;

			if (withinBounds) {
				chosenTile = board[(int) p.getX() - 1][(int) p.getY() + 1];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Center left (x,y-1)

			pointX = (int) p.getX();
			pointY = (int) p.getY() - 1;
			withinBounds = pointY - 1 >= 0;

			if (withinBounds) {
				chosenTile = board[(int) p.getX()][(int) p.getY() - 1];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Center right (x,y+1)

			pointX = (int) p.getX();
			pointY = (int) p.getY() + 1;
			withinBounds = pointY + 1 < board[0].length;

			if (withinBounds) {
				chosenTile = board[(int) p.getX()][(int) p.getY() + 1];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Bottom left (x+1,y-1)

			pointX = (int) p.getX() + 1;
			pointY = (int) p.getY() - 1;
			withinBounds = pointX + 1 < board.length && pointY - 1 >= 0;

			if (withinBounds) {
				chosenTile = board[(int) p.getX() + 1][(int) p.getY() - 1];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Bottom center (x+1,y)

			pointX = (int) p.getX() + 1;
			pointY = (int) p.getY();
			withinBounds = pointX + 1 < board[0].length;

			if (withinBounds) {
				chosenTile = board[(int) p.getX() + 1][(int) p.getY()];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

			// Bottom right (x+1,y+1)

			pointX = (int) p.getX() + 1;
			pointY = (int) p.getY() + 1;
			withinBounds = pointX + 1 < board.length && pointY + 1 < board[0].length;

			if (withinBounds) {
				chosenTile = board[(int) p.getX() + 1][(int) p.getY() + 1];
				if (chosenTile.getNeighborBombs() == 0) {
					clearNonProximity(new Point(pointX, pointY), board, hiddenFields);
				}
			}

		}

	}

	public static Point getPlayerInput(Scanner consol,Tile[][] board) {
		
		System.out.print("Enter x and y coordinates for the field to press: ");
		Point input = new Point(consol.nextInt(), consol.nextInt());
		while (board[(int)input.getX()][(int)input.getY()].isCleared()) {
			System.out.print("The selected fields has already been cleared. Choose another field");
			input = new Point(consol.nextInt(), consol.nextInt());
		}
		
		return input;
		
		
		
	}

	public static void setFlag(Tile[][] board, Point p) {
		board[(int) p.getX()][(int) p.getY()].setFlag();
	}

	public static boolean pressField(Point p, Tile[][] board, int hiddenFields, boolean firstTimeRun) {

		if (!firstTimeRun) {
			clearNonProximity(p, board, hiddenFields);
		}

		if (board[(int) p.getX()][(int) p.getY()].hasBomb()) {
			return true;
		} else {
			board[(int) p.getX()][(int) p.getY()].clearField();
			printArray(board);
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


