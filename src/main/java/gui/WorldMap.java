package gui;

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import distributed.ClientRequestHandler;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import simulation.Food;
import simulation.Hex;
import simulation.Rock;
import simulation.SimpleCritter;
import simulation.WorldObject;

/** A class that draws the hex grid for the user interface. */
public class WorldMap {
	private WorldModel model;
	private GraphicsContext gc;
	private Canvas canvas;
	private int[] selectedHex;

	/**
	 * The minimum acceptable hex sidelength (zoom will not allow the user to zoom
	 * in any further.
	 */
	private final int MIN_SIDELENGTH = 20;

	/**
	 * The maximum acceptable hex sidelength (zoom will not allow the user to zoom
	 * out any further.
	 */
	private final int MAX_SIDELENGTH = 60;

	/** How much each scroll tick zooms the hex grid by. */
	private final double ZOOM_FACTOR = 3.0;

	/** The background color of the canvas. */
	private final Color BACKGROUND_COLOR = Color.DIMGRAY;
	/** The outline color of hexagons. */
	private final Color HEX_COLOR = Color.LIGHTGRAY;
	/** The color of a highlighted hex. */
	private final Color HIGHLIGHT_COLOR = Color.rgb(176, 224, 230, 0.3);

	private double height;
	private double width;
	private int rows;
	private int columns;

	/** The sideLength of a hexagon. Used as a measure of scale. */
	private double sideLength;

	// these four variables are used to help drawing methods
	private int column_drawing_marker;
	private int row_drawing_marker;
	private double x_position_marker;
	private double y_position_marker;

	/**
	 * Marks the rectangular x coordinate of the origin (the (0, 0) hex coordinate).
	 */
	private double origin_x;

	/**
	 * Marks the rectangular y coordinate of the origin (the (0, 0) hex coordinate).
	 */
	private double origin_y;
	private boolean localMode;
	/** Holds the last drawn time step. */
	// private int currentTimeStep;
	// private boolean localMode;
	// private ClientRequestHandler handler;

	/**
	 * Creates a new world map.
	 *
	 * @param can
	 *            The Canvas to draw on
	 * @param wm
	 *            The WorldModel to work off of
	 */
	public WorldMap(Canvas can, WorldModel wm) {
		localMode = true;
		gc = can.getGraphicsContext2D();
		canvas = can;
		model = wm;
		height = canvas.getHeight();
		width = canvas.getWidth();

		columns = wm.getColumns();
		rows = wm.getRows();

		column_drawing_marker = columns;
		row_drawing_marker = rows;
		row_drawing_marker -= column_drawing_marker / 2;
		sideLength = 30;

		x_position_marker = ((double) width / 2) - ((((double) column_drawing_marker / 2) / 2) * 3 * sideLength)
				+ (sideLength / 2);
		y_position_marker = (((double) height / 2)
				- (((double) row_drawing_marker / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
	}

	/**
	 *
	 * @param can
	 * @param handler
	 * @param sessionId
	 */
	public WorldMap(Canvas can, ClientRequestHandler handler, int sessionId) {
		this.localMode = false;
		gc = can.getGraphicsContext2D();
		canvas = can;
		// this.handler = handler;
		height = canvas.getHeight();
		width = canvas.getWidth();

		columns = handler.getColumns(sessionId);
		rows = handler.getRows(sessionId);

		column_drawing_marker = columns;
		row_drawing_marker = rows;
		row_drawing_marker -= column_drawing_marker / 2;
		sideLength = 30;

		x_position_marker = ((double) width / 2) - ((((double) column_drawing_marker / 2) / 2) * 3 * sideLength)
				+ (sideLength / 2);
		y_position_marker = (((double) height / 2)
				- (((double) row_drawing_marker / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));

	}

	/**
	 * Determines whether or not a hex with column index {@code c} and row index
	 * {@code r} is on the world grid.
	 */
	private boolean isValidHex(int c, int r) {
		if (c < 0 || r < 0)
			return false;
		else if (c >= columns || r >= rows)
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}

	/** Redraws the world grid. */
	public void draw() {
		// resets the world grid
		height = canvas.getHeight();
		width = canvas.getWidth();
		gc.clearRect(0, 0, width, height);
		gc.setFill(BACKGROUND_COLOR);
		gc.fillRect(0, 0, width, height);
		// draws grid and sets the origin
		gc.setLineWidth(1);
		double hexMarkerX = x_position_marker;
		double hexMarkerY = y_position_marker;
		for (int i = 0; i < column_drawing_marker; i++) {
			if (i % 2 == 0 && column_drawing_marker % 2 == 0) {
				hexMarkerY += Math.sqrt(3) * (sideLength / 2);
			}
			if (i % 2 == 1 && column_drawing_marker % 2 == 1) {
				hexMarkerY += Math.sqrt(3) * (sideLength / 2);
				row_drawing_marker--;
			}

			for (int j = 0; j < row_drawing_marker; j++) {
				drawHex(hexMarkerX, hexMarkerY);
				hexMarkerY += (Math.sqrt(3) * (sideLength));
			}

			hexMarkerX += sideLength + (sideLength / 2);
			hexMarkerY = y_position_marker;
			if (i % 2 == 1 && column_drawing_marker % 2 == 1) {
				row_drawing_marker++;
			}
		}
		hexMarkerX = x_position_marker;
		origin_x = hexMarkerX;
		origin_y = hexMarkerY + (sideLength * (Math.sqrt(3)) * row_drawing_marker) - (Math.sqrt(3) * (sideLength / 2));
		if (column_drawing_marker % 2 == 0)
			origin_y += (sideLength / 2) * (Math.sqrt(3));
		origin_y -= sideLength / 2 * Math.sqrt(3);

		// draws world objects
		gc.setLineWidth(3);
		if (localMode)
			drawObjects();

		if (selectedHex != null) {
			double[] highlightCoordinates = hexToCartesian(selectedHex);
			highlightHex(highlightCoordinates[0], highlightCoordinates[1]);
		}

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

	void drawCritters(Set<Map.Entry<SimpleCritter, Hex>> critterSet){
		for (Map.Entry<SimpleCritter, Hex> entry : critterSet) {
			int c = entry.getValue().getColumnIndex();
			int r = entry.getValue().getRowIndex();
			drawCritter(entry.getKey(), c, r);
		}
	}
	void drawObjects(Set<Map.Entry<WorldObject, Hex>> objectSet){
		for (Map.Entry<WorldObject, Hex> entry : objectSet) {
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

		// convert to Cartesian coordinates
		int hexCoordinates[] = new int[] { c, r };
		double cartX = hexToCartesian(hexCoordinates)[0];
		double cartY = hexToCartesian(hexCoordinates)[1];

		if (sc == null) {
			return;
		}

		int critterSize = sc.size();
		double size = 0.9 * sideLength * (50 + critterSize / 2) / 100;

		double[] xPoints = new double[3];
		double[] yPoints = new double[3];

		// determine critter orientation
		int dir = sc.getOrientation();
		switch (dir) {
		case 0:
			xPoints[0] = 0;
			xPoints[1] = -size / 2;
			xPoints[2] = size / 2;
			yPoints[0] = -size / 2 * Math.sqrt(3);
			yPoints[1] = size / 2 * Math.sqrt(3);
			yPoints[2] = size / 2 * Math.sqrt(3);
			break;
		case 1:
			xPoints[0] = 3 * size / 4;
			xPoints[1] = -size;
			xPoints[2] = -size / 2;
			yPoints[0] = -size * Math.sqrt(3) / 4;
			yPoints[1] = 0;
			yPoints[2] = size * Math.sqrt(3) / 2;
			break;
		case 2:
			xPoints[0] = 3 * size / 4;
			xPoints[1] = -size / 2;
			xPoints[2] = -size;
			yPoints[0] = size * Math.sqrt(3) / 4;
			yPoints[1] = -size * Math.sqrt(3) / 2;
			yPoints[2] = 0;
			break;
		case 3:
			xPoints[0] = 0;
			xPoints[1] = size / 2;
			xPoints[2] = -size / 2;
			yPoints[0] = size / 2 * Math.sqrt(3);
			yPoints[1] = -size / 2 * Math.sqrt(3);
			yPoints[2] = -size / 2 * Math.sqrt(3);
			break;
		case 4:
			xPoints[0] = -3 * size / 4;
			xPoints[1] = size;
			xPoints[2] = size / 2;
			yPoints[0] = size * Math.sqrt(3) / 4;
			yPoints[1] = 0;
			yPoints[2] = -size * Math.sqrt(3) / 2;
			break;
		case 5:
			xPoints[0] = -3 * size / 4;
			xPoints[1] = size / 2;
			xPoints[2] = size;
			yPoints[0] = -size * Math.sqrt(3) / 4;
			yPoints[1] = size * Math.sqrt(3) / 2;
			yPoints[2] = 0;
			break;
		default:
			return;
		}

		// translate points to current hex
		for (int i = 0; i < 3; i++) {
			xPoints[i] += cartX;
		}
		for (int i = 0; i < 3; i++) {
			yPoints[i] += cartY;
		}

		// set critter color
		String species = sc.getName();
		int randomizer = (int) (Math.sqrt(Math.abs(species.hashCode())) * 100);
		int blendType = randomizer % 3;
		double blendLevel = (randomizer % 100) / 100.0;
		double red;
		double green;
		double blue;
		switch (blendType) {
		case 0:
			red = 0;
			green = blendLevel;
			blue = 1 - blendLevel;
			break;
		case 1:
			red = 1 - blendLevel;
			green = 0;
			blue = blendLevel;
			break;
		case 2:
			red = blendLevel;
			green = 1 - blendLevel;
			blue = 0;
			break;
		default:
			red = 0;
			green = 0;
			blue = 0;
		}
		Color color = new Color(red, green, blue, 1);

		// draw critter (outlines only)
		gc.setStroke(color);
		gc.strokePolygon(xPoints, yPoints, 3);

		// draw critter (filled in)
		// gc.setFill(color);
		// gc.fillPolygon(xPoints, yPoints, 3);
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

		if (wo instanceof Rock) {
			double size = 0.9 * sideLength;
			gc.setStroke(Color.BROWN);// GOLDENROD
			gc.strokeRect(cartX - size / 2, cartY - size / 2, size, size);
		}

		else if (wo instanceof Food) {
			int calories = ((Food) wo).getCalories();
			double size = 0.9 * sideLength;
			gc.setStroke(Color.WHITE);// RED
			gc.strokeOval(cartX - size / 2, cartY - size / 2, size, size);
			gc.setTextAlign(TextAlignment.CENTER);
			gc.setFont(new Font(8 * sideLength / 30));
			gc.setLineWidth(1);
			gc.strokeText(String.valueOf(calories), cartX, cartY);
			gc.setLineWidth(3);
		}
	}

	/**
	 * Draws a hexagon centered at the rectangular coordinates specified by
	 * {@code centerX} and {@code centerY}.
	 */
	private void drawHex(double centerX, double centerY) {
		gc.setStroke(HEX_COLOR);
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
		double oldSideLength = sideLength;
		if (zoomIn) {
			sideLength += ZOOM_FACTOR;
			if (sideLength >= MAX_SIDELENGTH)
				sideLength = MAX_SIDELENGTH;

		} else {
			sideLength -= ZOOM_FACTOR;
			if (sideLength <= MIN_SIDELENGTH)
				sideLength = MIN_SIDELENGTH;
		}
		double factor = sideLength / (oldSideLength);

		x_position_marker = width / 2 - (width / 2 - x_position_marker) * factor;
		y_position_marker = height / 2 - (height / 2 - y_position_marker) * factor;

		draw();
	}

	/**
	 * highlightHex highlights the hex that is currently selected
	 *
	 * @param x
	 *            x-coordinate of the spot that the user clicks
	 * @param y
	 *            y-coordinate of the spot that the user clicks
	 */
	public void highlightHex(double x, double y) {
		int[] hexCoordinates = closestHex(x, y);
		if (!isValidHex(hexCoordinates[0], hexCoordinates[1])) {
			return;
		}
		double a = (double) sideLength; // for visual clarity in the calculations
		double m = a * Math.sqrt(3) / 2.0; // for visual clarity in the calculations

		double[] xPoints = { x + a, x + a / 2, x - a / 2, x - a, x - a / 2, x + a / 2 };
		double[] yPoints = { y, y - m, y - m, y, y + m, y + m };

		gc.setFill(HIGHLIGHT_COLOR);
		gc.fillPolygon(xPoints, yPoints, 6);
	}

	/**
	 * drag implements panning
	 *
	 * @param deltaX
	 * @param deltaY
	 */
	public void drag(double deltaX, double deltaY) {
		x_position_marker += deltaX * 0.05;
		y_position_marker += deltaY * 0.05;

		if (x_position_marker - sideLength > width)
			x_position_marker = width - sideLength;
		if ((((3 * sideLength) / 2) * column_drawing_marker + x_position_marker) < 0) {
			x_position_marker = (2 * sideLength) - (((3 * sideLength) / 2) * column_drawing_marker);
		}
		if (y_position_marker > height)
			y_position_marker = height - Math.sqrt(3) * sideLength;
		if ((y_position_marker + Math.sqrt(3) * sideLength * row_drawing_marker) < 0)
			y_position_marker = Math.sqrt(3) * sideLength - Math.sqrt(3) * sideLength * row_drawing_marker;

		gc.clearRect(0, 0, width, height);
		draw();
	}

	public boolean select(double xCoordinate, double yCoordinate) {
		boolean returnValue;
		int[] closestHexCoordinates = closestHex(xCoordinate, yCoordinate);
		if (selectedHex != null && Arrays.equals(selectedHex, closestHexCoordinates)) {
			selectedHex = null;
			returnValue = false;
		} else {
			selectedHex = closestHexCoordinates;
			returnValue = true;
		}
		double[] highlightCoordinates = hexToCartesian(closestHexCoordinates);
		highlightHex(highlightCoordinates[0], highlightCoordinates[1]);
		draw();
		return returnValue;
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
	public int[] closestHex(double xCoordinate, double yCoordinate) {
		// determines the possible hexes that the point could be in
		int possibleColumnOne = (int) Math.ceil(2.0 * (xCoordinate - origin_x) / (3.0 * sideLength));
		int possibleColumnTwo = (int) (2.0 * (xCoordinate - origin_x) / (3.0 * sideLength));
		int possibleRowOne = (int) Math.ceil((-yCoordinate + origin_y) / (Math.sqrt(3.0) * sideLength)
				+ ((xCoordinate - origin_x) / (3.0 * sideLength)));
		int possibleRowTwo = (int) ((-yCoordinate + origin_y) / (Math.sqrt(3.0) * sideLength)
				+ ((xCoordinate - origin_x) / (3.0 * sideLength)));

		int[][] possibleCoordinates = new int[4][2];
		possibleCoordinates[0] = new int[] { possibleColumnOne, possibleRowOne };
		possibleCoordinates[1] = new int[] { possibleColumnOne, possibleRowTwo };
		possibleCoordinates[2] = new int[] { possibleColumnTwo, possibleRowOne };
		possibleCoordinates[3] = new int[] { possibleColumnTwo, possibleRowTwo };

		double distanceSquared = Integer.MAX_VALUE;
		int returnIndex = 0;
		for (int i = 0; i < 4; i++) {
			double tempArray[] = hexToCartesian(possibleCoordinates[i]);
			double tempDistanceSquared = Math.pow(xCoordinate - tempArray[0], 2)
					+ Math.pow(yCoordinate - tempArray[1], 2);
			if (tempDistanceSquared < distanceSquared) {
				distanceSquared = tempDistanceSquared;
				returnIndex = i;
			}
		}
		return possibleCoordinates[returnIndex];
	}

	/**
	 * A method that converts a hex coordinate pair converts it to cartesian
	 * coordinates.
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

	public int[] getSelectedHex() {
		return selectedHex;
	}
}
