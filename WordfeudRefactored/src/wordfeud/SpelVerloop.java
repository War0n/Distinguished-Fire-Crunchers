package wordfeud;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map.Entry;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SpelVerloop implements Runnable, ActionListener{
	private Spel spel;
	private ArrayList<HashMap<Point, Stone>> woordenLijst;
	private int gepasst;
	private Account account;
	private boolean spelOver;
	private ResultSet myResultSet;
	private Connectie connect;
	private int beurt;
	private Bord spelBord;
	private String woordCheck;
	
	private ArrayList<String> gelegdeWoordenInBeurt;

	public SpelVerloop(Spel spel) {
		spelOver = false;
		this.spel = spel;
		account = new Account();
		Thread checkBeurten = new Thread(this);
		checkBeurten.start();

		spelBord = spel.getBord();
		woordCheck = "";
	}

	class letterSortStruct
	{
		int pos;
		char val;
		
		public letterSortStruct(int p, char c)
		{
			pos = p;
			val = c;
		}
	}
	
	public class letterCompare implements Comparator<letterSortStruct> {
	    public int compare(letterSortStruct a, letterSortStruct b) {
	        if (a.pos < b.pos) {
	            return -1;
	        }
	        else if (a.pos > b.pos) {
	            return 1;
	        }
	        else {
	            return 0;
	        }
	    }
	}

	public void play() {
		ArrayList<HashMap<Point, Stone>> myList = vindWoord();
		if( myList != null && myList.size() > 0)
		{
			ArrayList<String> woordenGevonden = new ArrayList<String>();
			for(int i = 0; i < myList.size(); i++)
			{
				ArrayList<letterSortStruct> letters = new ArrayList<letterSortStruct>();
				String tmp = "";
				for(Point pt : myList.get(i).keySet())
				{
					int curPt = pt.y * 15 + pt.x;
					letters.add(new letterSortStruct(curPt, myList.get(i).get(pt).isBlancoLetter()?myList.get(i).get(pt).getBlancoLetter() : myList.get(i).get(pt).getLetter()));
				}
				Collections.sort(letters, new letterCompare());
				for(letterSortStruct ss : letters)
				{
					tmp += ss.val;
				}
				woordenGevonden.add(tmp);
			}
			System.out.println("Woorden gevonden: "+woordenGevonden.size());
			int numWoorden = 0;
			gelegdeWoordenInBeurt = new ArrayList<String>();
			for(String str : woordenGevonden)
			{
				if(checkWoordInDB(str) == true)
				{
					numWoorden++;
					System.out.println(str + " is een woord.");
					gelegdeWoordenInBeurt.add(str);
				}
				else
				{
					woordCheck = str; // Moderator voor goedkeuring vragen
					int selection = JOptionPane.showConfirmDialog(
                            null
                    , "" + woordCheck + " is geen geldig woord, wil je dit woord naar de moderator sturen voor goedkeuring?"
                    , "Ongeldig woord"
                    , JOptionPane.OK_CANCEL_OPTION
                    , JOptionPane.INFORMATION_MESSAGE);     
					if (selection == JOptionPane.OK_OPTION){
						askModerator(woordCheck);
		            }
				}
			}
			if( numWoorden == woordenGevonden.size() && woordenGevonden.size() > 0)
			{
				doTurn("Word", true);
				pushLettersNaarDatabase();
				spel.getBord().lockField();
			}
		}
		//spel.getVerloop().doTurn("Word");
	}
	
	public void pushLettersNaarDatabase()
	{
		Connectie con = new Connectie();
		HashMap<Point, Stone> tiles = spelBord.getNewTiles();
		for(Point pt : tiles.keySet())
		{
			System.out.println("LetterID "+tiles.get(pt).getLetterId()+"\nSpel ID: " + spel.getSpelId() + "\nX: " + pt.x + "\nY: " + pt.y + "\nBord naam: " + spelBord.getName() + "\nBlanco:" + (tiles.get(pt).isBlancoLetter() ? "Ja, " + tiles.get(pt).getBlancoLetter() + "\n" : "Nee. NULL\n") );
			con.doInsertUpdate("INSERT INTO gelegdeletter (Letter_ID, Spel_ID, Beurt_ID, Tegel_X, Tegel_Y, Tegel_Bord_naam, BlancoLetterKarakter) VALUES (%1$d, %2$d, (SELECT MAX(ID) FROM beurt WHERE Spel_ID = %2$d), %3$d, %4$d, '%5$s', %6$s)", tiles.get(pt).getLetterId(), spel.getSpelId(), pt.x+1, pt.y+1, spelBord.getName(), tiles.get(pt).isBlancoLetter() ? "'"+tiles.get(pt).getBlancoLetter()+"'" : "NULL" );
		}
		
		String bericht = "[LEGT] ";
		for(String str : gelegdeWoordenInBeurt){
			bericht += str + " ";
		}
		
		String q = "INSERT INTO chatregel VALUES('" + Account.getAccountNaam() + "',"+ spel.getSpelId() +",NOW(),'"+ bericht +"')";
		con.doInsertUpdate(q);
		con.closeConnection();
	}

	@SuppressWarnings("static-access")
	public boolean myTurn() {
		boolean myTurn = false;
		Connectie con = new Connectie();
		ResultSet rs;
		String accountNaam = "";
		String beurtType = "";
		rs = con.doSelect("SELECT Account_naam, ID, Aktie_type FROM beurt WHERE Spel_ID = "
				+ spel.getSpelId() + " ORDER BY ID DESC LIMIT 1");
		try {
			if (rs.next()) {
				accountNaam = rs.getString("Account_naam");
				beurtType = rs.getString("Aktie_type");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!account.getAccountNaam().equals(accountNaam)) 
		{
			if( !beurtType.equals("Win") )
				myTurn = true;
		}
		winByPass(); //kijken of er 3x gepasst is
		con.closeConnection();
		return myTurn;
	}
	
	public void doTurn(String moveType, boolean bUpdateScore) {
		if (!myTurn()) {
			return;
		}
		Connectie con = new Connectie();
		ResultSet rs;
		try {
			rs = con.voerSelectQueryUit("SELECT MAX(ID) FROM beurt WHERE Spel_ID = "
					+ spel.getSpelId());
			if (rs.next()) {
				int ID = rs.getInt(1);
				con.doInsertUpdate(
						"INSERT INTO beurt (ID,  Spel_ID, Account_naam, score, Aktie_type) VALUES ('%1$d', '%2$d', '%3$s', '%4$d', '%5$s')",
						ID + 1, spel.getSpelId(), Account.getAccountNaam(), bUpdateScore ? puntenTeller() : 0,
						moveType); // moet aangepast worden aan nieuwe versie met
									// puntenteller ipv 0
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.closeConnection();
		pakLetters();
	}

	public Integer puntenTeller() {
		int score = 0;
		for (HashMap<Point, Stone> woordTiles : woordenLijst) { // nullpointer
			int woordScore = 0;
			String woord = "";
			boolean DW = false;
			boolean TW = false;
			for (Entry<Point, Stone> steen : woordTiles.entrySet()) {
				Point xyPos = steen.getKey();
				Stone curStone = steen.getValue();
				char stoneChar = curStone.getLetter();
				int stonevalue = curStone.getValue(spel.getLetterSet());
				Tile curTile = spelBord.getTile(xyPos);

				if(!curTile.getStone().getLocked()){
					switch (curTile.getTileType()) {
					case TYPE_DL:
						stonevalue = stonevalue * 2;
						break;
					case TYPE_TL:
						stonevalue = stonevalue * 3;
						break;
					case TYPE_DW:
						DW = true;
						break;
					case TYPE_TW:
						TW = true;
						break;
					default:
						break;
					}
				}
				woordScore = woordScore + stonevalue;
				woord = woord + stoneChar;
			}
			if (TW) {
				woordScore = woordScore * 3;
			}
			if (DW) {
				woordScore = woordScore * 2;
			}
			System.out.println(woord);
			score = score + woordScore;
		}
		return score;
	}
	
	private ArrayList<HashMap<Point, Stone>> vindWoord() 
	{
		/*
		 * De methode returnd een ArrayList met daarin alle woorden die deze
		 * beurt gelegd zijn. In een hashmap worden de stenen zelf meegegeven.
		 * 
		 * Bij dit voorbeeld zijn de hoofdletters nieuw en kleine oud
		 *   1 2 3 4 5
		 * 1 - - g e k
		 * 2 - - e - -
		 * 3 - E e N D
		 * 4 - - n - o
		 * 5 - - - - m
		 * 
		 * Geeft: Woordenlijst[0]: 2.3 , E 3.3 , e 3.4 , N 3.5 , D
		 * Woordenlijst[1]: 5.3 , D 5.4 , o 5.5 , m
		 */
		Stone startStone = spelBord.getStoneAt(new Point(7,7));
		if(startStone==null)
			return null;
		
		Stone[] newStones = spelBord.getNewTiles().values().toArray(new Stone[spelBord.getNewTiles().size()]);
		ArrayList<HashMap<Point, Stone>> wordlist = new ArrayList<HashMap<Point, Stone>>();
		Point[] newTiles = new Point[newStones.length];
		for(int i = 0; i < newStones.length; i++)
		{
			newTiles[i] = spelBord.getCoordinate(newStones[i]);
		}
		if(newStones.length > 1)
		{			
			boolean horiz = false;
			if(newTiles[0].y == newTiles[1].y)
			{
				horiz = true;
			}
			else if(newTiles[0].x != newTiles[1].x)
			{
				return null;
			}
			
			for(int i = 0; i < newTiles.length-1; i++)
			{
				if(horiz == true)
				{
					if(newTiles[i].y != newTiles[i+1].y)
						return null;
				}
				else
				{
					if(newTiles[i].x != newTiles[i+1].x)
						return null;
				}
			}
			
			HashMap<Point, Stone> pt = vindWoord(newTiles[0], horiz);
			if(pt != null)
			{
				int x = 0;
				for(Point pat : pt.keySet())
				{
					x += pt.get(pat).getLocked() == true? 0 : 1;
				}
				if( x != newTiles.length)
				{
					return null;
				}
				wordlist.add(pt);
			}
			for(int i = 0; i < newTiles.length; i++)
			{
				pt = vindWoord(newTiles[i], !horiz);
				if(pt != null)
				{
					wordlist.add(pt);
				}
			}
		}
		else
		{
			HashMap<Point, Stone> pt = vindWoord(newTiles[0], true);
			if(pt != null)
			{
				wordlist.add(pt);
			}
			pt = vindWoord(newTiles[0], false);
			if(pt != null)
			{
				wordlist.add(pt);
			}
		}
		woordenLijst = wordlist;
		
		boolean heeftWoordGrens = false;
		if(wordlist != null && wordlist.size() > 0)
		{
			for(int i = 0; i < wordlist.size(); i++)
			{
				int x = 0;
				for(Point pt : wordlist.get(i).keySet())
				{
					x += wordlist.get(i).get(pt).getLocked() == true? 1 : 0;
				}
				if( x > 0)
				{
					heeftWoordGrens = true;
				}
			}
		}
		
		if( !heeftWoordGrens && startStone.getLocked() == true )
			return null;
			
			
		return wordlist;
	}
	
	private HashMap<Point, Stone> vindWoord(Point p, boolean horizontaal) 
	{
		HashMap<Point, Stone> myWord = new HashMap<Point, Stone>();
		Point tmp = new Point(p.x, p.y);
		if(horizontaal)
		{
			while(nextStone(tmp,'l') != null)
			{
				tmp.x -= 1;
			}
			tmp.x -= 1;
			do
			{
				tmp.x += 1;
				myWord.put(new Point(tmp.x, tmp.y), spelBord.getStoneAt(tmp));
			} while(nextStone(tmp,'r') != null);
		}
		else
		{
			while(nextStone(tmp,'u') != null)
			{
				tmp.y -= 1;
			}
			tmp.y -= 1;
			do
			{
				tmp.y += 1;
				myWord.put(new Point(tmp.x, tmp.y), spelBord.getStoneAt(tmp));
			} while(nextStone(tmp,'d') != null);
		}
		
		if(myWord.size() <= 1)
			return null;
		
		return myWord;
	}
	

	private Stone nextStone(Point position, char direction) {
		int x = (int) position.getX();
		int y = (int) position.getY();
		Point p = null;

		switch (direction) {
		case 'u': {
			p = new Point(x, y - 1);
			break;
		}
		case 'r': {
			p = new Point(x + 1, y);
			break;
		}
		case 'd': {
			p = new Point(x, y + 1);
			break;
		}
		case 'l': {
			p = new Point(x - 1, y);
			break;
		}
		default:
			return null;
		}
		if(p.x < 0 || p.x > 14 || p.y < 0 || p.y > 14 || spelBord.getStoneAt(p) == null)
			return null;
		
		return spelBord.getStoneAt(p);
	}

	@Override
	public void run() {
		Connectie connect2 = new Connectie();

		while (!spelOver) {
			spelBord.plaatsLetters();
			if (!myTurn()) {
				spel.getSpelPanel().getPlayButton().setEnabled(false);
				spel.getSpelPanel().getShuffleButton().setEnabled(false);
				spel.getSpelPanel().getSkipButton().setEnabled(false);
				spel.getSpelPanel().getSwapButton().setEnabled(false);
				spel.getSpelPanel().getClearButton().setEnabled(false);
			} else {
				// zet alles op het bord waar nodig, update score moet nog
				spel.getSpelPanel().getShuffleButton().setEnabled(true);
				if(spel.getLetterBak() != null)
				{
					spel.getLetterBak().lockButtons();
				}
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			spel.getSpelPanel().updateScoreLabel();
		}
		if(spelOver){
			int myScore = 0;
			int opScore = 0;
			JFrame popup = null;
			connect2.voerInsertOrUpdateQueryUit("UPDATE spel SET Toestand_type = 'Finished' WHERE ID = " + spel.getSpelId() + ";");
			myResultSet = connect2.voerSelectQueryUit("SELECT totaalscore FROM score WHERE Account_naam = '" + Account.getAccountNaam() + "' AND Spel_ID = " + spel.getSpelId() + ";");
			try{
				if(myResultSet.next()){
					myScore = myResultSet.getInt(1);
				}
			}catch(SQLException e) {}
			myResultSet = connect2.voerSelectQueryUit("SELECT totaalscore FROM score WHERE NOT Account_naam = '" + Account.getAccountNaam() + "' AND Spel_ID = " + spel.getSpelId() + ";");
			try{
				if(myResultSet.next()){
					opScore = myResultSet.getInt(1);
				}
			}catch(SQLException e) {}
			if(myScore > opScore){
				JOptionPane.showMessageDialog(popup,
					"Je hebt gewonnen met een score van " + myScore + " tegen " + opScore + "!", "Gewonnen",
					JOptionPane.WARNING_MESSAGE);
				popup = null;
			}
			if(myScore < opScore){
				JOptionPane.showMessageDialog(popup,
					"Je hebt verloren met een score van " + myScore + " tegen " + opScore + ".", "Verloren",
					JOptionPane.WARNING_MESSAGE);
				popup = null;
			}
			if(myScore == opScore){
				JOptionPane.showMessageDialog(popup,
					"Je hebt gelijk gespeeld met beide een score van " + myScore + ".", "Gelijkspel",
					JOptionPane.WARNING_MESSAGE);
				popup = null;
			}
			spel.getSpelPanel().getPlayButton().setEnabled(false);
			spel.getSpelPanel().getShuffleButton().setEnabled(false);
			spel.getSpelPanel().getSkipButton().setEnabled(false);
			spel.getSpelPanel().getSwapButton().setEnabled(false);
			spel.getSpelPanel().getClearButton().setEnabled(false);
		}
		connect2.closeConnection();
	}

	private void pakLetters() {
		Connectie con = new Connectie();
		for (int i = 0; i < 7; i++) {
			if (spel.getLetterBak().getTile(i).getStone() != null) {
				con.doInsertUpdate(
						"INSERT INTO letterbakjeletter (Spel_ID, Letter_ID, Beurt_ID) VALUES (%1$d, %2$d, (SELECT MAX(ID) FROM beurt WHERE Spel_ID = %1$d AND Account_naam = '%3$s'))",
						spel.getSpelId(), spel.getLetterBak().getTile(i)
								.getStone().getLetterId(),
						Account.getAccountNaam());
			}
		}
		ResultSet rs = con
				.doSelect(
						"SELECT Letter_ID FROM pot WHERE Spel_ID = %1$d ORDER BY RAND() LIMIT %2$d",
						spel.getSpelId(), 7 - spel.getLetterBak()
								.getNumberOfStones());
		try {
			while (rs.next()) {
				con.doInsertUpdate(
						"INSERT INTO letterbakjeletter (Spel_ID, Letter_ID, Beurt_ID) VALUES (%1$d, %2$d, (SELECT MAX(ID) FROM beurt WHERE Spel_ID = %1$d AND Account_naam = '%3$s'))",
						spel.getSpelId(), rs.getInt(1),
						Account.getAccountNaam());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ResultSet rs2 = con
				.doSelect(
						"SELECT lb.Spel_ID, lb.Beurt_ID, l.ID, l.LetterType_LetterSet_code, l.LetterType_karakter FROM letterbakjeletter AS lb JOIN letter as l ON lb.Letter_ID = l.ID AND lb.Spel_ID = l.Spel_ID WHERE lb.Spel_ID = %1$d AND lb.Beurt_ID = (SELECT MAX(ID) FROM Beurt WHERE Spel_ID = %1$d AND Account_Naam = '%2$s') LIMIT 7;",
						spel.getSpelId(), Account.getAccountNaam());
		int i = 0;
		try {
			while (rs2.next()) {
				spel.getLetterBak()
						.getTiles()
						.get(i)
						.setStone(
								new Stone(rs2.getString("LetterType_karakter")
										.charAt(0), rs2.getInt("ID")));
				i++;
			}
		} catch (SQLException io) {
			io.printStackTrace();
		}
		con.closeConnection();
		
		spel.getSpelPanel().getLetterbakPanel().repaint();
	}

	public int getBeurt() {
		return this.beurt;
	}

	public boolean checkWoordInDB(String woord) {
		connect = new Connectie();
		ResultSet woorden = connect
				.voerSelectQueryUit("SELECT * FROM woordenboek WHERE status = 'Accepted' AND woord ='"
						+ woord + "'");

		try {
			if (woorden.last()) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect.closeConnection();
		return false;
	}
	
	public void winByPass(){
		Connectie connect3 = new Connectie();
		myResultSet = connect3
				.voerSelectQueryUit("SELECT Aktie_type FROM beurt WHERE Spel_ID = "
						+ spel.getSpelId() + " ORDER BY ID DESC LIMIT 3");
		try {
			while (myResultSet.next()) {
				if (myResultSet.getString("Aktie_type").equals("Pass")) {
					gepasst++;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (gepasst >= 3) {
			spelOver = true;
		}
		gepasst = 0;
		connect3.closeConnection();
	}
	
	public void askModerator(String str){
		Connectie connecteer = new Connectie();
		JFrame popup = null;
		ResultSet rs = connecteer.voerSelectQueryUit("SELECT COUNT(*), status FROM woordenboek WHERE woord = '" + str + "'");
		try {
			if(rs.next()){
				if(rs.getInt(1) == 0){
					connecteer.voerInsertOrUpdateQueryUit("INSERT INTO woordenboek (woord,status) VALUES ('" + str + "', 'Pending')");
				}
				else{
					if(rs.getString("status").equals("Pending")){
						JOptionPane.showMessageDialog(popup,
							"" + woordCheck + " wacht al op een actie van de moderator.", "Al ingestuurd",
							JOptionPane.WARNING_MESSAGE);
						popup = null;
					}
					if(rs.getString("status").equals("Denied")){
						JOptionPane.showMessageDialog(popup,
							"" + woordCheck + " is al een keer verworpen.", "Al verworpen",
							JOptionPane.WARNING_MESSAGE);
						popup = null;
					}
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connecteer.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		
	}
}
