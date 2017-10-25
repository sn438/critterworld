package console;

import java.io.*;

public class FileParser
{
	/**
	 * Parses the attributes for a critter from a file.
	 * @param filename
	 * @return a String array containing the memory attributes needed to create the critter.
	 */
	public static String[] parseAttributes(BufferedReader br)
	{
		String name = parseAttributeFromLine(br, "species: ");
		String memsize = parseAttributeFromLine(br, "memsize: ");
		String defense = parseAttributeFromLine(br, "defense: ");
		String offense = parseAttributeFromLine(br, "offense: ");
		String size = parseAttributeFromLine(br, "size: ");
		String energy = parseAttributeFromLine(br, "energy: ");
		String posture = parseAttributeFromLine(br, "posture: ");
		
		return new String[] {name, memsize, defense, offense, size, energy, posture};
	}
	
	/**
	 * Given a BufferedReader, parses one attribute line from a critter file and returns a string containing only the attribute by
	 * trimming out a specified substring {@code substringToCut}. If {@code substringToCut} is not present in the line or the end
	 * of the file is reached, returns an empty string.
	 * 
	 * @param b the BufferedReader to read lines from
	 * @param substringToCut the substring to trim
	 * @return A string containing only the critter attribute given on the line
	 */
	private static String parseAttributeFromLine(BufferedReader b, String substringToCut)
	{
		String result = "";
		try
		{
			String line = b.readLine();
			int len = substringToCut.length();
			if(line != null && line.startsWith(substringToCut) && len < line.length())
				result = line.substring(len);
		}
		catch (IOException e)
		{
			return "";
		}
		return result;
	}
	
	public static String parseConstants(String filename) throws FileNotFoundException
	{
		//TODO implement
		return null;
	}
}