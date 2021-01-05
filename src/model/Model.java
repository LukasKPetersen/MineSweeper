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

		printArray(board);

		Point firstPlayerAction = getPlayerInput(consol);

		pressField(firstPlayerAction, board, hiddenFields);

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
					if (i - 1 >= 0 && j + 1 < board.length) {
						board[i - 1][j + 1].incNeighborBombs();
					}
					// Center left
					if (j - 1 >= 0) {
						board[i][j - 1].incNeighborBombs();
					}
					// Center right
					if (j + 1 < board.length) {
						board[i][j + 1].incNeighborBombs();
					}
					// Bottom left
					if (i + 1 < board[0].length && j - 1 >= 0) {
						board[i + 1][j - 1].incNeighborBombs();
					}
					// Bottom center
					if (i + 1 < board[0].length) {
						board[i + 1][j].incNeighborBombs();
					}
					// Bottom right
					if (i + 1 < board.length && j + 1 < board[0].length) {
						board[i + 1][j + 1].incNeighborBombs();
					}
				}

			}
		}

		// Gameloop
		while (true) {

			printNeighbor(board);
			printArray(board);
			gameOver = pressField(getPlayerInput(consol), board, hiddenFields);
			System.out.println();
			System.out.print("******************");
			System.out.println();

			if (gameOver) {
				System.out.print("Game over! :(");
				break;
			} else if (hiddenFields == 0) {
				System.out.print("You won, Poul is proud! <3");
				break;
			}
		}

		consol.close();

	}

	public static Point getPlayerInput(Scanner consol) {
		System.out.print("Enter x and y coordinates for the field to press: ");
		return new Point(consol.nextInt(), consol.nextInt());
	}

	public static void setFlag(Tile[][] board, Point p) {
		board[(int) p.getX()][(int) p.getY()].setFlag();
	}

	public static boolean pressField(Point p, Tile[][] board, int hiddenFields) {

		if (board[(int) p.getX()][(int) p.getY()].hasBomb()) {
			return true;
		} else {
			board[(int) p.getX()][(int) p.getY()].clearField();
			hiddenFields--;
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
