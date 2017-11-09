package gui;

import java.io.InputStream;

public class GettingResources
{
	public static void main(String[] args)
	{
		InputStream in = WorldMap.class.getClassLoader().getResourceAsStream("gui/images/critter_0.png");
		if(in == null)
			System.out.println("AHHHHHH");
		else
			System.out.println("yes");
	}
}
