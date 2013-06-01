package wordfeud;

import java.util.ArrayList;

public class Letterbak
{
	private ArrayList<Tile> stenen;
	private Spel curSpel;
	
	public Letterbak(Spel currentSpel)
	{
		stenen = new ArrayList<Tile>();
		curSpel = currentSpel;
		for(int i = 0; i < 7; i++)
		{
			stenen.add(new Tile());
			stenen.get(i).setStone(new Stone('A', i));
		}
	}
	
	public ArrayList<Tile> getTiles()
	{
		return stenen;
	}
	
	public Tile getTile(int i)
	{
		return stenen.get(i);
	}
	
	public int getNumberOfStones()
	{
		int numStones = 0;
		for(int i = 0; i < 7; i++)
		{
			if(getTile(i).getStone() != null)
			{
				numStones++;
			}
		}
		return numStones;
	}
	
	//public void 
}
