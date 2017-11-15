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
	
	public Server(int portNumber, String readPassword, String writePassword, String adminPassword) {
		this.portNumber = portNumber;
		this.readPassword = readPassword;
		this.writePassword = writePassword;
		this.adminPassword = adminPassword;
	}
	public void run() {
		port(portNumber);
		
		get("/hello", (request, response) -> "Hello World");
		
		
	}
}
