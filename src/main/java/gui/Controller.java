package gui;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import ast.Program;
import distributed.Server;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.VBox;
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
	private Label lastRuleDisplay;
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
	private ToggleButton chkRandom;
	@FXML
	private ToggleButton chkSpecify;
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

	private double panMarkerX;
	private double panMarkerY;

	private boolean hexSelectionMood = true; //TODO what is this?

	/** The rate at which the simulation is run. */
	private long simulationRate;
	/** The executor that is used to step the world periodically. */
	private ScheduledExecutorService executor;

	private boolean startup = true;
	private LoginInfo loginInfo;
	private Server server;

	@FXML
	public void initialize() {
		// this code only runs on startup
		if(startup) {
			server = Server.getInstance();
			System.out.println(server.getPortNum()); //why is this static method accessed statically?
			login();
			startup = false;
		}
		
		model = new WorldModel();
		simulationRate = 30;
		newWorld.setDisable(false);
		loadWorld.setDisable(false);
		loadCritterFile.setDisable(true);
		chkRandom.setSelected(false);
		chkRandom.setDisable(true);
		chkSpecify.setSelected(false);
		chkSpecify.setDisable(true);
		numCritters.clear();
		numCritters.setDisable(true);
		stepForward.setDisable(true);
		run.setDisable(true);
		pause.setDisable(true);
		reset.setDisable(true);
		simulationSpeed.setDisable(true);
		memSizeText.setText("");
		speciesText.setText("");
		defenseText.setText("");
		offenseText.setText("");
		sizeText.setText("");
		energyText.setText("");
		passText.setText("");
		tagText.setText("");
		postureText.setText("");

		c.getGraphicsContext2D().clearRect(0, 0, c.getWidth(), c.getHeight());
		c.setDisable(true);
		c.setVisible(false);
		c.addEventFilter(MouseEvent.ANY, (e) -> c.requestFocus());

		c.heightProperty().bind(scroll.heightProperty());
		c.widthProperty().bind(scroll.widthProperty());

		// listeners that dynamically redraw the canvas in response to window resizing
		c.heightProperty().addListener(update -> {
			if (map != null)
				map.draw();
		});
		c.widthProperty().addListener(update -> {
			if (map != null)
				map.draw();
		});

		LoadChoice.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> ov, Toggle oldT, Toggle newT) {
				if (newT == null) {
					numCritters.setDisable(true);
					loadCritterFile.setDisable(true);
				} else if (newT == (Toggle) chkRandom) {
					numCritters.setDisable(false);
					loadCritterFile.setDisable(false);
				} else if (newT == (Toggle) chkSpecify) {
					numCritters.setDisable(true);
					loadCritterFile.setDisable(false);
				}
			}
		});

		// adds a listener to the slider to adjust world speed as the slider is changed
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
		chkRandom.setDisable(false);
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
		File f = new File(".\\src\\test\\resources\\simulationtests"); // TODO remove
		fc.setInitialDirectory(f); // TODO remove
		File worldFile = fc.showOpenDialog(new Popup());
		if (worldFile == null)
			return;

		try {
			model.loadWorld(worldFile);
		} catch (FileNotFoundException e) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		}
		map = new WorldMap(c, model);

		newWorld.setDisable(true);
		loadWorld.setDisable(true);
		chkRandom.setDisable(false);
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
	private void handleLoadCritters(MouseEvent me) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose Critter File");
		File f = new File(".\\\\src\\\\test\\\\resources\\\\simulationtests"); // TODO remove
		fc.setInitialDirectory(f); // TODO remove
		File critterFile = fc.showOpenDialog(new Popup());
		if (critterFile == null)
			return;

		ToggleButton choice = (ToggleButton) LoadChoice.getSelectedToggle();
		if (choice == chkRandom) {
			try {
				int n = Integer.parseInt(numCritters.getText());
				model.loadRandomCritters(critterFile, n);
			} catch (NumberFormatException e) {
				Alert a = new Alert(AlertType.ERROR, "Make sure you've inputed a valid number of critters to load in.");
				a.setTitle("Invalid Number");
				a.showAndWait();
				return;
			}
		} else {
			TextInputDialog dialog = new TextInputDialog();
			dialog.setTitle("Choose Hex");
			dialog.setHeaderText("Enter \"[columns] [rows]\".");
			Optional<String> result = dialog.showAndWait();

			try {
				result.ifPresent(location -> {
					String col = result.get().split(" ")[0];
					String row = result.get().split(" ")[1];
					int c = Integer.parseInt(col);
					int r = Integer.parseInt(row);
					model.loadCritterAtLocation(critterFile, c, r);
				});
			} catch (Exception e) {
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
		updateInfoBox();
		map.draw();
		crittersAlive.setText("Critters Alive: " + model.numCritters);
		stepsTaken.setText("Time: " + model.time);
	}

	@FXML
	private void handleRunPressed(MouseEvent me) {
		if (simulationRate == 0)
			return;

		Thread worldUpdateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				model.advanceTime();
			}
		});
		worldUpdateThread.setDaemon(true);

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(worldUpdateThread, 0, 1000 / simulationRate, TimeUnit.MILLISECONDS);

		timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 30), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent ae) {
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
		chkRandom.setDisable(true);
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
		chkRandom.setDisable(false);
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
		if (me.getButton() == MouseButton.PRIMARY && hexSelectionMood) {
			double xCoordinateSelected = me.getSceneX();
			double yCoordinateSelected = me.getSceneY() - 25;
			map.select(xCoordinateSelected, yCoordinateSelected);
			updateInfoBox();
		}
		hexSelectionMood = true;
	}

	private void updateInfoBox() {
		if (map.getSelectedHex() != null) {
			int[] hexCoordinatesSelected = map.getSelectedHex();
			rowText.setText(String.valueOf(hexCoordinatesSelected[0]));
			columnText.setText(String.valueOf(hexCoordinatesSelected[1]));
			if (model.getCritter(hexCoordinatesSelected[0], hexCoordinatesSelected[1]) != null) {
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
			} else {
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
			map.zoom(true, se.getDeltaX(), se.getDeltaY());
		else
			map.zoom(false, se.getDeltaX(), se.getDeltaY());
	}

	@FXML
	private void handleMapDrag(MouseEvent me) {
		if (me.isPrimaryButtonDown()) {
			if (hexSelectionMood) {
				// TODO should all these references to getScreen be getScene instead?
				panMarkerX = me.getScreenX();
				panMarkerY = me.getScreenY();
			}
			hexSelectionMood = false;

			map.drag((me.getScreenX() - panMarkerX) / 0.05, (me.getScreenY() - panMarkerY) / 0.05);

			panMarkerX = me.getScreenX();
			panMarkerY = me.getScreenY();
		}
	}

	@FXML
	private void handleMapPan(KeyEvent ke) {
		// TODO make it possible to press multiple keys at once for panning? seems to be
		// difficult.
		if (ke.getCode().equals(KeyCode.UP)) {
			map.drag(0, 400);
		}
		if (ke.getCode().equals(KeyCode.LEFT)) {
			map.drag(400, 0);
		}
		if (ke.getCode().equals(KeyCode.DOWN)) {
			map.drag(0, -400);
		}
		if (ke.getCode().equals(KeyCode.RIGHT)) {
			map.drag(-400, 0);
		}

	}

	@FXML
	private void help(ActionEvent ae) {
		GraphicsContext gc = c.getGraphicsContext2D();
		gc.strokeText("SUJITH", 100, 100);
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

	private void login() {
		Gson gson = new Gson();
		Dialog<LoginInfo> dialog = new Dialog<>();
		dialog.setTitle("Login Info");
		dialog.setHeaderText("Please Enter In The Passwords You Have Access To");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField levelTextField = new TextField("Level");
		TextField passwordTextField = new TextField("Password");
		dialogPane.setContent(new VBox(8, levelTextField, passwordTextField));

		Platform.runLater(levelTextField::requestFocus);
		dialog.setResultConverter((ButtonType button) -> {

			if (button == ButtonType.OK) {
				return new LoginInfo(levelTextField.getText(), passwordTextField.getText());
			}
			return null;
		});

		Optional<LoginInfo> optionalResult = dialog.showAndWait();
		optionalResult.ifPresent((LoginInfo results) -> {
			loginInfo = new LoginInfo(results.level, results.password);
			System.out.println(loginInfo.level + " " + loginInfo.password);
		});
		URL url = null;
		try {
			if (loginInfo == null) {
				System.out.println(true);
			} else {
				System.out.println(false);
			}
			url = new URL("http://localhost:" + 8080 + "/login");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			System.out.println(url.toString());
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");

			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(loginInfo, LoginInfo.class));
			w.flush();

			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			System.out.println(r.readLine());

		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
	}

	class LoginInfo {

		String level;
		String password;

		private LoginInfo(String level, String password) {
			this.level = level;
			this.password = password;
		}
	}
}