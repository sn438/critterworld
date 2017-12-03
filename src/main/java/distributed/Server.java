package distributed;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import com.google.gson.Gson;

import ast.Program;
import gui.WorldModel;
import parse.ParserImpl;
import simulation.Critter;
import simulation.FileParser;
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
	private int session_id_count;
	private HashMap<Integer, String> sessionIdMap;
	private ServerWorldModel model;

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
				return responseValue;
			} else if (level.equals("write") && password.equals(writePassword)) {
				session_id_count++;
				responseValue.put("session_id", new Integer(session_id_count));
				sessionIdMap.put(session_id_count, "write");
				return responseValue;
			} else if (level.equals("admin") && password.equals(adminPassword)) {
				session_id_count++;
				responseValue.put("session_id", new Integer(session_id_count));
				sessionIdMap.put(session_id_count, "admin");
				return responseValue;
			} else {
				response.status(401);
				return "Please enter in a proper password.";
			}

		}, gson::toJson);

		// handles a client request to load a new world
		post("/world", (request, response) -> {
			response.header("Content-Type", "text/plain");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1));
			String json = request.body();
			System.out.println(json);
			LoadWorldInfoJSON loadWorldInfo = gson.fromJson(json, LoadWorldInfoJSON.class);
			String description = loadWorldInfo.getDescription();
			if (sessionIdMap.get(session_id) == null || !sessionIdMap.get(session_id).equals("admin")) {
				response.status(401);
				return "User does not have admin access.";
			} else {
				model.loadWorld(description);
				return "Ok";
			}
		});
		
		// handles a client request to retrieve the world state
		get("/world", (request, response) -> {
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int indexOfSessionID = queryString.indexOf("session_id=", 0) + 11;
			int sessionID = -1;
			int updateSince = 0;
			if(queryString.contains("update_since")) {
				int indexOfUpdateSince = queryString.indexOf("update_since=") + 13;
				sessionID = Integer.parseInt(queryString.substring(indexOfSessionID , queryString.indexOf("update_since")));
				updateSince = Integer.parseInt(queryString.substring(indexOfUpdateSince));
			} else {
				sessionID = Integer.parseInt(queryString.substring(indexOfSessionID + 1));
			}

			if(sessionIdMap.get(sessionID) == null) {
				response.status(401);
				return "User does not have permission to view the world.";
			} else {
				int time = model.getCurrentTimeStep();
				int version = model.getCurrentVersionNumber();
				float rate = model.getRate();
				String name = model.getWorldName();
				int population = model.getNumCritters();
				int columns = model.getColumns();
				int rows = model.getRows();
				int[] deadList = model.getCumulativeDeadCritters();
				HashMap<Hex, WorldObject> objects = model.updateSince(updateSince);
				JSONWorldObject state[] = new JSONWorldObject[objects.size()];
				for(Entry<Hex, WorldObject> entry : objects.entrySet()) {
					int counter = 0;
					int c = entry.getKey().getColumnIndex();
					int r = entry.getKey().getRowIndex();
					WorldObject wo = entry.getValue();
					if(wo instanceof SimpleCritter) {
						SimpleCritter sc = (SimpleCritter) wo;
						int critterID = model.getID(sc);
						boolean permissions = model.hasCritterPermissions(sc, sessionID);
						state[counter] = new JSONWorldObject(sc, c, r, critterID, permissions);
					} else {
						state[counter] = new JSONWorldObject(wo, c, r);
					}
				}
				return new WorldStateJSON(time, version, updateSince, rate, name, population, columns, rows, deadList, state);
			}
		}, gson::toJson);
		
		// handles a client request to retrieve a single critter
//		get("/critter", (request, response) -> {
//			response.header("Content-Type", "application/json");
//			String queryString = request.queryString();
//			int indexOfCritterID; 
//		});
		
		// handles a client request to load in critters
		post("/critters", (request, response) -> {
			System.out.println("We have reached critters"); //TODO remove
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1));
			if (!(sessionIdMap.get(session_id) != null && (sessionIdMap.get(session_id).equals("admin") || sessionIdMap.get(session_id).equals("write")))) {
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
				for (PositionJSON positionHolder: positions) {
					int c = positionHolder.getColumn();
					int r = positionHolder.getRow();
					int id = model.loadCritterAtLocation(critter, c, r, session_id);
					ids[counter] = id;
					counter++;
				}
			}
 			LoadCritterResponseJSON lcr = new LoadCritterResponseJSON(critter.getName(), ids);
 			System.out.println(model.getNumCritters());
			return lcr;
		}, gson::toJson);
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
