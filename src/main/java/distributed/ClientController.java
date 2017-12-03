package distributed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import ast.Program;
import gui.GUI;
import gui.WorldMap;
import gui.WorldModel;
import gui.Controller.LoginInfo;
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
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.ImageView;
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
public class ClientController {
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
	private Slider simulationSpeed;

	@FXML
	private ScrollPane scroll;
	@FXML
	private Canvas c;
	@FXML
	private Label crittersAlive;
	@FXML
	private Label stepsTaken;

	/** A timeline that redraws the world periodically. */
	private Timeline timeline;
	/** The model that contains the world state. */
	private WorldModel model;
	/** Controls the hex grid. */
	private WorldMap map;
	/** The rate at which the simulation is run. */
	private long simulationRate;
	/** The executor that is used to step the world periodically. */
	private ScheduledExecutorService executor;

	private double panMarkerX;
	private double panMarkerY;

	/**
	 * True when the user is in the process of dragging, so that upon release, hex
	 * selection is NOT performed on the hex currently under the mouse pointer.
	 */
	private boolean isCurrentlyDragging = false;
	private LoginInfo loginInfo;
	private String urlInitial;
	private SessionID sessionID;
	private ClientRequestHandler handler;

	@FXML
	public void initialize() {
		login();
		loadCritterFile.setDisable(true);
		numCritters.setDisable(true);
		pause.setDisable(true);

		setupCanvas();
		setGUIReady(false);
	}

	private void doReset() {
		if (executor != null)
			executor.shutdownNow();
		if (timeline != null)
			timeline.stop();

		model = new WorldModel();
		simulationRate = 30;

		loadCritterFile.setDisable(true);
		numCritters.setDisable(true);
		pause.setDisable(true);
		setGUIReady(false);
		resetInfo();

		c.getGraphicsContext2D().clearRect(0, 0, c.getWidth(), c.getHeight());

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

	private void setupCanvas() {
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
	}

	private void setGUIReady(boolean isReady) {
		chkRandom.setDisable(!isReady);
		chkSpecify.setDisable(!isReady);
		stepForward.setDisable(!isReady);
		run.setDisable(!isReady);
		simulationSpeed.setDisable(!isReady);
		displayProgram.setDisable(!isReady);
		c.setDisable(!isReady);
		c.setVisible(isReady);
	}

	private void resetInfo() {
		numCritters.clear();

		memSizeText.setText("");
		speciesText.setText("");
		defenseText.setText("");
		offenseText.setText("");
		sizeText.setText("");
		energyText.setText("");
		passText.setText("");
		tagText.setText("");
		postureText.setText("");
		lastRuleDisplay.setText("");
		lastRuleDisplay.setWrapText(true);

		chkRandom.setSelected(false);
		chkSpecify.setSelected(false);
	}

	@FXML
	private void handleNewWorldPressed(MouseEvent me) {
		doReset();
		boolean localMode = false;
		if (localMode) {
			newWorld();
		} else {
			newWorldServer();
		}

	}

	private void newWorld() {
		model.createNewWorld();
		map = new WorldMap(c, model);
		setGUIReady(true);
		crittersAlive.setText("Critters Alive: " + model.getNumCritters());
		stepsTaken.setText("Time: " + model.getCurrentTimeStep());

		map.draw();
	}

	private void newWorldServer() {
		if (handler.createNewWorld(sessionID.getSessionId())) {
			map = new WorldMap(c, handler, sessionID.getSessionId());
			map.draw();
		} else
			return;
		setGUIReady(true);
		crittersAlive.setText("Critters Alive: " + model.getNumCritters());
		stepsTaken.setText("Time: " + model.getCurrentTimeStep());
	}

	@FXML
	private void handleLoadWorldPressed(MouseEvent me) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose World File");
		File initDirectory = new File("./src/test/resources/simulationtests"); // TODO remove before submitting?
		fc.setInitialDirectory(initDirectory); // TODO remove before submitting?
		File worldFile = fc.showOpenDialog(new Popup());
		if (worldFile == null) {
			return;
		}
		doReset();
		boolean localMode = false;
		if (localMode)
			loadWorld(worldFile);
		else
			loadServerWorld(worldFile);
	}

	private void loadServerWorld(File worldFile) {
		try {
			handler.loadWorld(worldFile, sessionID.getSessionId());
			map = new WorldMap(c, handler, sessionID.getSessionId());
			map.draw();
		} catch (FileNotFoundException e) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		} catch (IllegalArgumentException e) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		} catch (IOException e) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		}
		chkRandom.setDisable(false);
		chkSpecify.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setDisable(false);
		c.setVisible(true);
	}

	private void loadWorld(File worldFile) {
		try {
			model.loadWorld(worldFile);
			map = new WorldMap(c, model);
			map.draw();
		} catch (FileNotFoundException e) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		} catch (IllegalArgumentException e) {
			Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
			a.setTitle("Invalid File");
			a.showAndWait();
			return;
		}
		chkRandom.setDisable(false);
		chkSpecify.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setDisable(false);
		c.setVisible(true);
	}

	@FXML
	private void handleLoadCritters(MouseEvent me) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose Critter File");
		File f = new File("./src/test/resources/simulationtests"); // TODO remove
		fc.setInitialDirectory(f); // TODO remove
		File critterFile = fc.showOpenDialog(new Popup());
		if (critterFile == null)
			return;

		ToggleButton choice = (ToggleButton) LoadChoice.getSelectedToggle();
		if (choice == chkRandom) {
			try {
				int n = Integer.parseInt(numCritters.getText());
				boolean localMode = false;
				if (localMode)
					model.loadRandomCritters(critterFile, n);
				else
					handler.loadRandomCritters(critterFile, n, sessionID.getSessionId());

			} catch (NumberFormatException e) {
				Alert a = new Alert(AlertType.ERROR, "Make sure you've inputed a valid number of critters to load in.");
				a.setTitle("Invalid Number");
				a.showAndWait();
				return;
			} catch (FileNotFoundException e) {
				Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
				a.setTitle("Invalid File");
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
					boolean localMode = false;
					if (localMode)
						model.loadCritterAtLocation(critterFile, c, r);
					else
						try {
							handler.loadCritterAtLocation(critterFile, c, r, sessionID.getSessionId());
						} catch (FileNotFoundException e) {
							Alert a = new Alert(AlertType.ERROR, "Your file could not be read. Please try again.");
							a.setTitle("Invalid File");
							a.showAndWait();
							return;
						}
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
		crittersAlive.setText("Critters Alive: " + model.getNumCritters());
		stepsTaken.setText("Time: " + model.getCurrentTimeStep());
	}

	@FXML
	private void handleRunPressed(MouseEvent me) {
		if (simulationRate == 0)
			return;

		Thread worldUpdateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				model.advanceTime();
				// System.out.println("ASFAS");
			}
		});
		worldUpdateThread.setDaemon(false);

		executor = Executors.newSingleThreadScheduledExecutor();
		executor.scheduleAtFixedRate(worldUpdateThread, 0, 1000 / simulationRate, TimeUnit.MILLISECONDS);

		timeline = new Timeline(new KeyFrame(Duration.millis(1000 / 30), new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent ae) {
				updateInfoBox();
				map.draw();
				updateInfoBox();
				crittersAlive.setText("Critters Alive: " + model.getNumCritters());
				stepsTaken.setText("Time: " + model.getCurrentTimeStep());
			}
		}));

		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		newWorld.setDisable(true); // TODO should we take these 4 lines out so you can create a new world even
									// while the current one is still running?
		loadWorld.setDisable(true); // TODO refer to above
		loadCritterFile.setDisable(true);
		chkRandom.setDisable(true);
		chkSpecify.setDisable(true);
		numCritters.setDisable(true);
		stepForward.setDisable(true);
		run.setDisable(true);
		simulationSpeed.setDisable(true);

		pause.setDisable(false);
	}

	@FXML
	private void handlePauseClicked(MouseEvent me) {
		executor.shutdownNow();

		newWorld.setDisable(false); // TODO refer to above
		loadWorld.setDisable(false); // TODO refer to above
		loadCritterFile.setDisable(false);
		chkRandom.setDisable(false);
		chkSpecify.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		simulationSpeed.setDisable(false);

		timeline.stop();
		pause.setDisable(true);
	}

	@FXML
	private void handleMapClicked(MouseEvent me) {
		if (me.getButton() == MouseButton.PRIMARY && !isCurrentlyDragging) {
			double xCoordinateSelected = me.getSceneX();
			double yCoordinateSelected = me.getSceneY() - 25;
			map.select(xCoordinateSelected, yCoordinateSelected);
			updateInfoBox();
		}
		isCurrentlyDragging = false;
	}

	private void updateInfoBox() {
		if (map.getSelectedHex() != null) {
			int[] hexCoordinatesSelected = map.getSelectedHex();
			columnText.setText(String.valueOf(hexCoordinatesSelected[0]));
			rowText.setText(String.valueOf(hexCoordinatesSelected[1]));
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
				lastRuleDisplay.setText("Last rule: " + "\n" + critter.getLastRuleString());
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
		} else {
			columnText.setText("");
			rowText.setText("");
		}
	}

	@FXML
	private void handleMapScroll(ScrollEvent se) {
		if (se.getDeltaY() > 0) {
			map.zoom(true);
		} else {
			map.zoom(false);
		}
	}

	@FXML
	private void handleMapDrag(MouseEvent me) {
		if (me.isPrimaryButtonDown()) {
			if (!isCurrentlyDragging) {
				// sets initial coordinates for the drag
				panMarkerX = me.getSceneX();
				panMarkerY = me.getSceneY();
			}
			isCurrentlyDragging = true;

			map.drag((me.getSceneX() - panMarkerX) / 0.05, (me.getSceneY() - panMarkerY) / 0.05);

			panMarkerX = me.getSceneX();
			panMarkerY = me.getSceneY();
		}
	}

	@FXML
	private void handleKeyEvents(KeyEvent ke) {
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
		if (ke.getCode().equals(KeyCode.EQUALS)) {
			map.zoom(true);
		} else if (ke.getCode().equals(KeyCode.MINUS)) {
			map.zoom(false);
		}
	}

	@FXML
	private void help(ActionEvent ae) {
		String fileName = "instructions.txt";
		ImageView imgview = new ImageView(GUI.icon);
		Alert alert = new Alert(AlertType.INFORMATION);	
		alert.setGraphic(imgview);
		try {
			String helpText = new String(Files.readAllBytes(Paths.get(fileName)));
			alert.setContentText(helpText);
			alert.setHeaderText("How to Use CRITTERWORLD");
			alert.showAndWait();
		} catch (IOException e) {
			String errorText = "Please refer to user manual for instructions on how to use product.";
			alert.setContentText(errorText);
			alert.setHeaderText(null);
			alert.showAndWait();
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
		if (hexCoordinates == null) {
			return;
		}
		if (model.getCritter(hexCoordinates[0], hexCoordinates[1]) != null) {
			SimpleCritter critter = model.getCritter(hexCoordinates[0], hexCoordinates[1]);
			Program critterProgram = critter.getProgram();
			String critterProgramString = critterProgram.toString();
			Alert alert = new Alert(AlertType.INFORMATION, critterProgramString);
			alert.setHeaderText("Critter Program");
			alert.showAndWait();
		}
	}

	/** Logs into the server. */
	private void login() {
		Gson gson = new Gson();
		Dialog<LoginInfo> dialog = new Dialog<>();
		dialog.setTitle("Login Info");
		dialog.setHeaderText("Please Enter In The Passwords You Have Access To");
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
		TextField levelTextField = new TextField("Level");
		TextField passwordTextField = new TextField("Password");
		// TextField urlTextField = new TextField("http://localhost:8080");
		TextField urlTextField = new TextField("http://hexworld.herokuapp.com:80/hexworld");
		dialogPane.setContent(new VBox(8, levelTextField, passwordTextField, urlTextField));
		Platform.runLater(levelTextField::requestFocus);
		dialog.setResultConverter((ButtonType button) -> {
			if (button == ButtonType.OK) {
				return new LoginInfo(levelTextField.getText(), passwordTextField.getText(), urlTextField.getText());
			}
			return null;
		});
		Optional<LoginInfo> optionalResult = dialog.showAndWait();
		optionalResult.ifPresent((LoginInfo results) -> {
			loginInfo = new LoginInfo(results.level, results.password);
			this.urlInitial = results.url;
		});
		URL url = null;
		try {
			url = new URL(this.urlInitial + "/login");
			// url = new URL("http://hexworld.herokuapp.com:80/hexworld/login");
			System.out.println(gson.toJson(loginInfo, LoginInfo.class));
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			System.out.println(url.toString());
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(loginInfo, LoginInfo.class));
			w.flush();
			if (connection.getResponseCode() == 401) {

				Alert alert = new Alert(AlertType.CONFIRMATION);
				alert.setTitle("Login Error");
				alert.setHeaderText("Credentials Not Recognized");
				alert.setContentText("The login credentials you entered were invalid. Click "
						+ "OK to continue in local mode or Cancel to exit the program.");
				Optional<ButtonType> result = alert.showAndWait();

				if (result.get() == ButtonType.OK) {
					boolean localMode = true;
					model = new WorldModel();
					return;
				} else {
					System.exit(0);
				}
			}
			
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String sessionIdString = "";
			String holder = r.readLine();
			while(holder != null) {
				sessionIdString += holder;
				holder = r.readLine();
			}
			sessionID = gson.fromJson(sessionIdString, SessionID.class);
			System.out.println(sessionID.getSessionId());
			
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
			boolean localMode = true;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Login Error");
			alert.setHeaderText("Credentials Not Recognized");
			alert.setContentText("The login credentials you entered were invalid. Click "
					+ "OK to continue in local mode or Cancel to exit the program.");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				localMode = true;
				model = new WorldModel();
				return;
			} else {
				System.exit(0);
			}
			return;
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
			boolean localMode = true;
			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.setTitle("Login Error");
			alert.setHeaderText("Credentials Not Recognized");
			alert.setContentText("The login credentials you entered were invalid. Click "
					+ "OK to continue in local mode or Cancel to exit the program.");
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == ButtonType.OK) {
				localMode = true;
				model = new WorldModel();
				return;
			} else {
				System.exit(0);
			}
			return;
		}
		boolean localMode = false;
		handler = new ClientRequestHandler(this.urlInitial);
	}

	public class LoginInfo {

		String level;
		String password;
		String url;

		private LoginInfo(String level, String password) {
			this.level = level;
			this.password = password;
		}

		private LoginInfo(String level, String password, String url) {
			this.level = level;
			this.password = password;
			this.url = url;
		}
	}
}