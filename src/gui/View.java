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
import javafx.scene.control.Button;
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
	private MineSweeperMedia media;
	private int amountTilesHeight;
	private int amountTilesLength;
	private int amountBombs;
	private int height;
	private int length;
	private int tileSize;
	private Controller controller;
	private Group center;
	private Group header;
	private String lastSmileyString;
	private Scene wholeScene;
	private Model model;
	private boolean gameIsOver = false;
	private BorderPane borderPane;
	private int headerHeight;
	private int borderThickness = 25;////almost scales properly
	private int amountBombsLeft;
	private Image smileyImage;
	private Clock timer;
	private Button btn;

	public static void Main(String[] args) throws FileNotFoundException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			//
			
			// initializing soundmedia
			media = new MineSweeperMedia();
			media.playSoundTrack();
			media.stopSoundTrack();//// must be made to button later

			// initializing map: must be put in media later
			this.amountTilesLength = 16;
			this.amountTilesHeight = 16;
			this.tileSize = 30;
			this.amountBombs = 10;
			this.headerHeight = 75;
			this.height = tileSize * amountTilesHeight + borderThickness * 2;
			this.length = tileSize * amountTilesLength + borderThickness * 2;

			// initializing borderpane and thereby the visual layout
			this.borderPane = new BorderPane();
			this.center = new Group();
			drawEdgeCenter();
			this.header = new Group();
			drawEdgeHeader();

			// initializing headerPane with each element
			updateBombsLeft(0,false);
			this.timer = new Clock();
			smileyFaceSetter("HappySmiley");

			// putting the two groups together in borderpane and setting scene
			this.borderPane.setTop(header);
			this.borderPane.setCenter(center);
			this.wholeScene = new Scene(borderPane);

			// initializing model and controllerclass
			this.model = new Model(this, amountTilesHeight, amountTilesLength, amountBombs);
			this.controller = new Controller(model, this, tileSize, headerHeight, borderThickness, length, height);

			// setting mouseactionhandler for entire scene
			this.wholeScene.setOnMousePressed(controller.getEventHandler());
			this.wholeScene.setOnMouseReleased(controller.getEventHandler());

			
			ImageView image = new ImageView(media.getBombImage());
			image.setFitHeight(40);
			image.setFitWidth(40);
			/*
			this.btn = new Button();
			this.btn.setLayoutX(length/4+20);
			this.btn.setLayoutY(headerHeight / 2 - 9);
			this.btn.setGraphic(image);
			this.btn.setMaxWidth(40);
			this.btn.setMaxHeight(40);
			this.btn.setMinWidth(40);
			this.btn.setMinHeight(40);
			this.btn.setFocusTraversable(false);
			this.header.getChildren().add(btn);
			*/
			// initializing window and displaying scene
			primaryStage.setTitle("Minesweeper");
			primaryStage.getIcons().add(media.getBombImage());
			primaryStage.setScene(wholeScene);
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	

	// resets view class so its ready for new game
	public void reset() { 
		this.timer.reset();
		updateBombsLeft(amountBombs,true);
		this.gameIsOver = false;
		smileyFaceSetter("HappySmiley");
		updateBombsLeft(0,false);
	}

	// Draws smileyfacebox. Wont be a part of header: its easier, and wont cause problems
	public void smileyFaceSetter(String s) {

		// to remember last smiley when header updates because of counter
		this.lastSmileyString = s;

		// finds the picture of necessary smiley
		if (s.equals("HappySmiley")) {
			this.smileyImage = media.getHappySmileyImage();
		} else if (s.equals("DeadSmiley")) {
			this.smileyImage = media.getDeadSmileyImage();
		} else if (s.equals("TenseSmiley")) {
			this.smileyImage = media.getTenseSmileyImage();
		} else if (s.equals("WinSmiley")) {
			this.smileyImage = media.getWinSmileyImage();
		}

		// draws smileybutton if its pressed
		if (s.equals("Pressed")) {
			Rectangle rect = new Rectangle((double) length / 2 - 20, (double) headerHeight / 2 - 10, 40, 40);
			rect.setArcWidth(1);
			rect.setArcHeight(1);
			rect.setFill(Color.DARKGREY);
			rect.setStroke(Color.GREY);
			this.header.getChildren().add(rect);

			ImageView image = new ImageView(smileyImage);
			image.setX((double) length / 2 - 14);
			image.setY((double) headerHeight / 2 - 4);
			image.setFitHeight(30);
			image.setFitWidth(30);
			this.header.getChildren().add(image);

		} else { // draws smileybutton if its not pressed
			ImageView button = new ImageView(media.getButtonImage());
			button.setX((double) length / 2 - 20);
			button.setY((double) headerHeight / 2 - 10);
			button.setFitHeight(40);
			button.setFitWidth(40);
			this.header.getChildren().add(button);

			ImageView image = new ImageView(smileyImage);
			image.setX((double) length / 2 - 15);
			image.setY((double) headerHeight / 2 - 5);
			image.setFitHeight(30);
			image.setFitWidth(30);
			this.header.getChildren().add(image);
		}

	}

	// clears and redraws header, boolean updateBombsLeft is so its clear
	// wether its bombcounter or timer that needs to be updated

	public void updateHeader(int count, boolean updateBombsleft) {
		this.header.getChildren().clear();
		drawEdgeHeader();
		smileyFaceSetter(lastSmileyString);
		if (updateBombsleft) {
			updateBombsLeft(timer.getTimer(),false);
			updateBombsLeft(count,true);
		} else {
			updateBombsLeft(count,false);
			updateBombsLeft(amountBombsLeft,true);
		}
		this.header.getChildren().add(btn);
	}

	// Draws bombsleft box or Draws timeleft box  //// can be named better
	public void updateBombsLeft(int count, boolean updateBombs) {
		
		//Draws background of each box
		Rectangle background;
		if (updateBombs) {
			this.amountBombsLeft = count;
			
			background = new Rectangle(length - 99, 29, 70, 36);
		} else {
			background = new Rectangle(30, 29, 70, 36);
		}
		background.setFill(Color.BLACK);
		this.header.getChildren().add(background);
		
		//making sure number is between 0 and 999
		count = (count < 0 || count>999) ? 0 : count;
		
		//Draws the low opacity 8's of each box to get that digital look
		Text amountBombBackground = new Text();
		amountBombBackground.setFont(media.getDigitalFont());
		amountBombBackground.setY(headerHeight - 12);
		amountBombBackground.setFill(Color.RED);
		amountBombBackground.setText("888");
		amountBombBackground.setOpacity(0.2);
		if (updateBombs) {
			amountBombBackground.setX(length - 100);
		} else {
			amountBombBackground.setX(30);
		}
		this.header.getChildren().add(amountBombBackground);

		//Draws either the amount of bombsleft, or the count of the timer
		int copyCounts = count;
		for (int i = 0; i < 3; i++) {
			int digit = (int) ((copyCounts % (Math.pow(10, i + 1))) / Math.pow(10, i));
			copyCounts -= digit;

			int digitIsOne = 0;
			if (digit == 1) {
				digitIsOne = 16;
			}
			Text digitDisplayer = new Text();
			digitDisplayer.setFont(media.getDigitalFont());
			digitDisplayer.setFill(Color.RED);
			digitDisplayer.setText(digit + "");
			digitDisplayer.setY(headerHeight - 12);
			if (updateBombs) {
				digitDisplayer.setX(length - 53 - i * 23 + digitIsOne);
			} else {
				digitDisplayer.setX(76 - i * 23 + digitIsOne);
			}
			this.header.getChildren().add(digitDisplayer);
		}
	}

	// Draws all noninteractive elements of the header
	public void drawEdgeHeader() {
		//The background of the header
		Rectangle headerBackground = new Rectangle(0, 0, length, headerHeight + 3);
		headerBackground.setFill(Color.LIGHTGREY);

		//the line on the left side
		Rectangle rect1 = new Rectangle(20, 20, length - 40, 5);
		rect1.setFill(Color.DARKGREY);

		//the line on the top
		Rectangle rect2 = new Rectangle(20, 25, 5, headerHeight - 25);
		rect2.setFill(Color.DARKGREY);

		//the line on the bottom
		Rectangle rect3 = new Rectangle(25, headerHeight - 5, length - 45, 5);
		rect3.setFill(Color.WHITE);

		//the line on the left
		Rectangle rect4 = new Rectangle(length - 25, 20, 5, headerHeight - 25);
		rect4.setFill(Color.WHITE);

		//the transition from line on left to line on bottom
		Polygon polygon1 = new Polygon();
		polygon1.getPoints().addAll(new Double[] { (double) 20.0, (double) headerHeight, 25.0, (double) headerHeight,
				25.0, (double) headerHeight - 5 });
		polygon1.setFill(Color.WHITE);

		//the transition from line on right to line on top
		Polygon polygon2 = new Polygon();
		polygon2.getPoints().addAll(new Double[] { (double) length - 25.0, 25.0, (double) length - 20.0, 25.0,
				(double) length - 20.0, 20.0 });
		polygon2.setFill(Color.WHITE);

		this.header.getChildren().addAll(headerBackground, rect2, rect3, rect4, polygon1, rect1, polygon2);
	
	}

	// Draws all noninteractive elements of the center
	public void drawEdgeCenter() {
		//The background of the center
		Rectangle centerBackground = new Rectangle(0, 0, length, height);
		centerBackground.setFill(Color.LIGHTGREY);

		//the line on the left side
		Rectangle rect1 = new Rectangle(20, 20, length - 40, 5);
		rect1.setFill(Color.DARKGREY);

		//the line on the top
		Rectangle rect2 = new Rectangle(20, 25, 5, height - 45);
		rect2.setFill(Color.DARKGREY);

		//the line on the bottom
		Rectangle rect3 = new Rectangle(25, height - 25, length - 50, 5);
		rect3.setFill(Color.WHITE);

		//the line on the left
		Rectangle rect4 = new Rectangle(length - 25, 20, 5, height - 40);
		rect4.setFill(Color.WHITE);

		//the transition from line on left to line on bottom
		Polygon polygon1 = new Polygon();
		polygon1.getPoints().addAll(new Double[] { (double) 20.0, (double) height - 20, 25.0, (double) height - 20,
				25.0, (double) height - 25 });
		polygon1.setFill(Color.WHITE);

		//the transition from line on right to line on top
		Polygon polygon2 = new Polygon();
		polygon2.getPoints().addAll(new Double[] { (double) length - 25.0, 25.0, (double) length - 20.0, 25.0,
				(double) length - 20.0, 20.0 });
		polygon2.setFill(Color.WHITE);
		
		this.center.getChildren().addAll(centerBackground,rect2, rect3, rect4, polygon1, rect1, polygon2);
	}

	//Middleman method, that determines what should be drawn on a noncleared tile.
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
			//clears center and draws edges of center
			this.center.getChildren().clear();
			drawEdgeCenter();

			if (!gameFinished) {
				//draws game if its not finished
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
			} else {	
				//draws game if it is finished
				for (int i = 0; i < amountTilesHeight; i++) {
					for (int j = 0; j < amountTilesLength; j++) {
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
				//says to appropriate methods that no more moves must be done, and stops timer
				this.gameIsOver = true;
				this.timer.pause();
				//Draws smiley and calls mathod for the sound for wether game was lost or won
				if (gameLost) {
					smileyFaceSetter("DeadSmiley");
					media.gameLost();
				} else { //then game must be won
					smileyFaceSetter("WinSmiley");
					media.gameWon();
				}
			}
		}
	}

	//draws number on a given til in the array
	private void drawNumber(int y, int x, int number) {
		if (number != 0) {
			//sets text colour
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
			
			//draws text on the board
			text.setText(Integer.toString(number));
			text.setX(x * tileSize + tileSize / 2 + borderThickness);
			text.setY(y * tileSize + tileSize / 2 + borderThickness);
			text.setFont(Font.font((24.1 / 30) * tileSize));
			text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
			text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
			this.center.getChildren().add(text);
		}

	}

	private void drawPressedButton(int y, int x) {
		//draws the pressedbutton by call of image
		drawImage(y, x, media.getPressedButtonImage());
		//draws border of the pressed button
		Rectangle rect = new Rectangle(x * tileSize + borderThickness, y * tileSize + borderThickness, tileSize,
				tileSize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.center.getChildren().add(rect);
	}
	
	//draws an image on a given tile.
	private void drawImage(int y, int x, Image im) {
		ImageView image = new ImageView(im);
		image.setX(x * tileSize + borderThickness);
		image.setY(y * tileSize + borderThickness);
		image.setFitHeight(tileSize);
		image.setFitWidth(tileSize);
		this.center.getChildren().add(image);
	}

	//class that counts time, for the timerbox
	public class Clock extends Pane {
		private Timeline animation;
		//time went in seconds
		private int time = 0;

		private Clock() {
			//initiates timer and how often it should update
			animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> timeLabel()));
			animation.setCycleCount(Timeline.INDEFINITE);
		}

		public int getTimer() {
			return time;
		}

		private void timeLabel() {//// bad naming
			//updates timer
			if (time < 1000) {
				time++;
				updateHeader(time, false);
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
			this.time = 0;
		}
	}

	
	// rest is short methods
	
	//starts timer when game starts
	public void firstMove() { ////bad naming
		this.timer.play();
	}

	public void mousePressSound() {
		media.mousePressed();
	}

	public void mouseReleaseSound() {
		media.mouseReleased();
	}

	private void drawPressedBomb(int y, int x) {
		drawImage(y, x, media.getPressedBombImage());
	}

	private void drawBomb(int y, int x) {
		drawImage(y, x, media.getBombImage());
	}

	private void drawButton(int y, int x) {
		drawImage(y, x, media.getButtonImage());
	}

	private void drawFlag(int y, int x) {
		drawImage(y, x, media.getFlagImage());
	}

	// Getters
	
	public int getAmountTilesHeight() {
		return amountTilesHeight;
	}
	public int getAmountTilesLength() {
		return amountTilesLength;
	}
	public int getTilesize() {
		return tileSize;
	}
	public int getHeight() {
		return height;
	}
	public int getLength() {
		return length;
	}
	public int getHeaderHeight() {
		return headerHeight;
	}
	public int getBorderThickness() {
		return borderThickness;
	}

	// Setters
	
	public void setAmountTilesHeight(int amountTilesHeight) {
		this.amountTilesHeight = amountTilesHeight;
	}
	public void setAmountTilesLength(int amountTilesLength) {
		this.amountTilesLength = amountTilesLength;
	}	
	public void setTilesize(int tileSize) {
		this.tileSize = tileSize;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public void setLength(int length) {
		this.length = length;
	}
	public void setHeaderHeight(int headerHeight) {
		this.headerHeight = headerHeight;
	}
	public void setBorderThickness(int borderThickness) {
		this.borderThickness = borderThickness;
	}
}