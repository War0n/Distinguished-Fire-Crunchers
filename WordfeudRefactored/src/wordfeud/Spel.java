package wordfeud;

public class Spel
{
	private Bord bord; 
	private Letterbak letterbak;
	private LetterSet letterSet;
	private int spelID;
	
	public Spel()
	{
		bord = new Bord();
		letterbak = new Letterbak();
		letterSet = new LetterSet("nl-NL"); // Nederlands? :)
	}
	
	public LetterSet getLetterSet()
	{
		return letterSet;
	}
	
	public Letterbak getLetterBak()
	{
		return letterbak;
	}
	
	public Bord getBord()
	{
		return bord;
	}
	
	public int getSpelId(){
		return spelID;
	}
}