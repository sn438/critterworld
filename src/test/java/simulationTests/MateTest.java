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
	
	
	@Test
	public void simpleTest() {
		System.out.println("simpleTest");
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
	}
	
	
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
	
	/**
	 * mateLittleEnergy checks to see what happens to the parents when they try to mate when they
	 * do not have enough energy. The parents then die. 
	 */
	@Test
	public void mateLittleEnergy() {
		System.out.println("mateLittleEnergy");
		console3.worldInfo();
		console3.advanceTime(1);
		console3.worldInfo();
	}
	
	
	
	/**
	 * mateDifferentTimeStep checks to see what happens when two parents want to mate, but they
	 * want to mate on different time steps. While one of the parents wants to mate, the other one waits
	 * and vice versa. As a result, the parents should not be able to mate. During this test, wait is also
	 * tested in this test.
	 */
	@Test
	public void mateDifferentTimeStep() {
		System.out.println("mateDifferentTimeStep");
		console4.worldInfo();
		console4.advanceTime(17);
		console4.worldInfo();
	}
	
}
