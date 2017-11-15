package distributed;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;
import com.google.gson.Gson;

/**
 * Server responds to HTTP requests.
 *
 */
public class Server {

	/**
	 * port on which the server will be run.
	 */
	private int portNumber;
	private String readPassword;
	private String writePassword;
	private String adminPassword;

	public Server(int portNum, String readPass, String writePass, String adminPass) {
		portNumber = portNum;
		readPassword = readPass;
		writePassword = writePass;
		adminPassword = adminPass;
	}

	public void run() {
		Gson gson;
		port(portNumber);

		
		get("/login", (request, response) -> "Hello World");
		
		post("/login2",
	            (request, response)
	                -> {
	                    response.header("Content-Type", "application/json");
	                    // Shows how to read information encoded in the URL
	                    return "Hello World";
	                });
	            //gson::toJson);

		get("/hello", (request, response) -> "Hello World");

	}
}