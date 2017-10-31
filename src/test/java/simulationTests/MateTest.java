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
	/*
	@Test
	public void SimpleTest() {
		System.out.println("SimpleTest");
		console1.worldInfo();
		console1.advanceTime(2);
		console1.worldInfo();
	}
	*/
	
	@Test
	public void MateWithRock() {
		System.out.println("Mating Around Rocks");
		console2.worldInfo();
		console2.advanceTime(2);
		console2.worldInfo();
	}
	
	
	/*
	@Test
	public void MateLittleEnergy() {
		System.out.println("MateWithLittleEnergy");
		console3.worldInfo();
		console3.advanceTime(1);
		console3.worldInfo();
	}
	
	
	
	
	@Test
	public void MateDifferentTimeStep() {
		System.out.println("MateWithLittleEnergy");
		console4.worldInfo();
		console4.advanceTime(17);
		console4.worldInfo();
	}
	*/
	
	
}
