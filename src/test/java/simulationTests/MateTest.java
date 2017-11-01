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
		console2.loadWorld("MateWorldRock.txt");
		console3 = new Console();
		console3.loadWorld("MateWorldLittleEnergy.txt");
		console4 = new Console();
		console4.loadWorld("MateWorldDifferentTimeSteps.txt");
	}
	
	/**
	 * simpleTest tests to see if two critters who want to mate can mate and create an offspring.
	 */
	
	/*
	@Test
	public void simpleTest() {
		System.out.println("simpleTest");
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
	*/
	
	/**
	 * mateWithRock tests to see what happens when a couple of critters tries to mate but with a rock behind one of
	 * them. The mating works if the baby is put behind the critter that is not in front of the rock, and the baby dies
	 * if it is placed on the rock. 
	 */
	
	@Test
	public void mateWithRock() {
		System.out.println("mateWithRock");
		console2.worldInfo();
		console2.advanceTime(1);
		console2.worldInfo();
	}
	
	
	/*
	@Test
	public void MateLittleEnergy() {
		System.out.println("mateLittleEnergy");
		console3.worldInfo();
		console3.advanceTime(1);
		console3.worldInfo();
	}
	
	
	
	
	@Test
	public void mateDifferentTimeStep() {
		System.out.println("mateDifferentTimeStep");
		console4.worldInfo();
		console4.advanceTime(17);
		console4.worldInfo();
	}
	*/
}
