package src.gui;



import javafx.application.Application;
import javafx.stage.Stage;
import src.controller.Controller;
import src.model.Tile;

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


public class View extends Application {
	private GraphicsContext gc;

	private int amountTilesHeight;
	private int amountTilesLength;
	private int height;
	private int length;
	private int tilesize;
	private int pressedTileX;
	private int pressedTileY;
	private Controller controller = new Controller(tilesize);
	private Image Bombimage;
	private Image Buttonimage;
	private Image Flagimage;
	private Group root;

	public static void main(String[] args) throws FileNotFoundException {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		
		//for fun Random tiles game: 
		Tile[][] tilearr = new Tile[20][10];
		Random random = new Random();
		for (int i=0; i<tilearr.length; i++) {
			for (int j=0; j<tilearr[0].length; j++) {
				tilearr[i][j] = new Tile(
						random.nextBoolean(), 
						random.nextBoolean(), 
						random.nextBoolean()
						);
			}
		}
		
		
		

		primaryStage.setTitle("MineDraw");

		this.root = new Group();
		
		this.Bombimage = new Image(new FileInputStream("Pictures\\Bomb.png"));
		this.Buttonimage = new Image(new FileInputStream("Pictures\\Button.png"));
		this.Flagimage = new Image(new FileInputStream("Pictures\\Flag.png"));

		this.amountTilesLength = 10;
		this.amountTilesHeight = 20;
		this.tilesize=30;

		
		this.height = tilesize * amountTilesHeight;
		this.length = tilesize * amountTilesLength;

		Canvas canvas = new Canvas(length, height);
		gc = canvas.getGraphicsContext2D();
		gc.setFill(Color.DARKGRAY);
		gc.fillRect(0, 0, length, height);

		gc.setLineWidth(1);
		gc.setStroke(Color.GRAY);
		for (int i = 1; i < amountTilesLength; i++) {
			gc.strokeLine(tilesize * i, 0, tilesize * i, height);
		}
		for (int i = 1; i < amountTilesHeight; i++) {
			gc.strokeLine(0, tilesize * i, length, tilesize * i);
		}
		GridPane gridPane = new GridPane();
		gridPane.setMinSize(amountTilesHeight, amountTilesLength);

		this.root.getChildren().add(canvas);
		this.root.getChildren().add(gridPane);
		Scene scene = new Scene(root);

		scene.setOnMousePressed(controller.getEventHandler());
		scene.setOnMouseReleased(controller.getEventHandler());
		//scene.setOnMousePressed(this::handleMousePressed);
		//scene.setOnMouseReleased(this::handleMouseReleased);

		
		//for fun random tiles game
		for (int i=0; i<amountTilesHeight; i++) {
			for (int j=0; j<amountTilesLength; j++) {
				if (tilearr[i][j].hasFlag()) {
					drawButton(i,j);
					drawFlag(i,j);	
				} else if (tilearr[i][j].hasBomb()){
					drawBomb(i,j);
				} else if (!tilearr[i][j].isCleared()) {
					drawButton(i,j);
				}				
				//drawButton(i,j);
				//drawNumber(i,j,2);
				//drawBomb(i,j);
			}
		}
		
		
		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();
	}
	
	
	private void drawNumber(int y, int x, int number) { //coordinates given in arraytiles
		Text text = new Text();
		text.setText(Integer.toString(number));
		text.setX(x*tilesize+tilesize/2);// Calculate by desired tile + half tile
		text.setY(y*tilesize+tilesize/2);// Calculate by desired tile + half tile
		text.setFill(Color.BLACK);
		text.setFont(Font.font((24.1/30)*tilesize));
		text.setX(text.getX() - text.getLayoutBounds().getWidth() / 2);
		text.setY(text.getY() + text.getLayoutBounds().getHeight() / 4);
		this.root.getChildren().add(text);
	}

	private void drawBomb(int y, int x) {//coordinates given in arraytiles
		ImageView Bomb = new ImageView(Bombimage);
		Bomb.setX(x*tilesize);// Calculate by desired tile + 1/6 tile
		Bomb.setY(y*tilesize);// Calculate by desired tile + 1/6 tile
		Bomb.setFitHeight(tilesize);// Calculate by desired tile + 2/3 tile
		Bomb.setFitWidth(tilesize);
		//Bomb.setX(Bomb.getX() - Bomb.getLayoutBounds().getWidth() / 2);
		//Bomb.setY(Bomb.getY() - Bomb.getLayoutBounds().getHeight() / 2);
		//Bomb.setFitHeight(tilesize);
		//Bomb.setFitWidth(tilesize);
		this.root.getChildren().add(Bomb);
	}

	private void drawButton(int y, int x) {//coordinates given in arraytiles
		ImageView Button = new ImageView(Buttonimage);
		Button.setX(x*tilesize);
		Button.setY(y*tilesize);
		Button.setFitHeight(tilesize);
		Button.setFitWidth(tilesize);
		this.root.getChildren().add(Button);
	}

	private void drawFlag(int y, int x) {//coordinates given in arraytiles
		ImageView Flag = new ImageView(Flagimage);
		Flag.setX(x*tilesize);
		Flag.setY(y*tilesize);
		Flag.setFitHeight(tilesize);
		Flag.setFitWidth(tilesize);
		this.root.getChildren().add(Flag);
	}

	private void handleMouseReleased(MouseEvent event) {
		

		// get mineArray

		// sender det mouseevent videre til controller klassen

		// Laver mouseevent om til x og y coordinater ifht. tiles
		// NOTE: y aksen er søjlen af tiles helt til venstre og X aksen er rækken af
		// tiles i bunden
		double x = event.getX();
		double y = event.getY();
		int xCoorTiles = 0;
		int yCoorTiles = 0;
		while (x - tilesize > 0) {
			x = x - tilesize;
			xCoorTiles++;
		}
		while (y - tilesize > 0) {
			y = y - tilesize;
			yCoorTiles++;
		}
		gc.setFill(Color.WHITE);
		gc.fillRect(pressedTileX * tilesize, pressedTileY * tilesize, tilesize, tilesize);
		gc.setStroke(Color.GREY);
		gc.strokeRect(pressedTileX * tilesize, pressedTileY * tilesize, tilesize, tilesize);
		System.out.println(yCoorTiles + "," + xCoorTiles);
	}

	private void handleMousePressed(MouseEvent event) {
		

		// get mineArray

		// sender det mouseevent videre til controller klassen

		// Laver mouseevent om til x og y coordinater ifht. tiles
		// NOTE: y aksen er søjlen af tiles helt til venstre og X aksen er rækken af
		// tiles i bunden

		double x = event.getX();
		double y = event.getY();
		int xCoorTiles = 0;
		int yCoorTiles = 0;
		while (x - tilesize > 0) {
			x = x - tilesize;
			xCoorTiles++;
		}
		while (y - tilesize > 0) {
			y = y - tilesize;
			yCoorTiles++;
		}
		// Only relevant until pressedTile is in Model class
		this.pressedTileX = xCoorTiles;
		this.pressedTileY = yCoorTiles;
		gc.setFill(Color.GREY);
		gc.fillRect(pressedTileX * tilesize, pressedTileY * tilesize, tilesize, tilesize);
		System.out.println(yCoorTiles + "," + xCoorTiles);
	}

}
