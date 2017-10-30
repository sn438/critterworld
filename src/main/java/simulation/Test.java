package simulation;

import java.io.FileNotFoundException;

public class Test
{
	public static void main(String[] args) throws FileNotFoundException, IllegalArgumentException
	{
		SimpleWorld sw = new World("a5world.txt");
		System.out.println(sw.printGrid().toString());
		
		/*for(int i = 0; i < 1000; i++)
			sw.advanceOneTimeStep();
		System.out.println(sw.printGrid().toString() + "\ncritter #: " + sw.numRemainingCritters());*/
		
		/*StringBuilder result = new StringBuilder();
		for(int i = 0; i < 68; i++)
		{
			StringBuilder sb = new StringBuilder();
			if(i % 2 == 0)
				sb.append("   ");
			for(int c = 0, r = i % 2; c < 50 && r < 68; c += 2, r++)
			{
				sb.append("" + c + "," + r + "   ");
			}
			result.insert(0, sb.toString() + "\n");
		}
		System.out.println(result.toString());*/
	}
}