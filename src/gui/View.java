package src.gui;
import java.util.Timer;
import java.util.TimerTask;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.event.EventHandler;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import java.util.*;
import java.awt.Graphics;
import java.io.IOException;
import javafx.scene.layout.GridPane;

public class View extends Application {
	private GraphicsContext gc;

	private int amountTilesHeight;
	private int amountTilesLength;
	private int height;
	private int length;
	private int tilesize = 30;

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		primaryStage.setTitle("MineDraw");
		Group root = new Group();

		this.amountTilesLength = 10;
		this.amountTilesHeight = 20;

		this.height = tilesize * amountTilesHeight;
		this.length = tilesize * amountTilesLength;

		Canvas canvas = new Canvas(length, height);
		gc = canvas.getGraphicsContext2D();

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

		root.getChildren().add(canvas);
		root.getChildren().add(gridPane);
		Scene scene = new Scene(root);

		scene.setOnMouseClicked(this::handleMouseEvent);
		

		primaryStage.setScene(scene);
		primaryStage.setResizable(false);
		primaryStage.show();

	}

	private void handleMouseEvent(MouseEvent event) {

		// get mineArray

		// sender det mouseevent videre til controller klassen
		
		
		//Laver mouseevent om til x og y coordinater ifht. tiles 
		//NOTE: y aksen er søjlen af tiles helt til venstre og X aksen er rækken af tiles i bunden 
		//Skal smides over i Controller
		double x = event.getX();
		double y = event.getY();
		int xCoorTiles = 0;
		int yCoorTiles = 0;
		while (x - tilesize > 0) {
			x=x-tilesize;
			xCoorTiles++;
		}
		while (y - tilesize > 0) {
			y=y-tilesize;
			yCoorTiles++;
		}
		System.out.println(yCoorTiles + "," + xCoorTiles);
	}

}
