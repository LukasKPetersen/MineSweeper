<<<<<<< HEAD
package controller;

import java.io.FileInputStream;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller extends Application {
	
	public static void main(String[] args) {
	// inside JVM launch method calls start method
		Application.launch(args);
	}
	
	public void start(Stage outStage) throws IOException {
		// Setting title to screen
		outStage.setTitle("FXML Controller Button with CSS Styles");
		// Creating FXML Loader instance
		FXMLLoader loader = new FXMLLoader();
		// FXML path
		String fxmlActualPath = "C://Users//paramesh//Desktop//Desktop//Verinon Purpose//FXMLController//src//com//fxml//controller/FXMLLabelAddingButtonController.fxml";
		// Setting FXML path
		FileInputStream fxmlStream = new FileInputStream(fxmlActualPath);
		// Creating VBox to add FXML label and text fields
		VBox vBox = (VBox) loader.load(fxmlStream);
		// Creating scene
		Scene screen = new Scene(vBox, 500, 500);
		// Setting screen stage
		outStage.setScene(screen);
		// Showing the screen
		outStage.show();
	}
=======
package src.controller;
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

public class Controller {
	
>>>>>>> f715c0bf86201a31c3ed1a03ce0c5c428c32d2ee
}
