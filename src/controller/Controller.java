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
}
