package simulation;

import java.io.FileNotFoundException;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException
	{
		SimpleWorld sw = new World("world.txt");
		System.out.println(sw.printGrid().toString());
	}
}