package src.gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.util.Duration;
import src.controller.Controller;
import src.gui.View.Clock;
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
import javafx.scene.text.TextAlignment;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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
	private Model model;
	private Controller Controller;
	private boolean gameIsOver = false;
	private BorderPane bpane;
	private int headerHeight;
	private int borderThickNess = 25;
	private Image SmileyImage;
	private int amountBombsLeft;
	private Font DigitalFont;

	private Clock timer;
	private Image HappySmileyImage;
	private Image WinSmileyImage;
	private Image DeadSmileyImage;
	private Image TenseSmileyImage;
	private String lastSmileyString;
	private Scene wholeScene;

	public static void main(String[] args) throws FileNotFoundException {
		// setParameters(args);
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			//Must be put in minesweepermedia
			this.Bombimage = new Image(new FileInputStream("Pictures/Bomb.png"));
			this.Buttonimage = new Image(new FileInputStream("Pictures/Button.png"));
			this.Flagimage = new Image(new FileInputStream("Pictures/Flag.png"));
			this.PressedButtonimage = new Image(new FileInputStream("Pictures/PressedButton.png"));
			this.PressedBombimage = new Image(new FileInputStream("Pictures/PressedBomb.png"));
			this.HappySmileyImage = new Image(new FileInputStream("Pictures/HappySmiley.png"));
			this.WinSmileyImage = new Image(new FileInputStream("Pictures/WinSmiley.png"));
			this.DeadSmileyImage = new Image(new FileInputStream("Pictures/DeadSmiley.png"));
			this.TenseSmileyImage = new Image(new FileInputStream("Pictures/TenseSmiley.png"));
			this.DigitalFont = Font.loadFont("file:Fonts/Digital.ttf", 50);
			
			
			// initializing soundmedia
			audio = new MineSweeperAudio();
			audio.playSoundTrack();
			audio.stopSoundTrack();// must be made to button later
			
			//initializing map: must be put in media later
			this.amountTilesLength = 16;
			this.amountTilesHeight = 16;
			this.tilesize = 30;
			this.amountBombs = 8;
			this.headerHeight = 75;
			this.height = tilesize * amountTilesHeight + borderThickNess * 2;
			this.length = tilesize * amountTilesLength + borderThickNess * 2;
			
			
			//initializing borderpane and thereby the visual layout
			this.bpane = new BorderPane();
			this.root = new Group();
			drawEdgeCenter();
			this.header = new Group();
			drawEdgeHeader();
			
			//initializing headerPane with each element
			updateTimeLeft(0);
			this.timer = new Clock();
			smileyFaceSetter("HappySmiley");

			//putting the two groups together in borderpane and setting scene
			this.bpane.setTop(header);
			this.bpane.setCenter(root);
			this.wholeScene = new Scene(bpane);
			
			//initializing model and controllerclass
			this.model = new Model(this, amountTilesHeight, amountTilesLength, amountBombs);
			this.controller = new Controller(model, this, tilesize, headerHeight, borderThickNess, length, height);

			//setting mouseactionhandler for entire scene
			this.wholeScene.setOnMousePressed(controller.getEventHandler());
			this.wholeScene.setOnMouseReleased(controller.getEventHandler());

		
			//initializing window and displaying scene
			primaryStage.setTitle("Minesweeper");
			primaryStage.getIcons().add(Bombimage);
			primaryStage.setScene(wholeScene);
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void reset() { //resets view class so its ready for new game
		this.timer.reset();
		updateBombsLeft(amountBombs);
		this.gameIsOver = false;
		smileyFaceSetter("HappySmiley");
		updateTimeLeft(0);
	}
	
	//Draws smileyfacebox. ////Determine wether it should be part of updateheader;
	public void smileyFaceSetter(String s) {
		
		//to remember last smiley when header updates because of counter
		this.lastSmileyString = s; 
		
		//finds the picture of necessary smiley
		if (s.equals("HappySmiley")) { 
			this.SmileyImage = HappySmileyImage;
		} else if (s.equals("DeadSmiley")) {
			this.SmileyImage = DeadSmileyImage;
		} else if (s.equals("TenseSmiley")) {
			this.SmileyImage = TenseSmileyImage;
		} else if (s.equals("WinSmiley")) {
			this.SmileyImage = WinSmileyImage;
		}

		//draws smileybutton if its pressed
		if (s.equals("Pressed")) {
			Rectangle rect = new Rectangle((double) length / 2 - 20, (double) headerHeight / 2 - 10, 40, 40);
			rect.setArcWidth(1);
			rect.setArcHeight(1);
			rect.setFill(Color.DARKGREY);
			rect.setStroke(Color.GREY);
			this.header.getChildren().add(rect);

			ImageView image = new ImageView(SmileyImage);
			image.setX((double) length / 2 - 14);
			image.setY((double) headerHeight / 2 - 4);
			image.setFitHeight(30);
			image.setFitWidth(30);
			this.header.getChildren().add(image);
		
		
		} else { //draws smileybutton if its not pressed
			ImageView button = new ImageView(this.Buttonimage);
			button.setX((double) length / 2 - 20);
			button.setY((double) headerHeight / 2 - 10);
			button.setFitHeight(40);
			button.setFitWidth(40);
			this.header.getChildren().add(button);

			ImageView image = new ImageView(SmileyImage);
			image.setX((double) length / 2 - 15);
			image.setY((double) headerHeight / 2 - 5);
			image.setFitHeight(30);
			image.setFitWidth(30);
			this.header.getChildren().add(image);
		}

	}

	//clears and redraws header, boolean updateBombsLeft is so its clear
	//wether its bombcounter or timer that needs to be updated 
	public void updateHeader(int count, boolean updateBombsleft) { 
		this.header.getChildren().clear();
		drawEdgeHeader();
		smileyFaceSetter(lastSmileyString);
		if (updateBombsleft) {
			updateTimeLeft(timer.getTimer());
			updateBombsLeft(count);
		} else {
			updateTimeLeft(count);
			updateBombsLeft(amountBombsLeft);
		}
	}

	//Draws timeleft box //// can be more efficient
	public void updateTimeLeft(int count) {
		int copyCounts = count;
		int ones = copyCounts % 10;
		copyCounts -= ones;
		int tenths = copyCounts % 100 / 10;
		copyCounts -= tenths;
		int hundreds = copyCounts % 1000 / 100;
		copyCounts -= hundreds;

		Rectangle backGround = new Rectangle(30, 29, 70, 36);
		backGround.setFill(Color.BLACK);
		this.header.getChildren().add(backGround);

		Text amountBombBackground = new Text();
		amountBombBackground.setFont(DigitalFont);
		amountBombBackground.setX(30);
		amountBombBackground.setY(headerHeight - 12);
		amountBombBackground.setFill(Color.RED);
		amountBombBackground.setText("888");
		amountBombBackground.setOpacity(0.2);
		this.header.getChildren().add(amountBombBackground);

		int oneOnes = 0;
		if (ones == 1) {
			oneOnes = 16;
		}
		Text amountBombsOnes = new Text();
		amountBombsOnes.setFont(DigitalFont);
		amountBombsOnes.setFill(Color.RED);
		amountBombsOnes.setText(ones + "");
		amountBombsOnes.setY(headerHeight - 12);
		amountBombsOnes.setX(76 + oneOnes);
		this.header.getChildren().add(amountBombsOnes);

		int oneTenths = 0;
		if (tenths == 1) {
			oneTenths = 16;
		}
		Text amountBombsTenths = new Text();
		amountBombsTenths.setFont(DigitalFont);
		amountBombsTenths.setFill(Color.RED);
		amountBombsTenths.setText(tenths + "");
		amountBombsTenths.setY(headerHeight - 12);
		amountBombsTenths.setX(53 + oneTenths);
		this.header.getChildren().add(amountBombsTenths);

		int oneHundreds = 0;
		if (hundreds == 1) {
			oneHundreds = 16;
		}
		Text amountBombsHundreds = new Text();
		amountBombsHundreds.setFont(DigitalFont);
		amountBombsHundreds.setFill(Color.RED);
		amountBombsHundreds.setY(headerHeight - 12);
		amountBombsHundreds.setX(30 + oneHundreds);
		amountBombsHundreds.setText(hundreds + "");
		this.header.getChildren().add(amountBombsHundreds);
	}

	//Draws bombsleft box //// can be more efficient
	public void updateBombsLeft(int count) {
		this.amountBombsLeft=count;
		count = count < 0 ? 0 : count;

		int copyCounts = count;
		int ones = copyCounts % 10;
		copyCounts -= ones;
		int tenths = copyCounts % 100 / 10;
		copyCounts -= tenths;
		int hundreds = copyCounts % 1000 / 100;
		copyCounts -= hundreds;

		Rectangle backGround = new Rectangle(length - 99, 29, 70, 36);
		backGround.setFill(Color.BLACK);
		this.header.getChildren().add(backGround);

		Text amountBombBackground = new Text();
		amountBombBackground.setTextAlignment(TextAlignment.RIGHT);
		amountBombBackground.setFont(DigitalFont);
		amountBombBackground.setX(length - 100);
		amountBombBackground.setY(headerHeight - 12);
		amountBombBackground.setFill(Color.RED);
		amountBombBackground.setText("888");
		amountBombBackground.setOpacity(0.2);
		this.header.getChildren().add(amountBombBackground);

		int oneOnes = 0;
		if (ones == 1) {
			oneOnes = 16;
		}
		Text amountBombsOnes = new Text();
		amountBombsOnes.setTextAlignment(TextAlignment.RIGHT);
		amountBombsOnes.setFont(DigitalFont);
		amountBombsOnes.setFill(Color.RED);
		amountBombsOnes.setText(ones + "");
		amountBombsOnes.setY(headerHeight - 12);
		amountBombsOnes.setX(length - 53 + oneOnes);
		this.header.getChildren().add(amountBombsOnes);

		int oneTenths = 0;
		if (tenths == 1) {
			oneTenths = 16;
		}
		Text amountBombsTenths = new Text();
		amountBombsTenths.setTextAlignment(TextAlignment.RIGHT);
		amountBombsTenths.setFont(DigitalFont);
		amountBombsTenths.setFill(Color.RED);
		amountBombsTenths.setText(tenths + "");
		amountBombsTenths.setY(headerHeight - 12);
		amountBombsTenths.setX(length - 77 + oneTenths);
		this.header.getChildren().add(amountBombsTenths);

		int oneHundreds = 0;
		if (hundreds == 1) {
			oneHundreds = 16;
		}
		Text amountBombsHundreds = new Text();
		amountBombsHundreds.setTextAlignment(TextAlignment.RIGHT);
		amountBombsHundreds.setFont(DigitalFont);
		amountBombsHundreds.setFill(Color.RED);
		amountBombsHundreds.setY(headerHeight - 12);
		amountBombsHundreds.setX(length - 99 + oneHundreds);
		amountBombsHundreds.setText(hundreds + "");
		this.header.getChildren().add(amountBombsHundreds);

	}

	//Draws all noninteractive elements of the header
	public void drawEdgeHeader() {
		Rectangle headerBackGround = new Rectangle(0, 0, length, headerHeight + 3);
		headerBackGround.setFill(Color.LIGHTGREY);

		Rectangle rect1 = new Rectangle(20, 20, length - 40, 5);
		rect1.setFill(Color.DARKGREY);

		Rectangle rect2 = new Rectangle(20, 25, 5, headerHeight - 25);
		rect2.setFill(Color.DARKGREY);

		Rectangle rect3 = new Rectangle(25, headerHeight - 5, length - 45, 5);
		rect3.setFill(Color.WHITE);

		Rectangle rect4 = new Rectangle(length - 25, 20, 5, headerHeight - 25);
		rect4.setFill(Color.WHITE);

		Polygon polygon1 = new Polygon();
		polygon1.getPoints().addAll(new Double[] { (double) 20.0, (double) headerHeight, 25.0, (double) headerHeight,
				25.0, (double) headerHeight - 5 });
		polygon1.setFill(Color.WHITE);

		Polygon polygon2 = new Polygon();
		polygon2.getPoints().addAll(new Double[] { (double) length - 25.0, 25.0, (double) length - 20.0, 25.0,
				(double) length - 20.0, 20.0 });
		polygon2.setFill(Color.WHITE);

		this.header.getChildren().addAll(headerBackGround, rect2, rect3, rect4, polygon1, rect1, polygon2);
	}

	//Draws all noninteractive elements of the center
	public void drawEdgeCenter() {
		
		Rectangle centerBackGround = new Rectangle(0, 0, length, height);
		centerBackGround.setFill(Color.LIGHTGREY);
		this.root.getChildren().add(centerBackGround);

		Rectangle rect1 = new Rectangle(20, 20, length - 40, 5);
		rect1.setFill(Color.DARKGREY);

		Rectangle rect2 = new Rectangle(20, 25, 5, height - 45);
		rect2.setFill(Color.DARKGREY);

		Rectangle rect3 = new Rectangle(25, height - 25, length - 50, 5);
		rect3.setFill(Color.WHITE);

		Rectangle rect4 = new Rectangle(length - 25, 20, 5, height - 40);
		rect4.setFill(Color.WHITE);

		Polygon polygon1 = new Polygon();
		polygon1.getPoints().addAll(new Double[] { (double) 20.0, (double) height - 20, 25.0, (double) height - 20,
				25.0, (double) height - 25 });
		polygon1.setFill(Color.WHITE);

		Polygon polygon2 = new Polygon();
		polygon2.getPoints().addAll(new Double[] { (double) length - 25.0, 25.0, (double) length - 20.0, 25.0,
				(double) length - 20.0, 20.0 });
		polygon2.setFill(Color.WHITE);
		this.root.getChildren().addAll(rect2, rect3, rect4, polygon1, rect1, polygon2);
	}

	//
	public void updateFlagOrPressedTile(Tile[][] tilearr, int y, int x) {
		if (!tilearr[y][x].isCleared()) {
			drawButton(y, x);
			if (tilearr[y][x].hasFlag()) {
				drawFlag(y, x);
			} else if (tilearr[y][x].isPressedButton()) {
				drawPressedButton(y, x);
			}
		}
	}

	public void update(Tile[][] tilearr, boolean gameFinished, boolean gameWon, boolean gameLost) {
		if (!gameIsOver) {
			this.root.getChildren().clear();
			drawEdgeCenter();
	
			if (!gameFinished) {
				for (int i = 0; i < amountTilesHeight; i++) {
					for (int j = 0; j < amountTilesLength; j++) {
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
					}	
				}
			}else{
				for (int i=0; i<amountTilesHeight; i++) {
					for (int j=0; j<amountTilesLength; j++) {
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
						this.gameIsOver = true;
					}
				}
			}
			if (gameLost) {
				smileyFaceSetter("DeadSmiley");
				audio.gameLost();
			}
			if (gameWon) {
				smileyFaceSetter("WinSmiley");
				audio.gameWon();
			}
		}

	}

	private void drawNumber(int y, int x, int number) { // coordinates given in arraytiles
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
		Rectangle rect = new Rectangle(x * tilesize + borderThickNess, y * tilesize + borderThickNess, tilesize,
				tilesize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.root.getChildren().add(rect);
	}

	private void drawImage(int y, int x, Image im) {
		ImageView image = new ImageView(im);
		image.setX(x * tilesize + borderThickNess);
		image.setY(y * tilesize + borderThickNess);
		image.setFitHeight(tilesize);
		image.setFitWidth(tilesize);
		this.root.getChildren().add(image);
	}

	
	public class Clock extends Pane {
		private Timeline animation;
		private int tap = 0;
		

		Text txt = new Text();

		private Clock() {
			animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> timelabel()));
			animation.setCycleCount(Timeline.INDEFINITE);
		}

		public int getTimer() {
			return tap;
		}

		private void timelabel() {
			if (gameIsOver) {
				animation.pause();
			}
			if (tap < 1000) {
				tap++;
				updateHeader(tap, false);
			}
		}

		private void pause() {
			animation.pause();
		}

		private void play() {
			animation.play();
		}

		private void reset() {
			animation.pause();
			this.tap = 0;
		}
	}

	//short methods
	public void firstMove() {
		this.timer.play();
	}
	
	public void mousePressSound() {
		audio.mousePressed();
	}

	public void mouseReleaseSound() {
		audio.mouseReleased();
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