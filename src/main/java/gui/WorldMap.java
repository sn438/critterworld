package gui;

import javafx.scene.canvas.Canvas;

public class WorldMap
{
	private Canvas canvas;
	private WorldModel world;
	
	public WorldMap(Canvas c, WorldModel w)
	{
		canvas = c;
		world = w;
	}
}