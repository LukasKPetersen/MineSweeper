package src.gui;

import javafx.application.Application;
import javafx.stage.Stage;
import src.controller.Controller;
import src.model.Model;
import src.model.Tile;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Timer;
import java.util.TimerTask;

import javafx.scene.Scene;
import javafx.scene.Group;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class View extends Application {
	private MineSweeperAudio audio;
	private int amountTilesHeight;
	private int amountTilesLength;
	private int amountBombs;
	private int height;
	private int length;
	private int tilesize;
	private Controller controller;
	private Image Bombimage;
	private Image Buttonimage;
	private Image Flagimage;
	private Image PressedBombimage;
	private Group root;
	private Group header;
	private Image PressedButtonimage;
	private Tile[][] tilearr;
	private Model model;
	private Controller Controller;
	private boolean gameIsOver = false;
	private BorderPane bpane;
	private int headerHeight;
	private int borderThickNess=25;
	private int timer;

	public static void main(String[] args) throws FileNotFoundException {
		// setParameters(args);
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			primaryStage.setTitle("MineDraw");
			
			audio = new MineSweeperAudio();
			audio.playSoundTrack();
			//audio.stopSoundTrack();

			this.Bombimage = new Image(new FileInputStream("Pictures\\Bomb.png"));
			this.Buttonimage = new Image(new FileInputStream("Pictures\\Button.png"));
			this.Flagimage = new Image(new FileInputStream("Pictures\\Flag.png"));
			this.PressedButtonimage = new Image(new FileInputStream("Pictures\\PressedButton.png"));
			this.PressedBombimage = new Image(new FileInputStream("Pictures\\PressedBomb.png"));

			this.amountTilesLength = 10;
			this.amountTilesHeight = 10;

			this.tilesize = 20;
			int amountBombs = 99;
			this.headerHeight=75;
			
			this.height = tilesize * amountTilesHeight+borderThickNess*2;
			this.length = tilesize * amountTilesLength+borderThickNess*2;

			this.root = new Group();
			Rectangle centerBackGround = new Rectangle(0,0, length, height);
			centerBackGround.setFill(Color.LIGHTGREY);
			this.root.getChildren().add(centerBackGround);
			drawEdgeCenter();
			
			this.header = new Group();
			Rectangle HeaderBackGround = new Rectangle(0,0, length, headerHeight+1);
			HeaderBackGround.setFill(Color.LIGHTGREY);
			this.header.getChildren().add(HeaderBackGround);
			drawEdgeHeader();
			
			Timer updater = new Timer();
			updater.schedule(new TimerTask() {
				@Override
				public void run() {
					drawTimer(timer);
					timer++;
				}
			}, 0, 1000);
			
			this.bpane = new BorderPane();
			this.bpane.setTop(header);
			this.bpane.setCenter(root);
			
			
			this.model = new Model(this, amountTilesHeight, amountTilesLength, amountBombs);
			this.controller = new Controller(model, this, tilesize,headerHeight,borderThickNess,height,length);

			
			Scene wholeScene = new Scene(bpane);
			
			wholeScene.setOnMousePressed(controller.getEventHandler());
			wholeScene.setOnMouseReleased(controller.getEventHandler());
			
			
			
			primaryStage.setScene(wholeScene);
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
	}
	public void drawTimer(int timer) {
		Text text = new Text();
		text.setFill(Color.BLACK);
		text.setText(Integer.toString(timer));
		text.setX(30);
		text.setY((double)headerHeight/2);
		text.setFont(Font.font((30)));
		text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
		text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
		this.header.getChildren().add(text);
	}
	
	public void drawEdgeHeader() {
		Rectangle rect1 = new Rectangle(20,20,length-40,5);
		rect1.setFill(Color.DARKGREY);

		Rectangle rect2 = new Rectangle(20,25,5,headerHeight-25);
		rect2.setFill(Color.DARKGREY);
		
		Rectangle rect3 = new Rectangle(25,headerHeight-5,length-45,5);
		rect3.setFill(Color.WHITE);
		
		Rectangle rect4 = new Rectangle(length-25,20,5,headerHeight-25);
		rect4.setFill(Color.WHITE);
		
		Polygon polygon1 = new Polygon();
	    polygon1.getPoints().addAll(new Double[]{
	    		(double) 20.0, (double)headerHeight,
	           25.0, (double)headerHeight,
	           25.0,(double) headerHeight-5});
	    polygon1.setFill(Color.WHITE);
	    
	    Polygon polygon2 = new Polygon();
	    polygon2.getPoints().addAll(new Double[]{
	    		(double)length-25.0, 25.0,
	    		(double)length-20.0, 25.0,
	    		(double)length-20.0,20.0
	    		});
	    polygon2.setFill(Color.WHITE);
		
		this.header.getChildren().addAll(rect2,rect3,rect4,polygon1,rect1,polygon2);
	}
	public void drawEdgeCenter() {
		Rectangle rect1 = new Rectangle(20,20,length-40,5);
		rect1.setFill(Color.DARKGREY);

		Rectangle rect2 = new Rectangle(20,25,5,height-45);
		rect2.setFill(Color.DARKGREY);
		
		Rectangle rect3 = new Rectangle(25,height-25,length-50,5);
		rect3.setFill(Color.WHITE);
		
		Rectangle rect4 = new Rectangle(length-25,20,5,height-40);
		rect4.setFill(Color.WHITE);
		
		
		Polygon polygon1 = new Polygon();
	    polygon1.getPoints().addAll(new Double[]{
	    		(double) 20.0, (double)height-20,
	           25.0, (double) height-20,
	           25.0,(double) height-25});
	    polygon1.setFill(Color.WHITE);
	    
	    Polygon polygon2 = new Polygon();
	    polygon2.getPoints().addAll(new Double[]{
	    		(double)length-25.0, 25.0,
	    		(double)length-20.0, 25.0,
	    		(double)length-20.0,20.0
	    		});
	    polygon2.setFill(Color.WHITE);
		this.root.getChildren().addAll(rect2,rect3,rect4,polygon1,rect1,polygon2);
		
	}

	public void update(Tile[][] tilearr, boolean gameOver) {
		if (!gameIsOver) {
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
								audio.bombHit();
							} else {
								drawBomb(i, j);
							}
						} else if (tilearr[i][j].getNeighborBombs() != 0) {
							drawNumber(i, j, tilearr[i][j].getNeighborBombs());
						}
						this.gameIsOver=true;
					}
				}
			}
		}
	}

	private void drawNumber(int y, int x, int number) { // coordinates given in arraytiles
		if (number != 0) {
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
			text.setX(x * tilesize + tilesize / 2 + borderThickNess);
			text.setY(y * tilesize + tilesize / 2 + borderThickNess);
			text.setFont(Font.font((24.1 / 30) * tilesize));
			text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
			text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
			this.root.getChildren().add(text);
		}

	}

	private void drawPressedButton(int y, int x) {
		drawImage(y, x, PressedButtonimage);
		Rectangle rect = new Rectangle(x * tilesize+borderThickNess, y * tilesize+borderThickNess, tilesize, tilesize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.root.getChildren().add(rect);
	}

	private void drawImage(int y, int x, Image im) {
		ImageView image = new ImageView(im);
		image.setX(x * tilesize+borderThickNess);
		image.setY(y * tilesize+borderThickNess);
		image.setFitHeight(tilesize);
		image.setFitWidth(tilesize);
		this.root.getChildren().add(image);
	}

	private void drawPressedBomb(int y, int x) {
		drawImage(y, x, PressedBombimage);
	}

	private void drawBomb(int y, int x) {// coordinates given in arraytiles
		drawImage(y, x, Bombimage);
	}

	private void drawButton(int y, int x) {
		drawImage(y, x, Buttonimage);
	}

	private void drawFlag(int y, int x) {
		drawImage(y, x, Flagimage);
	}

	// Getters and setters methods

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