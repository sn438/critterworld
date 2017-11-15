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
		port(portNumber);

		get("/hello", (request, response) -> "Hello World");
	}
}