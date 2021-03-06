package console;

import java.io.*;
import java.util.Scanner;

import simulation.SimpleCritter;
import simulation.SimpleWorld;
import simulation.World;

/** The console user interface for Assignment 5. */
public class Console
{
	private Scanner scan;
	public boolean done;
	public PrintStream out;

	/* =========================== */
	/* DO NOT EDIT ABOVE THIS LINE */
	/* (except imports...) */
	/* =========================== */

	// TODO world representation...
	private SimpleWorld world;

	/** Starts new random world simulation. */
	public void newWorld()
	{
		try
		{
			world = new World();
		}
		catch (UnsupportedOperationException i)
		{
			System.err.println("The constants.txt file could not be read. Please check if it exists or is formatted properly.");
		}
	}

	/**
	 * Starts new simulation with world specified in filename.
	 * @param filename
	 */
	public void loadWorld(String worlddesc)
	{
		try
		{
			world = new World(worlddesc);
		}
		catch (IllegalArgumentException f)
		{
			System.err.println("World description invalid. Loading defaultly generated world...");
			world = new World();
		}
		catch (UnsupportedOperationException i)
		{
			System.err.println("The constants.txt file could not be read. Please check if it is formatted properly.");
			System.exit(0);
		}
	}

	/**
	 * Loads critter definition from {@code filename} and randomly places n critters with that definition into the world.
	 * If the file cannot be found, this method prints an error message and loads 0 critters. If the file contains invalid
	 * attributes, critters are supplied with default memory values. A full list of default critter memory values can be
	 * found in our overview document.
	 *
	 * @param filename
	 * @param n
	 */
	public void loadCritters(String filename, int n)
	{
		//This method can't do anything if no world has been created yet.
		if(world == null)
		{
			System.err.println("You must first create a world before you load critters into it.");
			printHelp();
			return;
		}
		
		world.loadCritters(filename, n, -1);
	}

	/**
	 * Advances the world by n time steps.
	 * @param n
	 */
	public void advanceTime(int n)
	{
		//This method can't do anything if no world has been created yet.
		if(world == null)
		{
			System.err.println("You must first create a world before you advance time in it.\n");
			printHelp();
			return;
		}
		for(int i = 0; i < n; i++)
			world.advanceOneTimeStep();
	}

	/** Prints current time step, number of critters, and world map of the simulation. */
	public void worldInfo()
	{
		//This method can't do anything if no world has been created yet.
		if(world == null)
		{
			System.err.println("You must first create a world before you can view its information\n");
			printHelp();
			return;
		}
		
		out.println(world.printGrid().toString());
		worldInfo(world.getTimePassed(), crittersAlive());
	}

	/** Returns the number of critters still alive. Useful for testing purposes. */
	public int crittersAlive()
	{
		if(world == null)
			return Integer.MIN_VALUE;
		return world.numRemainingCritters();
	}
	
	/**
	 * Prints description of the contents of hex (c,r).
	 *
	 * @param c column of hex
	 * @param r row of hex
	 */
	public void hexInfo(int c, int r)
	{
		//This method can't do anything if no world has been created yet.
		if(world == null)
		{
			System.err.println("You must first create a world before you can view its information.\n");
			printHelp();
			return;
		}
		
		if(!world.isValidHex(c, r))
		{
			System.err.println("Invalid world coordinates.");
			return;
		}
		
		SimpleCritter sc;
		int hexAnalysis = world.analyzeHex(c, r);
		if(hexAnalysis <= 0)
			terrainInfo(hexAnalysis);
		
		sc = world.analyzeCritter(c, r);
		if(sc == null)
			return;
		
		//critterInfo(sc.getName(), sc.getMemoryCopy(), sc.getProgram().toString(), sc.getLastRuleIndex());
	}

	/* =========================== */
	/* DO NOT EDIT BELOW THIS LINE */
	/* =========================== */

	/**
	 * Be sure to call this function, we will override it to grade.
	 *
	 * @param numSteps
	 *            The number of steps that have passed in the world.
	 * @param crittersAlive
	 *            The number of critters currently alive.
	 */
	protected void worldInfo(int numSteps, int crittersAlive)
	{
		out.println("steps: " + numSteps);
		out.println("critters: " + crittersAlive);
	}

	/**
	 * Be sure to call this function, we will override it to grade.
	 *
	 * @param species
	 *            The species of the critter.
	 * @param mem
	 *            The memory of the critter.
	 * @param program
	 *            The program of the critter pretty printed as a String. This should
	 *            be able to be parsed back to the same AST.
	 * @param lastrule
	 *            The last rule executed by the critter pretty printed as a String.
	 *            This should be able to be parsed back to the same AST. If no rule
	 *            has been executed, this parameter should be null.
	 */
	protected void critterInfo(String species, int[] mem, String program, String lastrule)
	{
		out.println("Species: " + species);
		StringBuilder sbmem = new StringBuilder();
		for (int i : mem)
		{
			sbmem.append(" ").append(i);
		}
		out.println("Memory:" + sbmem.toString());
		out.println("Program: " + program);
		out.println("Last rule: " + lastrule);
	}

	/**
	 * Be sure to call this function, we will override it to grade.
	 *
	 * @param terrain
	 *            0 is empty, -1 is rock, -X is (X-1) food
	 */
	protected void terrainInfo(int terrain)
	{
		if (terrain == 0)
			out.println("Empty");
		else if (terrain == -1)
			out.println("Rock");
		else
			out.println("Food: " + (-terrain - 1));
	}

	/** Prints a list of possible commands to the standard output. */
	public void printHelp()
	{
		out.println("new: start a new simulation with a random world");
		out.println("load <world_file>: start a new simulation with " + "the world loaded from world_file");
		out.println("critters <critter_file> <n>: add n critters " + "defined by critter_file randomly into the world");
		out.println("step <n>: advance the world by n timesteps");
		out.println("info: print current timestep, number of critters " + "living, and map of world");
		out.println("hex <c> <r>: print contents of hex " + "at column c, row r");
		out.println("exit: exit the program");
	}

	/** Constructs a new Console capable of reading a given input. */
	public Console(InputStream in, PrintStream out)
	{
		this.out = out;
		scan = new Scanner(in);
		done = false;
	}

	/** Constructs a new Console capable of reading the standard input. */
	public Console()
	{
		this(System.in, System.out);
	}

	/** Processes a single console command provided by the user. */
	public void handleCommand()
	{
		out.print("Enter a command or \"help\" for a list of commands.\n> ");
		String command = scan.next();
		switch (command)
		{
			case "new":
			{
				newWorld();
				break;
			}
			case "load":
			{
				String filename = scan.next();
				loadWorld(filename);
				break;
			}
			case "critters":
			{
				String filename = scan.next();
				int n = scan.nextInt();
				loadCritters(filename, n);
				break;
			}
			case "step":
			{
				int n = scan.nextInt();
				advanceTime(n);
				break;
			}
			case "info":
			{
				worldInfo();
				break;
			}
			case "hex":
			{
				int c = scan.nextInt();
				int r = scan.nextInt();
				hexInfo(c, r);
				break;
			}
			case "help":
			{
				printHelp();
				break;
			}
			case "exit":
			{
				done = true;
				break;
			}
			default:
				out.println(command + " is not a valid command.");
		}
	}

	public static void main(String[] args)
	{
		Console console = new Console();
		while (!console.done)
		{
			console.handleCommand();
		}
	}

}
