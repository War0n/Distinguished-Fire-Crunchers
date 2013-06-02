package wordfeud;

import java.awt.Dimension;
import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class SpelVerloop implements Runnable {
	private Spel spel;
	private ArrayList<ArrayList<Tile>> woordenLijst;
	private int gepasst;
	private Account account;
	private boolean spelOver;
	private ResultSet myResultSet;
	private Connectie connect;
	private int beurt;
	private Bord spelBord;
	private ArrayList<String> woordenboek;
	private int beurtVerdelen;
	private String accountEersteBeurt;

	public SpelVerloop(Spel spel) {
		spelOver = false;
		this.spel = spel;
		account = new Account();
		woordenboek = new ArrayList<String>();
		Thread checkBeurten = new Thread(this);
		checkBeurten.start();

		spelBord = spel.getBord();
		accountEersteBeurt = "";
	}

	public void play() {
		vindWoord();
		pakLetters();
	}

	@SuppressWarnings("static-access")
	public boolean myTurn() {
		boolean myTurn = false;
		Connectie con = new Connectie();
		ResultSet rs;
		String accountNaam = "";
		int beurt = 0;
		rs = con.doSelect("SELECT Account_naam, ID FROM beurt WHERE Spel_ID = "
				+ spel.getSpelId() + " ORDER BY ID DESC LIMIT 1");
		try {
			if(rs.next()) {
				accountNaam = rs.getString("Account_naam");
				beurt = rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!account.getAccountNaam().equals(accountNaam))
		{
				myTurn = true;
		}
		con.closeConnection();
		return myTurn;
	}

	public void skipTurn() {
		if( !myTurn() )
		{
			return;
		}
		Connectie con = new Connectie();
		ResultSet rs;
		try {
			rs = con.voerSelectQueryUit("SELECT MAX(ID) FROM beurt WHERE Spel_ID = " + spel.getSpelId());
			if (rs.next()) {
				int ID = rs.getInt(1);
				con.doInsertUpdate("INSERT INTO beurt (ID,  Spel_ID, Account_naam, score, Aktie_type) VALUES ('%1$d', '%2$d', '%3$s', '%4$d', '%5$s')",
						ID + 1, spel.getSpelId(), Account.getAccountNaam(), 0 , "Pass"); // moet aangepast worden aan nieuwe versie met puntenteller ipv 0
			}
		} catch (SQLException e) {
			e.printStackTrace();}
		con.closeConnection();
		pakLetters();
	}

	private Integer puntenTeller() {
		int score = 0;
		boolean doubleword = false;
		boolean tripleword = false;
		for (int i = 0; i < woordenLijst.size(); i++) { //nullpointer
			for (int ii = 0; ii < woordenLijst.get(i).size(); ii++) {
				Tile currTile = (Tile) woordenLijst.get(i).get(ii);
				if (currTile.isDoubleLetter()) {
					score = score
							+ (currTile.getStone()
									.getValue(new LetterSet("NL")) * 2);
				} else if (currTile.isTripleLetter()) {
					score = score
							+ (currTile.getStone()
									.getValue(spel.getLetterSet()) * 3);
				} else if (currTile.isDoubleWord()) {
					score = (score + currTile.getStone().getValue(
							spel.getLetterSet()));
					doubleword = true;
				} else if (currTile.isTripleWord()) {
					score = (score + currTile.getStone().getValue(
							spel.getLetterSet()));
					tripleword = true;
				}
			}
		}
		if (doubleword) {
			score = score * 2;
		} else if (tripleword) {
			score = score * 3;
		}
		return score;

	}

	private HashMap<Point, Tile> vindWoord() {
		HashMap<Point, Tile> wordTilePositions = new HashMap<Point, Tile>();
		ArrayList<Tile> newTiles = spelBord.getNewTiles();
		int score = 0;

		if (!newTiles.isEmpty()) {
			Tile tmpTile = newTiles.get(0);
			Point position = spelBord.getCoordinat(tmpTile);
			String woord = String.valueOf(tmpTile.getStone().getLetter());
			score = tmpTile.getStone().getValue(spel.getLetterSet());

			while (nextTile(position, 's').getStone() != null) {
				if (newTiles.contains(nextTile(position, 's'))) {
					woord = woord
							+ nextTile(position, 's').getStone().getLetter();
					score = score
							+ nextTile(position, 's').getStone().getValue(
									spel.getLetterSet());
					newTiles.remove(tmpTile);
					tmpTile = nextTile(position, 's');
					position = spelBord.getCoordinat(tmpTile);
				}
			}

			while (nextTile(position, 'e').getStone() != null) {
				if (newTiles.contains(nextTile(position, 'e'))) {
					woord = woord
							+ nextTile(position, 'e').getStone().getLetter();
					score = score
							+ nextTile(position, 'e').getStone().getValue(
									spel.getLetterSet());
					newTiles.remove(tmpTile);
					tmpTile = nextTile(position, 'e');
					position = spelBord.getCoordinat(tmpTile);
				}
			}

			while (nextTile(position, 'w').getStone() != null) {
				if (newTiles.contains(nextTile(position, 'w'))) {
					woord = nextTile(position, 'w').getStone().getLetter()
							+ woord;
					score = score
							+ nextTile(position, 'w').getStone().getValue(
									spel.getLetterSet());
					newTiles.remove(tmpTile);
					tmpTile = nextTile(position, 'w');
					position = spelBord.getCoordinat(tmpTile);
				}
			}

			while (nextTile(position, 'n').getStone() != null) {
				if (newTiles.contains(nextTile(position, 'n'))) {
					woord = nextTile(position, 'n').getStone().getLetter()
							+ woord;
					score = score
							+ nextTile(position, 'n').getStone().getValue(
									spel.getLetterSet());
					newTiles.remove(tmpTile);
					tmpTile = nextTile(position, 'n');
					position = spelBord.getCoordinat(tmpTile);
				}
			}
			woord.toLowerCase();
			if (checkWoordInDB(woord)) {
				System.out.println(woord + " voor " + score);
			} else {
				System.out.println(woord + " komt niet voor in de db");
			}

		}

		return wordTilePositions;
	}

	private Tile nextTile(Point position, char direction) {
		int x = (int) position.getX();
		int y = (int) position.getY();

		switch (direction) {
		case 'n': {
			if (!spelBord.getTile(x, y + 1).equals(null)) {
				return spelBord.getTile(x, y + 1);
			}
			break;
		}

		case 'e': {
			if (!spelBord.getTile(x, y + 1).equals(null)) {
				return spelBord.getTile(x, y + 1);
			}
			break;
		}

		case 's': {
			if (!spelBord.getTile(x + 1, y).equals(null)) {
				return spelBord.getTile(x + 1, y);
			}
			break;
		}

		case 'w': {
			if (!spelBord.getTile(x, y - 1).equals(null)) {
				return spelBord.getTile(x, y - 1);
			}
			break;
		}

		default:
			return null;
		}

		return null;
	}

	@Override
	public void run() {
		Connectie connect2 = new Connectie();

		while (!spelOver) {
			if (!myTurn()) {
				spel.getSpelPanel().getPlayButton().setEnabled(false);
				spel.getSpelPanel().getShuffleButton().setEnabled(false);
				spel.getSpelPanel().getSkipButton().setEnabled(false);
				spel.getSpelPanel().getSwapButton().setEnabled(false);
				spel.getSpelPanel().getClearButton().setEnabled(false);
				myResultSet = connect2.voerSelectQueryUit("SELECT Aktie_type FROM beurt WHERE Spel_ID = " + spel.getSpelId() +  " ORDER BY ID ASC LIMIT 3"); // kijken of winnaar is
				try {
					while(myResultSet.next()){
						if(myResultSet.getString("Aktie_type").equals("Pass")){
							gepasst ++;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if(gepasst >= 3){
					spelOver = true;
				}
				gepasst = 0;
			}
			if(myTurn()){
				// zet alles op het bord waar nodig, update score moet nog
				spel.getSpelPanel().getPlayButton().setEnabled(true);
				spel.getSpelPanel().getShuffleButton().setEnabled(true);
				spel.getSpelPanel().getSkipButton().setEnabled(true);
				spel.getSpelPanel().getSwapButton().setEnabled(true);
				spel.getSpelPanel().getClearButton().setEnabled(true);
			}
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		connect2.closeConnection();
	}
	
	private void pakLetters() 
	{
		Connectie con = new Connectie();
		for(int i = 0; i < 7; i++)
		{
			if( spel.getLetterBak().getTile(i).getStone() != null)
			{
				con.doInsertUpdate(
					"INSERT INTO letterbakjeletter (Spel_ID, Letter_ID, Beurt_ID) VALUES (%1$d, %2$d, (SELECT MAX(ID) FROM beurt WHERE Spel_ID = %1$d AND Account_naam = '%3$s'))",
					spel.getSpelId(), spel.getLetterBak().getTile(i).getStone().getLetterId(), Account.getAccountNaam());
			}
		}
		ResultSet rs = con.doSelect("SELECT Letter_ID FROM pot WHERE Spel_ID = %1$d ORDER BY RAND() LIMIT %2$d", spel.getSpelId(), 7 - spel.getLetterBak().getNumberOfStones());
		try {
			while (rs.next()) {
				con.doInsertUpdate(
					"INSERT INTO letterbakjeletter (Spel_ID, Letter_ID, Beurt_ID) VALUES (%1$d, %2$d, (SELECT MAX(ID) FROM beurt WHERE Spel_ID = %1$d AND Account_naam = '%3$s'))",
					spel.getSpelId(), rs.getInt(1), Account.getAccountNaam());
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ResultSet rs2 = con.doSelect("SELECT lb.Spel_ID, lb.Beurt_ID, l.ID, l.LetterType_LetterSet_code, l.LetterType_karakter FROM letterbakjeletter AS lb JOIN letter as l ON lb.Letter_ID = l.ID AND lb.Spel_ID = l.Spel_ID WHERE lb.Spel_ID = %1$d AND lb.Beurt_ID = (SELECT MAX(ID) FROM Beurt WHERE Spel_ID = %1$d AND Account_Naam = '%2$s') LIMIT 7;", spel.getSpelId(), Account.getAccountNaam());
        int i = 0;
        try
        {
                while(rs2.next())
                {
                        spel.getLetterBak().getTiles().get(i).setStone(new Stone(rs2.getString("LetterType_karakter").charAt(0), rs2.getInt("ID")));
                        i++;
                }
        }
        catch(SQLException io)
        {
                io.printStackTrace();
        }
		con.closeConnection();
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
}
