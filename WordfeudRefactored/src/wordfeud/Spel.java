package wordfeud;

import java.util.Random;

public class Spel {
	private Bord bord;
	private Letterbak letterbak;
	private LetterSet letterSet;
	private int spelID;
	private SpelVerloop verloop;

	public Spel() {
		bord = new Bord();
		letterbak = new Letterbak();
		letterSet = new LetterSet("NL"); // Nederlands? :)
		spelID = new Integer(new Random().nextInt(20)); // ALLEEN VOOR TESTEN!!
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
}