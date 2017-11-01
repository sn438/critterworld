package simulationTests;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class TurnTests {

	Console console1 = null;
	Console console2 = null;
	Console console3 = null;
	Console console4 = null;

	@Before
	public void setUp() {
		console1 = new Console();
		console1.loadWorld("TurnWorldTwo.txt");
		console2 = new Console();
		console2.loadWorld("TurnNearbyWorld.txt");

	}

	/**
	 * simpleTurn checks to see if a critter can turn right and left properly
	 */
	
	/*
	@Test
	public void simpleTurn() {
		System.out.println("simpleTurn");
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
	*/
	/**
	 * turnIfFood tests to see if a critter can turn if there is food right next to
	 * it.
	 */
	
	@Test
	public void turnIfFood() {
		System.out.println("turnIfFood");
		console2.worldInfo();
		console2.advanceTime(1);
		console2.worldInfo();
	}
}
