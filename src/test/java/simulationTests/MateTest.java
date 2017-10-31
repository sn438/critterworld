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
		
	}
	
	@Test
	public void SimpleTest() {
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
}
