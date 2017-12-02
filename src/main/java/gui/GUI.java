package gui;

import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class GUI extends Application {
	public static final Image icon = new Image(GUI.class.getClassLoader().getResourceAsStream("gui/critterworld_favicon.png"));
	// public static final Image icon = new Image("gui/critterworld_favicon.png"); TODO why is it not just something as simple as this?

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

// Sujith:
// TODO work on server stuff

// Rishi:
// TODO when is "numValidHexes: [insert number]" printed? why is it printed? (something to do with load critter?, or is this server stuff)
// TODO add food add rocks buttons = use addWorldObject()
// TODO implement smell
// TODO draw written problem #3 in paint
// TODO create some smarter critter programs for better testing (and as prep for
// critter tournament :))
// TODO optimize zooming so that it only redraws visible portion and not whole
// entire hex grid?
// TODO load critter button and num critters field should reset after critters are loaded, right?
// TODO what does display program do if no critter is selected? should it be disabled unless a critter is selected?
// TODO review instructions at end to make sure they are consistent with any changes to gui made
// TODO add change view angle thing if time, or kozen easter egg, or sounds

// Andy:
// TODO has below bug been fixed?
// TODO double check to make sure all A4 and A5 grader bugs have been fixed
// TODO start worldmodel updates and stuff
// TODO find better way to loop through all critters???
// TODO locks in world model - ask about those?
// TODO list of changed hexes
// TODO is there a way to have the program stop automatically when the GUI is
// closed while running?
// TODO fix bug for non terminating threads
// TODO provide way for server to provide list of IDs of dead critters

// Bug: new world,
// reset,
// loadworld,
// spiralcritterworld
// loaded 20 noenergybudcritters
// ConcurrencyModificationException: probably because things inside the map are
// being modified??
// iterating through list of critters and objects
// critter species names?