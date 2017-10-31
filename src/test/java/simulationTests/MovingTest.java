package simulationTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class MovingTest
{
	Console console = null;
	@Before
	public void setUp()
	{
		console = new Console();
		console.loadWorld("MovingWorld.txt");
	}
	
	@Test
	public void test()
	{
		console.worldInfo();
		console.advanceTime(1);
		console.worldInfo();
	}

}
