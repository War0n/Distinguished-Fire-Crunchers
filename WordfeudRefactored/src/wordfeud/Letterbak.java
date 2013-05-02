package wordfeud;

import java.util.ArrayList;

public class Letterbak
{
	private ArrayList<Stone> stenen;
	
	public Letterbak()
	{
		stenen = new ArrayList<Stone>();
		for(int i = 0; i < 7; i++)
		{
			stenen.add(new Stone('A'));
		}
	}
	
	public ArrayList<Stone> getStones()
	{
		return stenen;
	}
}
