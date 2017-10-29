package simulation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Random;

import ast.Program;
import ast.ProgramImpl;
import ast.Rule;

/** A class to simulate the world state. */
public class World extends AbstractWorld {
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

	/** Loads a world based on a world description file. */
	public World(String filename) throws FileNotFoundException, IllegalArgumentException {
		super();
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		critterList = new LinkedList<SimpleCritter>();
		timePassed = 0;

		BufferedReader bf = new BufferedReader(new FileReader(filename));

		// parses the world name, and if no valid one is parsed, supplies a default one
		worldname = FileParser.parseAttributeFromLine(bf, "name ");
		if (worldname.equals(""))
			worldname = "Arrakis";

		// parses world dimensions, and supplies default ones if no valid dimensions are
		// parsed
		try {
			String worldDimensions = FileParser.parseAttributeFromLine(bf, "size ");
			String[] dim = worldDimensions.split(" ");
			columns = Integer.parseInt(dim[0]);
			rows = Integer.parseInt(dim[1]);

			if (!(columns > 0 && rows > 0 && 2 * rows - columns > 0)) {
				columns = CONSTANTS.get("COLUMNS").intValue();
				rows = CONSTANTS.get("ROWS").intValue();
			}
		} catch (Exception e) {
			columns = CONSTANTS.get("COLUMNS").intValue();
			rows = CONSTANTS.get("ROWS").intValue();
		}
		numValidHexes = 0;

		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (isValidHex(i, j)) {
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
				}

		try {
			String line = bf.readLine();
			while (line != null) {
				String[] info = line.split(" ");
				switch (info[0]) {
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
					loadOneCritter(sc, Integer.parseInt(info[2]), Integer.parseInt(info[3]));
					break;
				}
				line = bf.readLine();
			}
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Generates a default size world containing nothing but randomly placed rocks.
	 */
	public World() throws IllegalArgumentException {
		super();
		worldname = "Arrakis";
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		critterList = new LinkedList<SimpleCritter>();
		timePassed = 0;

		columns = CONSTANTS.get("COLUMNS").intValue();
		rows = CONSTANTS.get("ROWS").intValue();
		numValidHexes = 0;

		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++) {
				if (isValidHex(i, j)) {
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
				}
			}

		// randomly fills about 1/20 of the hexes in the world with rocks
		int c = (int) (Math.random() * columns);
		int r = (int) (Math.random() * rows);
		int n = 0;
		while (n < numValidHexes / 20) {
			c = (int) (Math.random() * columns);
			r = (int) (Math.random() * rows);
			if (isValidHex(c, r)) {
				grid[c][r].addContent(new Rock());
				n++;
			}
		}
	}

	/**
	 * Parses the constants file in the project directory and stores the constants
	 * in the CONSTANTS field.
	 */
	private void setConstants() throws IllegalArgumentException {
		/*
		 * InputStream in =
		 * World.class.getResourceAsStream("src/main/resources/constants.txt"); if(in ==
		 * null) { System.err.
		 * println("The constants.txt file could not be found in src/main/resources.");
		 * System.exit(0); }
		 * 
		 * BufferedReader bf = new BufferedReader(new InputStreamReader(in)); CONSTANTS
		 * = FileParser.parseConstants(bf);
		 */

		try {
			BufferedReader bf = new BufferedReader(new FileReader("src/main/resources/constants.txt"));
			CONSTANTS = FileParser.parseConstants(bf);
		} catch (FileNotFoundException e) {
			System.err.println("The constants.txt file could not be found in src/main/resources.");
			System.exit(0);
		}
	}

	/** Checks if a coordinate pair is within the bounds of this world grid. */
	public boolean isValidHex(int c, int r) {
		if (c < 0 || r < 0)
			return false;
		if ((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}

	@Override
	public void loadCritters(String filename, int n, int direction) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			SimpleCritter sc = FileParser.parseCritter(br, getMinMemory(), direction);

			for (int i = 0; i < n; i++) {

				int randc = (int) (Math.random() * columns);
				int randr = (int) (Math.random() * rows);
				while (!isValidHex(randc, randr)) {
					randc = (int) (Math.random() * columns);
					randr = (int) (Math.random() * rows);
				}

				if (isValidHex(randc, randr))
					loadOneCritter(sc, randc, randr);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Critter file not found.");
			return;
		}
	}

	/**
	 * Loads a single critter into the world at the specified coordinates, if
	 * possible. Does nothing if the hex is not within the world boundaries or if
	 * there is something already present at the hex.
	 * 
	 * @param sc
	 *            the Critter to add
	 * @param c
	 *            the column index of the hex where the critter will be added
	 * @param r
	 *            the row index of the hex where the critter will be added
	 */
	private void loadOneCritter(SimpleCritter sc, int c, int r) {
		if (!isValidHex(c, r))
			return;
		boolean added = grid[c][r].addContent(sc);
		if (added) {
			critterList.add(sc);
			critterMap.put(sc, grid[c][r]);
		}
	}

	/**
	 * Loads a single non-critter world object into the world at the specified
	 * coordinates, if possible. Does nothing if the hex is not within the world
	 * boundaries or if there is something already present at the hex. This method
	 * cannot be used to add critters into the world. Use the method
	 * {@code loadCritter(SimpleCritter sc, int c, int r)} instead.
	 * 
	 * @param sc
	 *            the object to add
	 * @param c
	 *            the column index of the hex where the object will be added
	 * @param r
	 *            the row index of the hex where the object will be added
	 */
	private void addNonCritterObject(WorldObject wo, int c, int r) {
		if (wo instanceof Critter)
			return;
		if (!isValidHex(c, r))
			return;
		grid[c][r].addContent(wo);
	}

	@Override
	public int searchNearby(SimpleCritter sc, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int searchAhead(SimpleCritter sc, int index) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean moveCritter(SimpleCritter sc, boolean forward) {
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = CONSTANTS.get("MOVE_COST").intValue() * sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return false;
		}

		int[] changeInCoords = sc.changeInPosition(forward);
		int newc = c + changeInCoords[0];
		int newr = r + changeInCoords[1];

		if (!isValidHex(newc, newr) || !grid[newc][newr].isEmpty())
			return false;
		grid[c][r].removeContent();
		critterMap.remove(sc);
		critterMap.put(sc, grid[newc][newr]);
		return true;
	}

	@Override
	public void critterEat(SimpleCritter sc) {
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return;
		}

		int newc = c + sc.changeInPosition(true)[0];
		int newr = r + sc.changeInPosition(true)[1];
		if (!isValidHex(newc, newr))
			return;

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof Food) {
			Food nourishment = (Food) directlyInFront.getContent();
			sc.updateEnergy(nourishment.getCalories(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
			directlyInFront.removeContent();
		}
		if (sc.getEnergy() == 0)
			kill(sc);
	}

	public void growCritter(SimpleCritter sc) {
		int cost = sc.size()
				* sc.complexity(CONSTANTS.get("RULE_COST").intValue(), CONSTANTS.get("ABILITY_COST").intValue());
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return;
		}

		int currentSize = sc.readMemory(3);
		sc.setMemory(currentSize + 1, 3);
	}

	@Override
	public void critterBattle(SimpleCritter attacker) {
		Hex location = critterMap.get(attacker);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = attacker.size() * CONSTANTS.get("ATTACK_COST").intValue();
		attacker.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (attacker.getEnergy() < 0) {
			kill(attacker);
			return;
		}

		int newc = c + attacker.changeInPosition(true)[0];
		int newr = r + attacker.changeInPosition(true)[1];
		if (!isValidHex(newc, newr))
			return;

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof SimpleCritter) {
			// Calculates the damage dealt to the target critter
			SimpleCritter target = (SimpleCritter) (directlyInFront.getContent());
			int baseDamage = CONSTANTS.get("BASE_DAMAGE").intValue();
			int dmgMultiplier = CONSTANTS.get("DAMAGE_INC").intValue();
			int dmgBeforeScaling = attacker.size() * attacker.readMemory(2) - target.size() * target.readMemory(1);
			int damage = baseDamage * attacker.size() * logisticFunction(dmgMultiplier * dmgBeforeScaling);

			target.updateEnergy(-1 * damage, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
			if (target.getEnergy() <= 0)
				kill(target);
		}

		if (attacker.getEnergy() == 0)
			kill(attacker);
	}

	/**
	 * Performs the logistic function 1 / (1 + e^-x), floored to an integer value.
	 */
	private int logisticFunction(double x) {
		double exponent = -1 * x;
		return (int) (1 / (1 + Math.exp(exponent)));
	}

	@Override
	public void critterBud(SimpleCritter sc) {
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		int newc = c + sc.changeInPosition(false)[0];
		int newr = r + sc.changeInPosition(false)[1];
		if (!isValidHex(newc, newr))
			return;

		if (!location.isEmpty())
			return;

		int complexity = sc.complexity(CONSTANTS.get("RULE_COST").intValue(), CONSTANTS.get("ABILITY_COST").intValue());
		sc.updateEnergy(-9 * complexity, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return;
		}

		// Constructs the baby critter's memory, copying memory length, offense, and
		// defense from the parent
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

		SimpleCritter baby = new Critter(prog, babymem, name, 0);
		loadOneCritter(baby, newc, newr);

		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void critterMate(SimpleCritter sc) {
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		int behindColumnParent1 = c + sc.changeInPosition(false)[0];
		int behindRowParent1 = r + sc.changeInPosition(false)[1];
		if (!isValidHex(behindColumnParent1, behindRowParent1))
			return;
		// coordinates of Parent 2
		int columnParent2 = c + sc.changeInPosition(true)[0];
		int rowParent2 = r + sc.changeInPosition(true)[1];
		if (!isValidHex(columnParent2, rowParent2))
			return;
		Hex directlyInFront = grid[columnParent2][rowParent2];
		if (!(directlyInFront.getContent() instanceof SimpleCritter))
			return;
		SimpleCritter parent2 = (SimpleCritter) (directlyInFront.getContent());
		int behindColumnParent2 = columnParent2 + sc.changeInPosition(false)[0];
		int behindRowParent2 = rowParent2 + sc.changeInPosition(false)[1];
		if (!isValidHex(behindColumnParent2, behindRowParent2))
			return;
		// checks if Parent 2 wants to mate
		if (!parent2.wantsToMate())
			return;
		int parent1Direction = sc.getOrientation().getValue();
		int parent2Direction = parent2.getOrientation().getValue();

		// direction checking
		if (!(Math.abs(parent1Direction - parent2Direction) == 3))
			return;

		// energy calculation
		sc.updateEnergy(-sc.size(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		parent2.updateEnergy(-parent2.size(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		if (sc.getEnergy() < 0 || parent2.getEnergy() < 0) {
			if (sc.getEnergy() < 0)
				kill(sc);
			if (parent2.getEnergy() < 0) 
				kill(parent2);
			return;
		}
		initiateMatingProcess(sc, parent2);

	}

	/**
	 * Randomly determines the number of mutations that will occur during mating or
	 * budding.
	 */
	private int numberMutations() {
		double randomNumber = Math.random();
		int returnValue = 0;
		double temp = 0.25;

		for (int i = 0; i < 10; i++) {
			if (randomNumber < temp)
				returnValue++;
			temp = Math.pow(0.25, i + 1);
		}
		return returnValue;
	}

	/**
	 * Executes the mating process, as long as there is one empty hex around the two
	 * critters.
	 */
	private void initiateMatingProcess(SimpleCritter sc1, SimpleCritter sc2) {
		Random random = new Random();
		// energy calculation
		int complexity1 = sc1.complexity(CONSTANTS.get("RULE_COST").intValue(),
				CONSTANTS.get("ABILITY_COST").intValue());
		int complexity2 = sc2.complexity(CONSTANTS.get("RULE_COST").intValue(),
				CONSTANTS.get("ABILITY_COST").intValue());
		sc1.updateEnergy(-5 * complexity1, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		sc2.updateEnergy(-5 * complexity2, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
		if (sc1.getEnergy() < 0 || sc2.getEnergy() < 0) {
			if (sc1.getEnergy() < 0)
				kill(sc1);
			if (sc2.getEnergy() < 0) 
				kill(sc2);
			return;
		}

		// generating RuleSet
		LinkedList<Rule> babyRules = new LinkedList<Rule>();
		int ruleSetSize = 0;
		if (random.nextBoolean()) {
			ruleSetSize = sc1.getProgram().getRulesList().size();
		} else {
			ruleSetSize = sc2.getProgram().getRulesList().size();
		}
		for (int i = 0; i < ruleSetSize; i++) {
			if (random.nextBoolean()) {
				if (i >= sc1.getProgram().getRulesList().size()) {
					babyRules.add(sc2.getProgram().getRulesList().get(i));
				} else {
					babyRules.add(sc1.getProgram().getRulesList().get(i));
				}
			} else {
				if (i >= sc2.getProgram().getRulesList().size()) {
					babyRules.add(sc1.getProgram().getRulesList().get(i));
				} else {
					babyRules.add(sc2.getProgram().getRulesList().get(i));
				}
			}
		}
		Program prog = new ProgramImpl(babyRules);
		
		// generating memory
		int[] babymem = null;
		if (random.nextBoolean()) {
			babymem = new int[sc1.getMemLength()];
			babymem[0] = sc1.getMemLength();
		} else {
			babymem = new int[sc2.getMemLength()];
			babymem[0] = sc2.getMemLength();
		}
		for (int i = 1; i <= 2; i++) {
			if (random.nextBoolean()) {
				babymem[i] = sc1.readMemory(i);
			} else {
				babymem[i] = sc2.readMemory(i);
			}
		}
		babymem[3] = 1;
		babymem[4] = 250;
		for (int i = 5; i < babymem.length; i++)
			babymem[i] = 0;
		
		//coordinate Generation
		int babyColumn = 0;
		int babyRow = 0;
		if (random.nextBoolean()) {
			Hex location = critterMap.get(sc1);
			babyColumn = location.getColumnIndex() + sc1.changeInPosition(false)[0];
			babyRow = location.getRowIndex() + sc1.changeInPosition(false)[1];
		} else {
			Hex location = critterMap.get(sc2);
			babyColumn = location.getColumnIndex() + sc2.changeInPosition(false)[0];
			babyRow = location.getRowIndex() + sc2.changeInPosition(false)[1];
		}
		
		int numMutations = numberMutations();
		for (int i = 0; i < numMutations; i++)
			prog = prog.mutate();
		String name = sc1.getName() + sc2.getName() + " Jr.";
		SimpleCritter baby = new Critter(prog, babymem, name, 0);
		loadOneCritter(baby, babyColumn, babyRow);
		
		if (sc1.getEnergy() == 0 || sc2.getEnergy() == 0) {
			if (sc1.getEnergy() == 0)
				kill(sc1);
			if (sc2.getEnergy() == 0) 
				kill(sc2);
			return;
		}
	}

	@Override
	public void critterTag(SimpleCritter tagger, int index) {
		Hex location = critterMap.get(tagger);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

	}

	@Override
	public void critterServe(SimpleCritter donator, int index) {
		Hex location = critterMap.get(donator);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
	}

	/**
	 * Kills a critter and removes it from any lists or mappings of critters. Rest
	 * in peace, buddy.
	 */
	private void kill(SimpleCritter sc) {
		Hex location = critterMap.get(sc);
		location.removeContent();
		critterMap.remove(sc);
		critterList.remove(sc);

		Food remnant = new Food(CONSTANTS.get("FOOD_PER_SIZE").intValue() * sc.size());
		location.addContent(remnant);
	}

	@Override
	public StringBuilder printGrid() {
		StringBuilder result = new StringBuilder("World name: " + worldname + "\n");
		for (int i = 0; i < columns; i++) {
			for (int j = 0; j < rows; j++) {
				if (grid[i][j] == null)
					result.append("% ");
				else
					result.append(grid[i][j].toString() + " ");
			}
			result.append("\n");
		}
		return result;
	}

	@Override
	public void advanceOneTimeStep() {

	}
}