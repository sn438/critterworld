package simulationTests;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class MateTest {
	Console console1 = null;
	Console console2 = null;
	Console console3 = null;
	Console console4 = null;
	
	@Before
	public void setUp()
	{
		console1 = new Console();
		console1.loadWorld("MateWorld.txt");
		console2 = new Console();
		console2.loadWorld("MovingWorldFileRock.txt");
		console3 = new Console();
		console3.loadWorld("MovingWorldThree.txt");
		console4 = new Console();
		console4.loadWorld("MovingWorldFour.txt");
		
	}
	
	@Test
	public void SimpleTest() {
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
}
