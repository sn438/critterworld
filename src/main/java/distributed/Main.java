package distributed;

import java.util.Arrays;

import gui.GUI;

public class Main {
	private static Server server;
	public static void main(String[] args) {
		GUI gui = null;
		String readPassword = null;
		String writePassword = null;
		String adminPassword = null;
		boolean serverLoaded = false;
		int portNumber = 0;
		try {
		if (args.length == 0) {
			GUI.main(null);
		} else if(args.length == 4) {
			portNumber = Integer.valueOf(args[0]);
			readPassword = args[1];
			writePassword = args[2];
			adminPassword = args[3];
			serverLoaded = true;
			server = new Server(portNumber, readPassword, writePassword, adminPassword);
			server.run();
			
		}
		} catch(Exception e) {
			System.out.println("Server was not started. Please start server first.");
		}

	}

}
