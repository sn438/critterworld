package gui;

import java.io.File;
import java.io.FileNotFoundException;

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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;

/**
 * This class handles user inputs and sends information to the world model and
 * world view to update their states accordingly.
 */
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

	@FXML private Canvas c;
	@FXML private Label crittersAlive;
	@FXML private Label stepsTaken;
	
	private Timeline timeline;
	private WorldModel model;
	private WorldMap map;
	
	private double mousePanPressedX;
	private double mousePanPressedY;

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
		c.setDisable(true);
		c.setVisible(false);
	}

	@FXML
	private void handleNewWorldPressed(MouseEvent me) {
		model.createNewWorld();
		loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setDisable(false);
		c.setVisible(true);
		
		map = new WorldMap(c, model);
		map.draw();
	}

	@FXML
	private void handleLoadWorldPressed(MouseEvent me) throws FileNotFoundException, IllegalArgumentException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose World File");
		File worldFile = fc.showOpenDialog(new Popup());
		try
		{
			model.loadWorld(worldFile);
		}
		catch (FileNotFoundException f)
		{
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		}
		
		loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setDisable(false);
		c.setVisible(true);
		
		map = new WorldMap(c, model);
		map.draw();
	}
	
	@FXML
	private void handleRunPressed(MouseEvent me)
	{
		timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				map.draw();
			}
		}));
		
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
	}
	
	@FXML
	private void handleMapClicked(MouseEvent me)
	{
		if (!me.isPrimaryButtonDown()) {
			mousePanPressedX = me.getScreenX();
			mousePanPressedY = me.getScreenY();
		} else {
			double xCoordinateSelected = me.getSceneX();
			double yCoordinateSelected = me.getSceneY();
			map.select(xCoordinateSelected, yCoordinateSelected);

		}
	}
	
	@FXML
	private void handleMapScroll(ScrollEvent se)
	{
		if (se.getDeltaY() > 0)
			map.zoom(true);
		else
			map.zoom(false);
	}
	
	@FXML
	private void handleMapDrag(MouseEvent me)
	{
		if (!me.isPrimaryButtonDown()) {
			map.drag(me.getScreenX() - mousePanPressedX, me.getScreenY() - mousePanPressedY);
		}
	}
}