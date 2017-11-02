package simulationtests;

import org.junit.Test;

import console.Console;

public class SpiralCritterTest
{

	@Test
	/** 
	 * There isn't really any world functionality that allows us to perform automated testing for the spiral critter in any
	 * practical way, so we just used this simple test and analyzed the successive printouts of the world grid after each
	 * successive turn.
	 */
	public void test()
	{
		Console c = new Console();
		c.loadWorld("src/test/resources/simulationTests/SpiralCritterWorld.txt");
		c.worldInfo();
		
		for(int i = 0; i < 60; i++)
		{
			c.advanceTime(1);
			c.worldInfo();
		}
	}

}
