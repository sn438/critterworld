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
		column = 50;
		row = 100;
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
		origin_y = y_position+ (sideLength*(Math.sqrt(3))*row) - (Math.sqrt(3) * (sideLength / 2));
		if (column%2 == 0)
			origin_y += (sideLength/2)*(Math.sqrt(3));
		highlightOrigin();
	}

	public void zoom(boolean zoomIn) {
		if (zoomIn) {
			sideLength += 5;
			if (sideLength >= 70)
				sideLength = 70;
		}
		else {
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
	
	public void highlightOrigin() {
		
		double fill_x = origin_x;
		double fill_y = origin_y -  (Math.sqrt(3) * (sideLength / 2));
		
		gc.fillPolygon(
				new double[] { fill_x + sideLength, fill_x + (sideLength / 2),
						fill_x - (sideLength / 2), fill_x - sideLength, fill_x - (sideLength / 2),
						fill_x + (sideLength / 2) },
				new double[] { fill_y, fill_y - (Math.sqrt(3) * (sideLength / 2)),
						fill_y - (Math.sqrt(3) * (sideLength / 2)), fill_y,
						fill_y + (Math.sqrt(3) * (sideLength / 2)),
						fill_y + (Math.sqrt(3) * (sideLength / 2)) },
				6);
				
	}
	
	public void drag(double deltaX, double deltaY) {
		x_position_marker += deltaX*15;
		y_position_marker += deltaY*15;
		gc.clearRect(0, 0, width, height);
		draw();
	}
}


