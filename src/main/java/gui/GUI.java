package gui;

import java.net.URL;
import java.util.Optional;

import javax.swing.text.html.Option;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GUI extends Application {
	private static int sessionId;
	//private static LoginInfo loginInfo;
	
	public static void main(String[] args) {
		//sessionId = Integer.parseInt(args[0]);
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
			stage.setTitle("CRITTERWORLD!");
			stage.setScene(scene);
			stage.sizeToScene();
			stage.show();
			
		} catch (Exception e) {
			
		}
	}

	public static int getSessionId() {
		return sessionId;
	}
}
