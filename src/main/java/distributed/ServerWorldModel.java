package distributed;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import simulation.SimpleCritter;
import simulation.SimpleWorld;
import simulation.World;
import simulation.WorldObject;
import simulation.Hex;

/**
 * The model that stores world and critter states server-side. It also serves as
 * an abstraction barrier between the world and the modules that interact with
 * the world.
 */
public class ServerWorldModel {

	/** An instance of the world. */
	private SimpleWorld world;
	/** The number of critters. */
	private int numCritters;
	/** The number of time steps taken. */
	private int time;
	/** The current world version number. */
	private int versionNumber;
	/** A running list of all critters that have died across all worlds simulated in this session. */
	private LinkedList<SimpleCritter> cumulativeDeadCritters;
	/** Supplies the locks for the models. */
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
	/**
	 * A log of all the changes that have occurred to the world since version 0
	 * (which is a blank world).
	 */
	private ArrayList<LinkedList<Hex>> diffLog;
	/** The rate at which the world is run. */
	private float rate;

	/** Creates a new blank world model. */
	public ServerWorldModel() {
		rwl.writeLock().lock();
		try {
			numCritters = 0;
			time = 0;
			versionNumber = 0;
			diffLog = new ArrayList<LinkedList<Hex>>();
			cumulativeDeadCritters = new LinkedList<SimpleCritter>();
		} finally {
			rwl.writeLock().unlock();
		}
	}

	/**
	 * Loads in a world based on a description.
	 *
	 * @param desc
	 * @throws IllegalArgumentException
	 *             if the description is invalid
	 * @throws UnsupportedOperationException
	 *             if the constants.txt file could not be read
	 */
	public void loadWorld(String desc) throws IllegalArgumentException, UnsupportedOperationException {
		rwl.writeLock().lock();
		try {
			// if a world already exists, adds all its dead critters to the cumulative dead
			// critter list
			if (world != null) {
				cumulativeDeadCritters.addAll(world.collectCritterCorpses());
			}
			world = new World(desc);
			//System.out.println(world.getAndResetUpdatedHexes());
			diffLog.add(world.getAndResetUpdatedHexes());
			time = 0;
			versionNumber++;
			numCritters = world.numRemainingCritters();
		} finally {
			rwl.writeLock().unlock();
		}
	}

	/** Returns the name of the world. */
	public String getWorldName() {
		try {
			rwl.readLock().lock();
			return world.getWorldName();
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Returns the number of columns in the world. */
	public int getColumns() {
		rwl.readLock().lock();
		try {
			return world.getColumns();
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Returns the number of rows in the world. */
	public int getRows() {
		rwl.readLock().lock();
		try {
			return world.getRows();
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Returns the number of living critters in the world. */
	public int getNumCritters() {
		try {
			rwl.readLock().lock();
			return numCritters;
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Returns the current time step of the world. */
	public int getCurrentTimeStep() {
		try {
			rwl.readLock().lock();
			return time;
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Returns the current simulation rate. */
	public float getRate() {
		try {
			rwl.readLock().lock();
			return rate;
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Returns the current version number. */
	public int getCurrentVersionNumber() {
		try {
			rwl.readLock().lock();
			return versionNumber;
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Retrieves the running list of dead critters. */
	public int[] getCumulativeDeadCritters() {
		try {
			rwl.readLock().lock();
			int[] result = new int[cumulativeDeadCritters.size()];
			for(int i = 0; i < cumulativeDeadCritters.size(); i++) {
				result[i] = world.getCritterID(cumulativeDeadCritters.get(i));
			}
			return result;
		} finally {
			rwl.readLock().unlock();
		}

	}

	/** Returns an array of all living critters. */
	public SimpleCritter[] listCritters() {
		try {
			//TODO implement
			rwl.readLock().lock();
			SimpleCritter[] result = new SimpleCritter[world.getCritterList().size()];
			return world.getCritterList().toArray(result);
		} finally {
			rwl.readLock().unlock();
		}
	}

	/**
	 * Gets a critter's ID from a pointer to that critter
	 * @param sc
	 * @return The critter's ID, or 0 if no critter ID exists for that critter
	 */
	public int getID(SimpleCritter sc) {
		try {
			rwl.readLock().lock();
			return world.getCritterID(sc);
		} finally {
			rwl.readLock().unlock();
		}
	}


	/** Determines whether a given sessionID has full permissions for a given critter. */
	public boolean hasCritterPermissions(SimpleCritter sc, int sessionID) {
		try {
			rwl.readLock().lock();
			Integer creatorID = world.getCritterCreatorID(sc);
			assert creatorID != null;
			return creatorID == sessionID;
		} finally {
			rwl.readLock().unlock();
		}

	}

	/**
	 * Returns a number giving information about a hex.
	 * @param c
	 * @param r
	 * @return
	 */
	public int hexInfo(int c, int r) {
		rwl.readLock().lock();
		try {
			return world.analyzeHex(c, r);
		} finally {
			rwl.readLock().unlock();
		}
	}

	/**
	 *
	 * @param c
	 * @param r
	 * @return
	 */
	public SimpleCritter getCritter(int c, int r) {
		rwl.readLock().lock();
		try {
			return world.analyzeCritter(c, r);
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Advances one time step. */
	public void advanceTime() {
		try {
			rwl.writeLock().lock();
			world.advanceOneTimeStep();
			time++;
			versionNumber++;
			//System.out.println(world.getAndResetUpdatedHexes());
			diffLog.add(world.getAndResetUpdatedHexes());
			rwl.writeLock().unlock();

			rwl.readLock().lock();
			numCritters = world.numRemainingCritters();
		} finally {
			rwl.readLock().unlock();
		}
	}

	/**
	 * Provides a map of everything that has changed in the world since the initial
	 * version.
	 *
	 * @param initialVersionNumber
	 * @return a HashMap mapping changed hexes to the objects at those hexes.
	 */
	public HashMap<Hex, WorldObject> updateSince(int initialVersionNumber) {
		try {
			rwl.readLock().lock();
			HashMap<Hex, WorldObject> result = new HashMap<Hex, WorldObject>();
			if (initialVersionNumber < 0 || initialVersionNumber > diffLog.size())
				return null;
			for (int i = initialVersionNumber; i < diffLog.size(); i++) {
				for (Hex h : diffLog.get(i)) {
					int c = h.getColumnIndex();
					int r = h.getRowIndex();
					if(isValidHex(c, r))
						result.put(h, world.getHexContent(c, r));
				}
			}
			return result;
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Determines whether or not a hex with column index {@code c} and row index {@code r} is on the world grid. */
	private boolean isValidHex(int c, int r) {
		if (c < 0 || r < 0)
			return false;
		else if (c >= world.getColumns() || r >= world.getRows())
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * world.getRows() - world.getColumns()))
			return false;
		return true;
	}
	/**
	 * Returns a critter object based on its ID
	 * @param id
	 * @return The critter with the specified ID, or {@code null} if no such critter exists
	 */
	public SimpleCritter retrieveCritter(int id) {
		try {
			rwl.readLock().lock();
			return world.getCritterFromID(id);
		} finally {
			rwl.readLock().unlock();
		}
	}

	/**
	 * 
	 * @param sc
	 * @return
	 */
	public int[] getCritterLocation(SimpleCritter sc) {
		try {
			rwl.readLock().lock();
			return world.getCritterLocation(sc);
		} finally {
			rwl.readLock().unlock();
		}
	}
	
	/**
	 * Removes a critter from the world, if it is there.
	 * @param id The ID of the critter to remove
	 */
	public void removeCritter(int id) {
		try {
			rwl.writeLock().lock();
			world.removeCritter(world.getCritterFromID(id));
		} finally {
			diffLog.add(world.getAndResetUpdatedHexes());
			versionNumber++;
			numCritters = world.numRemainingCritters();
			rwl.writeLock().unlock();
		}
	}

	/**
	 * Loads in critters of a certain species into the world at random locations.
	 * @param sc The critter species
	 * @param n The number to add
	 * @param sessionID
	 * @return The IDs of the added critters
	 */
	public int[] loadCritterRandomLocations(SimpleCritter sc, int n, int sessionID) {
		try {
			rwl.writeLock().lock();
			return world.loadCritters(sc, n, sessionID);
		} finally {
			diffLog.add(world.getAndResetUpdatedHexes());
			versionNumber++;
			numCritters = world.numRemainingCritters();
			rwl.writeLock().unlock();
		}
	}

	/**
	 * Loads in a critter of a certain species into the world.
	 * @param sc The critter species
	 * @param c The column index at which to add the critter
	 * @param r The row index at which to add the critter
	 * @param sessionID
	 * @return The ID of the added critter
	 */
	public int loadCritterAtLocation(SimpleCritter sc, int c, int r, int sessionID) {
		try {
			rwl.writeLock().lock();
			return world.loadOneCritter(sc, c, r, sessionID);
		} finally {
			diffLog.add(world.getAndResetUpdatedHexes());
			versionNumber++;
			numCritters = world.numRemainingCritters();
			rwl.writeLock().unlock();
		}
	}

	/**
	 * Loads a non-critter world object into the world.
	 * @param wo The object to load in (can be food or a rock)
	 * @param c The column index at which to add the object
	 * @param r The row index at which to add the object
	 */
	public void addWorldObject(WorldObject wo, int c, int r) {
		try {
			rwl.writeLock().lock();
			world.addNonCritterObject(wo, c, r);
		} finally {
			diffLog.add(world.getAndResetUpdatedHexes());
			versionNumber++;
			rwl.writeLock().unlock();
		}
	}
}