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
			stage.getIcons().add(icon);
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
// TODO optimize zooming so that it only redraws visible portion and not whole
// entire hex grid?

// Sujith:
// TODO work on server stuff

// Rishi:
// TODO implement smell - dijkstra
// TODO do written problems
// TODO merge run and pause buttons?
// TODO create some smarter critter programs for better testing (and as prep for
// critter tournament :))
// TODO make ctrl + arrow keys zooming if wants and has time (or qw or -+)
// TODO why doesn't critter info box passively update when GUI is in run mode
// instead of step by step mode?
// TODO why doesn't critter info box clear when critter leaves the selected hex?

// Andy:
// TODO double check to make sure all A4 and A5 grader bugs have been fixed
// TODO start worldmodel updates and stuff
// TODO find better way to loop through all critters???
// TODO locks in world model - ask about those?
// TODO list of changed hexes
// TODO is there a way to have the program stop automatically when the GUI is
// closed while running?
// TODO fix bug for non terminating threads
// TODO do dead critter list

// Bug: new world,
// reset,
// loadworld,
// spiralcritterworld
// loaded 20 noenergybudcritters
// ConcurrencyModificationException: probably because things inside the map are
// being modified??
// iterating through list of critters and objects
// critter species names?