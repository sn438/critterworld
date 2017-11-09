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
	private double x_position;
	private double y_position;
	private double x_position_marker;
	private double y_position_marker;
	private double origin_x;
	private double origin_y;

	public WorldMap(Canvas canvas, WorldModel wm) {
		gc = canvas.getGraphicsContext2D();
		c = canvas;
		model = wm;
		height = c.getHeight();
		width = c.getWidth();
		columns = wm.getColumns();
		rows = wm.getRows();
		sideLength = 30;
		x_position_marker = ((double) width / 2) - ((((double) columns / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) rows / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
	}

	public void refreshDimensions()
	{
		height = c.getHeight();
		width = c.getWidth();
	}
	
	public void draw() {
		x_position = x_position_marker;
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
		// testing i think?
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

	public void highlightHex(double xCoordinate, double yCoordinate) {

		double fill_x = xCoordinate;
		double fill_y = yCoordinate - (Math.sqrt(3) * (sideLength / 2));

		gc.setFill(Color.SKYBLUE);
		gc.fillPolygon(
				new double[] { fill_x + sideLength, fill_x + (sideLength / 2), fill_x - (sideLength / 2),
						fill_x - sideLength, fill_x - (sideLength / 2), fill_x + (sideLength / 2) },
				new double[] { fill_y, fill_y - (Math.sqrt(3) * (sideLength / 2)),
						fill_y - (Math.sqrt(3) * (sideLength / 2)), fill_y, fill_y + (Math.sqrt(3) * (sideLength / 2)),
						fill_y + (Math.sqrt(3) * (sideLength / 2)) },
				6);

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
		int possibleColumnOne = (int) Math.ceil(2 * (xCoordinate - origin_x) / (3 * sideLength));
		int possibleColumnTwo = (int) Math.floor(2 * (xCoordinate - origin_x) / (3 * sideLength));
		int possibleRowOne = (int) Math.ceil((-yCoordinate + origin_y) / (Math.sqrt(3.0) * sideLength)
				+ ((xCoordinate - origin_x) / (3 * sideLength)));
		int possibleRowTwo = (int) Math.floor((-yCoordinate + origin_y) / (Math.sqrt(3.0) * sideLength)
				+ ((xCoordinate - origin_x) / (3 * sideLength)));

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
			double tempDistanceSquared = Math.pow(xCoordinate - tempArray[0], 2) + Math.pow(yCoordinate - tempArray[1], 2);
			if (tempDistanceSquared < distanceSquared) {
				distanceSquared = tempDistanceSquared;
				returnIndex = i;
			}
		}
		System.out.println(possibleCoordinates[returnIndex][0] + " " + possibleCoordinates[returnIndex][1]);
		System.out.println("\n");
		return possibleCoordinates[returnIndex];
		// TODO why does this give off by one errors on colum number when mouse is in right half of hex????
	}

	private double[] hexToCartesian(int[] hexCoordinates) {
		double x_coordinate = ((3 * sideLength) / 2) * hexCoordinates[0] + origin_x;
		double y_coordinate = (((-Math.sqrt(3)) * sideLength) / 2) * hexCoordinates[0]
				+ sideLength * Math.sqrt(3) * hexCoordinates[1] + origin_y;
		return new double[] { x_coordinate, y_coordinate };
	}

}