package simulation;

public interface SimpleWorld
{
	int searchNearby(Critter c, int index);
	
	int searchAhead(Critter c, int index);

}