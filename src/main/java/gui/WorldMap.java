package gui;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import simulation.Food;
import simulation.Hex;
import simulation.Rock;
import simulation.SimpleCritter;
import simulation.WorldObject;

public class WorldMap {
	private WorldModel model;
	private GraphicsContext gc;
	private Canvas canvas;
	private int[] selectedHex;

	/** The minimum acceptable hex sidelength (zoom will not allow the user to zoom in any further. */
	private final int MIN_SIDELENGTH = 30;
	/** The maximum acceptable hex sidelength (zoom will not allow the user to zoom out any further. */
	private final int MAX_SIDELENGTH = 60;
	/** How much each scroll tick zooms the hex grid by. */
	private final double ZOOM_FACTOR = 3.0;

	private double height;
	private double width;
	private int columns;
	private int rows;

	private int worldRows;
	private int worldColumns;

	/** The sideLength of a hexagon. Used as a measure of scale. */
	private int sideLength;
	// TODO have sujith tell us what position markers are
	// x_position and y_position are just used as the left markers from which the
	// rest of the canvas
	// is drawn, it is only used during drawing of the map
	private double x_position_marker;
	private double y_position_marker;

	/** Marks the rectangular x coordinate of the origin (the (0, 0) hex coordinate). */
	private double origin_x;
	/** Marks the rectangular y coordinate of the origin (the (0, 0) hex coordinate). */
	private double origin_y;

	private HashMap<String, Image> pictures;
	// distance between hex centers is sideLength * sqrt(3)

	/**
	 * Creates a new world map.
	 * 
	 * @param can The Canvas to draw on
	 * @param wm The WorldModel to work off of
	 */
	public WorldMap(Canvas can, WorldModel wm) {
		gc = can.getGraphicsContext2D();
		canvas = can;
		model = wm;
		height = canvas.getHeight();
		width = canvas.getWidth();

		worldColumns = wm.getColumns();
		worldRows = wm.getRows();

		columns = worldColumns;
		rows = worldRows;
		rows -= columns / 2;
		sideLength = 30;

		x_position_marker = ((double) width / 2) - ((((double) columns / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) rows / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));

		initializeImages();
	}

	/** Reads the images needed to display world objects and stores them in a hashmap. */
	private void initializeImages() {
		pictures = new HashMap<String, Image>();
		InputStream is1 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_0.png");
		InputStream is2 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_60.png");
		InputStream is3 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_120.png");
		InputStream is4 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_180.png");
		InputStream is5 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_240.png");
		InputStream is6 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_300.png");
		InputStream is7 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/rock.png");
		InputStream is8 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/apple.png");
		// TODO add general critter image

		Image i1 = new Image(is1);
		Image i2 = new Image(is2);
		Image i3 = new Image(is3);
		Image i4 = new Image(is4);
		Image i5 = new Image(is5);
		Image i6 = new Image(is6);
		Image i7 = new Image(is7);
		Image i8 = new Image(is8);

		pictures.put("CRITTER_NORTH", i1);
		pictures.put("CRITTER_NORTHEAST", i2);
		pictures.put("CRITTER_SOUTHEAST", i3);
		pictures.put("CRITTER_SOUTH", i4);
		pictures.put("CRITTER_SOUTHWEST", i5);
		pictures.put("CRITTER_NORTHWEST", i6);
		pictures.put("ROCK", i7);
		pictures.put("FOOD", i8);
	}

	/**
	 * Determines whether or not a hex with column index {@code c} and row index
	 * {@code r} is on the world grid.
	 */
	private boolean isValidHex(int c, int r) {
		if (c < 0 || r < 0)
			return false;
		else if (c >= worldColumns || r >= worldRows)
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * worldRows - worldColumns))
			return false;
		return true;
	}

	/** Redraws the world grid. */
	public void draw() {
		height = canvas.getHeight();
		width = canvas.getWidth();

		gc.clearRect(0, 0, width, height);
		double hexMarkerX = x_position_marker;
		double hexMarkerY = y_position_marker;
		for (int i = 0; i < columns; i++) {
			if (i % 2 == 0 && columns % 2 == 0) {
				hexMarkerY += Math.sqrt(3) * (sideLength / 2);
			}
			if (i % 2 == 1 && columns % 2 == 1) {
				hexMarkerY += Math.sqrt(3) * (sideLength / 2);
				rows--;
			}

			for (int j = 0; j < rows; j++) {
				drawHex(hexMarkerX, hexMarkerY);
				hexMarkerY += (Math.sqrt(3) * (sideLength));
			}

			hexMarkerX += sideLength + (sideLength / 2);
			hexMarkerY = y_position_marker;
			if (i % 2 == 1 && columns % 2 == 1) {
				rows++;
			}
		}
		hexMarkerX = x_position_marker;
		origin_x = hexMarkerX;
		origin_y = hexMarkerY + (sideLength * (Math.sqrt(3)) * rows) - (Math.sqrt(3) * (sideLength / 2));
		if (columns % 2 == 0)
			origin_y += (sideLength / 2) * (Math.sqrt(3));
		drawObjects();
	}

	/** Used to update the grid and draw updates after each time step. */
	public void updateGrid() {

	}

	/** Draws the world objects onto the grid. */
	private void drawObjects() {
		for (Map.Entry<SimpleCritter, Hex> entry : model.getCritterMap()) {
			int c = entry.getValue().getColumnIndex();
			int r = entry.getValue().getRowIndex();
			drawCritter(entry.getKey(), c, r);
		}

		for (Map.Entry<WorldObject, Hex> entry : model.getObjectMap()) {
			int c = entry.getValue().getColumnIndex();
			int r = entry.getValue().getRowIndex();
			drawWorldObject(entry.getKey(), c, r);
		}
	}

	/**
	 * Draws one critter onto the world grid.
	 * 
	 * @param sc
	 * @param c
	 * @param r
	 */
	private void drawCritter(SimpleCritter sc, int c, int r) {
		if (!isValidHex(c, r))
			return;

		int hexCoordinates[] = new int[] { c, r };
		double cartX = hexToCartesian(hexCoordinates)[0];
		double cartY = hexToCartesian(hexCoordinates)[1];

		if (sc == null)
			return;
		int size = sc.size();
		int dir = sc.getOrientation();
		String imageKey;
		switch (dir) {
		case 0:
			imageKey = "CRITTER_NORTH";
			break;
		case 1:
			imageKey = "CRITTER_NORTHEAST";
			break;
		case 2:
			imageKey = "CRITTER_SOUTHEAST";
			break;
		case 3:
			imageKey = "CRITTER_SOUTH";
			break;
		case 4:
			imageKey = "CRITTER_SOUTHWEST";
			break;
		case 5:
			imageKey = "CRITTER_NORTHWEST";
			break;
		default:
			return;
		}
		Image critter = pictures.get(imageKey);
		gc.drawImage(critter, cartX - (sideLength / 2), cartY - ((sideLength * Math.sqrt(3))), sideLength,
				sideLength * Math.sqrt(3));
	}

	/**
	 * Draws one non-critter object onto the world grid.
	 * 
	 * @param wo
	 * @param c
	 * @param r
	 */
	private void drawWorldObject(WorldObject wo, int c, int r) {
		if (!isValidHex(c, r))
			return;

		int hexCoordinates[] = new int[] { c, r };
		double cartX = hexToCartesian(hexCoordinates)[0];
		double cartY = hexToCartesian(hexCoordinates)[1];

		Image obj = null;
		if (wo instanceof Rock)
			obj = pictures.get("ROCK");
		else if (wo instanceof Food) {
			int calories = ((Food) wo).getCalories();
			obj = pictures.get("FOOD");
		}

		gc.drawImage(obj, cartX - (sideLength / 2), cartY - ((sideLength * Math.sqrt(3))), sideLength,
				sideLength * Math.sqrt(3));
	}

	/**
	 * 
	 * @param centerX
	 * @param centerY
	 */
	private void drawHex(double centerX, double centerY) {
		gc.strokePolygon(
				new double[] { centerX + sideLength, centerX + (sideLength / 2), centerX - (sideLength / 2),
						centerX - sideLength, centerX - (sideLength / 2), centerX + (sideLength / 2) },
				new double[] { centerY, centerY - (Math.sqrt(3) * (sideLength / 2)),
						centerY - (Math.sqrt(3) * (sideLength / 2)), centerY,
						centerY + (Math.sqrt(3) * (sideLength / 2)), centerY + (Math.sqrt(3) * (sideLength / 2)) },
				6);
	}

	/**
	 * 
	 * @param zoomIn
	 */
	public void zoom(boolean zoomIn) {
		if (zoomIn) {
			sideLength += ZOOM_FACTOR;
			if (sideLength >= MAX_SIDELENGTH)
				sideLength = MAX_SIDELENGTH;
		} else {
			sideLength -= ZOOM_FACTOR;
			if (sideLength <= MIN_SIDELENGTH)
				sideLength = MIN_SIDELENGTH;
		}
		x_position_marker = ((double) width / 2) - ((((double) columns / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) rows / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
		draw();

	}

	/**
	 * 
	 * @param x
	 * @param y
	 * @param c
	 */
	public void highlightHex(double x, double y, Color c) {
		double a = (double) sideLength; // for visual clarity in the calculations
		double m = a * Math.sqrt(3) / 2.0; // for visual clarity in the calculations

		double[] xPoints = { x + a, x + a / 2, x - a / 2, x - a, x - a / 2, x + a / 2 };
		double[] yPoints = { y, y - m, y - m, y, y + m, y + m };

		// this for loop somehow fixes off by one errors
		for (int i = 0; i < 6; i++) {
			yPoints[i] -= m;
		}

		gc.setFill(c);
		gc.fillPolygon(xPoints, yPoints, 6);
	}

	public void drag(double deltaX, double deltaY) {
		x_position_marker += deltaX * 0.05;
		y_position_marker += deltaY * 0.05;
		if (y_position_marker < 0)
			y_position_marker = Math.sqrt(3) * (sideLength / 2);
		if (x_position_marker - sideLength < 0)
			x_position_marker = sideLength;
		gc.clearRect(0, 0, width, height);
		draw();
	}

	public void select(double xCoordinate, double yCoordinate, TableView hexContent, TableView critterContent) {
		int[] closestHexCoordinates = closestHex(xCoordinate, yCoordinate);
		double[] highlightCoordinates = null;
		if (selectedHex != null && (!selectedHex.equals(closestHexCoordinates))) {
			highlightCoordinates = hexToCartesian(selectedHex);
			highlightHex(highlightCoordinates[0], highlightCoordinates[1], Color.WHITE);
			selectedHex = closestHexCoordinates;
		} else {
			selectedHex = closestHexCoordinates;
		}
		highlightCoordinates = hexToCartesian(closestHexCoordinates);
		highlightHex(highlightCoordinates[0], highlightCoordinates[1], Color.POWDERBLUE);
		// hexContent = new TableView<Hex>();
		critterContent.getItems().clear();
		ObservableList<Hex> data = FXCollections
				.observableArrayList(new Hex(closestHexCoordinates[0], closestHexCoordinates[1]));
		critterContent.setItems(data);
		hexContent.disabledProperty();
	}

	/**
	 * A method that, given a set of rectangular canvas coordinates, returns the
	 * coordinates of the hex it is located in.
	 * 
	 * @param xCoordinate
	 * @param yCoordinate
	 * @return An {@code int} array containing the (r, c) coordinates of the closest
	 *         hex.
	 */
	private int[] closestHex(double xCoordinate, double yCoordinate) {
		// determines the possible hexes that the point could be in
		int possibleColumnOne = (int) Math.ceil(2.0 * (xCoordinate - origin_x) / (3.0 * sideLength));
		int possibleColumnTwo = (int) Math.floor(2.0 * (xCoordinate - origin_x) / (3.0 * sideLength));
		int possibleRowOne = (int) Math.ceil((-yCoordinate + origin_y) / (Math.sqrt(3.0) * sideLength)
				+ ((xCoordinate - origin_x) / (3.0 * sideLength)));
		int possibleRowTwo = (int) Math.floor((-yCoordinate + origin_y) / (Math.sqrt(3.0) * sideLength)
				+ ((xCoordinate - origin_x) / (3.0 * sideLength)));

		int[][] possibleCoordinates = new int[4][2];
		possibleCoordinates[0] = new int[] { possibleColumnOne, possibleRowOne };
		possibleCoordinates[1] = new int[] { possibleColumnOne, possibleRowTwo };
		possibleCoordinates[2] = new int[] { possibleColumnTwo, possibleRowOne };
		possibleCoordinates[3] = new int[] { possibleColumnTwo, possibleRowTwo };

		double distanceSquared = Integer.MAX_VALUE;
		int returnIndex = 0;
		for (int i = 0; i < 4; i++) {
			System.out.println("Option #" + i + ": " + possibleCoordinates[i][0] + " " + possibleCoordinates[i][1]);
			double tempArray[] = hexToCartesian(possibleCoordinates[i]);
			double tempDistanceSquared = Math.pow(xCoordinate - tempArray[0], 2)
					+ Math.pow(yCoordinate - tempArray[1], 2);
			if (tempDistanceSquared < distanceSquared) {
				distanceSquared = tempDistanceSquared;
				returnIndex = i;
			}
		}
		System.out.println(possibleCoordinates[returnIndex][0] + " " + possibleCoordinates[returnIndex][1]);
		System.out.println("\n");
		return possibleCoordinates[returnIndex];
	}

	/**
	 * A method that converts a hex coordinate pair and gives the hex coordinates of
	 * 
	 * @param hexCoordinates
	 * @return
	 */
	private double[] hexToCartesian(int[] hexCoordinates) {
		double x_coordinate = ((3 * sideLength) / 2) * hexCoordinates[0] + origin_x;
		double y_coordinate = ((Math.sqrt(3) * sideLength) / 2) * hexCoordinates[0]
				- sideLength * Math.sqrt(3) * hexCoordinates[1] + origin_y;
		return new double[] { x_coordinate, y_coordinate };
	}
}