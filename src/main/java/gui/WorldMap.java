package gui;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;

public class WorldMap {
	private WorldModel model;
	private GraphicsContext gc;
	private Canvas canvas;
	private double height;
	private double width;
	private int columns;
	private int rows;
	private int sideLength;
	// TODO have sujith tell us what position markers are
	// x_position and y_position are just used as the left markers from which the rest of the canvas
	// is drawn, it is only used during drawing of the map
	private double x_position_marker;
	private double y_position_marker;
	private double origin_x;
	private double origin_y;
	
	private HashMap<String, Image> pictures;
	// distance between hex centers is sideLength * sqrt(3)

	/**
	 * Creates a new world map.
	 * @param can The Canvas to draw on
	 * @param wm The WorldModel to work off of
	 */
	public WorldMap(Canvas can, WorldModel wm) {
		gc = can.getGraphicsContext2D();
		canvas = can;
		model = wm;
		height = canvas.getHeight();
		width = canvas.getWidth();
		
		columns = 17;//wm.getColumns();
		rows = 19;//wm.getRows();
		rows -= columns / 2;
		sideLength = 30;
		
		x_position_marker = ((double) width / 2) - ((((double) columns / 2) / 2) * 3 * sideLength) + (sideLength / 2);
		y_position_marker = (((double) height / 2) - (((double) rows / 2) * (Math.sqrt(3) * (sideLength))))
				+ (Math.sqrt(3) * (sideLength / 2));
		
		initializeImages();
	}
	
	private void initializeImages()
	{
		pictures = new HashMap<String, Image>();
		InputStream is1 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_0.png");
		InputStream is2 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_60.png");
		InputStream is3 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_120.png");
		InputStream is4 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_180.png");
		InputStream is5 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_240.png");
		InputStream is6 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_300.png");
		InputStream is7 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/rock.png");
		InputStream is8 = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/apple.png");
		//TODO add general critter image
	
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
	
	private void drawWorldObject(int r, int c)
	{
//		InputStream in = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_0.png");
//		Image image = null;
//		try
//		{
//			in = new FileInputStream("src/main/resources/gui/images/critter_0.png");
//			image = new Image(in);
//		}
//		catch (FileNotFoundException e)
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		Image image = pictures.get("FOOD");
		
		int hexCoordinates[] = new int[] {r, c};
		double cartX = hexToCartesian(hexCoordinates)[0];
		double cartY = hexToCartesian(hexCoordinates)[1];

		gc.drawImage(image, cartX - (sideLength / 2), cartY - ((sideLength * Math.sqrt(3))),  sideLength, sideLength * Math.sqrt(3));
	}

	public void draw() {
		height = canvas.getHeight();
		width = canvas.getWidth();
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
		drawWorldObject(0, 0);
		double [] cooridnates = hexToCartesian(new int[] {0, 1});
		highlightHex(cooridnates[0], cooridnates[1]); //TODO remove eventually because just for
		// testing i think?
	}

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
		for (int i = 0; i < 6; i++) {
			yPoints[i] -= m;
		}

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

	/**
	 * A method that converts 
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