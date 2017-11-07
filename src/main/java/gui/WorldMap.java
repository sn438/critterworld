package gui;

import javafx.scene.canvas.Canvas;

import javafx.scene.canvas.GraphicsContext;

public class WorldMap

{

	private GraphicsContext gc;
	private double height;
	private double width;
	private int column;
	private int row;
	private int sideLength;
	private double x_position;
	private double y_position;
	private double x_position_marker;
	private double y_position_marker;
	private double origin_x;
	private double origin_y;

	public WorldMap(Canvas canvas, double height, double width) {
		gc = canvas.getGraphicsContext2D();
		this.height = height;
		this.width = width;
		column = 6;
		row = 10;
		row -= column / 2;
		sideLength = 30;
		x_position_marker = ((double) width / 2) - ((((double) column / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) row / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
	}

	public void draw() {
		x_position = x_position_marker;
		y_position = y_position_marker;
		for (int i = 0; i < column; i++) {

			if (i % 2 == 0 && column % 2 == 0) {
				y_position += Math.sqrt(3) * (sideLength / 2);
			}
			if (i % 2 == 1 && column % 2 == 1) {
				y_position += Math.sqrt(3) * (sideLength / 2);
				row--;
			}

			for (int j = 0; j < row; j++) {
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
			if (i % 2 == 1 && column % 2 == 1) {
				row++;
			}
		}
		x_position = x_position_marker;
		origin_x = x_position;
		origin_y = y_position + (sideLength * (Math.sqrt(3)) * row) - (Math.sqrt(3) * (sideLength / 2));
		if (column % 2 == 0)
			origin_y += (sideLength / 2) * (Math.sqrt(3));
		highlightHex(origin_x, origin_y);
	}

	public void zoom(boolean zoomIn) {
		if (zoomIn) {
			sideLength += 5;
			if (sideLength >= 70)
				sideLength = 70;
		} else {
			sideLength -= 5;
			if (sideLength <= 10)
				sideLength = 10;
		}
		x_position_marker = ((double) width / 2) - ((((double) column / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) row / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
		gc.clearRect(0, 0, width, height);
		draw();

	}

	public void highlightHex(double xCoordinate, double yCoordinate) {

		double fill_x = xCoordinate;
		double fill_y = yCoordinate - (Math.sqrt(3) * (sideLength / 2));

		gc.fillPolygon(
				new double[] { fill_x + sideLength, fill_x + (sideLength / 2), fill_x - (sideLength / 2),
						fill_x - sideLength, fill_x - (sideLength / 2), fill_x + (sideLength / 2) },
				new double[] { fill_y, fill_y - (Math.sqrt(3) * (sideLength / 2)),
						fill_y - (Math.sqrt(3) * (sideLength / 2)), fill_y, fill_y + (Math.sqrt(3) * (sideLength / 2)),
						fill_y + (Math.sqrt(3) * (sideLength / 2)) },
				6);

	}

	public void drag(double deltaX, double deltaY) {
		x_position_marker += deltaX * 15;
		y_position_marker += deltaY * 15;
		gc.clearRect(0, 0, width, height);
		draw();
	}
	
	public void select(double xCoordinate, double yCoordinate) {
		int[] closestHexCoordinates = closestHex(xCoordinate, yCoordinate);
		double[] highlightCoordinates = hexToCartesian(closestHexCoordinates);
		highlightHex(highlightCoordinates[0], highlightCoordinates[1]);
 		
	}

	private int[] closestHex(double xCoordinate, double yCoordinate) {
		int possibleColumnOne = (int) Math.ceil(((2) * (xCoordinate - origin_x)) / (3 * sideLength));
		int possibleColumnTwo = (int) Math.floor(((2) * (xCoordinate - origin_x)) / (3 * sideLength));
		int possibleRowOne = (int) Math
				.ceil(((Math.sqrt(3) * (-yCoordinate + origin_y) + (xCoordinate - origin_x)) / (3 * sideLength)));
		int possibleRowTwo = (int) Math
				.floor(((Math.sqrt(3) * (-yCoordinate + origin_y) + (xCoordinate - origin_x)) / (3 * sideLength)));

		int[][] possibleCoordinates = new int[4][2];
		possibleCoordinates[0] = new int[] { possibleColumnOne, possibleRowOne };
		possibleCoordinates[1] = new int[] { possibleColumnOne, possibleRowTwo };
		possibleCoordinates[2] = new int[] { possibleColumnTwo, possibleRowOne };
		possibleCoordinates[3] = new int[] { possibleColumnTwo, possibleRowTwo };
		int counter = 0;
		double distanceSquared = Integer.MAX_VALUE;
		int returnIndex = 0;
		while (counter < 4) {
			System.out.println(possibleCoordinates[counter][0] + " " + possibleCoordinates[counter][1]);
			double tempArray[] = hexToCartesian(possibleCoordinates[counter]);
			double distanceSquare = Math.pow(xCoordinate - tempArray[0], 2) + Math.pow(yCoordinate - tempArray[1], 2);
			System.out.println(distanceSquare);
			if (distanceSquare < distanceSquared) {
				distanceSquared = distanceSquare;
				returnIndex = counter;
			}
			counter++;
		}
		System.out.println(possibleCoordinates[returnIndex][0]+ " " +  possibleCoordinates[returnIndex][1]);
		//System.out.println(possibleCoordinates[counter][0] + " " +  possibleCoordinates[counter][1]);
		return possibleCoordinates[returnIndex];
	}
	
	private double[] hexToCartesian(int[] hexCoordinates) {
		double x_coordinate = ((3*sideLength)/2)*hexCoordinates[0] + origin_x;
		double y_cooridnate = (((-Math.sqrt(3))*sideLength)/2)*hexCoordinates[0] + sideLength*Math.sqrt(3)*hexCoordinates[1] + origin_y;
		return new double[] {x_coordinate, y_cooridnate};
	}
}
