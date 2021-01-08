package gui;

import javafx.application.Application;
import javafx.stage.Stage;
import controller.Controller;
import model.Model;
import model.Tile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Random;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class View extends Application {
	private GraphicsContext gc;
	private int amountTilesHeight;
	private int amountTilesLength;
	private int height;
	private int length;
	private int tilesize;
	private int pressedTileX;
	private int pressedTileY;
	private Controller controller;
	private Image Bombimage;
	private Image Buttonimage;
	private Image Flagimage;
	private Image PressedBombimage;
	private Group root;
	private Image PressedButtonimage;
	private Canvas canvas;
	private Tile[][] tilearr;
	private Model model;
	private Controller Controller;

	public static void main(String[] args) throws FileNotFoundException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("MineDraw");
		this.root = new Group();

		this.Bombimage = new Image(new FileInputStream("Pictures\\Bomb.png"));
		this.Buttonimage = new Image(new FileInputStream("Pictures\\Button.png"));
		this.Flagimage = new Image(new FileInputStream("Pictures\\Flag.png"));
		this.PressedButtonimage = new Image(new FileInputStream("Pictures\\PressedButton.png"));
		this.PressedBombimage = new Image(new FileInputStream("Pictures\\PressedBomb.png"));
		
		this.amountTilesLength = 10;
		this.amountTilesHeight = 10;

		this.tilesize = 30;
		int amountBombs = 5;

		this.model = new Model(this, amountTilesHeight, amountTilesLength, amountBombs);
		this.controller = new Controller(model, this, tilesize);

		this.height = tilesize * amountTilesHeight;
		this.length = tilesize * amountTilesLength;

		this.canvas = new Canvas(length, height);
		gc = canvas.getGraphicsContext2D();
		this.root.getChildren().add(canvas);

		Scene scene = new Scene(root);

		scene.setOnMousePressed(controller.getEventHandler());
		scene.setOnMouseReleased(controller.getEventHandler());
		// scene.setOnMousePressed(this::handleMousePressed);
		// scene.setOnMouseReleased(this::handleMouseReleased);

		// for fun Random tiles game:
		this.tilearr = new Tile[amountTilesHeight][amountTilesLength];
		for (int i = 0; i < tilearr.length; i++) {
			for (int j = 0; j < tilearr[0].length; j++) {
				drawButton(i, j);
			}
		}

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}

	public void update(Tile[][] tilearr) {
		for (int i = 0; i < amountTilesHeight; i++) {
			for (int j = 0; j < amountTilesLength; j++) {
				if (!model.isGameOver()) {
					if (!tilearr[i][j].isCleared()) {
						drawButton(i, j);
						if (tilearr[i][j].hasFlag()) {
							drawFlag(i, j);
						}else if (tilearr[i][j].isPressedButton()){
							drawPressedButton(i,j);
						}
					} else {
						drawPressedButton(i, j);
						if (tilearr[i][j].getNeighborBombs() != 0) {
							drawNumber(i, j, tilearr[i][j].getNeighborBombs());
						}
					}
				} else {
					drawPressedButton(i,j);
					if (tilearr[i][j].hasBomb()) {
						if (tilearr[i][j].isPressedBomb()) {
							drawPressedBomb(i,j);
						} else {
						drawBomb(i,j);
						}
					} else if (tilearr[i][j].getNeighborBombs()!=0) {
						drawNumber(i,j,tilearr[i][j].getNeighborBombs());
					}
				}
			}
		}
	}

	private void drawNumber(int y, int x, int number) { // coordinates given in arraytiles
		if (number!=0) {
			Text text = new Text();
			if (number == 1) {
				text.setFill(Color.BLUE);
			}
			if (number == 2) {
				text.setFill(Color.GREEN);
			}
			if (number == 3) {
				text.setFill(Color.RED);
			}
			if (number == 4) {
				text.setFill(Color.DARKBLUE);
			}
			if (number == 5) {
				text.setFill(Color.DARKRED);
			}
			if (number == 6) {
				text.setFill(Color.LIGHTBLUE);
			}
			if (number == 7) {
				text.setFill(Color.BLACK);
			}
			if (number == 8) {
				text.setFill(Color.GREY);
			}

			text.setText(Integer.toString(number));
			text.setX(x * tilesize + tilesize / 2);// Calculate by desired tile + half tile
			text.setY(y * tilesize + tilesize / 2);// Calculate by desired tile + half tile
			text.setFont(Font.font((24.1 / 30) * tilesize));
			text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
			text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
			this.root.getChildren().add(text);
		}
		
	}

	private void drawPressedButton(int y, int x) {
		ImageView PressedButton = new ImageView(PressedButtonimage);
		PressedButton.setX(x * tilesize);
		PressedButton.setY(y * tilesize);
		PressedButton.setFitHeight(tilesize);
		PressedButton.setFitWidth(tilesize);
		this.root.getChildren().add(PressedButton);
		Rectangle rect = new Rectangle(x * tilesize, y * tilesize, tilesize, tilesize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.root.getChildren().add(rect);
	}

	private void drawPressedBomb(int y, int x) {
		ImageView PressedBomb = new ImageView(PressedBombimage);
		PressedBomb.setX(x * tilesize);
		PressedBomb.setY(y * tilesize);
		PressedBomb.setFitHeight(tilesize);
		PressedBomb.setFitWidth(tilesize);
		this.root.getChildren().add(PressedBomb);
	}
	private void drawBomb(int y, int x) {// coordinates given in arraytiles
		ImageView Bomb = new ImageView(Bombimage);
		Bomb.setX(x * tilesize);
		Bomb.setY(y * tilesize);
		Bomb.setFitHeight(tilesize);
		Bomb.setFitWidth(tilesize);
		this.root.getChildren().add(Bomb);
	}

	private void drawButton(int y, int x) {// coordinates given in arraytiles
		ImageView Button = new ImageView(Buttonimage);
		Button.setX(x * tilesize);
		Button.setY(y * tilesize);
		Button.setFitHeight(tilesize);
		Button.setFitWidth(tilesize);
		this.root.getChildren().add(Button);
	}

	private void drawFlag(int y, int x) {// coordinates given in arraytiles
		ImageView Flag = new ImageView(Flagimage);
		Flag.setX(x * tilesize);
		Flag.setY(y * tilesize);
		Flag.setFitHeight(tilesize);
		Flag.setFitWidth(tilesize);
		this.root.getChildren().add(Flag);
	}
	/*
	 * private void handleMouseReleased(MouseEvent event) {
	 * 
	 * double x = event.getX(); double y = event.getY(); int xCoorTiles = 0; int
	 * yCoorTiles = 0; while (x - tilesize > 0) { x = x - tilesize; xCoorTiles++; }
	 * while (y - tilesize > 0) { y = y - tilesize; yCoorTiles++; } Tile tile =
	 * tilearr[pressedTileY][pressedTileX]; if (!tile.hasFlag() &&
	 * !tile.isCleared()) { drawButton(yCoorTiles,xCoorTiles);//we need tile is
	 * explored else doesn't work }
	 * 
	 * 
	 * }
	 * 
	 * private void handleMousePressed(MouseEvent event) {
	 * 
	 * double x = event.getX(); double y = event.getY(); int xCoorTiles = 0; int
	 * yCoorTiles = 0; while (x - tilesize > 0) { x = x - tilesize; xCoorTiles++; }
	 * while (y - tilesize > 0) { y = y - tilesize; yCoorTiles++; } Tile tile =
	 * tilearr[yCoorTiles][xCoorTiles]; if (!tile.hasFlag() && !tile.isCleared()) {
	 * //we need tile is explored else doesn't work this.pressedTileX = xCoorTiles;
	 * this.pressedTileY = yCoorTiles; drawPressedButton(yCoorTiles,xCoorTiles); }
	 * 
	 * }
	 */
}