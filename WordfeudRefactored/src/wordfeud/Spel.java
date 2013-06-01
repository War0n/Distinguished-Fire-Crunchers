package wordfeud;

import java.util.Random;

public class Spel {
	private Bord bord;
	private Letterbak letterbak;
	private LetterSet letterSet;
	private int spelID;
	private SpelVerloop verloop;
	private SpelPanel spelPanel;

	public Spel(SpelPanel panel, int clickedSpelID) {
		letterbak = new Letterbak(this);
		letterSet = new LetterSet("NL"); // Nederlands? :)
		spelID = clickedSpelID;//new Integer(new Random().nextInt(20)); // ALLEEN VOOR TESTEN!!
		bord = new Bord(this, "placeHolderName");
		spelPanel = panel;
		verloop = new SpelVerloop(this);
	}
	
	public Spel(int clickedSpelID) {
		letterbak = new Letterbak(this);
		letterSet = new LetterSet("NL"); // Nederlands? :)
		spelID = clickedSpelID;
		bord = new Bord(this, "placeHolderName");
		spelPanel = new SpelPanel(clickedSpelID);
		verloop = new SpelVerloop(this);
	}

	public LetterSet getLetterSet() {
		return letterSet;
	}

	public Letterbak getLetterBak() {
		return letterbak;
	}

	public Bord getBord() {
		return bord;
	}

	public int getSpelId() {
		return spelID;
	}

	public SpelVerloop getVerloop() {
		return verloop;
	}
	
	public void setSpelPanel(SpelPanel panel)
	{
		this.spelPanel = panel;
	}
	
	public SpelPanel getSpelPanel(){//knoppen kunnen disabelen in verloop
		return spelPanel;
	}
}