package distributed;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.HashMap;

import org.json.simple.JSONObject;
import com.google.gson.Gson;
import gui.WorldModel;

/** A server that responds to HTTP requests. */
public class Server {
	/** The single instance of the server. */
	private static Server serverInstance;
	/** The port on which the server is run. */
	private static int portNumber;
	private final String readPassword;
	private final String writePassword;
	private final String adminPassword;
	private int session_id;
	private HashMap<Integer, String> sessionIdMap;
	private WorldModel model;

	
	private Server(int portNum, String readPass, String writePass, String adminPass) {
		portNumber = portNum;
		readPassword = readPass;
		writePassword = writePass;
		adminPassword = adminPass;
		sessionIdMap = new HashMap<Integer, String>();
	}

	public static void singletonConstructor(int portNum, String readPass, String writePass, String adminPass) {
		serverInstance = new Server(portNum, readPass, writePass, adminPass);
	}

	public static Server getInstance() {
		return serverInstance;
	}

	@SuppressWarnings("unchecked")
	/** Runs the server by receiving requests from the client and responding appropriately. */
	public void run() {
		Gson gson = new Gson();
		port(portNumber);

		post("/login", (request, response) -> {
			System.out.println("oksjdfhdfkjghdfkjghfdkjgh");
			System.out.println(request.body());
			response.header("Content-Type", "application/json");
			JSONObject responseValue = null;
			String json = request.body();
			LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
			String level = loginInfo.level;
			String password = loginInfo.password;
			if (level.equals("read") && password.equals(readPassword)) {
				session_id++;
				responseValue = new JSONObject();
				responseValue.put("session_id", new Integer(session_id));
				sessionIdMap.put(session_id, "read");
				return responseValue;
			} else if (level.equals("write") && password.equals(writePassword)) {
				session_id++;
				responseValue = new JSONObject();
				responseValue.put("session_id", new Integer(session_id));
				sessionIdMap.put(session_id, "write");
				return responseValue;
			} else if (level.equals("admin") && password.equals(adminPassword)) {
				session_id++;
				responseValue = new JSONObject();
				responseValue.put("session_id", new Integer(session_id));
				sessionIdMap.put(session_id, "admin");
				return responseValue;
			} else {
				response.status(401);
				return "Please enter in a proper password.";
			}

		}, gson::toJson);

		post("/login", (request, response) -> {
			System.out.println("oksjdfhdfkjghdfkjghfdkjgh");
			System.out.println(request.body());
			response.header("Content-Type", "application/json");
			JSONObject responseValue = null;
			String json = request.body();
			LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
			String level = loginInfo.level;
			String password = loginInfo.password;
			if (level.equals("read") && password.equals(readPassword)) {
				session_id++;
				responseValue = new JSONObject();
				responseValue.put("session_id", new Integer(session_id));
				sessionIdMap.put(session_id, "read");
				return responseValue;
			} else if (level.equals("write") && password.equals(writePassword)) {
				session_id++;
				responseValue = new JSONObject();
				responseValue.put("session_id", new Integer(session_id));
				sessionIdMap.put(session_id, "write");
				return responseValue;
			} else if (level.equals("admin") && password.equals(adminPassword)) {
				session_id++;
				responseValue = new JSONObject();
				responseValue.put("session_id", new Integer(session_id));
				sessionIdMap.put(session_id, "admin");
				return responseValue;
			} else {
				response.status(401);
				return "Please enter in a proper password.";
			}

		}, gson::toJson);
		
		post("/world", (request, response) -> {
			response.header("Content-Type", "text/plain");
			String json = request.body();
			LoadWorldInfoJSON loadWorldInfo = gson.fromJson(json, LoadWorldInfoJSON.class);
			String description = loadWorldInfo.getDescription();
			if (sessionIdMap.get(session_id) == null || !sessionIdMap.get(session_id).equals("admin")) {
				response.status(401);
				return "User does not have admin access.";
			} else {
				if (model != null) {
					model.getCritterMap(); // TODO why?
				}
				model = new WorldModel();
				model.loadNewWorld(description);
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
				if (model != null) {
					model.getCritterMap(); // TODO why?
				}
				model = new WorldModel();
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