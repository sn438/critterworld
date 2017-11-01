package simulationTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class MovingTest
{
	Console console1 = null;
	Console console2 = null;
	Console console3 = null;
	Console console4 = null;
	
	@Before
	public void setUp()
	{
		console1 = new Console();
		console1.loadWorld("src/test/resources/simulationTests/MovingWorld.txt");
		console2 = new Console();
		console2.loadWorld("src/test/resources/simulationTests/MovingWorldFileRock.txt");
		console3 = new Console();
		console3.loadWorld("src/test/resources/simulationTests/MovingWorldThree.txt");
		console4 = new Console();
		console4.loadWorld("src/test/resources/simulationTests/MovingWorldFour.txt");
		
	}
	/**
	 * testNormalMove tests to see if moving forward normally works.
	 */
	@Test
	public void testNormalMove()
	{
		System.out.println("testNormalMove");
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
	
	/**
	 * testMoveWithRock tests to see that critter won't move when there is a rock in front of it.
	 */
	@Test
	public void testMoveWithRock() {
		System.out.println("testMoveWithRock");
		console2.worldInfo();
		console2.advanceTime(1);
		console2.worldInfo();
	}
	
	/**
	 * testMovingWithNoEnergy tests to see that a critter without enough energy to move dies.
	 */
	@Test
	public void testMovingWithNoEnergy() {
		System.out.println("testMovingWithNoEnergy");
		console3.worldInfo();
		console3.advanceTime(1);
		console3.worldInfo();
	}

	/**
	 * testMoveInvalidLocation checks to see if a critter does not move to an invalid location
	 */
	@Test
	public void testMovingInvalidLocation() {
		System.out.println("testMovingInvalidLocation");
		console4.worldInfo();
		console4.advanceTime(1);
		console4.worldInfo();
	}
}
