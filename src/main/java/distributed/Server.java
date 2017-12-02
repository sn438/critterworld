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

import org.json.simple.JSONObject;
import com.google.gson.Gson;

import ast.Program;
import gui.WorldModel;
import parse.ParserImpl;
import simulation.Critter;
import simulation.FileParser;
import simulation.SimpleCritter;

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

		post("/critters", (request, response) -> {
			System.out.println("We have reached critters");
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1, queryString.length()));
			String json = request.body();
			System.out.println(json);
			CritterJSON loadCritterInfo = gson.fromJson(json, CritterJSON.class);
			ParserImpl parser = new ParserImpl();
			InputStream stream = new ByteArrayInputStream(
					loadCritterInfo.getProgram().getBytes(StandardCharsets.UTF_8.name()));
			BufferedReader reader = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
			Program program = parser.parse(reader);
			int[] memory = loadCritterInfo.getMem();
			SimpleCritter critter = new Critter(program, loadCritterInfo.getMem(), loadCritterInfo.getSpeciesId());
			if (loadCritterInfo.getPositions() == null)
				model.loadCritterRandomLocations(critter, loadCritterInfo.getNum(), session_id);
			else {
				PositionJSON[] positions = loadCritterInfo.getPositions();
				for (PositionJSON positionHolder: positions) {
					int c = positionHolder.getColumn();
					int r = positionHolder.getRow();
					model.loadCritterAtLocation(critter, c, r, session_id);
				}
			}	
			System.out.println(model.getNumCritters());
			if (!(sessionIdMap.get(session_id) != null && (sessionIdMap.get(session_id).equals("admin")
					|| sessionIdMap.get(session_id).equals("write")))) {
				response.status(401);
				return "User does not have admin access.";
			} else {

				return "Ok";
			}
		}, gson::toJson);

		post("/world", (request, response) -> {
			response.header("Content-Type", "text/plain");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1, queryString.length()));
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

		get("/world/generic", (request, response) -> {
			response.header("Content-Type", "application/json");
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1, queryString.length()));
			if (sessionIdMap.get(session_id) == null || !sessionIdMap.get(session_id).equals("admin")) {
				response.status(401);
				return "User does not have admin access.";
			} else {
				model.createNewWorld();
				return "Ok";
			}
		}, gson::toJson);

		get("/rowNum", (request, response) -> {
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1, queryString.length()));
			if (sessionIdMap.get(session_id) == null) {
				response.status(401);
				return "User does not have read access";
			} else
				return model.getRows();
		}, gson::toJson);

		get("/colNum", (request, response) -> {
			String queryString = request.queryString();
			int indexOfSessionId = queryString.indexOf("session_id=", 0) + 10;
			int session_id = Integer.parseInt(queryString.substring(indexOfSessionId + 1, queryString.length()));
			if (sessionIdMap.get(session_id) == null) {
				response.status(401);
				return "User does not have read access";
			} else
				return model.getColumns();
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