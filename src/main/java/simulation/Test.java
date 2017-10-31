package simulation;

import java.io.FileNotFoundException;

import console.Console;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException
	{
		Console c = new Console();
		c.loadWorld("SpiralCritterWorld.txt");
		c.worldInfo();
		
		for(int i = 0; i < 30; i++)
		{
			c.advanceTime(1);
			//c.worldInfo();
		}
	}
}