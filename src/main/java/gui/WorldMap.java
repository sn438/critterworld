package gui;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class WorldMap


{
	private GraphicsContext gc;
	private double height;
	private double width;
	
	public WorldMap(Canvas canvas, double height, double width) {
		gc = canvas.getGraphicsContext2D();
		this.height = height;
		this.width = width;
	}
	
	public void draw() {
		int column = 6;
		int row = 10;
		row -= column / 2;
		double a = 30;
		double x_position = ((double) width / 2) - ((((double) column / 2) / 2) * 3 * a) + (a / 2);
		double y_position = (((double) height / 2) - (((double) row / 2)*(Math.sqrt(3) * (a))))  + (Math.sqrt(3) * (a / 2));
		for (int i = 0; i < column; i++) {
			if (i % 2 == 0) {
				y_position += Math.sqrt(3) * (a / 2);
			}
			if (i % 2 == 0 && column % 2 == 1) {
				y_position = (Math.sqrt(3) * (a / 2));
			}
			if (i % 2 == 1 && column % 2 == 1) {
				y_position += Math.sqrt(3) * (a / 2);
				row--;
			}

			for (int j = 0; j < row; j++) {
				gc.strokePolygon(
						new double[] { x_position + a, x_position + (a / 2), x_position - (a / 2), x_position - a,
								x_position - (a / 2), x_position + (a / 2) },
						new double[] { y_position, y_position - (Math.sqrt(3) * (a / 2)),
								y_position - (Math.sqrt(3) * (a / 2)), y_position,
								y_position + (Math.sqrt(3) * (a / 2)), y_position + (Math.sqrt(3) * (a / 2)) },
						6);
				y_position += (Math.sqrt(3) * (a));
			}

			x_position += a + (a / 2);
			y_position = (((double) height / 2) - (((double) row / 2)*(Math.sqrt(3) * (a))))  + (Math.sqrt(3) * (a / 2));;
			if (i % 2 == 1 && column % 2 == 1) {
				row++;
			}
		}
	}
}
