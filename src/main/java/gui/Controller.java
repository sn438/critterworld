package gui;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.jetty.websocket.server.WebSocketHandler.Simple;

import com.google.gson.Gson;
import com.sun.javafx.collections.MappingChange.Map;

import ast.Program;
import ast.ProgramImpl;
import distributed.ClientRequestHandler;
import distributed.JSONWorldObject;
import distributed.Mutex;
import distributed.SessionID;
import distributed.WorldStateJSON;
import javafx.animation.Animation;
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
import parse.ParserImpl;
import simulation.Critter;
import simulation.Food;
import simulation.Hex;
import simulation.Rock;
import simulation.SimpleCritter;
import simulation.World;
import simulation.WorldObject;

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
	private SessionID sessionId;
	private boolean localMode;
	private ClientRequestHandler handler;
	private HashMap<Hex, SimpleCritter> hexToCritterMap;
	private boolean devMode = true;

	@FXML
	public void initialize() {
		login();
		if (!localMode) {
			map = new WorldMap(c, handler, sessionId.getSessionID());
			loadCritterFile.setDisable(true);
			numCritters.setDisable(true);
			pause.setDisable(true);
			setupCanvas();
			setGUIReadyServer(true);
			Timeline tl = new Timeline();
			tl.setCycleCount(Animation.INDEFINITE);
			KeyFrame updateGUI = new KeyFrame(Duration.seconds(0.1000), new EventHandler<ActionEvent>() {

				public void handle(ActionEvent event) {
					map.draw();
					simulationSpeed.valueProperty().addListener(new ChangeListener<Number>() {
						public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
							simulationRate = new_val.longValue();
							handler.changeRate(simulationRate, sessionId.getSessionID());
						}
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
					loadCritterFile.setOnAction(new EventHandler<ActionEvent>() {
						@Override
						public void handle(ActionEvent event) {
							loadCritterFile.setDisable(true);
						}
					});
					try {
						setWorldState();
					} catch (UnsupportedEncodingException e) {
						System.out.println("The world could not be drawn.");
					} catch (IOException e) {
						System.out.println("The world could not be drawn.");
					}
				}
			});

			tl.getKeyFrames().add(updateGUI);
			tl.play();
		}
		else {
			handleNewWorldPressed(null);
		}
	}

	private void doReset() {
		if (executor != null)
			executor.shutdownNow();
		if (timeline != null)
			timeline.stop();

		loadCritterFile.setDisable(true);
		numCritters.setDisable(true);
		pause.setDisable(true);
		setGUIReady(false);
		resetInfo();

		model = new WorldModel();
		simulationRate = 30;
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

	private void setWorldState() throws IOException {
		WorldStateJSON worldState = null;
		if (map != null) {
			int[] bottomLeftCorner = map.closestHex(0, c.getHeight());
			int[] topRightCorner = map.closestHex(c.getWidth(), 0);
			for (int i = 0; i < 2; i++) {
				if (bottomLeftCorner[i] < 0) {
					bottomLeftCorner[i] = 0;
				}
			}
			for (int i = 0; i < 2; i++) {
				if (topRightCorner[i] < 0) {

					topRightCorner[i] = 0;
				}
			}
			worldState = handler.getWorldObjects(topRightCorner[0], bottomLeftCorner[0], topRightCorner[1],
					bottomLeftCorner[1], sessionId.getSessionID());
			if (worldState == null) {
				return;
			}
			JSONWorldObject[] worldObjects = worldState.getWorldObjects();
			HashMap<WorldObject, Hex> objectMap = new HashMap<WorldObject, Hex>();
			HashMap<SimpleCritter, Hex> critterMap = new HashMap<SimpleCritter, Hex>();
			this.hexToCritterMap = new HashMap<Hex, SimpleCritter>();
			if (worldObjects != null) {
				for (JSONWorldObject worldObject : worldObjects) {
					if (worldObject.getType().equals("rock")) {
						int c = worldObject.getCol();
						int r = worldObject.getRow();
						Hex hex = new Hex(c, r);
						Rock rock = new Rock();
						objectMap.put(rock, hex);
					}
					if (worldObject.getType().equals("food")) {
						int c = worldObject.getCol();
						int r = worldObject.getRow();
						Hex hex = new Hex(c, r);
						int calories = worldObject.getCalories();
						Food food = new Food(calories);
						objectMap.put(food, hex);
					}
					if (worldObject.getType().equals("critter")) {
						int c = worldObject.getCol();
						int r = worldObject.getRow();
						Hex hex = new Hex(c, r);
						String programString = worldObject.getProgram();
						Program program = null;
						if (programString != null) {
							ParserImpl parser = new ParserImpl();
							InputStream stream = new ByteArrayInputStream(
									worldObject.getProgram().getBytes(StandardCharsets.UTF_8.name()));
							BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
							program = parser.parse(reader);
							reader.close();
						}
						int[] mem = worldObject.getMemory();
						String speciesName = worldObject.getSpeciesName();
						int orientation = worldObject.getOrientation();
						SimpleCritter critter = new Critter(program, mem, speciesName, orientation);
						critterMap.put(critter, hex);
						synchronized (this.hexToCritterMap) {
							this.hexToCritterMap.put(hex, critter);
						}
					}
				}
			}
			Set<Entry<SimpleCritter, Hex>> critterSet = critterMap.entrySet();
			Set<Entry<WorldObject, Hex>> objectSet = objectMap.entrySet();
			if (critterSet.size() > 0) {
				map.drawCritters(critterSet);
			}
			if (objectSet.size() > 0) {
				map.drawObjects(objectSet);
			}
			crittersAlive.setText("Critters Alive: " + worldState.getPopulation());
			stepsTaken.setText("Time: " + worldState.getCurrentTime());
		}

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

	private void setGUIReadyServer(boolean isReady) {
		chkRandom.setDisable(!isReady);
		chkSpecify.setDisable(!isReady);
		stepForward.setDisable(!isReady);
		run.setDisable(isReady);
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
		setGUIReadyServer(true);
		if (handler.createNewWorld(sessionId.getSessionID())) {
			map = new WorldMap(c, handler, sessionId.getSessionID());
			map.draw();
		} else {
			map.draw();
			return;
		}

	}

	@FXML
	private void handleLoadWorldPressed(MouseEvent me) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose World File");
		if (devMode) {
			File initDirectory = new File("./src/test/resources/simulationtests/worlds");
			fc.setInitialDirectory(initDirectory);
		}
		File worldFile = fc.showOpenDialog(new Popup());
		if (worldFile == null) {
			return;
		}
		doReset();
		if (localMode)
			loadWorld(worldFile);
		else
			loadServerWorld(worldFile);
	}

	private void loadServerWorld(File worldFile) {
		try {
			setGUIReadyServer(true);
			if (handler.loadWorld(worldFile, sessionId.getSessionID())) {
				map = new WorldMap(c, handler, sessionId.getSessionID());
				map.draw();
			} else {
				map.draw();
				return;
			}
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
	private void handleCheckRandom(MouseEvent me) {
		numCritters.setText(Integer.toString(1));
	}

	@FXML
	private void handleLoadCritters(MouseEvent me) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose Critter File");
		File critterFile = fc.showOpenDialog(new Popup());
		if (critterFile == null)
			return;

		ToggleButton choice = (ToggleButton) LoadChoice.getSelectedToggle();
		if (choice == chkRandom) {
			try {
				int n = Integer.parseInt(numCritters.getText());
				if (localMode)
					model.loadRandomCritters(critterFile, n);
				else
					handler.loadRandomCritters(critterFile, n, sessionId.getSessionID());

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
					if (localMode)
						model.loadCritterAtLocation(critterFile, c, r);
					else
						try {
							handler.loadCritterAtLocation(critterFile, c, r, sessionId.getSessionID());
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
		if (localMode) {
			model.advanceTime();
			updateInfoBox();
			map.draw();
			crittersAlive.setText("Critters Alive: " + model.getNumCritters());
			stepsTaken.setText("Time: " + model.getCurrentTimeStep());
		} else {
			if (this.simulationRate == 0)
				handler.advanceTime(sessionId.getSessionID());
		}
	}

	@FXML
	private void handleRunPressed(MouseEvent me) {
		if (localMode) {
			if (simulationRate == 0)
				return;

			Thread worldUpdateThread = new Thread(new Runnable() {
				@Override
				public void run() {
					model.advanceTime();
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

			newWorld.setDisable(true);
			loadWorld.setDisable(true);
			loadCritterFile.setDisable(true);
			chkRandom.setDisable(true);
			chkSpecify.setDisable(true);
			numCritters.setDisable(true);
			stepForward.setDisable(true);
			run.setDisable(true);
			simulationSpeed.setDisable(true);

			pause.setDisable(false);
		}
	}

	@FXML
	private void handlePauseClicked(MouseEvent me) {
		executor.shutdownNow();

		newWorld.setDisable(false);
		loadWorld.setDisable(false);
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
			if (map != null)
				map.select(xCoordinateSelected, yCoordinateSelected);
			updateInfoBox();
		}
		isCurrentlyDragging = false;
	}

	/** Updates the contents of the critter information box. */
	private void updateInfoBox() {
			memSizeText.setText("");
			speciesText.setText("");
			defenseText.setText("");
			offenseText.setText("");
			sizeText.setText("");
			energyText.setText("");
			passText.setText("");
			tagText.setText("");
			postureText.setText("");
			if (localMode) {
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
					}
				} else {
					columnText.setText("");
					rowText.setText("");
				}
			} else {
				synchronized (this.hexToCritterMap) {
				int[] hexCoordinatesSelected = map.getSelectedHex();
				if (hexCoordinatesSelected != null) {
					columnText.setText(String.valueOf(hexCoordinatesSelected[0]));
					rowText.setText(String.valueOf(hexCoordinatesSelected[1]));
					Hex hex = new Hex(hexCoordinatesSelected[0], hexCoordinatesSelected[1]);
					Set<Entry<Hex, SimpleCritter>> entryCritterSet = this.hexToCritterMap.entrySet();
					for (Entry entry : entryCritterSet) {
						if (entry.getKey().equals(hex)) {
							SimpleCritter critter = (SimpleCritter) entry.getValue();
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
							break;
						}

					}
				}
			}
		}
	}

	@FXML
	private void handleDisplayProgram(MouseEvent me) {
		if (localMode) {
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
		} else {
			int[] hexCoordinatesSelected = map.getSelectedHex();
			if (hexCoordinatesSelected != null) {
				Hex hex = new Hex(hexCoordinatesSelected[0], hexCoordinatesSelected[1]);
				Set<Entry<Hex, SimpleCritter>> entryCritterSet = this.hexToCritterMap.entrySet();
				for (Entry entry : entryCritterSet) {
					if (entry.getKey().equals(hex)) {
						SimpleCritter critter = (SimpleCritter) entry.getValue();
						if (critter.getProgram() != null) {
							String critterProgramString = critter.getProgram().toString();
							Alert alert = new Alert(AlertType.INFORMATION, critterProgramString);
							alert.setHeaderText("Critter Program");
							alert.showAndWait();
						}
						break;
					}
				}
			}
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
			if (map != null)
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
		TextField urlTextField = new TextField("http://localhost:8080");
		// TextField urlTextField = new
		// TextField("http://hexworld.herokuapp.com:80/hexworld");
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
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
					localMode = true;
					model = new WorldModel();
					return;
				} else {
					System.exit(0);
				}
			}

			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String sessionIdString = "";
			String holder = r.readLine();
			while (holder != null) {
				sessionIdString += holder;
				holder = r.readLine();
			}
			sessionId = gson.fromJson(sessionIdString, SessionID.class);

		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
			localMode = true;
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
			localMode = true;
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
		localMode = false;
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