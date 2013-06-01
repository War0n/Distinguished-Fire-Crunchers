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
	private int beurtVerdelen;
	private String accountEersteBeurt;

	public SpelVerloop(Spel spel) {
		this.spel = spel;
		account = new Account();
		woordenLijst = new ArrayList<ArrayList<Tile>>();
		Thread checkBeurten = new Thread(this);
		checkBeurten.start();
		spelBord = spel.getBord();
		accountEersteBeurt = "";
	}

	public void play() {
		vindWoord();
		puntenTeller();
		System.out.println(puntenTeller());
	}

	@SuppressWarnings("static-access")
	public boolean myTurn() {
		boolean myTurn = false;
		Connectie con = new Connectie();
		ResultSet rs;
		String accountNaam = "";
		int IDbeurt = 0;
		rs = con.doSelect("SELECT Account_naam, MAX( ID ) -1 AS maxid FROM beurt WHERE Spel_ID = "
				+ spel.getSpelId() + " ORDER BY ID DESC ");
		try {
			while (rs.next()) {
				accountNaam = rs.getString("Account_naam");
				IDbeurt = rs.getInt("maxid");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!account.getAccountNaam().equals(accountNaam)) {
			myTurn = true;
		}
		return myTurn;
	}

	public void skipTurn() {
		Connectie con = new Connectie();
		ResultSet rs;
		try {
			rs = con.doSelect(
					"SELECT MAX(ID), MAX(volgnummer) FROM beurt WHERE Spel_ID = %1$d AND Account_naam = '%2$s'",
					spel.getSpelId(), Account.getAccountNaam());
			if (rs.next()) {
				int ID = rs.getInt(1);
				int volgNummer = rs.getInt(2);
				con.doInsertUpdate(
						"INSERT INTO beurt (ID, Account_naam, Spel_ID, volgnummer, score, Aktie_type) VALUES (null, '%2$s', '%3$d', '%4$d', '%5$d', '%6$s')",
						+1, Account.getAccountNaam(), spel.getSpelId(),
						volgNummer + 1, puntenTeller().intValue(), "Pass");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		gepasst++;
	}

	private Integer puntenTeller() {
		int score = 0;
		boolean doubleword = false;
		boolean tripleword = false;
		for (int i = 0; i < woordenLijst.size(); i++) {
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
		
//		for (Tile selectedTile : newTiles) {
//			HashMap<>
//			int i;
//			
//			Point position = spelBord.getCoordinat(selectedTile);
//			if(nextTile(position, 'n') == null && nextTile(position, 'w') == null){
//				//Rechtsboven beginnen!
//				if(nextTile(position, 's') != null){
//					
//					break;
//				}else if(nextTile(position, 'e') != null){
//					wordTilePositions.put(position, selectedTile);
//					break;
//				}else{
//					System.out.println("losliggende letter!");
//					return null;
//				}
//			}
		while (!newTiles.isEmpty()){ 
			Tile tmpTile = newTiles.get(0);
			System.out.println(newTiles.get(0).getStone().getLetter());
			String woord = String.valueOf(tmpTile.getStone().getLetter());
			Point position = spelBord.getCoordinat(tmpTile);
			while(nextTile(position, 'e').getStone() != null)
			{
			       woord = nextTile(position, 'e').getStone().getLetter() + woord;
			       tmpTile = nextTile(position, 'e');
			       newTiles.remove(tmpTile);
			}
			while(nextTile(position, 'w').getStone() != null)
			{
			       woord = woord + nextTile(position, 'w').getStone().getLetter();
			       tmpTile = nextTile(position, 'w');
			       newTiles.remove(tmpTile);
			}
			while(nextTile(position, 'n').getStone() != null)
			{
			       woord = nextTile(position, 'n').getStone().getLetter() + woord;
			       tmpTile = nextTile(position, 'n');
			       newTiles.remove(tmpTile);
			}
			while(nextTile(position, 's').getStone() != null)
			{
			       woord = woord + nextTile(position, 's').getStone().getLetter();
			       tmpTile = nextTile(position, 's');
			       newTiles.remove(tmpTile);
			}
			
			System.out.println(woord);
		}
		return wordTilePositions;
	}

	private Tile nextTile(Point position, char direction) {
		int x = (int) position.getX();
		int y = (int) position.getY();

		switch (direction) {
		case 'n': {
			if (!spelBord.getTile(x - 1, y).equals(null)) {
				return spelBord.getTile(x - 1, y);
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
	public void run() { // kijken of er nieuwe beurten zijn
		Connectie connect2 = new Connectie();

		while (!spelOver) {
			if (!myTurn()) {
				spel.getSpelPanel().getPlayButton().setEnabled(false);
				spel.getSpelPanel().getShuffleButton().setEnabled(false);
				spel.getSpelPanel().getSkipButton().setEnabled(false);
				spel.getSpelPanel().getSwapButton().setEnabled(false);
				spel.getSpelPanel().getClearButton().setEnabled(false);
				myResultSet = connect2
						.voerSelectQueryUit("SELECT count(*) AS aant_spellen FROM beurt WHERE Spel_ID = "
								+ spel.getSpelId() + ";");
				beurt = 0;
				try {
					while (myResultSet.next()) {
						beurt = myResultSet.getInt("aant_spellen");
						beurtVerdelen = beurt % 2;
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			} else {
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

	private void pakLetter() {
		if (spel.getLetterBak().getNumberOfStones() < 7) {
			ResultSet result = connect
					.voerSelectQueryUit("SELECT * FROM pot WHERE Spel_ID = "
							+ spel.getSpelId());
			/*
			 * result.getArray en zo verder, array.length opvragen als er genoeg
			 * letters in de pot zitten, voeg letters toe aan plankje (new
			 * stone) werk DB bij, voeg records toe aan letterPlankje (Haal
			 * letters weg uit spel?)
			 */
		}
	}
}
