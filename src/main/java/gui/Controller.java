package gui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.util.Duration;

/** This class handles user inputs and sends information to the world model and world view to update their states accordingly. */
public class Controller
{
	@FXML private MenuItem help;
	@FXML private MenuItem close;
	
	@FXML private Button newWorld;
	@FXML private Button loadWorld;
	@FXML private Button loadCritterFile;
	@FXML private ToggleGroup HexChoice;
	@FXML private RadioButton chkRand;
	@FXML private RadioButton chkSpecify;
	@FXML private TextField numCritters;
	@FXML private Button stepForward;
	@FXML private Button run;
	@FXML private Button pause;
	@FXML private Button reset;
	@FXML private Slider simulationSpeed;
	
	@FXML private Canvas worldMap;
	@FXML private Label crittersAlive;
	@FXML private Label stepsTaken;
	private Timeline timeline;
	private WorldModel model;
	private WorldMap map;
	
	@FXML
	public void initialize()
	{
		model = new WorldModel();
		newWorld.setDisable(false);
		loadWorld.setDisable(false);
		loadCritterFile.setDisable(true);
		chkRand.setDisable(true);
		chkSpecify.setDisable(true);
		numCritters.setDisable(true);
		stepForward.setDisable(true);
		run.setDisable(true);
		pause.setDisable(true);
		reset.setDisable(true);
		simulationSpeed.setDisable(true);
		map = new WorldMap(worldMap, worldMap.getHeight(), worldMap.getWidth());
		//map.draw();
		
		
		timeline = new Timeline(new KeyFrame(Duration.millis(100),
	               new EventHandler<ActionEvent>() {
	                  @Override
	                  public void handle(ActionEvent ae) {
	                     map.draw();
	                  }
	               }));
	      timeline.setCycleCount(Timeline.INDEFINITE);
	      timeline.play();
	}
}