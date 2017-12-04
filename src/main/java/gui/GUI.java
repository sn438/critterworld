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

// TODO write tests?
// TODO check all todos before submitting (especially "// todo remove"s)

// TODO review instructions at end to make sure they are consistent with any
// changes to gui made
//TODO optimize zooming so that it only redraws visible portion and not whole
//entire hex grid?

// TODO double check to make sure all A4 and A5 grader bugs have been fixed
// TODO find better way to loop through all critters???
// TODO fix bug for non terminating threads
// TODO handle IllegalArgumentException in controller