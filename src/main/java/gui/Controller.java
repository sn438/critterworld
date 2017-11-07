package gui;

import java.io.File;

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
	private double xCoordinateSelected;
	private double yCoordinateSelected;

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
		c.setVisible(true); // TODO why was this false originally??????????????
		map = new WorldMap(c, model, c.getHeight(), c.getWidth());

		timeline = new Timeline(new KeyFrame(Duration.millis(100), new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent ae) {
				map.draw();
			}
		}));
		timeline.setCycleCount(Timeline.INDEFINITE);
		timeline.play();

		c.setOnScroll(new EventHandler<ScrollEvent>() {

			@Override
			public void handle(ScrollEvent event) {
				if (event.getDeltaY() > 0)
					map.zoom(true);
				else
					map.zoom(false);
			}
		});
		c.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!event.isPrimaryButtonDown()) {
					mousePanPressedX = event.getScreenX();
					mousePanPressedY = event.getScreenY();
				} else {
					xCoordinateSelected = event.getSceneX();
					yCoordinateSelected = event.getSceneY();
					map.select(xCoordinateSelected, yCoordinateSelected);

				}
			}
		});

		c.setOnDragDetected(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				if (!event.isPrimaryButtonDown()) {
					map.drag(event.getScreenX() - mousePanPressedX, event.getScreenY() - mousePanPressedY);

				}
			}
		});
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
		c.setVisible(true);
	}

	@FXML
	private void handleLoadWorldPressed(MouseEvent ae) {
		FileChooser fc = new FileChooser();
		fc.setTitle("Choose World File");
		File worldFile = fc.showOpenDialog(new Popup());
		model.loadWorld(worldFile);
		loadCritterFile.setDisable(false);
		chkRand.setDisable(false);
		chkSpecify.setDisable(false);
		numCritters.setDisable(false);
		stepForward.setDisable(false);
		run.setDisable(false);
		reset.setDisable(false);
		simulationSpeed.setDisable(false);
		c.setVisible(true);
	}
}