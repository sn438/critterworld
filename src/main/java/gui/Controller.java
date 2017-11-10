package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

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
import simulation.Hex;

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
	private long simulationRate;
	private ScheduledExecutorService executor;

	@FXML
	public void initialize() {
		model = new WorldModel();
		simulationRate = 30;
		isRunning = false;

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

		c.getGraphicsContext2D().clearRect(0, 0, c.getWidth(), c.getHeight());
		c.setDisable(true);
		c.setVisible(false);

		c.heightProperty().bind(scroll.heightProperty());
		c.widthProperty().bind(scroll.widthProperty());
	}

	@FXML
	private void handleNewWorldPressed(MouseEvent me) {
		model.createNewWorld();
		map = new WorldMap(c, model);
		newWorld.setDisable(true);
		loadWorld.setDisable(true);
		// loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		// numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setDisable(false);
		c.setVisible(true);

		map.draw();
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
		// loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		// numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setDisable(false);
		c.setVisible(true);

		map.draw();
	}

	@FXML
	private void handleChkRandom(ActionEvent ae) {
		numCritters.setDisable(false);
	}

	@FXML
	private void handleChkSpecify(ActionEvent ae) {
		numCritters.setDisable(true);
	}

	// @FXML
	// private void

	@FXML
	private void handleLoadCritters(MouseEvent me) {

	}

	@FXML
	private void handleStep(MouseEvent me) {
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
				Thread simulationHandler = new Thread(new Runnable() {
					@Override
					public void run() {
						// if(isRunning)
						model.advanceTime();
					}
				});
				simulationHandler.setDaemon(true);
				simulationHandler.start();
				Platform.runLater(() -> {
					map.draw();
					crittersAlive.setText("Critters Alive: " + model.numCritters);
					stepsTaken.setText("Time: " + model.time);
				});
			}
		}));

		/*
		 * isRunning = true; Thread worldUpdateThread = new Thread(new Runnable() {
		 * 
		 * @Override public void run() { model.advanceTime();
		 * System.out.println(model.time);//TODO REMOVE } });
		 * worldUpdateThread.setDaemon(true);
		 * 
		 * executor = Executors.newSingleThreadScheduledExecutor();
		 * executor.scheduleAtFixedRate(worldUpdateThread, 0, 1000 / simulationRate,
		 * TimeUnit.MILLISECONDS);
		 * 
		 * timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 30), new
		 * EventHandler<ActionEvent>() {
		 * 
		 * @Override public void handle(ActionEvent ae) { map.draw();
		 * crittersAlive.setText("Critters Alive: " + model.numCritters);
		 * stepsTaken.setText("Time: " + model.time); } }));
		 */

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
	private void handlePauseClicked(MouseEvent me) {
		// executor.shutdownNow();

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

		// isRunning = false;
		timeline.stop();
		pause.setDisable(true);
	}

	@FXML
	private void handleMapClicked(MouseEvent me) {
		if (!me.isPrimaryButtonDown()) {
			mousePanPressedX = me.getScreenX();
			mousePanPressedY = me.getScreenY();
		} else {
			double xCoordinateSelected = me.getSceneX();
			double yCoordinateSelected = me.getSceneY();
			map.select(xCoordinateSelected, yCoordinateSelected, critterContent, critterContent);
		}
	}

	@FXML
	private void handleResetClicked(MouseEvent me) {
		if (executor != null)
			executor.shutdownNow();
		if (timeline != null)
			timeline.stop();
		initialize();
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

	@FXML
	private void close(ActionEvent ae) {
		if (executor != null)
			executor.shutdownNow();
		if (timeline != null)
			timeline.stop();
		System.exit(0);
	}
}