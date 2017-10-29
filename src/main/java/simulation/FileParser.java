package simulation;

import java.io.*;
import java.util.HashMap;

import ast.Program;
import parse.Parser;
import parse.ParserFactory;

/** This class supplies several static methods that may be useful for parsing files needed for world creation and modification. */
public class FileParser
{
	public static SimpleCritter parseCritter(BufferedReader br, int minMemory, int direction)
	{
		String[] parsed = parseAttributes(br);
		String name = parsed[0].equals("") ? "Untitled #" : parsed[0];
		int[] critMem = FileParser.makeCritterMemory(parsed, minMemory);
		
		Parser p = ParserFactory.getParser();
		Program prog = p.parse(br);
		
		if((direction < 0 || direction > 5))
			return new Critter(prog, critMem, name);
		return new Critter(prog, critMem, name, direction);
	}
	
	/**
	 * Parses the attributes for a critter from a file into a String array.
	 * @param filename
	 * @return a String array containing the memory attributes needed to create the critter.
	 */
	private static String[] parseAttributes(BufferedReader br)
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
	 * Given a BufferedReader, parses one attribute line from a file and returns a string containing only the attribute by
	 * trimming out a specified substring {@code substringToCut}. If {@code substringToCut} is not present in the line or the end
	 * of the file is reached, returns an empty string.
	 * 
	 * @param b the BufferedReader to read lines from
	 * @param substringToCut the substring to trim
	 * @return A string containing only the attribute given on the line
	 */
	public static String parseAttributeFromLine(BufferedReader b, String substringToCut)
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
	
	/**
	 * Prepares an int array to be used as critter memory, based on a string array.<br>
	 * Precondition: the parameter {@code strs} MUST have been generated by the method {@code FileParser.parseAttributes(filename)}.
	 * @param strs an array of strings created by the method {@code FileParser.parseAttributes(filename)}
	 * @return an int array, ready to be used as critter memory. Returns a default set of memory if {@code strs} is not compatible.
	 */
	private static int[] makeCritterMemory(String[] strs, int minMemory)
	{
		//if the strs array is less than 7, then we revert to a default set of memory
		if(strs.length <= 7)
			return new int[] {minMemory, 3, 3, 1, 500, 0, 0, 0};
		
		int[] critterAttributes;
		
		int memsize = parseIntFromString(strs[1]);
		if(memsize < minMemory)
			memsize = minMemory;
		critterAttributes = new int[memsize];
		critterAttributes[0] = memsize;
		
		int defense = parseIntFromString(strs[2]);
		if(defense < 0)
			defense = 3;
		critterAttributes[1] = defense;
		
		int offense = parseIntFromString(strs[3]);
		if(offense < 0)
		offense = 3;
		critterAttributes[2] = offense;
		
		int size = parseIntFromString(strs[4]);
		if(size < 0)
			size = 1;
		critterAttributes[3] = size;
		
		int energy = parseIntFromString(strs[5]);
		if(energy < 0)
			energy = 500;
		critterAttributes[4] = energy;
		
		int pass = 0;
		critterAttributes[5] = pass;
		
		int tag = 0;
		critterAttributes[6] = tag;
		
		int posture = parseIntFromString(strs[5]);
		if(posture < 0 || posture > 99)
			posture = 0;
		critterAttributes[7] = posture;
		
		for(int i = 8; i < memsize; i++)
			critterAttributes[i] = 0;
		
		return critterAttributes;
	}
	/**
	 * Parses an integer value from a given string, or returns -1 if no integer was found.
	 * @param s the string to parse
	 * @return the int parsed from the string, or -1 if no integer was found
	 */
	private static int parseIntFromString(String s)
	{
		try
		{
			return Integer.parseInt(s);
		}
		catch (NumberFormatException n)
		{
			return -1;
		}
	}
	
	/**
	 * Parses constants from a file and returns them in the form of a HashMap. Assumes that the file adheres to the format
	 * specified by the <a href="http://www.cs.cornell.edu/courses/cs2112/2017fa/project/constants.txt">constants file given
	 * to us</a>.
	 * @param b a BufferedReader containing the file to be read.
	 * @return a HashMap containing the names of constants mapped to their values.
	 * @throws IllegalArgumentException if the file is not valid.
	 */
	public static HashMap<String, Double> parseConstants(BufferedReader b) throws IllegalArgumentException
	{
		HashMap<String, Double> result = new HashMap<String, Double>();
		try
		{
			String line = b.readLine();
			while(line != null)
			{
				String[] constant = line.split(" ");
				result.put(constant[0], Double.parseDouble(constant[1]));
				line = b.readLine();
			}
		}
		//If the constants file has any irregularities,
		catch (Exception e)
		{
			throw new IllegalArgumentException();
		}
		return result;
	}
}