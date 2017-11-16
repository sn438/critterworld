package gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class GUI extends Application {
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
			stage.setTitle("SUJITHWORLD!");
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

// Sujith:
// TODO make it so if args != 0 or 4, say bad input

// Rishi:
// TODO when displaying critter info, strings that are too long need to resize
// nicely so they don't overlap with other stuff
// TODO critter program box should not be editable
// TODO critter program should not display only first line of program (i think
// this is what it's doing right now)
// TODO make critter info box blank when hex is unselected
// TODO create some smarter critter programs for better testing (and as prep for
// critter tournament :))
// TODO make critters solid instead of outlines? try solid out and see how it
// looks.
// TODO make ctrl + arrow keys zooming if wants and has time

// Andy:
// TODO make new world the default instead of blank, and make reset button do
// new world (TODO #2)
// TODO remove new world button because reset button makes it redundant once
// TODO #2 is finished