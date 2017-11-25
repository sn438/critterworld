package distributed;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.google.gson.Gson;

import simulation.SimpleWorld;
import simulation.World;

/**
 * Server responds to HTTP requests.
 *
 */
public class Server {

	/**
	 * port on which the server will be run.
	 */
	private static Server serverInstance; // why are these static??
	private static int portNumber; 
	private final String readPassword;
	private final String writePassword;
	private final String adminPassword;
	private int sessionId;
	private Map<Integer, String> sessionIdMap;
	private SimpleWorld world;

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
	/**
	 * 
	 */
	public void run() {
		Gson gson = new Gson();
		port(portNumber);

		
		post("/login", (request, response) -> {
			
			response.header("Content-Type", "application/json");
			JSONObject responseValue = null;
			String json = request.body();
			LoginInfo loginInfo = gson.fromJson(json, LoginInfo.class);
			String level = loginInfo.level;
			String password = loginInfo.password;
			if (level.equals("read") && password.equals(readPassword)) {
				System.out.println("hello");
				sessionId++;
				responseValue = new JSONObject();
				responseValue.put("sessionId", new Integer(sessionId));
				sessionIdMap.put(sessionId, "read");
				return responseValue;
			} else if (level.equals("write") && password.equals(writePassword)) {
				sessionId++;
				responseValue = new JSONObject();
				responseValue.put("sessionId", new Integer(sessionId));
				sessionIdMap.put(sessionId, "write");
				return responseValue;
			} else if (level.equals("admin") && password.equals(adminPassword)) {
				sessionId++;
				responseValue = new JSONObject();
				responseValue.put("sessionId", new Integer(sessionId));
				sessionIdMap.put(sessionId, "admin");
				return responseValue;
			} else {
				response.status(401);
				return "Please enter in proper password.";
			}

		}, gson::toJson);
		
		 post("/world/generic", (request, response) -> {
			 response.header("Content-Type", "application/json");
			 int session_id = Integer.parseInt(request.attribute("session_id"));
			 System.out.println(session_id);
			 if (!sessionIdMap.get(session_id).equals("admin")) {
				 response.status(401);
				return "User does not have admin access.";
			 } else {
				 world = new World(); 
			 return "Ok";
			 }
		 }, gson::toJson);
	}

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