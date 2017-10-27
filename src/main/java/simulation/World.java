package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;

import interpret.Interpreter;
import interpret.InterpreterImpl;

/** A class to simulate the world state. */
public class World implements SimpleWorld
{
	/** The name of this world. */
	private String worldname;
	/** Contains the hex grid of the world. */
	private Hex[][] grid;
	/** Maps each critter to a location in the world */
	private HashMap<SimpleCritter, Hex> critterMap;
	/** Stores all the critters present in the world, in the order in which they were created. */
	private LinkedList<SimpleCritter> critterList;
	/** The number of columns in the world grid. */
	private int columns;
	/** The number of rows in the world grid. */
	private int rows;
	/** The number of hexes that lie on the world grid. */
	private int numValidHexes;
	/** A compilation of all the constants needed for world creation. */
	private HashMap<String, Double> CONSTANTS;
	/** The number of time steps passed since this world's genesis. */
	private int timePassed;
	
	/** Loads a world based on a world description file. */
	public World(String filename) throws FileNotFoundException, IllegalArgumentException
	{
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		critterList = new LinkedList<SimpleCritter>();
		timePassed = 0;
		
		BufferedReader bf = new BufferedReader(new FileReader(filename));
		
		//parses the world name, and if no valid one is parsed, supplies a default one
		worldname = FileParser.parseAttributeFromLine(bf, "name ");
		if(worldname.equals(""))
			worldname = "Arrakis";
		
		//parses world dimensions, and supplies default ones if no valid dimensions are parsed
		try
		{
			String worldDimensions = FileParser.parseAttributeFromLine(bf, "size ");
			String[] dim = worldDimensions.split(" ");
			columns = Integer.parseInt(dim[0]);
			rows = Integer.parseInt(dim[1]);
				
			if(!(columns > 0 && rows > 0 && 2 * rows - columns > 0))
			{
				columns = CONSTANTS.get("COLUMNS").intValue();
				rows = CONSTANTS.get("ROWS").intValue();
			}
		}
		catch (Exception e)
		{
			columns = CONSTANTS.get("COLUMNS").intValue();
			rows = CONSTANTS.get("ROWS").intValue();
		}
		numValidHexes = 0;
		
		grid = new Hex[columns][rows];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
				if(isValidHex(i, j))
				{
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
				}
		
		try
		{
			String line = bf.readLine();
			while(line != null)
			{
				String[] info = line.split(" ");
				switch(info[0])
				{
					case "rock":
						addNonCritterObject(new Rock(), Integer.parseInt(info[1]), Integer.parseInt(info[2]));
						break;
					case "food":
						Food f = new Food(Integer.parseInt(info[3]));
						addNonCritterObject(f, Integer.parseInt(info[1]), Integer.parseInt(info[2]));
						break;
					case "critter":
						BufferedReader critterreader = new BufferedReader(new FileReader(info[1]));
						SimpleCritter sc = FileParser.parseCritter(critterreader, getMinMemory(), Integer.parseInt(info[4]));
						loadOneCritter(sc, Integer.parseInt(info[2]), Integer.parseInt(info[3]));
						break;
				}
				line = bf.readLine();
			}
		}
		catch (Exception e)
		{
			return;
		}
	}
	
	/** Generates a default size world containing nothing but randomly placed rocks. */
	public World() throws IllegalArgumentException
	{
		worldname = "Arrakis";
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		critterList = new LinkedList<SimpleCritter>();
		timePassed = 0;
		
		columns = CONSTANTS.get("COLUMNS").intValue();
		rows = CONSTANTS.get("ROWS").intValue();
		numValidHexes = 0;
		
		grid = new Hex[columns][rows];
		for(int i = 0; i < grid.length; i++)
			for(int j = 0; j < grid[0].length; j++)
			{
				if(isValidHex(i, j))
				{
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
				}
			}
		
		//randomly fills about 1/12 of the hexes in the world with rocks
		int c = (int) (Math.random() * columns);
		int r = (int) (Math.random() * rows);
		int n = 0;
		while(n < numValidHexes / 12)
		{
			c = (int) (Math.random() * columns);
			r = (int) (Math.random() * rows);
			if(isValidHex(c, r))
			{
				grid[c][r].addContent(new Rock());
				n++;
			}
		}
	}

	/** Parses the constants file in the project directory and stores the constants in the CONSTANTS field. */
	private void setConstants() throws IllegalArgumentException
	{
		/*InputStream in = World.class.getResourceAsStream("src/main/resources/constants.txt");
		if(in == null)
		{
			System.err.println("The constants.txt file could not be found in src/main/resources.");
			System.exit(0);
		}
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		CONSTANTS = FileParser.parseConstants(bf);*/
		
		try
		{
			BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/constants.txt"));
			CONSTANTS = FileParser.parseConstants(bf);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("The constants.txt file could not be found in src/main/resources.");
			System.exit(0);
		}
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
		return CONSTANTS.get("MIN_MEMORY").intValue();
	}
	
	@Override
	public int getMaxRules()
	{
		return CONSTANTS.get("MAX_RULES_PER_TURN").intValue();
	}
	
	@Override
	public void loadCritters(String filename, int n, int direction)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			SimpleCritter sc = FileParser.parseCritter(br, getMinMemory(), direction);
			
			for(int i = 0; i < n; i++)
			{
				
				int randc = (int) (Math.random() * columns);
				int randr = (int) (Math.random() * rows);
				while(!isValidHex(randc, randr))
				{
					randc = (int) (Math.random() * columns);
					randr = (int) (Math.random() * rows);
				}
				
				if(isValidHex(randc, randr))
					loadOneCritter(sc, randc, randr);
			}
		}
		catch (FileNotFoundException e)
		{
			System.err.println("Critter file not found.");
			return;
		}
	}
	
	/**
	 * Loads a single critter into the world at the specified coordinates, if possible. Does nothing if the hex is not within
	 * the world boundaries or if there is something already present at the hex.
	 * @param sc the Critter to add
	 * @param c the column index of the hex where the critter will be added
	 * @param r the row index of the hex where the critter will be added
	 */
	private void loadOneCritter(SimpleCritter sc, int c, int r)
	{
		if(!isValidHex(c, r))
			return;
		boolean added = grid[c][r].addContent(sc);
		if(added)
		{
			critterList.add(sc);
			critterMap.put(sc, grid[c][r]);	
		}
	}
	
	/**
	 * Loads a single non-critter world object into the world at the specified coordinates, if possible. Does nothing if
	 * the hex is not within the world boundaries or if there is something already present at the hex. This method cannot
	 * be used to add critters into the world. Use the method {@code loadCritter(SimpleCritter sc, int c, int r)} instead.
	 * @param sc the object to add
	 * @param c the column index of the hex where the object will be added
	 * @param r the row index of the hex where the object will be added
	 */
	private void addNonCritterObject(WorldObject wo, int c, int r)
	{
		if(wo instanceof Critter)
			return;
		if(!isValidHex(c, r))
			return;
		grid[c][r].addContent(wo);
	}
	
	@Override
	public void advanceOneTimeStep()
	{
		for(SimpleCritter sc : critterList)
		{
			Interpreter i = new InterpreterImpl(sc, this);
			i.simulateCritterTurn();
		}
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
	public int numRemainingCritters()
	{
		return critterList.size();
	}
	
	@Override
	public int getTimePassed()
	{
		return timePassed;
	}
	
	@Override
	public StringBuilder printGrid()
	{
		StringBuilder result = new StringBuilder("World name: " + worldname + "\n");
		for(int i = 0; i < columns; i++)
		{
			for(int j = 0; j < rows; j++)
			{
				if(grid[i][j] == null)
					result.append("% ");
				else
					result.append(grid[i][j].toString() + " ");
			}
			result.append("\n");
		}
		return result;
	}
}