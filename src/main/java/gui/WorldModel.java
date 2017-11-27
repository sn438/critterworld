package gui;

import java.io.File;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import simulation.SimpleCritter;
import simulation.SimpleWorld;
import simulation.World;
import simulation.WorldObject;
import simulation.Hex;

public class WorldModel {
	private SimpleWorld world;
	int numCritters;
	int time;
	private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

	/** Creates a new blank world model. */
	public WorldModel() {
		rwl.writeLock().lock();
		try {
			numCritters = 0;
			time = 0;
		} finally {
			rwl.writeLock().unlock();
		}
	}

	/** Creates a new random world. */
	public void createNewWorld() {
		rwl.writeLock().lock();
		try {
			world = new World();
		} finally {
			rwl.writeLock().unlock();
		}
	}

	/**
	 * Loads in a world file.
	 * 
	 * @param worldfile
	 * @throws FileNotFoundException
	 *             if the file could not be found or is somehow invalid
	 * @throws IllegalArgumentException
	 *             if the constants.txt file could not be read
	 */
	public void loadWorld(File worldfile) throws FileNotFoundException, IllegalArgumentException {
		rwl.writeLock().lock();
		try {
			world = new World(worldfile);
		} finally {
			rwl.writeLock().unlock();
		}
	}

	public boolean isReady() {
		return world != null;
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

	public synchronized int hexContent(int c, int r) {
		rwl.readLock().lock();
		try {
			return world.analyzeHex(c, r);
		} finally {
			rwl.readLock().unlock();
		}
	}

	public synchronized SimpleCritter getCritter(int c, int r) {
		rwl.readLock().lock();
		try {
			return world.analyzeCritter(c, r);
		} finally {
			rwl.readLock().unlock();
		}
	}

	public synchronized Set<Map.Entry<SimpleCritter, Hex>> getCritterMap() {
		rwl.readLock().lock();
		try {
			return world.getCritterMap();
		} finally {
			rwl.readLock().unlock();
		}
	}

	public synchronized Set<Map.Entry<WorldObject, Hex>> getObjectMap() {
		rwl.readLock().lock();
		try {
			return world.getObjectMap();
		} finally {
			rwl.readLock().unlock();
		}
	}

	/** Advances one time step. */
	public synchronized void advanceTime() {
		try {
			rwl.writeLock().lock();
			world.advanceOneTimeStep();
			time++;
			rwl.writeLock().unlock();
			rwl.readLock().lock();
			numCritters = world.numRemainingCritters();
		} finally {
			rwl.readLock().unlock();
		}
	}

	public synchronized void loadRandomCritters(File f, int n) {
		try {
			rwl.writeLock().lock();
			world.loadCritters(f, n, -1);
			time++;
			rwl.writeLock().unlock();
			rwl.readLock().lock();
			numCritters = world.numRemainingCritters();
		} finally {
			rwl.readLock().unlock();
		}
	}

	public synchronized void loadCritterAtLocation(File f, int c, int r) {
		try {
			rwl.writeLock().lock();
			world.loadCritterAtLocation(f, c, r);
			time++;
			rwl.writeLock().unlock();
			rwl.readLock().lock();
			numCritters = world.numRemainingCritters();
		} finally {
			rwl.readLock().unlock();
		}
	}
}