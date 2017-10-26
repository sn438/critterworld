package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import ast.Program;
import console.FileParser;
import parse.Parser;
import parse.ParserFactory;

/** A class to simulate the world state. */
public class World implements SimpleWorld
{
	/** Contains the hex grid of the world. */
	private Hex[][] grid;
	/** Maps each critter to a location in the world */
	private HashMap<Critter, Hex> critterLocations;
	/** Stores all the critters present in the world, in the order in which they were created. */
	private LinkedList<Critter> critterList;
	/** The number of columns in the world grid. */
	private int columns;
	/** The number of rows in the world grid. */
	private int rows;
	
	private HashMap<String, Double> CONSTANTS;
	
	/** Loads a world based on a world description file. */
	public World(String filename) throws FileNotFoundException, IllegalArgumentException
	{
		parseConstants();
		BufferedReader bf = new BufferedReader(new FileReader(filename));
		
		
	}
	
	/** Generates a default size world containing nothing but randomly placed rocks. */
	public World() throws FileNotFoundException, IllegalArgumentException
	{
		parseConstants();
		
		columns = CONSTANTS.get("COLUMNS").intValue();
		rows = CONSTANTS.get("ROWS").intValue();
		
		grid = new Hex[columns][rows];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				grid[i][j] = new Hex(i, j);
	}

	/** Parses the constants file in the project directory and stores the constants in the CONSTANTS field. */
	private void parseConstants() throws FileNotFoundException, IllegalArgumentException
	{
		BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/constants.txt"));
		CONSTANTS = FileParser.parseConstants(bf);
	}
	
	/** Checks if a coordinate pair is within the bounds of this world grid. */
	public boolean isValidHex(int c, int r)
	{
		if(c < 0 || r < 0)
			return false;
		if((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}
	
	@Override
	public int getMinMemory()
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public int getMaxRules()
	{
		//TODO implement
		return 0;
	}
	
	public void loadCritter(Program prog, int[] mem, String name, int c, int r)
	{
		if(!isValidHex(c, r))
			return;
		//TODO implement
	}
	
	@Override
	public void loadCritters(String filename, int n)
	{
		// TODO Auto-generated method stub
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			String[] parsed = FileParser.parseAttributes(br);
			String name = parsed[0].equals("") ? "Untitled" : parsed[0];
			int[] critMem = FileParser.makeCritterMemory(parsed, minMemory);
			
			Parser p = ParserFactory.getParser();
			Program prog = p.parse(br);
			
			for(int i = 0; i < n; i++)
			{
				int c = (int) (Math.random() * columns);
				int r = (int) (Math.random() * rows);
				while(!isValidHex(c, r))
				{
					c = (int) (Math.random() * columns);
					r = (int) (Math.random() * rows);
				}
				loadCritter(prog, critMem, name, c, r);
			}
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Critter file not found.");
			return;
		}
	}
	
	/** Advances the world state by a single time step. */
	public void advanceTimeStep()
	{
		
	}
	
	@Override
	public int searchNearby(SimpleCritter c, int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int searchAhead(SimpleCritter c, int index)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean moveCritter(SimpleCritter c, boolean forward)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void printGrid()
	{
		
	}
}