package simulationTests;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class TurnAndNearbyTests {

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
<<<<<<< HEAD:src/test/java/simulationTests/TurnTests.java
=======
	
	
>>>>>>> 41fe28e826698ca5cf3e31913bdf54842644b50c:src/test/java/simulationTests/TurnAndNearbyTests.java
	@Test
	public void simpleTurn() {
		System.out.println("simpleTurn");
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
	
<<<<<<< HEAD:src/test/java/simulationTests/TurnTests.java
	
	/**
	 * turnIfFood tests to see if a critter can turn if there is food right next to it.
=======
	/**
	 * turnIfFood tests to see if a critter can turn if there is food right next to it. Nearby was
	 * also tested by placing the food at different points around the critter and having the critter turn if
	 * it can sense it. As a result, this test covers both functionalities. 
>>>>>>> 41fe28e826698ca5cf3e31913bdf54842644b50c:src/test/java/simulationTests/TurnAndNearbyTests.java
	 */
	
	@Test
	public void turnIfFood() {
		System.out.println("turnIfFood");
		console2.worldInfo();
		console2.advanceTime(1);
		console2.worldInfo();
	}
}
