package gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {
	public static final Image icon = new Image(
			GUI.class.getClassLoader().getResourceAsStream("gui/critterworld_favicon.png"));

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setMinHeight(900);
		stage.setMinWidth(1600);

		try {
			URL r = getClass().getResource("gui.fxml");
			if (r == null)
				throw new Exception("No FXML resource found.");
			Scene scene = new Scene(FXMLLoader.load(r));
			stage.setTitle("CRITTERWORLD");
			stage.getIcons().add(icon);
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

// Rishi:
// TODO use MAX_SMELL_DIST instead of 10

// Andy:
// TODO when loading a world, given critter orientations are ignored -- random values are used
// TODO reduce min width probably cuz it's a very high amount right now