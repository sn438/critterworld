package distributed;

import static spark.Spark.get;
import static spark.Spark.delete;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.json.simple.JSONObject;
import com.google.gson.Gson;

import ast.Program;
import distributed.SimulationControlJSON.CountJSON;
import distributed.SimulationControlJSON.RateJSON;
import parse.ParserImpl;
import simulation.Critter;
import simulation.Hex;
import simulation.SimpleCritter;
import simulation.WorldObject;

/** A server that responds to HTTP requests. */
public class Server {
	/** The single instance of the server. */
	private static Server serverInstance;
	/** The port on which the server is run. */
	private static int portNumber;
	/** The password for read access to the server. */
	private final String readPassword;
	/** The password for write access to the server. */
	private final String writePassword;
	/** The password for admin access to the server. */
	private final String adminPassword;
	/** The rate at which the world is run. */
	private float simulationRate;
	private int session_id_count;
	private HashMap<Integer, String> sessionIdMap;
	private ServerWorldModel model;
	private ReentrantReadWriteLock rwl;

	/**
	 * Creates a new server.
	 *
	 * @param portNum
	 * @param readPass
	 * @param writePass
	 * @param adminPass
	 */
	private Server(int portNum, String readPass, String writePass, String adminPass) {
		portNumber = portNum;
		readPassword = readPass;
		writePassword = writePass;
		adminPassword = adminPass;
		model = new ServerWorldModel();
		sessionIdMap = new HashMap<Integer, String>();
		rwl = new ReentrantReadWriteLock();
		simulationRate = 0;
	}

	public static Server getInstance(int portNum, String readPass, String writePass, String adminPass) {
		serverInstance = new Server(portNum, readPass, writePass, adminPass);
		return serverInstance;
	}

	@SuppressWarnings("unchecked")
	/**
	 * Runs the server by receiving requests from the client and responding
	 * appropriately.
	 */
	public void run() {
		Gson gson = new Gson();
		port(portNumber);

		post("/login", (request, response) -> {
			System.out.println(request.body());
			response.header("Content-Type", "application/json");
			JSONObject responseValue = new JSONObject();
			String json = request.body();
			LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
			String level = loginInfo.level;
			String password = loginInfo.password;
			if (level.equals("read") && password.equals(readPassword)) {
				session_id_count++;
				responseValue.put("session_id", new Integer(session_id_count));
				sessionIdMap.put(session_id_count, "read");
				response.status(200);
				return responseValue;
			} else if (level.equals("write") && password.equals(writePassword)) {
				session_id_count++;
				responseValue.put("session_id", new Integer(session_id_count));
				sessionIdMap.put(session_id_count, "write");
				response.status(200);
				return responseValue;
			} else if (level.equals("admin") && password.equals(adminPassword)) {
				session_id_count++;
				responseValue.put("session_id", new Integer(session_id_count));
				sessionIdMap.put(session_id_count, "admin");
				response.status(200);
				return responseValue;
			} else {
				response.status(401);
				return "Please enter in a proper password.";
			}

		}, gson::toJson);

		delete("/critter", (request, response) -> {
			response.header("Content-Type", "text/plain");
			String queryString = request.queryString();
			int sessionId = -1;
			int critterId = -1;
			if (queryString.indexOf("&id") != -1) {
				int indexOfSessionId = queryString.indexOf("session_id=") + "session_id=".length();
				int indexOfCritterId = queryString.indexOf("id=", queryString.indexOf('&')) + "id=".length();
				sessionId = Integer.parseInt(queryString.substring(indexOfSessionId, queryString.indexOf("&")));
				critterId = Integer.parseInt(queryString.substring(indexOfCritterId));
			} else {
				int indexOfSessionId = queryString.indexOf("session_id=") + "session_id=".length();
				int indexOfCritterId = queryString.indexOf("id=") + "id=".length();
				sessionId = Integer.parseInt(queryString.substring(indexOfSessionId));
				critterId = Integer.parseInt(queryString.substring(indexOfCritterId, queryString.indexOf("&")));
			}
			if ((model.hasCritterPermissions(model.retrieveCritter(critterId), sessionId) || sessionIdMap.get(sessionId).equals("admin"))&& model.retrieveCritter(critterId) != null) {
				model.removeCritter(critterId);
				response.status(204);
				return "Removal was successful.";
			}
			else {
				response.status(401);
				return "Removal was unsuccessful.";
			}
			
		});
		// handles a client request to load a new world
		post("/world", (request, response) -> {
			response.header("Content-Type", "text/plain");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 11;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId));
			String json = request.body();
			System.out.println(json);
			LoadWorldInfoJSON loadWorldInfo = gson.fromJson(json, LoadWorldInfoJSON.class);
			String description = loadWorldInfo.getDescription();
			if (sessionIdMap.get(session_id) == null || !sessionIdMap.get(session_id).equals("admin")) {
				response.status(401);
				return "User does not have admin access.";
			} else {
				model.loadWorld(description);
				response.status(201);
				return "Ok";
			}
		});

		get("/world", (request, response) -> {
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			if (queryString.indexOf("from_row") != -1) {
			String[] parameters = queryString.split("&");
			int[] values = new int[parameters.length];
			int sessionID = -1;
			int updateSince = 0;
			int from_row = 0;
			int to_row = 0;
			int from_column = 0;
			int to_column = 0;
			for (int i = 0; i < parameters.length; i++) {
				String holder = parameters[i];
				values[i] = Integer.parseInt(holder.substring(holder.indexOf("=") + 1));
			}
			for (int i = 0; i < values.length; i++) {
				if (parameters[i].indexOf("session_id") != -1) {
					sessionID = values[i];
				} else if (parameters[i].indexOf("update_since") != -1) {
					updateSince = values[i];
				} else if (parameters[i].indexOf("from_row") != -1) {
					from_row = values[i];
				} else if (parameters[i].indexOf("to_row") != -1) {
					to_row = values[i];
				} else if (parameters[i].indexOf("from_column") != -1) {
					from_column = values[i];
				} else if (parameters[i].indexOf("to_column") != -1) {
					to_column = values[i];
				}
			}
			if (from_row > to_row || from_column > to_column) {
				response.status(406);
				return "The from coordinates cannot be less than the two coordinates";
			}
			if (from_row < 0 || from_column < 0 || to_row < 0|| to_column < 0) {
				response.status(406);
				return "The boundary coordinates cannot be negative.";
			}
			if (sessionIdMap.get(sessionID) == null) {
				response.status(401);
				return "User does not have permission to view the world.";
			} else if (updateSince < 0 || updateSince > model.getCurrentVersionNumber()) {
				response.status(406);
				return "That version number is invalid.";
			} else if (!model.isReady()) {
				response.status(403);
				return "A world must be loaded before you can view the world state.";
			} else {
				int time = model.getCurrentTimeStep();
				int version = model.getCurrentVersionNumber();
				float rate = this.simulationRate;
				String name = model.getWorldName();
				int population = model.getNumCritters();
				int columns = model.getColumns();
				int rows = model.getRows();
				int[] deadList = model.getCumulativeDeadCritters();
				System.out.println(deadList);
				HashMap<Hex, WorldObject> objects = model.updateSince(updateSince, from_row, to_row, from_column, to_column);
				JSONWorldObject[] state = new JSONWorldObject[objects.size()];
				int counter = 0;
				for (Entry<Hex, WorldObject> entry : objects.entrySet()) {
					int c = entry.getKey().getColumnIndex();
					int r = entry.getKey().getRowIndex();
					WorldObject wo = entry.getValue();
					if (wo instanceof SimpleCritter) {
						SimpleCritter sc = (SimpleCritter) wo;
						int critterID = model.getID(sc);
						boolean permissions = model.hasCritterPermissions(sc, sessionID);
						state[counter] = new JSONWorldObject(sc, c, r, critterID, permissions);
					} else {
						state[counter] = new JSONWorldObject(wo, c, r);
					}
					counter++;
				}
				response.status(200);
				return new WorldStateJSON(time, version, updateSince, rate, name, population, columns, rows, deadList,
						state);
			}
			} else {
				int indexOfSessionID = queryString.indexOf("session_id=") + "session_id=".length();
				int sessionID = -1;
				int updateSince = 0;
				if (queryString.contains("&update_since")) {
					int indexOfUpdateSince = queryString.indexOf("&update_since=") + "&update_since=".length();
					sessionID = Integer
							.parseInt(queryString.substring(indexOfSessionID, queryString.indexOf("&update_since")));
					updateSince = Integer.parseInt(queryString.substring(indexOfUpdateSince));
				} else if (queryString.contains("&session_id=")) {
					indexOfSessionID = queryString.indexOf("&session_id=") + "&session_id=".length();
					int indexOfUpdateSince = queryString.indexOf("update_since=") + "update_since=".length();
					updateSince = Integer
							.parseInt(queryString.substring(indexOfUpdateSince, queryString.indexOf("&session_id=")));
					sessionID = Integer.parseInt(queryString.substring(indexOfSessionID));
				} else {
					sessionID = Integer.parseInt(queryString.substring(indexOfSessionID));
				}

				if (sessionIdMap.get(sessionID) == null) {
					response.status(401);
					return "User does not have permission to view the world.";
				} else if (updateSince < 0 || updateSince > model.getCurrentVersionNumber()) {
					response.status(406);
					return "That version number is invalid.";
				} else if (!model.isReady()) {
					response.status(403);
					return "A world must be loaded before you can view the world state.";
				} else {
					int time = model.getCurrentTimeStep();
					int version = model.getCurrentVersionNumber();
					float rate = simulationRate;
					String name = model.getWorldName();
					int population = model.getNumCritters();
					int columns = model.getColumns();
					int rows = model.getRows();
					int[] deadList = model.getCumulativeDeadCritters();
					System.out.println(deadList);
					HashMap<Hex, WorldObject> objects = model.updateSince(updateSince);
					JSONWorldObject state[] = new JSONWorldObject[objects.size()];
					int counter = 0;
					for (Entry<Hex, WorldObject> entry : objects.entrySet()) {
						int c = entry.getKey().getColumnIndex();
						int r = entry.getKey().getRowIndex();
						WorldObject wo = entry.getValue();
						if (wo instanceof SimpleCritter) {
							SimpleCritter sc = (SimpleCritter) wo;
							int critterID = model.getID(sc);
							boolean permissions = model.hasCritterPermissions(sc, sessionID);
							state[counter] = new JSONWorldObject(sc, c, r, critterID, permissions);
						} else {
							state[counter] = new JSONWorldObject(wo, c, r);
						}
						counter++;
					}
					response.status(200);
					return new WorldStateJSON(time, version, updateSince, rate, name, population, columns, rows, deadList,
							state);
				}
			}
		}, gson::toJson);

		get("/critters", (request, response) -> {
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 11;
			int sessionID = Integer.parseInt(queryString.substring(indexOfSessionId));
			if (sessionIdMap.get(sessionID) == null) {
				response.status(401);
				return "User does not have permission to view the world.";
			} else {
				SimpleCritter[] critters = model.listCritters();
				System.out.println(critters);
				JSONWorldObject[] crittersJSON = new JSONWorldObject[critters.length];
				int counter = 0;
				for (SimpleCritter critter : critters) {
					boolean hasFullPermission = model.hasCritterPermissions(critter, sessionID);
					int column = model.getCritterLocation(critter)[0];
					int row = model.getCritterLocation(critter)[0];
					int critterId = model.getID(critter);
					JSONWorldObject holder = new JSONWorldObject(critter, column, row, critterId, hasFullPermission);
					crittersJSON[counter] = holder;
					counter++;
				}
				response.status(200);
				return crittersJSON;
			}
		}, gson::toJson);

		get("/critter", (request, response) -> {
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int sessionId = -1;
			int critterId = -1;
			if (queryString.indexOf("&id") != -1) {
				int indexOfSessionId = queryString.indexOf("session_id=") + "session_id=".length();
				int indexOfCritterId = queryString.indexOf("id=", queryString.indexOf('&')) + "id=".length();
				sessionId = Integer.parseInt(queryString.substring(indexOfSessionId, queryString.indexOf("&")));
				critterId = Integer.parseInt(queryString.substring(indexOfCritterId));
			} else {
				int indexOfSessionId = queryString.indexOf("session_id=") + "session_id=".length();
				int indexOfCritterId = queryString.indexOf("id=") + "id=".length();
				sessionId = Integer.parseInt(queryString.substring(indexOfSessionId));
				critterId = Integer.parseInt(queryString.substring(indexOfCritterId, queryString.indexOf("&")));
			}
			if (sessionIdMap.get(sessionId) == null) {
				response.status(401);
				return "User does not have permission to view the world.";
			} else if (model.retrieveCritter(critterId) == null) {
				response.status(401);
				return "Critter with the following id does not exist in the world.";
			} else {
				SimpleCritter critter = model.retrieveCritter(critterId);
				boolean hasFullPermission = model.hasCritterPermissions(critter, sessionId);
				int column = model.getCritterLocation(critter)[0];
				int row = model.getCritterLocation(critter)[0];
				JSONWorldObject holder = new JSONWorldObject(critter, column, row, critterId, hasFullPermission);
				response.status(200);
				return holder;
			}

		}, gson::toJson);

		// handles a client request to load in critters
		post("/critters", (request, response) -> {
			System.out.println("We have reached critters"); // TODO remove
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1));
			if (!(sessionIdMap.get(session_id) != null && (sessionIdMap.get(session_id).equals("admin")
					|| sessionIdMap.get(session_id).equals("write")))) {
				response.status(401);
				return "User does not have write access.";
			}
			String json = request.body();
			System.out.println(json);
			CritterJSON loadCritterInfo = gson.fromJson(json, CritterJSON.class);
			ParserImpl parser = new ParserImpl();
			InputStream stream = new ByteArrayInputStream(
					loadCritterInfo.getProgram().getBytes(StandardCharsets.UTF_8.name()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			Program program = parser.parse(reader);
			reader.close();
			int[] ids = null;
			SimpleCritter critter = new Critter(program, loadCritterInfo.getMem(), loadCritterInfo.getSpeciesId());
			if (loadCritterInfo.getPositions() == null)
				ids = model.loadCritterRandomLocations(critter, loadCritterInfo.getNum(), session_id);
			else {
				PositionJSON[] positions = loadCritterInfo.getPositions();
				int counter = 0;
				ids = new int[positions.length];
				for (PositionJSON positionHolder : positions) {
					int c = positionHolder.getColumn();
					int r = positionHolder.getRow();
					int id = model.loadCritterAtLocation(critter, c, r, session_id);
					ids[counter] = id;
					counter++;
				}
			}
			LoadCritterResponseJSON lcr = new LoadCritterResponseJSON(critter.getName(), ids);
			System.out.println(model.getNumCritters());
			response.status(200);
			return lcr;
		}, gson::toJson);

		post("/step", (request, response) -> {
			System.out.println("Step has been reached");
			response.header("Content-Type", "text/plain");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=") + 11;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId));
			if (!(sessionIdMap.get(session_id) != null && (sessionIdMap.get(session_id).equals("admin")
					|| sessionIdMap.get(session_id).equals("write")))) {
				response.status(401);
				return "User does not have write access.";
			}
			if (simulationRate != 0) {
				response.status(406);
				return "User cannot step because rate is not 0.";
			}
			String json = request.body();
			System.out.println(json);
			CountJSON stepInfo = gson.fromJson(json, CountJSON.class);
			int count = 1;
			if (stepInfo != null) {
				if (stepInfo.getCount() < 0) {
					response.status(406);
					return "World cannot be stepped a negative amount of times.";
				}
				else 
					count = stepInfo.getCount();
			}
			
			for (int i = 0; i < count; i++) {
				System.out.println("ok");
				model.advanceTime();
			}
			response.status(200);
			return "Ok";
		});

		post("/run", (request, response) -> {
			response.header("Content-Type", "text/plain");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=") + 11;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId));
			if (!(sessionIdMap.get(session_id) != null && (sessionIdMap.get(session_id).equals("admin")
					|| sessionIdMap.get(session_id).equals("write")))) {
				response.status(401);
				return "User does not have write access.";
			}
			String json = request.body();
			RateJSON info = gson.fromJson(json, RateJSON.class);
			 
			if(info.getRate() < 0 || info.getRate() > 50) {
				response.status(406);
				return "Invalid simulation rate.";
			} else {
				rwl.writeLock().lock();
				simulationRate = info.getRate();
				rwl.writeLock().unlock();
			}
			return "Ok";
			
		});
		
		Thread worldUpdateThread = new Thread(new Runnable() {
			@Override
			public void run() {
				while(simulationRate > 0) {
					long time = (long) (1000 / simulationRate);
					rwl.writeLock().lock();
					model.advanceTime();
					rwl.writeLock().unlock();
				
					try {
						Thread.sleep(time);
					} catch(InterruptedException i) {
						System.out.println("Could not pause thread execution.");
					}
				}
			}});
		worldUpdateThread.start();
	}

	/** Returns the port number of the server. */
	public static int getPortNum() {
		return portNumber;
	}

	class LoginInfo {

		String level;
		String password;

		private LoginInfo(String level, String password) {
			this.level = level;
			this.password = password;
		}
	}
}
