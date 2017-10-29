package simulation;

import java.io.FileNotFoundException;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException
	{
		SimpleWorld sw = new World("biggerworld.txt");
		System.out.println(sw.printGrid().toString());
		
		for(int i = 0; i < 1000; i++)
			sw.advanceOneTimeStep();
		System.out.println(sw.printGrid().toString());
	}
}