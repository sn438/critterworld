package simulationTests;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import console.Console;

public class BudTest {

	Console console1 = null;
	Console console2 = null;
	Console console3 = null;

	@Before
	public void setup() {
		console1 = new Console();
		console1.loadWorld("BudWorld.txt");
		console2 = new Console();
		console2.loadWorld("BudWorldRock.txt");
		console3 = new Console();
		console3.loadWorld("BudWorld3.txt");
	}

	/**
	 * testBasicBud checks to see if a critter can bud under normal circumstances.
	 */
	@Test
	public void testBasicBud() {
		System.out.println("testBasicBud");
		int initialNumCritters = console1.crittersAlive();
		console1.worldInfo();
		console1.advanceTime(1);
		console1.worldInfo();
		assertEquals(initialNumCritters + 1, console1.crittersAlive());
	}

	/**
	 * testBudWithRock checks to see that a critter does not bud when there is a
	 * rock behind it.
	 */
	@Test
	public void testBudWithRock() {
		System.out.println("testBudWithRock");
		int initialNumCritters = console2.crittersAlive();
		console2.worldInfo();
		console2.advanceTime(1);
		console2.worldInfo();
		assertEquals(initialNumCritters, console2.crittersAlive());
	}

	/**
	 * testBudNoEnergy checks to see if a critter will die when it tries to bud with no energy.
	 * It also tests to see if critter death properly adds a food object onto the site of death.
	 */
	@Test
	public void testBudNoEnergy() {
		System.out.println("testBudNoEnergy");
		int initialNumCritters = console3.crittersAlive();
		console3.worldInfo();
		console3.advanceTime(1);
		console3.worldInfo();
		assertEquals(initialNumCritters - 1, console3.crittersAlive());
	}

}
