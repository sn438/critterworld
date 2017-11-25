package distributed;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map.Entry;

import com.google.gson.Gson;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Alert.AlertType;

import java.util.Optional;
import java.util.Set;

import simulation.Hex;
import simulation.SimpleCritter;
import simulation.WorldObject;

public class ClientHandler {

	
	public boolean createNewWorld(int sessionId) {
		Gson gson = new Gson();
		URL url = null;
		try {
			url = new URL("http://localhost:" + 8080 + "/world/generic/session_id=" + sessionId);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			System.out.println(url.toString());
			connection.setDoOutput(true); // send a POST message
			connection.setRequestMethod("POST");
			PrintWriter w = new PrintWriter(connection.getOutputStream());
			w.println();
			w.flush();
			if (connection.getResponseCode() == 401) {
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Login Error");
				alert.setHeaderText("Login Information Was False");
				alert.setContentText("User is not an admin so a New World cannot be created.");
				return false;
			}
		} catch (MalformedURLException e) {
			System.out.println("The URL entered was not correct.");
		} catch (IOException e) {
			System.out.println("Could not connect to the server");
		}
		return true;
	}

	
	public void loadWorld(File worldfile, int sessionId) throws FileNotFoundException, IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	
	public boolean isReady() {
		// TODO Auto-generated method stub
		return false;
	}

	
	public int getColumns() {
		// TODO Auto-generated method stub
		return 0;
	}

	
	public int getRows() {
		// TODO Auto-generated method stub
		return 0;
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

	
}
