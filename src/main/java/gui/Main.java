package gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage stage) {
		stage.setMinHeight(800);
		stage.setMinWidth(1000);

		try {
			URL r = getClass().getResource("gui.fxml");
			if (r == null)
				throw new Exception("No FXML resource found.");
			Scene scene = new Scene(FXMLLoader.load(r));
			stage.setTitle("Critters IN SPACE!");
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
		} catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
	}

}

// TODO fix zooming + panning position
// TODO fix hex selection upon zoom
// TODO fix weird lagginess in panning
// TODO black universe, grey hex grid, grey fully filled hexes as rocks, neon
// circles for food (maybe yellow), neon isosceles triangles for critters (maybe
// green + species variation so different shades)
// TODO tableview for hexinfo
// TODO when unselecting a hex, kill the critter info in corner
// TODO figure out what to do when selecting a rock or food
// TODO change title of critter program message box