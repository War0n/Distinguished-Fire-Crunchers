package wordfeud;

import java.util.Random;

public class Spel {
	private Bord bord;
	private Letterbak letterbak;
	private LetterSet letterSet;
	private int spelID;
	private SpelVerloop verloop;
	private SpelPanel spelPanel;

	public Spel() {
		letterbak = new Letterbak();
		letterSet = new LetterSet("NL"); // Nederlands? :)
		spelID = 511;//new Integer(new Random().nextInt(20)); // ALLEEN VOOR TESTEN!!
		bord = new Bord(this, "placeHolderName");
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
	
	public SpelPanel getSpelPanel(){
		
		return spelPanel;
	}
}