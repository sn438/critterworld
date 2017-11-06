package gui;
import java.io.FileInputStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.shape.Polygon;
import javafx.stage.*;

public class hexes extends Application {

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		int width = 400;
		int height = 600;
		primaryStage.setTitle("Drawing Operations Test");
		Group root = new Group();
		Canvas canvas = new Canvas(width, height);
		GraphicsContext gc = canvas.getGraphicsContext2D();

		int column = 6;
		int row = 10;
		row -= column / 2;
		double a = 30;
		double x_position = ((double) width / 2) - ((((double) column / 2) / 2) * 3 * a) + (a / 2);
		double y_position = (((double) height / 2) - (((double) row / 2)*(Math.sqrt(3) * (a))))  + (Math.sqrt(3) * (a / 2));
		System.out.println(y_position);
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
		gc.strokePolygon(new double[] { 0, 400, 400, 0 }, new double[] { 0, 0, 600, 600 }, 4);
		gc.strokePolygon(new double[] { -50, 0, 0, -50 }, new double[] { 0, 0, 50, 50 }, 4);
		root.getChildren().add(canvas);
		primaryStage.setScene(new Scene(root));
		primaryStage.show();

		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				canvas.setScaleX(1.5);
				canvas.setScaleY(1.5);
				System.out.println(event.getSceneX());

			}
		});
	}

}
