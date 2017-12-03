package distributed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.google.gson.Gson;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;


import simulation.FileParser;
import simulation.Hex;
import simulation.SimpleCritter;
import simulation.WorldObject;

/** A class to handle client requests to the server. */
public class ClientRequestHandler {
	/** Stores the most recently retrieved version of the world. */
	private int mostRecentVersion;
	private String url;

	public ClientRequestHandler(String url) {
		this.url = url;
	}

	/**
	 *
	 * @param sessionId
	 * @return
	 */
	public boolean createNewWorld(int sessionId) {
		Gson gson = new Gson();
		String description = "name Arrakis\r\nsize 50 68\r\n";
		boolean[][] isNotValidSpace = new boolean[50][68];

		// randomly fills about 1/40 of the hexes in the world with rocks
		int c = (int) (Math.random() * 50);
		int r = (int) (Math.random() * 68);
		int n = 0;
		while (n < 2150 / 40) {
			c = (int) (Math.random() * 50);
			r = (int) (Math.random() * 68);
			if (isValidHex(c, r, 50, 68) && isNotValidSpace[c][r] == false) {
				isNotValidSpace[c][r] = true;
				description += "rock " + c + " " + r + "\r\n";
				n++;
			}
		}
		LoadWorldInfoJSON loadWorldInfo = new LoadWorldInfoJSON(description);
		URL url = null;
		try {
			url = new URL(this.url + "/world?session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(loadWorldInfo, LoadWorldInfoJSON.class));
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setHeaderText("Access Denied");
				alert.setContentText("You do not have permission to create a new world.");
				return false;
			}
			BufferedReader r1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			System.out.println(r1.readLine());
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			e.printStackTrace(); //TODO remove
			System.out.println("Could not connect to the server");
		}
		return true;

	}

	/**
	 *
	 * @param worldfile
	 * @param sessionId
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IOException
	 */
	public boolean loadWorld(File worldfile, int sessionId) throws IllegalArgumentException, IOException {
		Gson gson = new Gson();
		BufferedReader br = new BufferedReader(new FileReader(worldfile));
		String description = "";
		String currentLine = br.readLine();
		while (currentLine != null) {
			if (currentLine.indexOf("critter") == -1) {
				description += currentLine;
				description += "\r\n";
				currentLine = br.readLine();
			} else {
				currentLine = br.readLine();
			}
		}
		LoadWorldInfoJSON loadWorldInfo = new LoadWorldInfoJSON(description);
		URL url = null;
		try {
			url = new URL(this.url + "/world?session_id=" + sessionId);
			// url = new URL("http://localhost:" + 8080 + "/world?session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(loadWorldInfo, LoadWorldInfoJSON.class));
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setHeaderText("Access Denied");
				alert.setContentText("You do not have permission to create a new world.");
				return false;
			}
			BufferedReader r1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			if (!r1.readLine().equals("Ok")) {
				return false;
			}
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			e.printStackTrace(); //TODO remove
			System.out.println("Could not connect to the server");
		} finally {
			br.close();
		}
		return true;

	}

	/**
	 * Returns the number of columns in the world.
	 * @return The number of columns, or -1 if the user does not have permission
	 */
	public int getColumns(int sessionId) {
		Gson gson = new Gson();
		URL url = null;
		int returnValue = 0;
		try {
			url = new URL(this.url + "/world?session_id=" + sessionId + "&update_since=" + this.mostRecentVersion);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setHeaderText("Access Denied");
				alert.setContentText("User is not an admin so a New World cannot be created.");
				return -1;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			WorldStateJSON worldState =  gson.fromJson(r, WorldStateJSON.class);
			returnValue = worldState.getCols();
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
		return returnValue;
	}

	public int getRows(int sessionId) {
		Gson gson = new Gson();
		URL url = null;
		int returnValue = 0;
		try {
			url = new URL(this.url + "/world?session_id=" + sessionId + "&update_since=" + this.mostRecentVersion);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setHeaderText("Access Denied");
				alert.setContentText("User is not an admin so a New World cannot be created.");
				return -1;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			WorldStateJSON worldState =  gson.fromJson(r, WorldStateJSON.class);
			returnValue = worldState.getRows();
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
		return returnValue;
	}

	public void advanceTime() {
		// TODO Auto-generated method stub

	}

	/**
	 * 
	 * @param f
	 * @param n
	 * @param sessionId
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 */
	public void loadRandomCritters(File f, int n, int sessionId) throws FileNotFoundException, IllegalArgumentException {
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		SimpleCritter critter = FileParser.parseCritter(reader, 8, 0);
		if(critter == null)
			throw new IllegalArgumentException();
		CritterJSON critterJSON = null;
		String programDescription = critter.getProgram().toString();
		int[] mem = critter.getMemoryCopy();
		critterJSON = new CritterJSON(critter.getName(), programDescription, mem, n);
		URL url;
		try {
			url = new URL(this.url + "/critters?session_id=" + sessionId);
			// url = new URL("http://localhost:" + 8080 + "/critters?session_id=" +
			// sessionId);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(critterJSON, CritterJSON.class));
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setHeaderText("Access Denied");
				alert.setContentText("You do not have permission to add critters.");
			}
			System.out.println(gson.toJson(critterJSON, CritterJSON.class));
			BufferedReader r1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = r1.readLine();
			while (line != null) {
				System.out.println(line);
				line = r1.readLine();
			}
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			e.printStackTrace(); //TODO remove
			System.out.println("Could not connect to the server");
		}
	}

	/**
	 * 
	 * @param f
	 * @param c
	 * @param r
	 * @param sessionId
	 * @throws FileNotFoundException
	 * @throws IllegalArgumentException
	 */
	public void loadCritterAtLocation(File f, int c, int r, int sessionId) throws FileNotFoundException, IllegalArgumentException {
		Gson gson = new Gson();
		BufferedReader reader = new BufferedReader(new FileReader(f));
		SimpleCritter critter = FileParser.parseCritter(reader, 8, 0);
		if(critter == null)
			throw new IllegalArgumentException();
		String programDescription = critter.getProgram().toString();
		int[] mem = critter.getMemoryCopy();
		PositionJSON[] positions = new PositionJSON[] { new PositionJSON(c, r) };
		CritterJSON critterJSON = new CritterJSON(critter.getName(), programDescription, mem, positions);
		URL requestURL;
		try {
			requestURL = new URL(this.url + "/critters?session_id=" + sessionId);
			// url = new URL("http://localhost:" + 8080 + "/critters?session_id=" +
			// sessionId);
			System.out.println(requestURL);
			HttpURLConnection connection = (HttpURLConnection) requestURL.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(critterJSON, CritterJSON.class));
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Access Denied");
				alert.setContentText("The user cannot create a new world because the user is not an admin.");
			}
			//System.out.println(gson.toJson(critterJSON, CritterJSON.class));
			BufferedReader r1 = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line = r1.readLine();
			while (line != null) {
				System.out.println(line);
				line = r1.readLine();
			}
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			e.printStackTrace(); //TODO remove
			System.out.println("Could not connect to the server");
		}
	}
	
	/**
	 *
	 * @param sessionID
	 * @param updateSince
	 * @return
	 */
	public WorldStateJSON updateSince(int sessionID, int updateSince) {
		Gson gson = new Gson();
		URL url;
		try {
			url = new URL("http://localhost:" + 8080 + "/world?session_id=" + sessionID + "&update_since=" + updateSince);
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Length", "0");
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setHeaderText("Access Denied");
				alert.setContentText("You do not have permission to view the world.");
				return null;
			} else if (connection.getResponseCode() == 403) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Invalid Request");
				alert.setContentText("A world must be loaded before you can view the world state.");
				return null;
			} else if (connection.getResponseCode() == 406) {
				return null;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			WorldStateJSON state = gson.fromJson(r, WorldStateJSON.class);
			return state;

		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
			return null;
		} catch (IOException e) {
			e.printStackTrace(); //TODO remove
			System.out.println("Could not connect to the server");
			return null;
		}
	}

	private boolean isValidHex(int c, int r, int columns, int rows) {
		if (c < 0 || r < 0)
			return false;
		else if (c >= columns || r >= rows)
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}
}
