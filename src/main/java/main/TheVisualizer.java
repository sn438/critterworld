package main;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * Test class for attempting to create a GUI.
 * @author rishi
 *
 */
public class TheVisualizer extends Application {
	public static void main(String[] args) {
		launch(args);
	}
// TODO this is broken pls fix
	public void start(Stage stage) {
		Group root = new Group();
		Scene scene = new Scene(root, 800, 600, Color.BLACK);
		stage.setScene(scene);
		stage.show();
	}
}
