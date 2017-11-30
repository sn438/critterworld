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
import java.util.Map.Entry;

import com.google.gson.Gson;

import gui.WorldModel;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;
import java.util.Set;

import simulation.Hex;
import simulation.Rock;
import simulation.SimpleCritter;
import simulation.WorldObject;

/** A class to handle client requests to the server. */
public class ClientRequestHandler {

	public boolean createNewWorld(int sessionId) {
		/*
		 * // We can probably refactor this into one class handling this block Gson gson
		 * = new Gson(); URL url = null; try { url = new URL("http://localhost:" + 8080
		 * + "/world/generic?session_id=" + sessionId); HttpURLConnection connection =
		 * (HttpURLConnection) url.openConnection(); System.out.println(url.toString());
		 * connection.connect(); if (connection.getResponseCode() == 401) { Alert alert
		 * = new Alert(AlertType.ERROR); alert.setTitle("Login Error");
		 * alert.setHeaderText("Access Denied"); alert.
		 * setContentText("The user cannot create a new world because the user is not an admin."
		 * ); return false; } BufferedReader r = new BufferedReader(new
		 * InputStreamReader(connection.getInputStream()));
		 * System.out.println(r.readLine()); } catch (MalformedURLException e) {
		 * System.out.println("The URL entered was not correct."); } catch (IOException
		 * e) { System.out.println("Could not connect to the server."); } return true;
		 */

		Gson gson = new Gson();
		String description = "name Arrakis\r\nsize 50 68\r\n";
		Hex[][] grid = new Hex[50][68];
		boolean [][] isNotValidSpace = new boolean[50][68];
	
		// randomly fills about 1/40 of the hexes in the world with rocks
		int c = (int) (Math.random() * 50);
		int r = (int) (Math.random() * 68);
		int n = 0;
		while (n < 2150 / 40)
		{
			c = (int) (Math.random() * 50);
			r = (int) (Math.random() * 68);
			if (isValidHex(c, r, 50, 68) && isNotValidSpace[c][r] == false)
			{
				isNotValidSpace[c][r] = true;
				description += "rock " + c + " " + r + "\r\n";
				n++;
			}
		}
		LoadWorldInfoJSON loadWorldInfo = new LoadWorldInfoJSON(description);
		URL url = null;
		try {
			//url = new URL("http://hexworld.herokuapp.com:80/hexworld/world?session_id=423956134");
			 url = new URL("http://localhost:" + 8080 + "/world?session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(loadWorldInfo, LoadWorldInfoJSON.class));
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Access Denied");
				alert.setContentText("The user cannot create a new world because the user is not an admin.");
				return false;
			}
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
		return true;

	}

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
		// System.out.println(description);
		LoadWorldInfoJSON loadWorldInfo = new LoadWorldInfoJSON(description);
		URL url = null;
		try {
			// url = new
			// URL("http://hexworld.herokuapp.com:80/hexworld/world?session_id=423956134");
			url = new URL("http://localhost:" + 8080 + "/world?session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println(gson.toJson(loadWorldInfo, LoadWorldInfoJSON.class));
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Access Denied");
				alert.setContentText("The user cannot create a new world because the user is not an admin.");
				return false;
			}
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
		return true;

	}

	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	/**
	 * Returns the number of columns in the world.
	 * 
	 * @return The number of columns, or -1 if the user does not have permission
	 */
	public int getColumns(int sessionId) {
		Gson gson = new Gson();
		URL url = null;
		int returnValue = 0;
		try {
			url = new URL("http://localhost:" + 8080 + "/colNum?session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Access Denied");
				alert.setContentText("User is not an admin so a New World cannot be created.");
				return -1;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			returnValue = Integer.parseInt(r.readLine());

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
			url = new URL("http://localhost:" + 8080 + "/rowNum?session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.connect();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Login Information Was False");
				alert.setContentText("User is not an admin so a New World cannot be created.");
				return 0;
			}
			BufferedReader r = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			returnValue = Integer.parseInt(r.readLine());

		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
		return returnValue;
	}

	public int hexContent(int c, int r) {
		// TODO Auto-generated method stub
		return 0;
	}

	public SimpleCritter getCritter(int c, int r) {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Entry<SimpleCritter, Hex>> getCritterMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public Set<Entry<WorldObject, Hex>> getObjectMap() {
		// TODO Auto-generated method stub
		return null;
	}

	public void advanceTime() {
		// TODO Auto-generated method stub

	}

	public void loadRandomCritters(File f, int n) {
		// TODO Auto-generated method stub

	}

	public void loadCritterAtLocation(File f, int c, int r) {
		// TODO Auto-generated method stub

	}
	
	private boolean isValidHex(int c, int r, int columns, int rows)
	{
		if (c < 0 || r < 0)
			return false;
		else if (c >= columns || r >= rows)
			return false;
		else if ((2 * r - c) < 0 || (2 * r - c) >= (2 * rows - columns))
			return false;
		return true;
	}
}