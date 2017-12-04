package simulation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
import distributed.JSONWorldObject;
import distributed.WorldStateJSON;

/** A class to simulate the world state. */
public class World extends AbstractWorld {
	/** The name of this world. */
	private String worldname;
	/** Contains the hex grid of the world. */
	private Hex[][] grid;
	/** Contains the objects of the world. */
	private JSONWorldObject[][] objects;
	/** Maps each critter to a location in the world. */
	private HashMap<SimpleCritter, Hex> critterMap;
	/** Maps each non critter object to a location in the world. */
	private HashMap<WorldObject, Hex> nonCritterObjectMap;
	/** The number of columns in the world grid. */
	private int columns;
	/** The number of rows in the world grid. */
	private int rows;
	/** The number of hexes that lie on the world grid. */
	private int numValidHexes;
	/**
	 * Assigned as a critter ID. Is modified every time a critter is added to ensure
	 * uniqueness of IDs.
	 */
	private int critterIDcount;
	/** Maps unique critter IDs to critters. */
	private HashMap<Integer, SimpleCritter> IDToCritterMap;
	/** Maps critters to their unique IDs. */
	private HashMap<SimpleCritter, Integer> critterToIDMap;
	/** Maps critter IDs to the session IDs that created them. */
	private HashMap<Integer, Integer> critterCreatorMap;

	/**
	 * Loads a world from a description.
	 * 
	 * @param worlddesc
	 *            A String description of the world.
	 * @throws UnsupportedOperationException
	 *             if the world constants file could not be found or was improperly
	 *             formatted IllegalArgumentException if the {@code worlddesc} is
	 *             invalid
	 */
	public World(String worlddesc) throws UnsupportedOperationException, IllegalArgumentException {
		// sets constants and initializes instance fields
		super();
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		nonCritterObjectMap = new HashMap<WorldObject, Hex>();
		critterIDcount = 1;
		IDToCritterMap = new HashMap<Integer, SimpleCritter>();
		critterToIDMap = new HashMap<SimpleCritter, Integer>();
		critterCreatorMap = new HashMap<Integer, Integer>();
		super.updatedHexes = new ArrayList<Hex>();
		super.critterList = new LinkedList<SimpleCritter>();
		super.deadCritters = new LinkedList<SimpleCritter>();
		super.timePassed = 0;

		String[] lines = worlddesc.split("\r\n");
		System.out.println(lines.length);
		for (String line : lines) {
			System.out.println(line);
		}
		if (lines.length < 2)
			throw new IllegalArgumentException();

		// parses the world name, and if no valid one is parsed, supplies a default one
		worldname = FileParser.parseAttributeFromLine(lines[0], "name ");

		if (worldname.equals(""))
			worldname = "Arrakis";

		// parses world dimensions, and supplies default ones if no valid dimensions are
		// parsed
		try {
			String worldDimensions = FileParser.parseAttributeFromLine(lines[1], "size ");
			String[] dim = worldDimensions.split(" ");
			columns = Integer.parseInt(dim[0]);
			System.out.println("columns: " + columns);
			rows = Integer.parseInt(dim[1]);
			System.out.println("rows: " + rows);

			if (!(columns > 0 && rows > 0 && 2 * rows - columns > 0)) {
				columns = CONSTANTS.get("COLUMNS").intValue();
				rows = CONSTANTS.get("ROWS").intValue();
				System.err.println("Invalid world dimensions. Supplying default world dimensions...");
			}
		} catch (Exception e) {
			columns = CONSTANTS.get("COLUMNS").intValue();
			rows = CONSTANTS.get("ROWS").intValue();
			System.err.println("Invalid world dimensions. Supplying default world dimensions...");
		}
		numValidHexes = 0;

		// initializes world grid
		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (isValidHex(i, j)) {
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
					updatedHexes.add(grid[i][j]);
				}

		// loads in food and rock objects from description (ignores critter additions)
		for (int i = 2; i < lines.length; i++) {
			String[] info = lines[i].split(" ");
			try {
				switch (info[0]) {
				case "rock":
					addNonCritterObject(new Rock(), Integer.parseInt(info[1]), Integer.parseInt(info[2]));
					break;
				case "food":
					Food f = new Food(Integer.parseInt(info[3]));
					addNonCritterObject(f, Integer.parseInt(info[1]), Integer.parseInt(info[2]));
					break;
				}
			} catch (Exception e) {
				break;
			}
		}
	}

	/** */
	public World(WorldStateJSON state) throws FileNotFoundException, IllegalArgumentException {
		// sets constants and initializes instance fields
		super();
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		nonCritterObjectMap = new HashMap<WorldObject, Hex>();
		critterIDcount = 1;
		IDToCritterMap = new HashMap<Integer, SimpleCritter>();
		critterToIDMap = new HashMap<SimpleCritter, Integer>();
		critterCreatorMap = new HashMap<Integer, Integer>();
		super.updatedHexes = new ArrayList<Hex>();
		super.critterList = new LinkedList<SimpleCritter>();
		super.deadCritters = new LinkedList<SimpleCritter>();
		super.timePassed = 0;
		
		columns = state.getCols();
		rows = state.getRows();
		worldname = state.getName();
		numValidHexes = 0;
		
		// initializes world grid
		grid = new Hex[columns][rows];
		objects = new JSONWorldObject[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (isValidHex(i, j)) {
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
					updatedHexes.add(grid[i][j]);
				}
		
		for(JSONWorldObject obj : state.getWorldObjects()) {
			objects[obj.getCol()][obj.getRow()] = obj;
			if(obj.getType().equals("critter")) {
				int[] mem = obj.getMemory();
				String name = obj.getSpeciesName();
				int dir = obj.getOrientation();
				loadOneCritter(new Critter(null, mem, name, dir), obj.getCol(), obj.getRow(), -1);
			} else if(obj.getType().equals("rock")) {
				addNonCritterObject(new Rock(), obj.getCol(), obj.getRow());
			} else if(obj.getType().equals("food")) {
				Food f = new Food(obj.getCalories());
				addNonCritterObject(f, obj.getCol(), obj.getRow());
			}
		}
	}

	/**
	 * Loads a world from a world description file, in the form of a pre-determined
	 * file.
	 * 
	 * @param file
	 *            The file that contains world information.
	 * @throws FileNotFoundException
	 *             if the world file could not be found
	 *             UnsupportedOperationException if the world constants file could
	 *             not be found or was improperly formatted
	 */
	public World(File file) throws FileNotFoundException, IllegalArgumentException {
		// sets constants and initializes instance fields
		super();
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		nonCritterObjectMap = new HashMap<WorldObject, Hex>();
		critterIDcount = 1;
		IDToCritterMap = new HashMap<Integer, SimpleCritter>();
		critterToIDMap = new HashMap<SimpleCritter, Integer>();
		critterCreatorMap = new HashMap<Integer, Integer>();
		super.updatedHexes = new ArrayList<Hex>();
		super.critterList = new LinkedList<SimpleCritter>();
		super.deadCritters = new LinkedList<SimpleCritter>();
		super.timePassed = 0;

		BufferedReader bf = new BufferedReader(new FileReader(file));

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
				System.err.println("Invalid world dimensions. Supplying default world dimensions...");
			}
		} catch (Exception e) {
			columns = CONSTANTS.get("COLUMNS").intValue();
			rows = CONSTANTS.get("ROWS").intValue();
			System.err.println("Invalid world dimensions. Supplying default world dimensions...");
		}
		numValidHexes = 0;
		// initializes world grid
		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++)
				if (isValidHex(i, j)) {
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
					updatedHexes.add(grid[i][j]);
				}

		try {
			// loads in world objects from file
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
					if (sc == null) {
						System.err.println("The critter file " + file.toString()
								+ " does not have the right syntax, so it was not loaded.");
						break;
					}

					loadOneCritter(sc, Integer.parseInt(info[2]), Integer.parseInt(info[3]), -1);
					break;
				}
				line = bf.readLine();
			}
		} catch (FileNotFoundException e) {
			System.out.println("Critter file not found.");
			return;
		} catch (IOException e) {
			return;
		} catch (Exception e) {
			return;
		}
	}

	/**
	 * Generates a default size world containing nothing but randomly placed rocks.
	 * 
	 * @throws UnsupportedOperationException
	 *             if the world constants file could not be found or was improperly
	 *             formatted
	 */
	public World() throws UnsupportedOperationException {
		// sets constants and initializes instance fields
		super();
		worldname = "Arrakis";
		setConstants();
		critterMap = new HashMap<SimpleCritter, Hex>();
		nonCritterObjectMap = new HashMap<WorldObject, Hex>();
		critterIDcount = 1;
		IDToCritterMap = new HashMap<Integer, SimpleCritter>();
		critterToIDMap = new HashMap<SimpleCritter, Integer>();
		critterCreatorMap = new HashMap<Integer, Integer>();
		super.updatedHexes = new ArrayList<Hex>();
		super.critterList = new LinkedList<SimpleCritter>();
		super.deadCritters = new LinkedList<SimpleCritter>();
		super.timePassed = 0;

		columns = CONSTANTS.get("COLUMNS").intValue();
		rows = CONSTANTS.get("ROWS").intValue();
		numValidHexes = 0;

		grid = new Hex[columns][rows];
		for (int i = 0; i < grid.length; i++)
			for (int j = 0; j < grid[0].length; j++) {
				if (isValidHex(i, j)) {
					grid[i][j] = new Hex(i, j);
					numValidHexes++;
					updatedHexes.add(grid[i][j]);
				}
			}

		// randomly fills about 1/40 of the hexes in the world with rocks
		int c = (int) (Math.random() * columns);
		int r = (int) (Math.random() * rows);
		int n = 0;
		while (n < numValidHexes / 40) {
			c = (int) (Math.random() * columns);
			r = (int) (Math.random() * rows);
			if (isValidHex(c, r) && grid[c][r].isEmpty()) {
				addNonCritterObject(new Rock(), c, r);
				n++;
			}
		}
	}

	/**
	 * Parses the constants file in the project directory and stores the constants
	 * in the CONSTANTS field.
	 * 
	 * @throws UnsupportedOperationException
	 *             if the constants file couldn't be found or is improperly
	 *             formatted
	 */
	private void setConstants() throws UnsupportedOperationException {
		InputStream in = World.class.getClassLoader().getResourceAsStream("simulation/constants.txt");
		if (in == null)
			throw new UnsupportedOperationException();

		BufferedReader bf = new BufferedReader(new InputStreamReader(in));
		CONSTANTS = FileParser.parseConstants(bf);
	}

	@Override
	public int getColumns() {
		return columns;
	}

	@Override
	public int getRows() {
		return rows;
	}

	@Override
	public String getWorldName() {
		return worldname;
	}

	@Override
	public boolean isValidHex(int c, int r) {
		if (c < 0 || r < 0)
			return false;
		else if (c >= columns || r >= rows)
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}

	@Override
	public void loadCritters(String filename, int n, int direction) {
		try {
			for (int i = 0; i < n; i++) {
				if (i >= numValidHexes - (critterList.size() + nonCritterObjectMap.size()))
					break;
				BufferedReader br = new BufferedReader(new FileReader(filename));
				SimpleCritter sc = FileParser.parseCritter(br, getMinMemory(), direction);
				int randc = (int) (Math.random() * columns);
				int randr = (int) (Math.random() * rows);
				while (!isValidHex(randc, randr) || !grid[randc][randr].isEmpty()) {
					randc = (int) (Math.random() * columns);
					randr = (int) (Math.random() * rows);
				}

				if (isValidHex(randc, randr))
					loadOneCritter(sc, randc, randr, -1);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Critter file not found.");
			return;
		}
	}

	@Override
	public void loadCritters(File file, int n, int direction) {
		try {
			for (int i = 0; i < n; i++) {
				if (i >= numValidHexes - (critterList.size() + nonCritterObjectMap.size()))
					break;
				BufferedReader br = new BufferedReader(new FileReader(file));
				SimpleCritter sc = FileParser.parseCritter(br, getMinMemory(), direction);
				if (sc == null) {
					return;
				}
				int randc = (int) (Math.random() * columns);
				int randr = (int) (Math.random() * rows);
				while (!isValidHex(randc, randr) || !grid[randc][randr].isEmpty()) {
					randc = (int) (Math.random() * columns);
					randr = (int) (Math.random() * rows);
				}

				if (isValidHex(randc, randr))
					loadOneCritter(sc, randc, randr, -1);
			}
		} catch (FileNotFoundException e) {
			System.err.println("Critter file not found.");
			return;
		}
	}

	@Override
	public int[] loadCritters(SimpleCritter sc, int n, int sessionID) {
		int[] result = new int[n];
		Program scProgram = sc.getProgram();
		int[] scMem = sc.getMemoryCopy();
		String scId = sc.getName();
		int scDir = sc.getOrientation();
		Arrays.fill(result, -1);
		for (int i = 0; i < n; i++) {
			if (i >= numValidHexes - (critterList.size() + nonCritterObjectMap.size()))
				break;
			int randc = (int) (Math.random() * columns);
			int randr = (int) (Math.random() * rows);
			while (!isValidHex(randc, randr) || !grid[randc][randr].isEmpty()) {
				randc = (int) (Math.random() * columns);
				randr = (int) (Math.random() * rows);
			}

			if (isValidHex(randc, randr)) {
				SimpleCritter holder = new Critter(scProgram, scMem, scId, scDir);
				result[i] = loadOneCritter(holder, randc, randr, sessionID);
			}
		}
		return result;
	}

	@Override
	public void loadCritterAtLocation(File file, int c, int r) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			SimpleCritter sc = FileParser.parseCritter(br, getMinMemory(), -1);
			if (isValidHex(c, r))
				loadOneCritter(sc, c, r, -1);
		} catch (FileNotFoundException e) {
			System.err.println("Critter file not found.");
			return;
		}
	}

	@Override
	public int loadOneCritter(SimpleCritter sc, int c, int r, int sessionID) {
		if (!isValidHex(c, r))
			return -1;
		int result;
		sc.randomizeOrientation();
		boolean added = grid[c][r].addContent(sc);
		if (added) {
			critterList.add(sc);
			critterMap.put(sc, grid[c][r]);
			updatedHexes.add(grid[c][r]);
			result = critterIDcount;
			IDToCritterMap.put(critterIDcount, sc);
			critterToIDMap.put(sc, critterIDcount);
			if (sessionID > 0)
				critterCreatorMap.put(critterIDcount, sessionID);
			critterIDcount++;
		} else {
			result = -1;
		}
		return result;
	}

	@Override
	public void addNonCritterObject(WorldObject wo, int c, int r) {
		if (wo instanceof Critter)
			return;
		if (!isValidHex(c, r))
			return;
		nonCritterObjectMap.put(wo, grid[c][r]);
		grid[c][r].addContent(wo);
		updatedHexes.add(grid[c][r]);
	}

	/* ========================================= */
	/* ----------- Critter Sensors ------------- */
	/* ========================================= */

	@Override
	public int searchNearby(SimpleCritter sc, int dir) {
		// determines the row and column coordinates of the critter
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
	public int searchAhead(SimpleCritter sc, int index) {
		// determines the row and column coordinates of the critter
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

	@Override
	public int smell(SimpleCritter sc) {
		int direction = 0;
		int distance = 1000;
		ArrayList<SmellValue> foodList = new ArrayList<SmellValue>();

		// adds all the possible hexes to be used in method to a hash map
		HashMap<Hex, SmellValue> graph = new HashMap<Hex, SmellValue>();
		for (int i = 0; i < grid.length; i++) {
			for (int j = 0; j < grid[i].length; j++) {
				if (isValidHex(i, j)) {
					if (grid[i][j].hexAppearance() == 0 || grid[i][j].hexAppearance() < -1) {
						SmellValue sv = new SmellValue();
						graph.put(grid[i][j], sv);
						if (grid[i][j].hexAppearance() < -1) {
							foodList.add(sv);
						}
					}
				}
			}
		}

		// sets up root hex for smell function
		Hex root = critterMap.get(sc);
		System.out.println(sc);
		System.out.println(root);
		SmellValue rootSmell = graph.get(root);
		rootSmell.totalDist = 0;
		rootSmell.orientation = sc.getOrientation();
		rootSmell.numSteps = 0;

		// sets up priority queue
		AdjustablePriorityQueue<Hex> frontier = new AdjustablePriorityQueue<Hex>();
		frontier.add(root);
		frontier.setPriority(root, 0);

		boolean initialIteration = true;
		while (!frontier.isEmpty()) {
			// pops hex from priority queue
			Hex curr = frontier.remove();
			SmellValue currSmell = graph.get(curr);
			int c = curr.getColumnIndex();
			int r = curr.getRowIndex();

			// find neighbor hexes if they exist
			Hex[] neighbors = new Hex[6];
			if (isValidHex(c, r + 1))
				neighbors[0] = grid[c][r + 1];
			if (isValidHex(c + 1, r + 1))
				neighbors[1] = grid[c + 1][r + 1];
			if (isValidHex(c, r + 1))
				neighbors[2] = grid[c + 1][r];
			if (isValidHex(c, r - 1))
				neighbors[3] = grid[c][r - 1];
			if (isValidHex(c - 1, r - 1))
				neighbors[4] = grid[c - 1][r - 1];
			if (isValidHex(c - 1, r))
				neighbors[5] = grid[c - 1][r];

			// apply an iteration of Dijkstra's algorithm
			for (int i = 0; i < neighbors.length; i++) {
				SmellValue sv = graph.get(neighbors[i]);
				if (sv != null) {
					boolean isFood = foodList.contains(sv);

					// moves on to the next hex if it has taken 11 steps to reach new hex (or 12 in
					// the case of food)
					if (currSmell.numSteps == (isFood ? 11 : 10)) {
						continue;
					}

					int newDistance = currSmell.totalDist;
					if (!isFood) {
						newDistance += calculateSmellWeight(currSmell, sv);
					}

					if (sv.totalDist == Integer.MAX_VALUE) {
						sv.totalDist = newDistance;
						sv.origin = initialIteration ? i : currSmell.origin;
						sv.numSteps = currSmell.numSteps + 1;
						frontier.add(neighbors[i]);
						frontier.setPriority(neighbors[i], sv.totalDist);
					} else {
						sv.totalDist = Math.min(sv.totalDist, newDistance);
						if (sv.totalDist == newDistance) {
							sv.origin = initialIteration ? i : currSmell.origin;
							sv.numSteps = currSmell.numSteps + 1;
						}
						frontier.setPriority(neighbors[i], sv.totalDist);
					}
				}
			}

			initialIteration = false;
		}

		// finds closest food
		for (SmellValue sv : foodList) {
			distance = Math.min(distance, sv.totalDist);
			if (distance == sv.totalDist) {
				direction = sv.origin;
			}
		}

		// TODO optimization: have a particular track of searching end if it hits food
		// TODO test method a bunch when done

		return distance * 1000 + direction;
	}

	private int calculateSmellWeight(SmellValue a, SmellValue b) {
		int x = a.orientation;
		int y = b.orientation;
		return Math.min(Math.abs(x - y), 6 - Math.abs(x - y)) + 1;
	}

	/* ========================================= */
	/* ----------- Critter Actions ------------- */
	/* ========================================= */

	@Override
	public void moveCritter(SimpleCritter sc, boolean forward) {
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int cost = CONSTANTS.get("MOVE_COST").intValue() * sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return;
		}

		int[] changeInCoords = sc.changeInPosition(forward, sc.getOrientation());
		int newc = c + changeInCoords[0];
		int newr = r + changeInCoords[1];

		if (!isValidHex(newc, newr) || !grid[newc][newr].isEmpty()) {
			if (sc.getEnergy() == 0)
				kill(sc);
			return;
		}
		grid[c][r].removeContent();
		critterMap.remove(sc);
		updatedHexes.add(grid[c][r]);
		grid[newc][newr].addContent(sc);
		critterMap.put(sc, grid[newc][newr]);
		updatedHexes.add(grid[newc][newr]);
		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void turnCritter(SimpleCritter sc, boolean clockwise) {
		int cost = sc.size();
		sc.updateEnergy(-1 * cost, CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return;
		}

		sc.turn(clockwise);
		updatedHexes.add(critterMap.get(sc));
		if (sc.getEnergy() == 0)
			kill(sc);
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

		int newc = c + sc.changeInPosition(true, sc.getOrientation())[0];
		int newr = r + sc.changeInPosition(true, sc.getOrientation())[1];
		if (!isValidHex(newc, newr)) {
			if (sc.getEnergy() == 0)
				kill(sc);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof Food) {
			Food nourishment = (Food) directlyInFront.getContent();
			sc.updateEnergy(nourishment.getCalories(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
			nonCritterObjectMap.remove(nourishment);
			directlyInFront.removeContent();
			updatedHexes.add(directlyInFront);
		}
		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
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
		updatedHexes.add(critterMap.get(sc));
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

		int newc = c + attacker.changeInPosition(true, attacker.getOrientation())[0];
		int newr = r + attacker.changeInPosition(true, attacker.getOrientation())[1];
		if (!isValidHex(newc, newr)) {
			if (attacker.getEnergy() == 0)
				kill(attacker);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof SimpleCritter) {
			// Calculates the damage dealt to the target critter
			SimpleCritter target = (SimpleCritter) (directlyInFront.getContent());
			int baseDamage = CONSTANTS.get("BASE_DAMAGE").intValue();
			double dmgMultiplier = CONSTANTS.get("DAMAGE_INC").doubleValue();
			int dmgBeforeScaling = (attacker.size() * attacker.readMemory(2)) - (target.size() * target.readMemory(1));
			int damage = (int) (baseDamage * attacker.size()
					* logisticFunction(dmgMultiplier * (double) dmgBeforeScaling));

			// kills the target if it took damage greater than or equal to its current
			// energy
			target.updateEnergy(-1 * damage, CONSTANTS.get("ENERGY_PER_SIZE").intValue());
			if (target.getEnergy() <= 0)
				kill(target);
		}

		if (attacker.getEnergy() == 0)
			kill(attacker);
	}

	/**
	 * Applies the logistic function, {@code f(x) = 1 / (1 + e^-x)}, to the given
	 * number.
	 */
	private double logisticFunction(double x) {
		double exponent = -1 * x;
		return (1 / (1 + Math.exp(exponent)));
	}

	@Override
	public void critterBud(SimpleCritter sc) {
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		int complexity = sc.complexity(CONSTANTS.get("RULE_COST").intValue(), CONSTANTS.get("ABILITY_COST").intValue());
		sc.updateEnergy(-1 * CONSTANTS.get("BUD_COST").intValue() * complexity,
				CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (sc.getEnergy() < 0) {
			kill(sc);
			return;
		}

		int newc = c + sc.changeInPosition(false, sc.getOrientation())[0];
		int newr = r + sc.changeInPosition(false, sc.getOrientation())[1];
		if (!isValidHex(newc, newr) || !grid[newc][newr].isEmpty()) {
			if (sc.getEnergy() == 0)
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

		String name = sc.getName();
		Program prog = sc.getProgram();
		int numMutations = numberMutations();
		for (int i = 0; i < numMutations; i++) {
			prog = prog.mutate();
		}

		SimpleCritter baby = new Critter(prog, babymem, name, sc.getOrientation());
		Integer parentCreatorID = critterCreatorMap.get(critterToIDMap.get(sc));
		int babyCreatorID = parentCreatorID == null ? -1 : parentCreatorID;
		loadOneCritter(baby, newc, newr, babyCreatorID);

		if (sc.getEnergy() == 0)
			kill(sc);
	}

	@Override
	public void critterMate(SimpleCritter sc) {
		sc.toggleMatingPheromones(true);
		Hex location = critterMap.get(sc);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		int behindColumnParent1 = c + sc.changeInPosition(false, sc.getOrientation())[0];
		int behindRowParent1 = r + sc.changeInPosition(false, sc.getOrientation())[1];
		if (!isValidHex(behindColumnParent1, behindRowParent1)) {
			sc.toggleMatingPheromones(false);
			return;
		}
		// coordinates of Parent 2
		int columnParent2 = c + sc.changeInPosition(true, sc.getOrientation())[0];
		int rowParent2 = r + sc.changeInPosition(true, sc.getOrientation())[1];
		if (!isValidHex(columnParent2, rowParent2)) {
			sc.toggleMatingPheromones(false);
			return;
		}
		Hex directlyInFront = grid[columnParent2][rowParent2];
		if (!(directlyInFront.getContent() instanceof SimpleCritter)) {
			sc.toggleMatingPheromones(false);
			return;
		}
		SimpleCritter parent2 = (SimpleCritter) (directlyInFront.getContent());
		int behindColumnParent2 = columnParent2 + sc.changeInPosition(false, sc.getOrientation())[0];
		int behindRowParent2 = rowParent2 + sc.changeInPosition(false, sc.getOrientation())[1];
		if (!isValidHex(behindColumnParent2, behindRowParent2)) {
			sc.toggleMatingPheromones(false);
			return;
		}
		// checks if Parent 2 wants to mate
		if (!parent2.wantsToMate())
			return;
		int parent1Direction = sc.getOrientation();
		int parent2Direction = parent2.getOrientation();

		// direction checking
		if (!(Math.abs(parent1Direction - parent2Direction) == 3)) {
			sc.toggleMatingPheromones(false);
			return;
		}

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
		if (random.nextBoolean())
			ruleSetSize = sc1.getProgram().getRulesList().size();
		else
			ruleSetSize = sc2.getProgram().getRulesList().size();
		for (int i = 0; i < ruleSetSize; i++) {
			if (random.nextBoolean()) {
				if (i >= sc1.getProgram().getRulesList().size())
					babyRules.add(sc2.getProgram().getRulesList().get(i));
				else
					babyRules.add(sc1.getProgram().getRulesList().get(i));
			} else {
				if (i >= sc2.getProgram().getRulesList().size())
					babyRules.add(sc1.getProgram().getRulesList().get(i));
				else
					babyRules.add(sc2.getProgram().getRulesList().get(i));
			}
		}
		Program prog = new ProgramImpl(babyRules);

		// generate memory
		int[] babymem = null;
		if (random.nextBoolean()) {
			babymem = new int[sc1.getMemLength()];
			babymem[0] = sc1.getMemLength();
		} else {
			babymem = new int[sc2.getMemLength()];
			babymem[0] = sc2.getMemLength();
		}
		for (int i = 1; i <= 2; i++) {
			if (random.nextBoolean())
				babymem[i] = sc1.readMemory(i);
			else
				babymem[i] = sc2.readMemory(i);
		}
		babymem[3] = 1;
		babymem[4] = 250;
		for (int i = 5; i < babymem.length; i++)
			babymem[i] = 0;

		// generate coordinates
		int babyColumn = 0;
		int babyRow = 0;
		if (random.nextBoolean()) {
			Hex location = critterMap.get(sc1);
			babyColumn = location.getColumnIndex() + sc1.changeInPosition(false, sc1.getOrientation())[0];
			babyRow = location.getRowIndex() + sc1.changeInPosition(false, sc1.getOrientation())[1];
		} else {
			Hex location = critterMap.get(sc2);
			babyColumn = location.getColumnIndex() + sc2.changeInPosition(false, sc2.getOrientation())[0];
			babyRow = location.getRowIndex() + sc2.changeInPosition(false, sc2.getOrientation())[1];
		}

		int numMutations = numberMutations();
		for (int i = 0; i < numMutations; i++)
			prog = prog.mutate();
		String name = random.nextBoolean() ? sc1.getName() : sc2.getName();
		Integer parentCreatorID = random.nextBoolean() ? critterCreatorMap.get(critterToIDMap.get(sc1))
				: critterCreatorMap.get(critterToIDMap.get(sc2));
		int babyCreatorID = parentCreatorID == null ? -1 : parentCreatorID;
		SimpleCritter baby = new Critter(prog, babymem, name, 0);
		loadOneCritter(baby, babyColumn, babyRow, babyCreatorID);

		if (sc1.getEnergy() == 0 || sc2.getEnergy() == 0) {
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
	public void critterTag(SimpleCritter tagger, int val) {
		Hex location = critterMap.get(tagger);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		tagger.updateEnergy(-1 * tagger.size(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (tagger.getEnergy() < 0) {
			kill(tagger);
			return;
		}

		int newc = c + tagger.changeInPosition(true, tagger.getOrientation())[0];
		int newr = r + tagger.changeInPosition(true, tagger.getOrientation())[1];
		if (!isValidHex(newc, newr)) {
			if (tagger.getEnergy() == 0)
				kill(tagger);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (!directlyInFront.isEmpty() && directlyInFront.getContent() instanceof SimpleCritter) {
			SimpleCritter taggee = (SimpleCritter) (directlyInFront.getContent());
			if (!(val < 0 || val > 99))
				taggee.setTag(val);
		}

		if (tagger.getEnergy() == 0)
			kill(tagger);
	}

	@Override
	public void critterServe(SimpleCritter donator, int index) {
		Hex location = critterMap.get(donator);
		int c = location.getColumnIndex();
		int r = location.getRowIndex();

		if (index < 0)
			index = 0;
		else if (index > donator.getEnergy() + donator.size())
			index = donator.getEnergy() + donator.size();

		donator.updateEnergy(-1 * (donator.size() + index), CONSTANTS.get("ENERGY_PER_SIZE").intValue());

		// if the critter did not have enough energy to complete this action, kills the
		// critter
		if (donator.getEnergy() < 0) {
			kill(donator);
			return;
		}

		int newc = c + donator.changeInPosition(true, donator.getOrientation())[0];
		int newr = r + donator.changeInPosition(true, donator.getOrientation())[1];
		if (!isValidHex(newc, newr)) {
			if (donator.getEnergy() == 0)
				kill(donator);
			return;
		}

		Hex directlyInFront = grid[newc][newr];
		if (directlyInFront.isEmpty()) {
			Food f = new Food(index);
			nonCritterObjectMap.put(f, directlyInFront);
			directlyInFront.addContent(f);
			updatedHexes.add(directlyInFront);
		}
		if (donator.getEnergy() == 0)
			kill(donator);
	}

	@Override
	public void critterSoakEnergy(SimpleCritter sc) {
		sc.updateEnergy(CONSTANTS.get("SOLAR_FLUX").intValue(), CONSTANTS.get("ENERGY_PER_SIZE").intValue());
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
		updatedHexes.add(location);
		deadCritters.add(sc);

		Food remnant = new Food(CONSTANTS.get("FOOD_PER_SIZE").intValue() * sc.size());
		nonCritterObjectMap.put(remnant, location);
		location.addContent(remnant);
	}

	@Override
	public void removeCritter(SimpleCritter sc) {
		if (sc == null)
			return;
		Hex location = critterMap.get(sc);
		location.removeContent();
		critterMap.remove(sc);
		critterList.remove(sc);
		updatedHexes.add(location);
		deadCritters.add(sc);
	}

	@Override
	public StringBuilder printGrid() {
		StringBuilder result = new StringBuilder();
		for (int i = 0; i < 2 * rows - columns; i++) {
			StringBuilder sb = new StringBuilder();
			if (i % 2 != 0)
				sb.append("  ");
			for (int c = i % 2, r = (int) Math.ceil(i / 2.0); c < columns && r < rows; c += 2, r++) {
				if (isValidHex(c, r))
					sb.append("" + grid[c][r].toString() + "   ");
			}
			result.insert(0, sb.toString() + "\n");
		}
		result.insert(0, "World name: " + worldname + "\n");
		return result;
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Map.Entry<SimpleCritter, Hex>> getCritterMap() {
		HashMap<SimpleCritter, Hex> copy = (HashMap<SimpleCritter, Hex>) critterMap.clone();
		return copy.entrySet();
	}

	@Override
	@SuppressWarnings("unchecked")
	public Set<Map.Entry<WorldObject, Hex>> getObjectMap() {
		HashMap<WorldObject, Hex> copy = (HashMap<WorldObject, Hex>) nonCritterObjectMap.clone();
		return copy.entrySet();
	}

	@Override
	public int analyzeHex(int c, int r) {
		if (!isValidHex(c, r))
			return Integer.MIN_VALUE;
		return grid[c][r].hexAppearance();
	}

	@Override
	public SimpleCritter analyzeCritter(int c, int r) {
		if (!isValidHex(c, r) || !(grid[c][r].getContent() instanceof SimpleCritter))
			return null;
		return (SimpleCritter) (grid[c][r].getContent());
	}

	@Override
	public int[] getCritterLocation(SimpleCritter sc) {
		Hex location = critterMap.get(sc);
		if (location == null)
			return null;
		int c = location.getColumnIndex();
		int r = location.getRowIndex();
		return new int[] { c, r };
	}

	@Override
	public int getCritterID(SimpleCritter sc) {
		Integer ID = critterToIDMap.get(sc);
		int result = (ID == null) ? 0 : ID;
		return result;
	}

	@Override
	public SimpleCritter getCritterFromID(int id) {
		return IDToCritterMap.get(id);
	}

	@Override
	public int getCritterCreatorID(SimpleCritter sc) {
		return critterCreatorMap.get(getCritterID(sc));
	}

	@Override
	public WorldObject getHexContent(int c, int r) {
		if (!isValidHex(c, r))
			return null;
		return grid[c][r].getContent();
	}
}