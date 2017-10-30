package simulationTests;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class BudTest {

	@Before
	public void setup() {
		Console console = new Console();
		console.loadWorld("world.txt");
	}
	
	@Test
	public void testOne() {
		Console console = new Console();
		console.loadWorld("SpiralCritterWorld.txt");
		//console.loadCritters("example-critter2.txt", 1);
		console.advanceTime(3);
		console.worldInfo();
	}
}
