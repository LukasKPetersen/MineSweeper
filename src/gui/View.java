package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import controller.Controller;
import model.Model;
import model.Tile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class View extends Application {
	private static int amountTilesHeight;
	private static int amountTilesLength;
	private static int amountBombs;
	private int height;
	private int length;
	private int tileSize = 30;
	private Controller controller;
	private Image bombImage;
	private Image buttonImage;
	private Image flagImage;
	private Image pressedBombImage;
	private Group root;
	private Image pressedButtonImage;
	private Model model;

	public static void main(String[] args) throws FileNotFoundException {
		setParameters(args);
		Application.launch(args);
	}

	// Reads prompt arguments and sets amount of: TilesHeight, TilesLength, and
	// bombs accordingly.
	public static void setParameters(String[] args) throws IllegalArgumentException {
		int arg1 = Integer.parseInt(args[0]);
		amountTilesHeight = ((arg1 >= 4 && arg1 <= 100) ? arg1 : 0);
		amountTilesLength = ((arg1 >= 4 && arg1 <= 100) ? arg1 : 0);
		if (amountTilesHeight == 0 || amountTilesLength == 0)
			throw new IllegalArgumentException();
		amountBombs = Integer.parseInt(args[2]);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("MineDraw");
			this.root = new Group();

			// Loads icon images to private field
			this.bombImage = new Image(new FileInputStream("Pictures\\Bomb.png"));
			this.buttonImage = new Image(new FileInputStream("Pictures\\Button.png"));
			this.flagImage = new Image(new FileInputStream("Pictures\\Flag.png"));
			this.pressedButtonImage = new Image(new FileInputStream("Pictures\\PressedButton.png"));
			this.pressedBombImage = new Image(new FileInputStream("Pictures\\PressedBomb.png"));

			this.model = new Model(this, amountTilesHeight, amountTilesLength, amountBombs);
			this.controller = new Controller(model, this, tileSize);

			this.height = tileSize * amountTilesHeight;
			this.length = tileSize * amountTilesLength;

			Scene scene = new Scene(root, length, height);

			scene.setOnMousePressed(controller.getEventHandler());
			scene.setOnMouseReleased(controller.getEventHandler());

			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	// Calls the methods necessary to draw the new board
	public void update(Tile[][] tilearr, boolean gameOver) {
		for (int i = 0; i < amountTilesHeight; i++) {
			for (int j = 0; j < amountTilesLength; j++) {
				if (!gameOver) {
					if (!tilearr[i][j].isCleared()) {
						drawButton(i, j);
						if (tilearr[i][j].hasFlag()) {
							drawFlag(i, j);
						} else if (tilearr[i][j].isPressedButton()) {
							drawPressedButton(i, j);
						}
					} else {
						drawPressedButton(i, j);
						if (tilearr[i][j].getNeighborBombs() != 0) {
							drawNumber(i, j, tilearr[i][j].getNeighborBombs());
						}
					}
				} else {
					drawPressedButton(i, j);
					if (tilearr[i][j].hasBomb()) {
						if (tilearr[i][j].isPressedBomb()) {
							drawPressedBomb(i, j);
						} else {
							drawBomb(i, j);
						}
					} else if (tilearr[i][j].getNeighborBombs() != 0) {
						drawNumber(i, j, tilearr[i][j].getNeighborBombs());
					}
				}
			}
		}
	}

	// Draws all the amount of neighbors of a given tile
	private void drawNumber(int y, int x, int number) {
		if (number != 0) {
			Text text = new Text();
			if (number == 1) {
				text.setFill(Color.BLUE);
			} else if (number == 2) {
				text.setFill(Color.GREEN);
			} else if (number == 3) {
				text.setFill(Color.RED);
			} else if (number == 4) {
				text.setFill(Color.DARKBLUE);
			} else if (number == 5) {
				text.setFill(Color.DARKRED);
			} else if (number == 6) {
				text.setFill(Color.LIGHTBLUE);
			} else if (number == 7) {
				text.setFill(Color.BLACK);
			} else if (number == 8) {
				text.setFill(Color.GREY);
			}

			text.setText(Integer.toString(number));
			text.setX(x * tileSize + tileSize / 2);
			text.setY(y * tileSize + tileSize / 2);
			text.setFont(Font.font((24.1 / 30) * tileSize));
			
			//Sets the given number in the center of tile
			text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
			text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
			
			this.root.getChildren().add(text);
		}

	}

	private void drawPressedButton(int y, int x) {
		drawImage(y, x, pressedButtonImage);
		Rectangle rect = new Rectangle(x * tileSize, y * tileSize, tileSize, tileSize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.root.getChildren().add(rect);
	}

	private void drawImage(int y, int x, Image im) {
		ImageView image = new ImageView(im);
		image.setX(x * tileSize);
		image.setY(y * tileSize);
		image.setFitHeight(tileSize);
		image.setFitWidth(tileSize);
		this.root.getChildren().add(image);
	}

	private void drawPressedBomb(int y, int x) {
		drawImage(y, x, pressedBombImage);
	}

	private void drawBomb(int y, int x) {// coordinates given in arraytiles
		drawImage(y, x, bombImage);
	}

	private void drawButton(int y, int x) {
		drawImage(y, x, buttonImage);
	}

	private void drawFlag(int y, int x) {
		drawImage(y, x, flagImage);
	}
}
