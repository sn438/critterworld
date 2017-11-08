package main;

import java.io.*;
import ast.*;
import parse.*;

public class ParseAndMutateApp {
	public static void main(String[] args) throws Exception {
		int n = 0;
		String file;
		try {
			if (args.length == 1) {
				file = args[0];
				InputStream in = new FileInputStream(file);
				Reader r = new BufferedReader(new InputStreamReader(in));
				Parser p = ParserFactory.getParser();
				Program critter = p.parse(r);
				System.out.println(critter.toString());

			} else if (args.length == 3 && args[0].equals("--mutate")) {
				n = parsePositive(args[1]);
				file = args[2];
				InputStream in = new FileInputStream(file);
				Reader r = new BufferedReader(new InputStreamReader(in));
				Parser p = ParserFactory.getParser();
				Program critter = p.parse(r);

				for (int i = 0; i < n; i++)
					critter = critter.mutate();

				System.out.println(critter.toString());
			} else {
				throw new IllegalArgumentException();
			}
		} catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e) {
			System.out.println("Usage:\n" + "  <input_file>\n" + "  --mutate <n> <input_file");
		} catch (FileNotFoundException f) {
			System.out.println("File not found.");
		}
	}

	/**
	 * Parses {@code str} to an integer.
	 * 
	 * @param str
	 *            - the string to parse
	 * @return the integer represented by {@code str}
	 * @throws NumberFormatException
	 *             if {@code str} does not contain a parsable integer
	 * @throws IllegalArgumentException
	 *             if {@code str} represents a negative integer
	 */
	public static int parsePositive(String str) {
		int n = Integer.parseInt(str);
		if (n < 0)
			throw new IllegalArgumentException();
		else
			return n;
	}
}
