package wordfeud;

import java.util.ArrayList;

public class Letterbak
{
	private ArrayList<Stone> stenen;
	
	public Letterbak()
	{
		stenen = new ArrayList<Stone>();
		stenen.add(new Stone('A'));//
		stenen.add(new Stone('B'));
		stenen.add(new Stone('C'));
		stenen.add(new Stone('D'));
		stenen.add(new Stone('E'));
		stenen.add(new Stone('F'));
		stenen.add(new Stone('G'));
	}
	
	public ArrayList<Stone> getStones()
	{
		return stenen;
	}
}
