package gui;

import java.io.File;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import javafx.stage.Popup;

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
		
		//map.draw();
	}
	
	@FXML
	private void handleNewWorldPressed(ActionEvent ae)
	{
		model.createNewWorld();
		loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
	}
	
	@FXML
	private void handleLoadWorldPressed(ActionEvent ae)
	{
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose World File");
		File worldFile = fc.showOpenDialog(new Popup());
		model.loadWorld(worldFile);
	}
}