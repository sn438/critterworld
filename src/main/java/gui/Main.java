package gui;

import java.awt.Insets;
import java.net.URL;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application
{

	public static void main(String[] args)
	{
		launch(args);
	}
	
	@Override
	public void start(Stage stage)
	{
		 try
		 {
	         URL r = getClass().getResource("gui.fxml");
	         if (r == null)
	        	 throw new Exception("No FXML resource found.");
	         Scene scene = new Scene(FXMLLoader.load(r));
	         stage.setTitle("CritterWorld Simulation");
	         stage.setScene(scene);
	         stage.sizeToScene();
	         stage.show();
		 }
		 catch (Exception e)
		 {
	         System.err.println(e.getMessage());
	         e.printStackTrace();
	     }
	}
	
	
}