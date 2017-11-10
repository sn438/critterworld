package gui;

import java.io.File;
import java.io.FileNotFoundException;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;

/**
 * This class handles user inputs and sends information to the world model and
 * world view to update their states accordingly.
 */
public class Controller {
	@FXML
	private MenuItem help;
	@FXML
	private MenuItem close;

	@FXML
	private Button newWorld;
	@FXML
	private Button loadWorld;
	@FXML
	private Button loadCritterFile;
	@FXML
	private ToggleGroup HexChoice;
	@FXML
	private RadioButton chkRand;
	@FXML
	private RadioButton chkSpecify;
	@FXML
	private TextField numCritters;
	@FXML
	private Button stepForward;
	@FXML
	private Button run;
	@FXML
	private Button pause;
	@FXML
	private Button reset;
	@FXML
	private Slider simulationSpeed;
	
	@FXML
	private TableView hexContent;
	@FXML
	private TableView critterContent;
	
	@FXML
	private ScrollPane scroll;
	@FXML
	private Canvas c;
	@FXML
	private Label crittersAlive;
	@FXML
	private Label stepsTaken;

	private Timeline timeline;
	private WorldModel model;
	private WorldMap map;

	private double mousePanPressedX;
	private double mousePanPressedY;
	
	private boolean isRunning;

	@FXML
	public void initialize() {
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
		c.setDisable(true); // hi
		c.setVisible(false); // hi

		c.heightProperty().bind(scroll.heightProperty());
		c.widthProperty().bind(scroll.widthProperty());
		
		c.heightProperty().addListener(update -> 
		{
			if(map != null)
				map.draw();
		});
		
		c.widthProperty().addListener(update -> 
		{
			if(map != null)
				map.draw();
		});
	}

	@FXML
	private void handleNewWorldPressed(MouseEvent me) {
		model.createNewWorld();
		map = new WorldMap(c, model);
		newWorld.setDisable(true);
		loadWorld.setDisable(true);
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

		map.draw();

		// c.getGraphicsContext2D().setFill(Color.BLACK);
		// c.getGraphicsContext2D().fillRect(0, 0, c.getWidth(), c.getHeight());
	}

	@FXML
	private void handleLoadWorldPressed(MouseEvent me) throws FileNotFoundException, IllegalArgumentException {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose World File");
		File worldFile = fc.showOpenDialog(new Popup());
		try {
			model.loadWorld(worldFile);
		} catch (FileNotFoundException f) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		}
		map = new WorldMap(c, model);
		
		newWorld.setDisable(true);
		loadWorld.setDisable(true);
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
		
		map.draw();
	}

	@FXML
	private void handleStep(MouseEvent me)
	{
		model.advanceTime();
		map.draw();
		crittersAlive.setText("Critters Alive: " + model.numCritters);
		stepsTaken.setText("Time: " + model.time);
	}
	
	@FXML
	private void handleRunPressed(MouseEvent me) {
		isRunning = true;
		timeline = new Timeline(new KeyFrame(Duration.millis(33), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				if(isRunning)
				{
					Thread simulationHandler = new Thread(new Runnable()
					{
						@Override
						public void run()
						{
							model.advanceTime();
						}
					});
					simulationHandler.setDaemon(true);
					simulationHandler.start();
					Platform.runLater(() -> 
					{
						map.draw();
						crittersAlive.setText("Critters Alive: " + model.numCritters);
						stepsTaken.setText("Time: " + model.time);
					});
				}
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();
		
		newWorld.setDisable(true);
		loadWorld.setDisable(true);
		loadCritterFile.setDisable(true);
		chkRand.setDisable(true);
		chkSpecify.setDisable(true);
		numCritters.setDisable(true);
		stepForward.setDisable(true);
		run.setDisable(true);
		reset.setDisable(true);
		simulationSpeed.setDisable(true);
		
		pause.setDisable(false);
	}

	@FXML
	private void handlePauseClicked(MouseEvent me)
	{
		newWorld.setDisable(false);
		loadWorld.setDisable(false);
		loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
		
		pause.setDisable(true);
		isRunning = false;
	}
	
	@FXML
	private void handleMapClicked(MouseEvent me) {
		if (!me.isPrimaryButtonDown()) {
			mousePanPressedX = me.getScreenX();
			mousePanPressedY = me.getScreenY();
		} else {
			double xCoordinateSelected = me.getSceneX();
			double yCoordinateSelected = me.getSceneY();
			map.select(xCoordinateSelected, yCoordinateSelected, hexContent, critterContent);
		}
	}

	@FXML
	private void handleMapScroll(ScrollEvent se) {
		if (se.getDeltaY() > 0)
			map.zoom(true);
		else
			map.zoom(false);
	}

	@FXML
	private void handleMapDrag(MouseEvent me) {
		if (!me.isPrimaryButtonDown()) {
			map.drag(me.getScreenX() - mousePanPressedX, me.getScreenY() - mousePanPressedY);
		}
	}
}