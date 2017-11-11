package gui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import ast.Program;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Popup;
import javafx.util.Duration;
import simulation.SimpleCritter;

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
	private Text columnText;
	@FXML
	private Text rowText;
	@FXML
	private Text memSizeText;
	@FXML
	private Text speciesText;
	@FXML
	private Text defenseText;
	@FXML
	private Text offenseText;
	@FXML
	private Text energyText;
	@FXML
	private Text passText;
	@FXML
	private Text tagText;
	@FXML
	private Text postureText;
	@FXML
	private Text sizeText;
	@FXML
	private TextArea lastRuleDisplay;
	@FXML
	private Button displayProgram;
	
	@FXML
	private Button newWorld;
	@FXML
	private Button loadWorld;
	@FXML
	private Button loadCritterFile;
	@FXML
	private ToggleGroup LoadChoice;
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
	private ScrollPane scroll;
	@FXML
	private Canvas c;
	@FXML
	private Label crittersAlive;
	@FXML
	private Label stepsTaken;

	private Timeline timeline;
	/** The model that contains the world state. */
	private WorldModel model;
	/** Controls the hex grid. */
	private WorldMap map;

	private double mousePanPressedX;
	private double mousePanPressedY;
	
	/** The rate at which the simulation is run. */
	private long simulationRate;
	/** The executor that is used to step the world periodically. */
	private ScheduledExecutorService executor;

	@FXML
	public void initialize() {
		model = new WorldModel();
		simulationRate = 30;
		
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
		
		simulationSpeed.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                        simulationRate = new_val.longValue();
                }
            });
	}

	@FXML
	private void handleNewWorldPressed(MouseEvent me) {
		model.createNewWorld();
		map = new WorldMap(c, model);
		newWorld.setDisable(true);
		loadWorld.setDisable(true);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
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
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
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
		loadCritterFile.setDisable(false);
	}

	@FXML
	private void handleChkSpecify(ActionEvent ae) {
		numCritters.setDisable(true);
		loadCritterFile.setDisable(false);
	}

	@FXML
	private void handleLoadCritters(MouseEvent me) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose Critter File");
		File critterFile = fc.showOpenDialog(new Popup());
		
		RadioButton choice = (RadioButton) LoadChoice.getSelectedToggle();
		if(choice == chkRand)
		{
			try
			{
				int n = Integer.parseInt(numCritters.getText());
				model.loadRandomCritters(critterFile, n);
			}
			catch (NumberFormatException e)
			{
				Alert a = new Alert(AlertType.ERROR, "Make sure you've inputed a valid number of critters to load in.");
				a.setTitle("Invalid Number");
				a.showAndWait();
				return;
			}
		}
		else
		{	
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Choose Hex");
			dialog.setHeaderText("Enter \"[columns] [rows]\".");
			Optional<String> result = dialog.showAndWait();
			
			try
			{
				result.ifPresent(location -> 
				{
					String col = result.get().split(" ")[0];
					String row = result.get().split(" ")[1];
					int c = Integer.parseInt(col);
					int r = Integer.parseInt(row);
					model.loadCritterAtLocation(critterFile, c, r);
				});
			}
			catch (Exception e)
			{
				Alert a = new Alert(AlertType.ERROR, "Make sure you've inputed a valid location");
				a.setTitle("Invalid Location");
				a.showAndWait();
				return;
			}
		}
		
		map.draw();
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
		if(simulationRate == 0)
			return;
		
		Thread worldUpdateThread = new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				model.advanceTime();
			}
		});
		worldUpdateThread.setDaemon(true);
		
		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(worldUpdateThread, 0, 1000 / simulationRate, TimeUnit.MILLISECONDS);
		
		timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 30), new EventHandler<ActionEvent>() {
		
			@Override
			public void handle(ActionEvent ae)
			{ 
				map.draw();
				crittersAlive.setText("Critters Alive: " + model.numCritters);
				stepsTaken.setText("Time: " + model.time);
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
	private void handlePauseClicked(MouseEvent me) {
		executor.shutdownNow();

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

		timeline.stop();
		pause.setDisable(true);
	}

	@FXML
	private void handleMapClicked(MouseEvent me) {
		if (!me.isPrimaryButtonDown()) {
			mousePanPressedX = me.getScreenX();
			mousePanPressedY = me.getScreenY();
		}
		else
		{
			double xCoordinateSelected = me.getSceneX();
			double yCoordinateSelected = me.getSceneY();
			int[] hexCoordinatesSelected = new int[2];
			boolean shouldUpdateRowColumn = map.select(xCoordinateSelected, yCoordinateSelected);
			hexCoordinatesSelected = map.getSelectedHex();
			if(shouldUpdateRowColumn)
			{
				rowText.setText(String.valueOf(hexCoordinatesSelected[0]));
				columnText.setText(String.valueOf(hexCoordinatesSelected[1]));
				if (model.getCritter(hexCoordinatesSelected[0], hexCoordinatesSelected[1]) != null)
				{
					SimpleCritter critter = model.getCritter(hexCoordinatesSelected[0], hexCoordinatesSelected[1]);
					memSizeText.setText(String.valueOf(critter.getMemLength()));
					speciesText.setText(critter.getName());
					int[] critterMemoryCopy = new int[critter.getMemLength()];
					critterMemoryCopy = critter.getMemoryCopy();
					defenseText.setText(String.valueOf(critterMemoryCopy[1]));
					offenseText.setText(String.valueOf(critterMemoryCopy[2]));
					sizeText.setText(String.valueOf(critterMemoryCopy[3]));
					energyText.setText(String.valueOf(critterMemoryCopy[4]));
					passText.setText(String.valueOf(critterMemoryCopy[5]));
					tagText.setText(String.valueOf(critterMemoryCopy[6]));
					postureText.setText(String.valueOf(critterMemoryCopy[7]));
					lastRuleDisplay.setText(critter.getLastRule());
				}
				else
				{
					memSizeText.setText("");
					speciesText.setText("");
					defenseText.setText("");
					offenseText.setText("");
					sizeText.setText("");
					energyText.setText("");
					passText.setText("");
					tagText.setText("");
					postureText.setText("");
				}
			}
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
	
	@FXML
	private void handleDisplayProgram(MouseEvent me) {
		int[] hexCoordinates = new int[2];
		hexCoordinates = map.getSelectedHex();
		if (model.getCritter(hexCoordinates[0], hexCoordinates[1]) != null) {
			SimpleCritter critter = model.getCritter(hexCoordinates[0], hexCoordinates[1]);
			Program critterProgram = critter.getProgram();
			String critterProgramString = critterProgram.toString();
			Alert alert = new Alert(AlertType.INFORMATION, critterProgramString);
			alert.setHeaderText("Critter Program");
			alert.showAndWait();
		}
	}
}