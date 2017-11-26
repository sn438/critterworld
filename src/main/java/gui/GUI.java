package gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {
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
			Image icon = new Image(GUI.class.getClassLoader().getResourceAsStream("gui/critterworld_favicon.png"));
			stage.getIcons().add(icon); // TODO make icon solid critter before submission if keeping this art style
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

// TODO add SOUNDS!
// TODO add SKINS!
// TODO add MICROTRANSACTIONS!

// Sujith:
// TODO work on server stuff

// Rishi:
// TODO implement smell
// TODO do written problems
// TODO create some smarter critter programs for better testing (and as prep for
// critter tournament :))
// TODO make ctrl + arrow keys zooming if wants and has time (or qw or -+)
// TODO why doesn't critter info box passively update when GUI is in run mode
// instead of step by step mode?
// TODO why doesn't critter info box clear when critter leaves the selected hex?

// Andy:
// TODO merge run and pause buttons?
// TODO fix a5 and a4 grader bugs
// TODO start worldmodel updates and stuff
// TODO why is first new world created not centered?
// TODO critter info box should reset upon world reset (even if new world isn't
// called in the code like it's setup right now)
// TODO change run and pause buttons to the standard icons for play and pause?
// TODO move run and pause buttons closer to each other to account for the new
// lack of a reset button
// TODO fix loading critters so that it won't allow loading in more critters
// than there are available hexes

// Bug: new world,
// reset,
// loadworld,
// spiralcritterworld
// loaded 20 noenergybudcritters
// ConcurrencyModificationException: probably because things inside the map are
// being modified??
// iterating through list of critters and objects
// critter species names?
// do we just remove all the non-server stuff
