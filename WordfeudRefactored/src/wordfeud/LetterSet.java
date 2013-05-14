package wordfeud;

import java.util.HashMap;

public class LetterSet
{
	public class Letter
	{
		public Letter(int waarde, int aantal)
		{
			this.waarde = waarde;
			this.aantal = aantal;
		}
		
		private int waarde;
		private int aantal;
		
		public int getWaarde()//
		{
			return waarde;
		}
		public int getAantal()
		{
			return aantal;
		}
	}
	
	private HashMap<Character, Letter> waarden;
	
	public LetterSet()
	{
		waarden = new HashMap<Character, Letter>();
		initDutch();
	}
	
	public LetterSet(String language_id)
	{
		waarden = new HashMap<Character, Letter>();
		setLanguage(language_id);
	}
	
	public Letter getLetter(char letter)
	{
		return waarden.get(letter);
	}
	
	public void setLanguage(String language_id)
	{
		switch(language_id)
		{
		case "nl-NL":
			initDutch();
			break;
		case "en-US":
		case "en-UK":
			initEnglish();
			break;
		default:
			initDutch();
			break;
		}
	}
	
	private void initDutch()
	{
		waarden.clear();
		waarden.put('A', new Letter(1, 7));
		waarden.put('B', new Letter(4, 2));
		waarden.put('C', new Letter(5, 2));
		waarden.put('D', new Letter(2, 5));
		waarden.put('E', new Letter(1, 18));
		waarden.put('F', new Letter(4, 2));
		waarden.put('G', new Letter(3, 3));
		waarden.put('H', new Letter(4, 2));
		waarden.put('I', new Letter(2, 4));
		waarden.put('J', new Letter(4, 2));
		waarden.put('K', new Letter(3, 3));
		waarden.put('L', new Letter(3, 3));
		waarden.put('M', new Letter(3, 3));
		waarden.put('N', new Letter(1, 11));
		waarden.put('O', new Letter(1, 6));
		waarden.put('P', new Letter(4, 2));
		waarden.put('Q', new Letter(10, 1));
		waarden.put('R', new Letter(2, 5));
		waarden.put('S', new Letter(2, 5));
		waarden.put('T', new Letter(2, 5));
		waarden.put('U', new Letter(2, 3));
		waarden.put('V', new Letter(4, 2));
		waarden.put('W', new Letter(5, 2));
		waarden.put('X', new Letter(8, 1));
		waarden.put('Y', new Letter(8, 1));
		waarden.put('Z', new Letter(5, 2));
	}
	
	private void initEnglish()
	{
		waarden.clear();
		waarden.put('A', new Letter(1, 10));
		waarden.put('B', new Letter(4, 2));
		waarden.put('C', new Letter(4, 2));
		waarden.put('D', new Letter(2, 5));
		waarden.put('E', new Letter(1, 12));
		waarden.put('F', new Letter(4, 2));
		waarden.put('G', new Letter(3, 3));
		waarden.put('H', new Letter(4, 3));
		waarden.put('I', new Letter(1, 9));
		waarden.put('J', new Letter(10, 1));
		waarden.put('K', new Letter(5, 1));
		waarden.put('L', new Letter(1, 4));
		waarden.put('M', new Letter(3, 2));
		waarden.put('N', new Letter(1, 6));
		waarden.put('O', new Letter(1, 7));
		waarden.put('P', new Letter(4, 2));
		waarden.put('Q', new Letter(10, 1));
		waarden.put('R', new Letter(1, 6));
		waarden.put('S', new Letter(1, 5));
		waarden.put('T', new Letter(1, 7));
		waarden.put('U', new Letter(2, 4));
		waarden.put('V', new Letter(4, 2));
		waarden.put('W', new Letter(4, 2));
		waarden.put('X', new Letter(8, 1));
		waarden.put('Y', new Letter(4, 2));
		waarden.put('Z', new Letter(10, 1));
	}
}
