package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class WorldMap {
	private WorldModel model;
	private GraphicsContext gc;
	private Canvas c;
	private double height;
	private double width;
	private int columns;
	private int rows;
	private int sideLength;
	//private double x_position;
	//private double y_position;
	// TODO have sujith tell us what position markers are
	private double x_position_marker;
	private double y_position_marker;
	
	private double origin_x;
	private double origin_y;
	// distance between hex centers is sideLength * sqrt(3)

	/**
	 * Creates a new world map.
	 * @param canvas
	 * @param wm
	 */
	public WorldMap(Canvas canvas, WorldModel wm) {
		gc = canvas.getGraphicsContext2D();
		c = canvas;
		model = wm;
		height = c.getHeight();
		width = c.getWidth();
		columns = 7;//wm.getColumns();
		rows = 9;//wm.getRows();
		sideLength = 30;
		
		double centerX = width / 2.0;
		double centerY = height / 2.0;
		
		double totalGridWidth = columns * (sideLength + sideLength * (.5 * Math.sqrt(3))) + sideLength * (.5 * Math.sqrt(3));
		double totalGridHeight = Math.ceil((2 * rows - columns) / 2.0) * sideLength * Math.sqrt(3);
		if(columns % 2 == 0)
			totalGridHeight += sideLength * (0.5 * Math.sqrt(3));
		
		//sets the origin to start drawing from
		origin_x = centerX - totalGridWidth / 2.0;
		origin_y = centerY + totalGridHeight / 2.0;
		
		x_position_marker = ((double) width / 2) - ((((double) columns / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) rows / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
	}

	public void refreshDimensions() {
		height = c.getHeight();
		width = c.getWidth();
	}

	public void draw() {
		/*x_position = x_position_marker;
		y_position = y_position_marker;
		for (int i = 0; i < columns; i++) {
			if (i % 2 == 0 && columns % 2 == 0) {
				y_position += Math.sqrt(3) * (sideLength / 2);
			}
			if (i % 2 == 1 && columns % 2 == 1) {
				y_position += Math.sqrt(3) * (sideLength / 2);
				rows--;
			}

			for (int j = 0; j < rows; j++) {
				gc.strokePolygon(
						new double[] { x_position + sideLength, x_position + (sideLength / 2),
								x_position - (sideLength / 2), x_position - sideLength, x_position - (sideLength / 2),
								x_position + (sideLength / 2) },
						new double[] { y_position, y_position - (Math.sqrt(3) * (sideLength / 2)),
								y_position - (Math.sqrt(3) * (sideLength / 2)), y_position,
								y_position + (Math.sqrt(3) * (sideLength / 2)),
								y_position + (Math.sqrt(3) * (sideLength / 2)) },
						6);
				y_position += (Math.sqrt(3) * (sideLength));
			}

			x_position += sideLength + (sideLength / 2);
			y_position = y_position_marker;
			if (i % 2 == 1 && columns % 2 == 1) {
				rows++;
			}
		}
		x_position = x_position_marker;
		origin_x = x_position;
		origin_y = y_position + (sideLength * (Math.sqrt(3)) * rows) - (Math.sqrt(3) * (sideLength / 2));
		if (columns % 2 == 0)
			origin_y += (sideLength / 2) * (Math.sqrt(3));
		// highlightHex(origin_x, origin_y); //TODO remove eventually because just for
		// testing i think?*/
		
		double hexCenterX = origin_x;
		double hexCenterY = origin_y;
		
		for (int i = 0; i < columns; i++)
		{
			if(i % 2 == 1)
				hexCenterY -= sideLength * (0.5 * Math.sqrt(3));
			
			int numHexesInColumn = (i % 2 == 0) ? (int) Math.ceil((2 * rows - columns) / 2.0) : (2 * rows - columns) / 2;
			for (int j = 0; j < numHexesInColumn; j++)
			{
				drawHex(hexCenterX, hexCenterY);
				hexCenterY -= sideLength * Math.sqrt(3);
			}
			hexCenterX += sideLength * 1.5;
			hexCenterY = origin_y;
		}
	}

	/**
	 * Draws a hexagon with the given center coordinates.
	 * @param centerX
	 * @param centerY
	 */
	private void drawHex(double centerX, double centerY)
	{
		gc.strokePolygon(
				new double[] { centerX + sideLength, centerX + (sideLength / 2),
						centerX - (sideLength / 2), centerX - sideLength, centerX - (sideLength / 2),
						centerX + (sideLength / 2) },
				new double[] { centerY, centerY - (Math.sqrt(3) * (sideLength / 2)),
						centerY - (Math.sqrt(3) * (sideLength / 2)), centerY,
						centerY + (Math.sqrt(3) * (sideLength / 2)),
						centerY + (Math.sqrt(3) * (sideLength / 2)) }, 6);
	}
	
	private boolean isValidHex(int col, int row)
	{
		if (col < 0 || row < 0)
			return false;
		else if (col >= columns || row >= rows)
			return false;
		else if ((2 * row - col) < 0 || (2 * row - col) >= (2 * rows - columns))
			return false;
		return true;
	}
	
	public void zoom(boolean zoomIn) {
		if (zoomIn) {
			sideLength += 3;
			if (sideLength >= 70)
				sideLength = 70;
		} else {
			sideLength -= 3;
			if (sideLength <= 10)
				sideLength = 10;
		}
		x_position_marker = ((double) width / 2) - ((((double) columns / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) rows / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
		gc.clearRect(0, 0, width, height);
		draw();

	}

	public void highlightHex(double x, double y) {
		double a = (double) sideLength; // for visual clarity in the calculations
		double m = a * Math.sqrt(3) / 2.0; // for visual clarity in the calculations

		double[] xPoints = { x + a, x + a / 2, x - a / 2, x - a, x - a / 2, x + a / 2 };
		double[] yPoints = { y, y - m, y - m, y, y + m, y + m };
		
		// this for loop somehow fixes off by one errors
		/*for (int i = 0; i < 6; i++) {
			yPoints[i] -= m;
		}*/

		gc.setFill(Color.HOTPINK);
		gc.fillPolygon(xPoints, yPoints, 6);
	}

	public void drag(double deltaX, double deltaY) {
		x_position_marker += deltaX * 0.05;
		y_position_marker += deltaY * 0.05;
		gc.clearRect(0, 0, width, height);
		draw();
	}

	public void select(double xCoordinate, double yCoordinate) {
		int[] closestHexCoordinates = closestHex(xCoordinate, yCoordinate);
		double[] highlightCoordinates = hexToCartesian(closestHexCoordinates);
		highlightHex(highlightCoordinates[0], highlightCoordinates[1]);

	}

	private int[] closestHex(double xCoordinate, double yCoordinate) {
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

	private double[] hexToCartesian(int[] hexCoordinates) {
		double x_coordinate = ((3 * sideLength) / 2) * hexCoordinates[0] + origin_x;
		double y_coordinate = ((Math.sqrt(3) * sideLength) / 2) * hexCoordinates[0]
				- sideLength * Math.sqrt(3) * hexCoordinates[1] + origin_y;
		return new double[] { x_coordinate, y_coordinate };
	}

}