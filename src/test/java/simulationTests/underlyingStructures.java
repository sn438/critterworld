package simulationTests;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

import ast.Program;
import ast.ProgramImpl;
import console.Console;
import simulation.Critter;
import simulation.FileParser;
import simulation.SimpleCritter;
import simulation.World;

public class underlyingStructures {

	@Test	
	public void setUp() throws FileNotFoundException {
		BufferedReader reader = new BufferedReader((new FileReader("example-critter.txt")));
		SimpleCritter baby = FileParser.parseCritter(reader, 8, -1);
	}
}
