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
	private int amountTilesHeight;
	private int amountTilesLength;
	private int height;
	private int length;
	private int tilesize;
	private Controller controller;
	private Image Bombimage;
	private Image Buttonimage;
	private Image Flagimage;
	private Image PressedBombimage;
	private Group root;
	private Image PressedButtonimage;
	private Tile[][] tilearr;
	private Model model;
	@SuppressWarnings("unused")
	private Controller Controller;

	public static void main(String[] args) throws FileNotFoundException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
		primaryStage.setTitle("MineDraw");
		this.root = new Group();

		this.Bombimage = new Image(new FileInputStream("Pictures\\Bomb.png"));
		this.Buttonimage = new Image(new FileInputStream("Pictures\\Button.png"));
		this.Flagimage = new Image(new FileInputStream("Pictures\\Flag.png"));
		this.PressedButtonimage = new Image(new FileInputStream("Pictures\\PressedButton.png"));
		this.PressedBombimage = new Image(new FileInputStream("Pictures\\PressedBomb.png"));
		
		this.amountTilesLength = 8;
		this.amountTilesHeight = 8;

		this.tilesize = 30;
		int amountBombs = 10;
		
		this.model = new Model(this, amountTilesHeight, amountTilesLength, amountBombs);
		this.controller = new Controller(model, this, tilesize);

		this.height = tilesize * amountTilesHeight;
		this.length = tilesize * amountTilesLength;

		Scene scene = new Scene(root,length,height);

		scene.setOnMousePressed(controller.getEventHandler());
		scene.setOnMouseReleased(controller.getEventHandler());
		
		this.tilearr = new Tile[amountTilesHeight][amountTilesLength];
		for (int i = 0; i < tilearr.length; i++) {
			for (int j = 0; j < tilearr[0].length; j++) {
				drawButton(i, j);
			}
		}

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void update(Tile[][] tilearr, boolean gameOver) {
		for (int i = 0; i < amountTilesHeight; i++) {
			for (int j = 0; j < amountTilesLength; j++) {
				if (!gameOver) {
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
			text.setX(x * tilesize + tilesize / 2);
			text.setY(y * tilesize + tilesize / 2);
			text.setFont(Font.font((24.1 / 30) * tilesize));
			text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
			text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
			this.root.getChildren().add(text);
		}
		
	}

	private void drawPressedButton(int y, int x) {
		drawImage(y,x,PressedButtonimage);
		Rectangle rect = new Rectangle(x * tilesize, y * tilesize, tilesize, tilesize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.root.getChildren().add(rect);
	}
	private void drawImage(int y, int x, Image im) {
		ImageView image = new ImageView(im);
		image.setX(x * tilesize);
		image.setY(y * tilesize);
		image.setFitHeight(tilesize);
		image.setFitWidth(tilesize);
		this.root.getChildren().add(image);
	}


	private void drawPressedBomb(int y, int x) {	
		drawImage(y,x,PressedBombimage);
	}
	private void drawBomb(int y, int x) {// coordinates given in arraytiles
		drawImage(y,x,Bombimage);
	}

	private void drawButton(int y, int x) {
		drawImage(y,x,Buttonimage);
	}

	private void drawFlag(int y, int x) {
		drawImage(y,x,Flagimage);
	}

	//Getters and setters methods
	
	public int getAmountTilesHeight() {
		return amountTilesHeight;
	}

	public void setAmountTilesHeight(int amountTilesHeight) {
		this.amountTilesHeight = amountTilesHeight;
	}

	public int getAmountTilesLength() {
		return amountTilesLength;
	}

	public void setAmountTilesLength(int amountTilesLength) {
		this.amountTilesLength = amountTilesLength;
	}

	public int getTilesize() {
		return tilesize;
	}

	public void setTilesize(int tilesize) {
		this.tilesize = tilesize;
	}
	
}