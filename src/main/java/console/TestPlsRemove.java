package console;
import simulation.World;

public class TestPlsRemove
{
	public static void main(String[] args)
	{
		World w = new World("name Small world\r\n" + 
				"size 10 15\r\n" + 
				"rock 2 2\r\n" + 
				"rock 3 6\r\n" + 
				"rock 9 10\r\n" + 
				"\r\n" + 
				"// Some food\r\n" + 
				"food 4 4 500\r\n" + 
				"food 1 3 1000\r\n" + 
				"\r\n" + 
				"// example-critter.txt should be in the working directory\r\n" + 
				"critter example-critter.txt 2 5 3\r\n" + 
				"critter example-critter.txt 4 3 1\r\n" + 
				"critter example-critter.txt 4 4 2\r\n" + 
				"");
		System.out.println(w.printGrid());
	}
}
