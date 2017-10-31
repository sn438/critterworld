package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import ast.Program;
import ast.ProgramImpl;
import ast.Rule;

/** A class to simulate the world state. */
public class World extends AbstractWorld
{
	/** The name of this world. */
	private String worldname;
	/** Contains the hex grid of the world. */
	private Hex[][] grid;
	/** Maps each critter to a location in the world */
	private HashMap<SimpleCritter, Hex> critterMap;
	/** The number of columns in the world grid. */
	private int columns;
	/** The number of rows in the world grid. */
	private int rows;
	/** The number of hexes that lie on the world grid. */
	private int numValidHexes;

	/**
	 * Loads a world based on a world description file.
	 * 
	 * @param filename The name of the file that contains world information.
	 * @throws FileNotFoundException if the world file could not be found
	 * 		   IllegalArgumentException if the world constants file could not be found or was improperly formatted
	 */
	public World(String filename) throws FileNotFoundException, IllegalArgumentException
	{
		// sets constants and initializes instance fields
		super();
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		super.critterList = new LinkedList<SimpleCritter>();
		super.timePassed = 0;

		BufferedReader bf = new BufferedReader(new FileReader(filename));

		// parses the world name, and if no valid one is parsed, supplies a default one
		worldname = FileParser.parseAttributeFromLine(bf, "name ");
		if (worldname.equals(""))
			worldname = "Arrakis";

		// parses world dimensions, and supplies default ones if no valid dimensions are
		// parsed
		try
		{
			String worldDimensions = FileParser.parseAttributeFromLine(bf, "size ");
			String[] dim = worldDimensions.split(" ");
			columns = Integer.parseInt(dim[0]);
			rows = Integer.parseInt(dim[1]);

			if (!(columns > 0 && rows > 0 && 2 * rows - columns > 0))
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

		// initializes world grid
		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (isValidHex(i, j))
				{
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
				}

		try
		{
			// loads in world objects from file
			String line = bf.readLine();
			while (line != null)
			{
				String[] info = line.split(" ");
				switch (info[0])
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
						SimpleCritter sc = FileParser.parseCritter(critterreader, getMinMemory(),
								Integer.parseInt(info[4]));
						if(sc == null)
							break;
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

	/**
	 * Generates a default size world containing nothing but randomly placed rocks.
	 * @throws IllegalArgumentException if the world constants file could not be found or was improperly formatted
	 */
	public World() throws IllegalArgumentException
	{
		// sets constants and initializes instance fields
		super();
		worldname = "Arrakis";
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		critterList = new LinkedList<SimpleCritter>();
		timePassed = 0;

		super.critterList = new LinkedList<SimpleCritter>();
		super.timePassed = 0;

		columns = CONSTANTS.get("COLUMNS").intValue();
		rows = CONSTANTS.get("ROWS").intValue();
		numValidHexes = 0;

		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
			{
				if (isValidHex(i, j))
				{
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
				}
			}

		// randomly fills about 1/40 of the hexes in the world with rocks
		int c = (int) (Math.random() * columns);
		int r = (int) (Math.random() * rows);
		int n = 0;
		while (n < numValidHexes / 40)
		{
			c = (int) (Math.random() * columns);
			r = (int) (Math.random() * rows);
			if (isValidHex(c, r))
			{
				grid[c][r].addContent(new Rock());
				n++;
			}
		}
	}

	/**
	 * Parses the constants file in the project directory and stores the constants in the CONSTANTS field.
	 * @throws IllegalArgumentException if the constants file couldn't be found or is improperly formatted
	 */
	private void setConstants() throws IllegalArgumentException
	{
		InputStream in = World.class.getResourceAsStream("constants.txt");
		if(in == null)
		{ 
			System.err.println("The constants.txt file could not be found in bin/simulation.");
			System.exit(0);
		}
		
		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		CONSTANTS = FileParser.parseConstants(bf);

		/*try
		{
			BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/constants.txt"));
			CONSTANTS = FileParser.parseConstants(bf);
		}
		catch (FileNotFoundException e)
		{
			System.err.println("The constants.txt file could not be found in src/main/resources.");
			throw new IllegalArgumentException();
		}*/
	}

	@Override
	public boolean isValidHex(int c, int r)
	{
		if (c < 0 || r < 0)
			return false;
		else if (c >= columns || r >= rows)
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}

	@Override
	public void loadCritters(String filename, int n, int direction)
	{
		try
		{
			BufferedReader br = new BufferedReader(new FileReader(filename));
			SimpleCritter sc = FileParser.parseCritter(br, getMinMemory(), direction);

			for (int i = 0; i < n; i++)
			{

				int randc = (int) (Math.random() * columns);
				int randr = (int) (Math.random() * rows);
				while (!isValidHex(randc, randr))
				{
					randc = (int) (Math.random() * columns);
					randr = (int) (Math.random() * rows);
				}

				if (isValidHex(randc, randr))
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
	 * Loads a single critter into the world at the specified coordinates, if possible. Does nothing if
	 * the hex is not within the world boundaries, or if there is something already present at the hex.
	 * 
	 * @param sc the critter to add
	 * @param c the column index of the hex where the critter will be added
	 * @param r the row index of the hex where the critter will be added
	 */
	private void loadOneCritter(SimpleCritter sc, int c, int r)
	{
		if (!isValidHex(c, r))
			return;
		boolean added = grid[c][r].addContent(sc);
		if (added)
		{
			critterList.add(sc);
			critterMap.put(sc, grid[c][r]);
		}
	}

	/**
	 * Loads a single non-critter world object into the world at the specified coordinates, if possible.
	 * Does nothing if the hex is not within the world boundaries or if there is something already present at
	 * the hex. This method cannot be used to add critters into the world. Use the method
	 * {@code loadCritter(SimpleCritter sc, int c, int r)} instead.
	 * 
	 * @param sc the object to add
	 * @param c the column index of the hex where the object will be added
	 * @param r the row index of the hex where the object will be added
	 */
	private void addNonCritterObject(WorldObject wo, int c, int r)
	{
		if (wo instanceof Critter)
			return;
		if (!isValidHex(c, r))
			return;
		grid[c][r].addContent(wo);
	}

	/* ========================================= */
	/* ----------- Critter Sensors ------------- */
	/* ========================================= */

	@Override
	public int searchNearby(SimpleCritter sc, int dir)
	{
		//determines the row and column coordinates of the critter
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		
		// finds the hex to look in, based on the value of dir
		if (dir < 0)
			dir = 0;
		else if (dir > 6)
			dir %= 6;
		int nearbyc = c + sc.changeInPosition(true, dir)[0];
		int nearbyr = r + sc.changeInPosition(true, dir)[1];

		// critters see rock when they look off the edge of the world
		if (!isValidHex(nearbyc, nearbyr))
			return -1;
		Hex nearby = grid[nearbyc][nearbyr];
		return nearby.hexAppearance();
	}

	@Override
	public int searchAhead(SimpleCritter sc, int index)
	{
		//determines the row and column coordinates of the critter
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		
		if (index < 0)
			index = 0;
		int aheadc = c + sc.changeInPosition(true, sc.getOrientation())[0] * index;
		int aheadr = r + sc.changeInPosition(true, sc.getOrientation())[1] * index;

		if (!isValidHex(aheadc, aheadr))
			return -1;
		Hex nearby = grid[aheadc][aheadr];
		return nearby.hexAppearance();
	}

	/* ========================================= */
	/* ----------- Critter Actions ------------- */
	/* ========================================= */

	@Override
	public void moveCritter(SimpleCritter sc, boolean forward)
	{
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = CONSTANTS.get("MOVE_COST").intValue() * sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the critter
		if (sc.getEnergy() < 0)
		{
			kill(sc);
			return;
		}

		int[] changeInCoords = sc.changeInPosition(forward, sc.getOrientation());
		int newc = c + changeInCoords[0];
		int newr = r + changeInCoords[1];

		if (!isValidHex(newc, newr) || !grid[newc][newr].isEmpty())
		{
			if (sc.getEnergy() == 0)
				kill(sc);
			return;
		}
		grid[c][r].removeContent();
		critterMap.remove(sc);
		grid[newc][newr].addContent(sc);
		critterMap.put(sc, grid[newc][newr]);
		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void turnCritter(SimpleCritter sc, boolean clockwise)
	{
		int cost = sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the critter
		if (sc.getEnergy() < 0)
		{
			kill(sc);
			return;
		}

		sc.turn(clockwise);
		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void critterEat(SimpleCritter sc)
	{
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the critter
		if (sc.getEnergy() < 0)
		{
			kill(sc);
			return;
		}

		int newc = c + sc.changeInPosition(true, sc.getOrientation())[0];
		int newr = r + sc.changeInPosition(true, sc.getOrientation())[1];
		if (!isValidHex(newc, newr))
		{
			if (sc.getEnergy() == 0)
				kill(sc);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof Food)
		{
			Food nourishment = (Food) directlyInFront.getContent();
			sc.updateEnergy(nourishment.getCalories(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
			directlyInFront.removeContent();
		}
		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void growCritter(SimpleCritter sc)
	{
		int cost = sc.size()
				* sc.complexity(CONSTANTS.get("RULE_COST").intValue(), CONSTANTS.get("ABILITY_COST").intValue());
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the critter
		if (sc.getEnergy() < 0)
		{
			kill(sc);
			return;
		}

		int currentSize = sc.readMemory(3);
		sc.setMemory(currentSize + 1, 3);
	}

	@Override
	public void critterBattle(SimpleCritter attacker)
	{
		Hex location = critterMap.get(attacker);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = attacker.size() * CONSTANTS.get("ATTACK_COST").intValue();
		attacker.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		//if the critter did not have enough energy to complete this action, kills the critter
		if (attacker.getEnergy() < 0)
		{
			kill(attacker);
			return;
		}

		int newc = c + attacker.changeInPosition(true, attacker.getOrientation())[0];
		int newr = r + attacker.changeInPosition(true, attacker.getOrientation())[1];
		if (!isValidHex(newc, newr))
		{
			if (attacker.getEnergy() == 0)
				kill(attacker);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof SimpleCritter)
		{
			// Calculates the damage dealt to the target critter
			SimpleCritter target = (SimpleCritter) (directlyInFront.getContent());
			int baseDamage = CONSTANTS.get("BASE_DAMAGE").intValue();
			int dmgMultiplier = CONSTANTS.get("DAMAGE_INC").intValue();
			int dmgBeforeScaling = attacker.size() * attacker.readMemory(2) - target.size() * target.readMemory(1);
			int damage = baseDamage * attacker.size() * logisticFunction(dmgMultiplier * dmgBeforeScaling);

			// kills the target if it took damage greater than or equal to its current energy
			target.updateEnergy(-1 * damage, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
			if (target.getEnergy() <= 0)
				kill(target);
		}

		if (attacker.getEnergy() == 0)
			kill(attacker);
	}

	/** Performs the logistic function 1 / (1 + e^-x), floored to an integer value. */
	private int logisticFunction(double x)
	{
		double exponent = -1 * x;
		return (int) (1 / (1 + Math.exp(exponent)));
	}

	@Override
	public void critterBud(SimpleCritter sc)
	{
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int complexity = sc.complexity(CONSTANTS.get("RULE_COST").intValue(), CONSTANTS.get("ABILITY_COST").intValue());
		sc.updateEnergy(-1 * CONSTANTS.get("BUD_COST").intValue() * complexity,
				CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		//if the critter did not have enough energy to complete this action, kills the critter
		if (sc.getEnergy() < 0)
		{
			kill(sc);
			return;
		}

		int newc = c + sc.changeInPosition(false, sc.getOrientation())[0];
		int newr = r + sc.changeInPosition(false, sc.getOrientation())[1];
		if (!isValidHex(newc, newr) || !grid[newc][newr].isEmpty())
		{
			if (sc.getEnergy() == 0)
				kill(sc);
			return;
		}

		//Constructs the baby critter's memory, copying memory length, offense, and defense from the parent
		int[] babymem = new int[sc.getMemLength()];
		babymem[0] = sc.getMemLength();
		babymem[1] = sc.readMemory(1);
		babymem[2] = sc.readMemory(2);
		babymem[3] = 1;
		babymem[4] = 250;
		for (int i = 5; i < babymem.length; i++)
			babymem[i] = 0;

		String name = sc.getName() + " Jr.";
		Program prog = sc.getProgram();
		int numMutations = numberMutations();
		for (int i = 0; i < numMutations; i++)
			prog = prog.mutate();

		SimpleCritter baby = new Critter(prog, babymem, name, sc.getOrientation());
		loadOneCritter(baby, newc, newr);

		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void critterMate(SimpleCritter sc)
	{
		sc.toggleMatingPheromones(true);
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		int behindColumnParent1 = c + sc.changeInPosition(false, sc.getOrientation())[0];
		int behindRowParent1 = r + sc.changeInPosition(false, sc.getOrientation())[1];
		if (!isValidHex(behindColumnParent1, behindRowParent1))
		{
			sc.toggleMatingPheromones(false);
			return;
		}
		// coordinates of Parent 2
		int columnParent2 = c + sc.changeInPosition(true, sc.getOrientation())[0];
		int rowParent2 = r + sc.changeInPosition(true, sc.getOrientation())[1];
		if (!isValidHex(columnParent2, rowParent2))
		{
			sc.toggleMatingPheromones(false);
			return;
		}
		Hex directlyInFront = grid[columnParent2][rowParent2];
		if (!(directlyInFront.getContent() instanceof SimpleCritter))
		{
			sc.toggleMatingPheromones(false);
			return;
		}
		SimpleCritter parent2 = (SimpleCritter) (directlyInFront.getContent());
		int behindColumnParent2 = columnParent2 + sc.changeInPosition(false, sc.getOrientation())[0];
		int behindRowParent2 = rowParent2 + sc.changeInPosition(false, sc.getOrientation())[1];
		if (!isValidHex(behindColumnParent2, behindRowParent2))
		{
			sc.toggleMatingPheromones(false);
			return;
		}
		// checks if Parent 2 wants to mate
		if (!parent2.wantsToMate())
			return;
		int parent1Direction = sc.getOrientation();
		int parent2Direction = parent2.getOrientation();

		// direction checking
		if (!(Math.abs(parent1Direction - parent2Direction) == 3))
		{
			sc.toggleMatingPheromones(false);
			return;
		}

		// energy calculation
		sc.updateEnergy(-sc.size(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		parent2.updateEnergy(-parent2.size(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		if (sc.getEnergy() < 0 || parent2.getEnergy() < 0)
		{
			if (sc.getEnergy() < 0)
				kill(sc);
			if (parent2.getEnergy() < 0)
				kill(parent2);
			return;
		}
		initiateMatingProcess(sc, parent2);
	}

	/** Randomly determines the number of mutations that will occur during mating or budding. */
	private int numberMutations()
	{
		double randomNumber = Math.random();
		int returnValue = 0;
		double temp = 0.25;

		for (int i = 0; i < 10; i++)
		{
			if (randomNumber < temp)
				returnValue++;
			temp = Math.pow(0.25, i + 1);
		}
		return returnValue;
	}

	/** Executes the mating process, as long as there is one empty hex around the two critters. */
	private void initiateMatingProcess(SimpleCritter sc1, SimpleCritter sc2)
	{
		Random random = new Random();
		// energy calculation
		int complexity1 = sc1.complexity(CONSTANTS.get("RULE_COST").intValue(),
				CONSTANTS.get("ABILITY_COST").intValue());
		int complexity2 = sc2.complexity(CONSTANTS.get("RULE_COST").intValue(),
				CONSTANTS.get("ABILITY_COST").intValue());
		sc1.updateEnergy(-5 * complexity1, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		sc2.updateEnergy(-5 * complexity2, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		if (sc1.getEnergy() < 0 || sc2.getEnergy() < 0)
		{
			if (sc1.getEnergy() < 0)
				kill(sc1);
			if (sc2.getEnergy() < 0)
				kill(sc2);
			return;
		}

		// generating RuleSet
		LinkedList<Rule> babyRules = new LinkedList<Rule>();
		int ruleSetSize = 0;
		if (random.nextBoolean())
			ruleSetSize = sc1.getProgram().getRulesList().size();
		else
			ruleSetSize = sc2.getProgram().getRulesList().size();
		for (int i = 0; i < ruleSetSize; i++)
		{
			if (random.nextBoolean())
			{
				if (i >= sc1.getProgram().getRulesList().size())
					babyRules.add(sc2.getProgram().getRulesList().get(i));
				else
					babyRules.add(sc1.getProgram().getRulesList().get(i));
			}
			else
			{
				if (i >= sc2.getProgram().getRulesList().size())
					babyRules.add(sc1.getProgram().getRulesList().get(i));
				else
					babyRules.add(sc2.getProgram().getRulesList().get(i));
			}
		}
		Program prog = new ProgramImpl(babyRules);

		//generating memory
		int[] babymem = null;
		if (random.nextBoolean())
		{
			babymem = new int[sc1.getMemLength()];
			babymem[0] = sc1.getMemLength();
		}
		else
		{
			babymem = new int[sc2.getMemLength()];
			babymem[0] = sc2.getMemLength();
		}
		for (int i = 1; i <= 2; i++)
		{
			if (random.nextBoolean())
				babymem[i] = sc1.readMemory(i);
			else
				babymem[i] = sc2.readMemory(i);
		}
		babymem[3] = 1;
		babymem[4] = 250;
		for (int i = 5; i < babymem.length; i++)
			babymem[i] = 0;

		// coordinate Generation
		int babyColumn = 0;
		int babyRow = 0;
		if (random.nextBoolean())
		{
			Hex location = critterMap.get(sc1);
			babyColumn = location.getColumnIndex() + sc1.changeInPosition(false, sc1.getOrientation())[0];
			babyRow = location.getRowIndex() + sc1.changeInPosition(false, sc1.getOrientation())[1];
		}
		else
		{
			Hex location = critterMap.get(sc2);
			babyColumn = location.getColumnIndex() + sc2.changeInPosition(false, sc2.getOrientation())[0];
			babyRow = location.getRowIndex() + sc2.changeInPosition(false, sc2.getOrientation())[1];
		}

		int numMutations = numberMutations();
		for (int i = 0; i < numMutations; i++)
			prog = prog.mutate();
		String name = sc1.getName() + sc2.getName() + " Jr.";
		SimpleCritter baby = new Critter(prog, babymem, name, 0);
		loadOneCritter(baby, babyColumn, babyRow);

		if (sc1.getEnergy() == 0 || sc2.getEnergy() == 0)
		{
			if (sc1.getEnergy() == 0)
				kill(sc1);
			if (sc2.getEnergy() == 0)
				kill(sc2);
			return;
		}
		sc1.toggleMatingPheromones(false);
		sc2.toggleMatingPheromones(false);
	}

	@Override
	public void critterTag(SimpleCritter tagger, int val)
	{
		Hex location = critterMap.get(tagger);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		tagger.updateEnergy(-1 * tagger.size(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		//if the critter did not have enough energy to complete this action, kills the critter
		if (tagger.getEnergy() < 0)
		{
			kill(tagger);
			return;
		}

		int newc = c + tagger.changeInPosition(true, tagger.getOrientation())[0];
		int newr = r + tagger.changeInPosition(true, tagger.getOrientation())[1];
		if (!isValidHex(newc, newr))
		{
			if (tagger.getEnergy() == 0)
				kill(tagger);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof SimpleCritter)
		{
			SimpleCritter taggee = (SimpleCritter) (directlyInFront.getContent());
			if (!(val < 0 || val > 99))
				taggee.setMemory(val, 6);
		}

		if (tagger.getEnergy() == 0)
			kill(tagger);
	}

	@Override
	public void critterServe(SimpleCritter donator, int index)
	{
		Hex location = critterMap.get(donator);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		if (index < 0)
			index = 0;
		else if(index > donator.getEnergy() + donator.size())
			index = donator.getEnergy() + donator.size();

		donator.updateEnergy(-1 * (donator.size() + index), CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		//if the critter did not have enough energy to complete this action, kills the critter
		if (donator.getEnergy() < 0)
		{
			kill(donator);
			return;
		}

		int newc = c + donator.changeInPosition(true, donator.getOrientation())[0];
		int newr = r + donator.changeInPosition(true, donator.getOrientation())[1];
		if (!isValidHex(newc, newr))
		{
			if (donator.getEnergy() == 0)
				kill(donator);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (directlyInFront.isEmpty())
		{
			Food f = new Food(index);
			directlyInFront.addContent(f);
		}
		if (donator.getEnergy() == 0)
			kill(donator);
	}

	@Override
	public void critterSoakEnergy(SimpleCritter sc)
	{
		sc.updateEnergy(CONSTANTS.get("SOLAR_FLUX").intValue(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
	}

	/** Kills a critter and removes it from any lists or mappings of critters. Rest in peace, buddy. */
	private void kill(SimpleCritter sc)
	{
		Hex location = critterMap.get(sc);
		location.removeContent();
		critterMap.remove(sc);
		critterList.remove(sc);

		Food remnant = new Food(CONSTANTS.get("FOOD_PER_SIZE").intValue() * sc.size());
		location.addContent(remnant);
	}

	@Override
	public StringBuilder printGrid()
	{	
		StringBuilder result = new StringBuilder();
		for(int i = 0; i < 2 * rows - columns; i++)
		{
			StringBuilder sb = new StringBuilder();
			if(i % 2 != 0)
				sb.append("  ");
			for(int c = i % 2, r = (int) Math.ceil(i / 2.0); c < columns && r < rows; c += 2, r++)
			{
				if(isValidHex(c, r))
					sb.append("" + grid[c][r].toString() + "   ");
			}
			result.insert(0, sb.toString() + "\n");
		}
		result.insert(0, "World name: " + worldname + "\n");
		return result;
	}
	
	@Override
	public int analyzeHex(int c, int r)
	{
		if(!isValidHex(c, r))
			return Integer.MIN_VALUE;
		return grid[c][r].hexAppearance();
	}
	
	@Override
	public SimpleCritter analyzeCritter(int c, int r)
	{
		if(!isValidHex(c, r) || !(grid[c][r].getContent() instanceof SimpleCritter))
			return null;
		return (SimpleCritter) (grid[c][r].getContent());
	}
}