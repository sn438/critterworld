package simulation;

import java.io.FileNotFoundException;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException
	{
		SimpleWorld sw = new World("world.txt");
		System.out.println(sw.printGrid().toString() + "\ncritter #: " + sw.numRemainingCritters());
		
		for(int i = 0; i < 1000; i++)
			sw.advanceOneTimeStep();
		System.out.println(sw.printGrid().toString() + "\ncritter #: " + sw.numRemainingCritters());
	}
}