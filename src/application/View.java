package application;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.stage.FileChooser;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import javax.print.DocFlavor.URL;

import application.View.Clock;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;

public class View extends Application {
	private static final int tileSize = 40;
	private static final int headerHeight = 75;
	private static final int borderThickness = 25; 
	private static MineSweeperMedia media;
	private static int amountTilesHeight;
	private static int amountTilesLength;
	private static int amountBombs;
	private static int height;
	private static int length;
	private static Controller controller;
	private static Group center;
	private static Group header;
	private static String lastSmileyString;
	private static Scene wholeScene;
	private static Model model;
	private static boolean gameOver = false;
	private static BorderPane borderPane;
	private static int amountBombsLeft;
	private static Image smileyImage;
	private static Clock timer;
	private static Stage primaryStage;
	private static Scene menuScene;
	private static boolean firstRound = true;
	private static boolean settingsButtonPressed = false;
	
	@FXML
	private Pane fileNotificationPane;
	@FXML
	private Text fileNotificationText;
	@FXML
	private static Pane easyPane;
	@FXML
	private static Pane mediumPane;
	@FXML
	private static Pane hardPane;
	@FXML
	private Text soundOnOffText;
	@FXML 
	private Text musicOnOffText;

	public static void main(String[] args) throws FileNotFoundException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws IOException {
		try {
			
			this.primaryStage = primaryStage;

			// initializing soundmedia
			if (firstRound) {
				this.media = new MineSweeperMedia();
				this.media.playSoundTrack();
				this.firstRound = false;
			}

			// initializing map
			if (this.amountTilesLength == 0 || this.amountTilesHeight == 0 || this.amountBombs == 0) {
				// initializing to easy difficulty
				this.amountTilesLength = 8;
				this.amountTilesHeight = 8;
				this.amountBombs = 10;
			}
			this.height = tileSize * amountTilesHeight + borderThickness * 2;
			this.length = tileSize * amountTilesLength + borderThickness * 2;

			// initializing borderpane and thereby the visual layout
			this.borderPane = new BorderPane();
			this.center = new Group();
			drawEdgeCenter();
			this.header = new Group();
			drawEdgeHeader();

			// initializing headerPane with each element
			updateBombsLeftOrTimer(0, false);
			this.timer = new Clock();
			smileyFaceSetter("HappySmiley");
			drawSettingsButton(false);

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

			//loading menuscene
			Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
			this.menuScene = new Scene(root);

			// initializing window and displaying scene
			this.primaryStage.setTitle("Minesweeper");
			this.primaryStage.getIcons().add(media.getBombImage());

			// this.primaryStage.setScene(menu);
			this.primaryStage.setScene(wholeScene);
			this.primaryStage.setResizable(false);
			this.primaryStage.show();
			this.primaryStage.centerOnScreen();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void drawSettingsButton(boolean isPressed) {
		//boolean saved to private field, as header must be redrawn for each update in the header
		this.settingsButtonPressed = isPressed;
		
		ImageView image = new ImageView(media.getSettingsImage());
		if (!isPressed) {
			ImageView button = new ImageView(media.getButtonImage());
			button.setX((double) length / 4 + 20);
			button.setY((double) headerHeight / 2 - 10);
			button.setFitHeight(40);
			button.setFitWidth(40);
			this.header.getChildren().add(button);

			image.setFitHeight(25);
			image.setFitWidth(25);
			image.setX(length / 4 + 27.5);
			image.setY(headerHeight / 2 - 2.5);
			this.header.getChildren().add(image);
			
		} else { //if its not pressed
			Rectangle rect = new Rectangle((double) length / 4 + 20, (double) headerHeight / 2 - 10, 40, 40);
			rect.setArcWidth(1);
			rect.setArcHeight(1);
			rect.setFill(Color.DARKGREY);
			rect.setStroke(Color.GREY);
			this.header.getChildren().add(rect);

			image.setX(length / 4 + 30);
			image.setY(headerHeight / 2);
			image.setFitHeight(25);
			image.setFitWidth(25);
			this.header.getChildren().add(image);	
		}
	}

	// Loads a saved file into the board array. 
	public void loadGameFromFile() throws InvocationTargetException, InterruptedException {
		FileChooser fileDlg = new FileChooser();
		fileDlg.setInitialDirectory(new File("Saved games"));
		fileDlg.setTitle("Choose saved Minesweeper game");
		
		File selectedFile = fileDlg.showOpenDialog(null);
		this.gameOver = false;
		try {
			setFileNotification("File Loaded succesfully");
			model.loadGameFromFile(selectedFile.getAbsolutePath());
		} catch (Exception fileNotFoundException) {
			setFileNotification("File not loaded succesfully");
			
			//Resets the game to easy
			this.amountTilesLength = 8;
			this.amountTilesHeight = 8;
			this.amountBombs = 10;
			reset();
			try {
				start(primaryStage);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			
			
		}

	}
	

	//Prepares view and controller class for, the previously loaded game from file
	public void setNewDimensions(Tile[][] board) {
		this.amountTilesLength = board[0].length;
		this.amountTilesHeight = board.length;
		this.height = this.tileSize * this.amountTilesHeight + this.borderThickness * 2;
		this.length = this.tileSize * this.amountTilesLength + this.borderThickness * 2;
		drawEdgeCenter();
		drawEdgeHeader();
		controller.setNewDimensions(height, length);
		updateBombsLeftOrTimer(0, false);
		timer.play();
		smileyFaceSetter("HappySmiley");
		drawSettingsButton(false);
		media.stopSoundTrack();
		media.restartSoundTrack(); //Skal sættes til restartSoundTrack	
	}
	
	
	public void muteUnMuteButton() {
		if (soundOnOffText.getText().equals("Sound Off")) {
			soundOnOffText.setText("Sound On");
		} else {
			soundOnOffText.setText("Sound Off");
		}
		media.muteUnmute();
	}

	public void musicOnOffButton() {
		if (musicOnOffText.getText().equals("Music Off")) {
			musicOnOffText.setText("Music On");
		} else {
			musicOnOffText.setText("Music Off");
		}
		media.musicOnOff();
	}

	
	public void setDifficultyButton(MouseEvent evt) {
		Pane pane = (Pane) evt.getSource();
		String diff = pane.getId().toString();
		if (diff.equals("easyPane")) { // easy difficulty
			this.amountTilesLength = 8;
			this.amountTilesHeight = 8;
			this.amountBombs = 10;
		} else if (diff.equals("mediumPane")) { // medium difficulty
			this.amountTilesLength = 15;
			this.amountTilesHeight = 13;
			this.amountBombs = 40;
		} else { // hard difficulty
			this.amountTilesLength = 30;
			this.amountTilesHeight = 16;
			this.amountBombs = 99;
		}

		// Initiate new game with set difficulty
		reset();
		try {
			start(primaryStage);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Draws smileyfaceboButton
	public void smileyFaceSetter(String s) {
		// to remember last smiley entered when header updates and must redraw this button
		this.lastSmileyString = s;
		
		// finds the picture of necessary smiley
		if (!gameOver) {
			if (s.equals("HappySmiley")) {
				this.smileyImage = media.getHappySmileyImage();
			} else if (s.equals("TenseSmiley")) {
				this.smileyImage = media.getTenseSmileyImage();
			} else if (s.equals("DeadSmiley")) {
				this.smileyImage = media.getDeadSmileyImage();
			} else if (s.equals("WinSmiley")) {
				this.smileyImage = media.getWinSmileyImage();
			}
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
	// true if bombcounter and false if timer needs to be updated
	public void updateHeader(int count, boolean updateBombsleft) {
		this.header.getChildren().clear();
		drawEdgeHeader();
		drawSettingsButton(settingsButtonPressed);
		smileyFaceSetter(lastSmileyString);
		if (updateBombsleft) {
			updateBombsLeftOrTimer(timer.getTimer(), false);
			updateBombsLeftOrTimer(count, true);
		} else {
			updateBombsLeftOrTimer(count, false);
			updateBombsLeftOrTimer(amountBombsLeft, true);
		}
	}

	// Draws bombsleft box or Draws timeleft box, 
	// true if bombcounter and false if timer needs to be updated
	public void updateBombsLeftOrTimer(int count, boolean updateBombs) {
		// Draws background of each box
		Rectangle background;
		if (updateBombs) {
			this.amountBombsLeft = count;

			background = new Rectangle(length - 99, 29, 70, 36);
		} else { //update timer
			background = new Rectangle(30, 29, 70, 36);
		}
		background.setFill(Color.BLACK);
		this.header.getChildren().add(background);

		// making sure number is between 0 and 999
		count = (count < 0 || count > 999) ? 0 : count;

		// Draws the low opacity 8's of each box to get that digital look ////amountbackground.
		Text amountBombBackground = new Text();
		amountBombBackground.setFont(media.getDigitalFont());
		amountBombBackground.setY(headerHeight - 12);
		amountBombBackground.setFill(Color.RED);
		amountBombBackground.setText("888");
		amountBombBackground.setOpacity(0.2);
		if (updateBombs) {
			amountBombBackground.setX(length - 100);
		} else { //update timer
			amountBombBackground.setX(30);
		}
		this.header.getChildren().add(amountBombBackground);

		// Draws either the amount of bombsleft, or the count of the timer
		int copyCounts = count;
		for (int i = 0; i < 3; i++) {
			//Isolates one of the three digits, from last digit to first depending on i value.
			int digit = (int) ((copyCounts % (Math.pow(10, i + 1))) / Math.pow(10, i));
			copyCounts -= digit;

			//if digit is 1, then shift x value of where the number must be placed by 16.
			int digitIsOne = 0;
			if (digit == 1) {
				digitIsOne = 16;
			}
			
			//draws and adds the number to the box
			Text digitDisplayer = new Text();
			digitDisplayer.setFont(media.getDigitalFont());
			digitDisplayer.setFill(Color.RED);
			digitDisplayer.setText(digit + "");
			digitDisplayer.setY(headerHeight - 12);
			if (updateBombs) {
				digitDisplayer.setX(length - 53 - i * 23 + digitIsOne);
			} else { //update timer
				digitDisplayer.setX(76 - i * 23 + digitIsOne);
			}
			this.header.getChildren().add(digitDisplayer);
		}
	}

	// Draws all noninteractive elements of the header
	public void drawEdgeHeader() {
		// The background of the header
		Rectangle headerBackground = new Rectangle(0, 0, length, headerHeight + 3);
		headerBackground.setFill(Color.LIGHTGREY);

		// the line on the left side
		Rectangle rect1 = new Rectangle(20, 20, length - 40, 5);
		rect1.setFill(Color.DARKGREY);

		// the line on the top
		Rectangle rect2 = new Rectangle(20, 25, 5, headerHeight - 25);
		rect2.setFill(Color.DARKGREY);

		// the line on the bottom
		Rectangle rect3 = new Rectangle(25, headerHeight - 5, length - 45, 5);
		rect3.setFill(Color.WHITE);

		// the line on the left
		Rectangle rect4 = new Rectangle(length - 25, 20, 5, headerHeight - 25);
		rect4.setFill(Color.WHITE);

		// the transition from line on left to line on bottom
		Polygon polygon1 = new Polygon();
		polygon1.getPoints().addAll(new Double[] { (double) 20.0, (double) headerHeight, 25.0, (double) headerHeight,
				25.0, (double) headerHeight - 5 });
		polygon1.setFill(Color.WHITE);

		// the transition from line on right to line on top
		Polygon polygon2 = new Polygon();
		polygon2.getPoints().addAll(new Double[] { (double) length - 25.0, 25.0, (double) length - 20.0, 25.0,
				(double) length - 20.0, 20.0 });
		polygon2.setFill(Color.WHITE);

		this.header.getChildren().addAll(headerBackground, rect2, rect3, rect4, polygon1, rect1, polygon2);

	}

	// Draws all noninteractive elements of the center
	public void drawEdgeCenter() {
		// The background of the center
		Rectangle centerBackground = new Rectangle(0, 0, length, height);
		centerBackground.setFill(Color.LIGHTGREY);

		// the line on the left side
		Rectangle rect1 = new Rectangle(20, 20, length - 40, 5);
		rect1.setFill(Color.DARKGREY);

		// the line on the top
		Rectangle rect2 = new Rectangle(20, 25, 5, height - 45);
		rect2.setFill(Color.DARKGREY);

		// the line on the bottom
		Rectangle rect3 = new Rectangle(25, height - 25, length - 50, 5);
		rect3.setFill(Color.WHITE);

		// the line on the left
		Rectangle rect4 = new Rectangle(length - 25, 20, 5, height - 40);
		rect4.setFill(Color.WHITE);

		// the transition from line on left to line on bottom
		Polygon polygon1 = new Polygon();
		polygon1.getPoints().addAll(new Double[] { (double) 20.0, (double) height - 20, 25.0, (double) height - 20,
				25.0, (double) height - 25 });
		polygon1.setFill(Color.WHITE);

		// the transition from line on right to line on top
		Polygon polygon2 = new Polygon();
		polygon2.getPoints().addAll(new Double[] { (double) length - 25.0, 25.0, (double) length - 20.0, 25.0,
				(double) length - 20.0, 20.0 });
		polygon2.setFill(Color.WHITE);

		this.center.getChildren().addAll(centerBackground, rect2, rect3, rect4, polygon1, rect1, polygon2);
	}

	//draws the new board. 
	public void update(Tile[][] tilearr, boolean gameFinished, boolean gameWon, boolean gameLost) {
		if (!gameOver) {
			// clears center and draws edges of center
			this.center.getChildren().clear();
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
			} else { // draws game if it is finished
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
				// Draws smiley and calls method for the sound for whether game was lost or won
				if (gameLost) {
					smileyFaceSetter("DeadSmiley");
					media.gameLost();
				} else { // then game must be won
					smileyFaceSetter("WinSmiley");
					media.gameWon();
				}
				this.gameOver = true;
				this.timer.pause();
			}
		}
	}

	private void drawNumber(int y, int x, int number) {
		if (number != 0) {
			// sets text colour
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

			// draws text on the board
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
		drawImage(y, x, media.getPressedButtonImage());
		
		// draws border of the pressed button
		Rectangle rect = new Rectangle(x * tileSize + borderThickness, y * tileSize + borderThickness, tileSize,
				tileSize);
		rect.setArcWidth(1);
		rect.setArcHeight(1);
		rect.setFill(Color.TRANSPARENT);
		rect.setStroke(Color.GREY);
		this.center.getChildren().add(rect);
	}

	// draws an image on a given tile.
	private void drawImage(int y, int x, Image im) {
		ImageView image = new ImageView(im);
		image.setX(x * tileSize + borderThickness);
		image.setY(y * tileSize + borderThickness);
		image.setFitHeight(tileSize);
		image.setFitWidth(tileSize);
		this.center.getChildren().add(image);
	}

	// class that counts time, for the timerbox
	public class Clock extends Pane {
		private Timeline animation;
		// time went in seconds
		private int time = 0;

		private Clock() {
			// initiates timer and how often it should update
			animation = new Timeline(new KeyFrame(Duration.seconds(1), e -> timeLabel()));
			animation.setCycleCount(Timeline.INDEFINITE);
		}

		public void setTimer(int time) {
			this.time = time;
		}

		public int getTimer() {
			return time;
		}

		private void timeLabel() {//// bad naming
			// updates timer
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

	//determines what should be drawn on a specific noncleared tile
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

	// resets view class so its ready for new game
	public void reset() {
		timer.reset();
		updateBombsLeftOrTimer(amountBombs, true);
		gameOver = false;
		smileyFaceSetter("HappySmiley");
		updateBombsLeftOrTimer(0, false);
		media.stopSoundTrack();
		media.restartSoundTrack();
	}
	
	public void quitGame() {
		System.exit(0);
	}

	public void setMenuScene() {
		primaryStage.setScene(menuScene);
		primaryStage.show();
		this.primaryStage.centerOnScreen();
	}

	public void saveGameToFile() throws IOException {
		model.saveGameToFile();
		setFileNotification("File Saved succesfully");
	}

	public void setFileNotification(String s) {
		fileNotificationText.setText(s);
		fileNotificationPane.setOpacity(1);
	}

	public void menuNotificationGoAway() {
		fileNotificationPane.setOpacity(0);
	}

	public void setGameScene() {
		primaryStage.setScene(wholeScene);
		primaryStage.centerOnScreen();
	}

	//makes the animation for when you release button in menu
	public void oneOpacityPane(MouseEvent evt) {
		Pane pane = (Pane) evt.getSource();
		pane.setOpacity(1);
	}

	//makes the animation for when you press button in menu
	public void zeroOpacityPane(MouseEvent evt) {
		Pane pane = (Pane) evt.getSource();
		pane.setOpacity(0);
	}

	//sets the timer of the clock class
	public void setTimerFromModel(int time) {
		timer.setTimer(time);
	}

	public int getTime() {
		return timer.getTimer();
	}

	// starts timer when game starts
	public void firstMove() { //// bad naming
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
}