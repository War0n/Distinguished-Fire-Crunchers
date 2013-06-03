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

import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SpelVerloop implements Runnable {
	private Spel spel;
	private ArrayList<HashMap<Point, Stone>> woordenLijst;
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
		spel.getVerloop().doTurn("Word");
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
			if (rs.next()) {
				accountNaam = rs.getString("Account_naam");
				beurt = rs.getInt("ID");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!account.getAccountNaam().equals(accountNaam)) {
			myTurn = true;
		}
		con.closeConnection();
		return myTurn;
	}

	public void doTurn(String moveType) {
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
						ID + 1, spel.getSpelId(), Account.getAccountNaam(), 0,
						moveType); // moet aangepast worden aan nieuwe versie met
									// puntenteller ipv 0
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.closeConnection();
		pakLetters();
	}

	private Integer puntenTeller() {
		int score = 0;
		boolean doubleword = false;
		boolean tripleword = false;
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

	private ArrayList<HashMap<Point, Stone>> vindWoord() {
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
		Stone[] newStones = (Stone[]) spelBord.getNewTiles().values().toArray();
		Stone currentStone = null;
		woordenLijst = new ArrayList<HashMap<Point, Stone>>();
		woordenLijst.add(new HashMap<Point, Stone>());
		boolean done = false;
		int index = 0;

		// Is er wel gelegd?
		if (newStones.length == 0) {
			// Er is niet gelegd
			return null;
		} else {
			currentStone = newStones[0];
		}
		// Woorden zoeken
		// Scroll naarboven vanaf de meest linksboven nieuwe letter
		while (!done) {
			if ((nextStone(spelBord.getCoordinat(currentStone), 'u') != null)) {
				currentStone = nextStone(
						spelBord.getCoordinat(currentStone), 'u');
			} else {
				done = true;
			}
		}
		done = false;
		while (!done) {
			woordenLijst.get(index).put(
					spelBord.getCoordinat(currentStone), currentStone);
			if ((nextStone(spelBord.getCoordinat(currentStone), 'd') != null)) {
				if (!currentStone.equals(newStones[0])) {
					if ((nextStone(spelBord.getCoordinat(currentStone),
							'l') != null)
							|| ((nextStone(
									spelBord.getCoordinat(currentStone),
									'r') != null) && !currentStone.getLocked())) {
						currentStone = (nextStone(
								spelBord.getCoordinat(currentStone), 'l'));
						boolean done2 = false;
						while (!done2) {
							if (!(nextStone(
									spelBord.getCoordinat(currentStone),
									'l') == null)) {
								currentStone = nextStone(spelBord
										.getCoordinat(currentStone), 'l');
							} else {
								done2 = true;
							}
							done2 = false;
						}
						woordenLijst
								.addAll(new ArrayList<HashMap<Point, Stone>>());
						while (!done2) {
							if ((nextStone(
									spelBord.getCoordinat(currentStone),
									'r') != null)) {
								woordenLijst.get(woordenLijst.size() - 1).put(
										spelBord.getCoordinat(
												currentStone), currentStone);
								currentStone = nextStone(spelBord
										.getCoordinat(currentStone), 'r');
							} else {
								done2 = true;
							}
						}
						currentStone = woordenLijst.get(index).get(
								spelBord.getCoordinat(currentStone));
					}
				}
				currentStone = nextStone(
						spelBord.getCoordinat(currentStone), 'd');
			} else {
				done = true;
			}
			done = false;
			while (!done) {
				if ((nextStone(spelBord.getCoordinat(currentStone), 'l') != null)) {
					currentStone = nextStone(
							spelBord.getCoordinat(currentStone), 'l');
				} else {
					done = true;
				}
			}
		}
		done = false;
		while (!done) {
			woordenLijst.get(index).put(
					spelBord.getCoordinat(currentStone), currentStone);
			if ((nextStone(spelBord.getCoordinat(currentStone), 'r') != null)) {
				if (!currentStone.equals(newStones[0])) {
					if ((nextStone(spelBord.getCoordinat(currentStone),
							'u') != null)
							|| (nextStone(
									spelBord.getCoordinat(currentStone),
									'd') != null) && !currentStone.getLocked()) {
						currentStone = nextStone(
								spelBord.getCoordinat(currentStone), 'u');
						boolean done2 = false;
						while (!done2) {
							if ((nextStone(
									spelBord.getCoordinat(currentStone),
									'u') != null)) {
								currentStone = nextStone(spelBord
										.getCoordinat(currentStone), 'u');
							} else {
								done2 = true;
							}
							done2 = false;
						}
						woordenLijst.add(woordenLijst.size(),
								new HashMap<Point, Stone>());
						while (!done2) {
							if ((nextStone(
									spelBord.getCoordinat(currentStone),
									'd') != null)) {
								woordenLijst.get(woordenLijst.size() - 1).put(
										spelBord.getCoordinat(
												currentStone), currentStone);
								currentStone = nextStone(spelBord
										.getCoordinat(currentStone), 'd');
							} else {
								done2 = true;
							}
						}
						currentStone = woordenLijst.get(index).get(
								spelBord.getCoordinat(currentStone));
					}
				}
				currentStone = nextStone(
						spelBord.getCoordinat(currentStone), 'r');
			} else {
				done = true;
			}
		}
		// controleer of er geen gaten zijn.
		int count = 0;
		for (HashMap<Point, Stone> woord : woordenLijst) {
			Stone[] woordArray = (Stone[]) woord.values().toArray();
			for (Stone stone : woordArray) {
				for (Stone newStone : newStones) {
					if (newStone.equals(stone)) {
						count++;
					}
				}
			}
		}
		if (count != newStones.length) {
			return null;
		}
		// Kijk of er woorden in een hoek zijn gelegd
		boolean xGelijk = false;
		boolean yGelijk = false;
		Point[] points = (Point[]) spelBord.getNewTiles().keySet().toArray();
		for(int teller = 0; teller < points.length -1 ; teller++ ){
			if(points[index].x != points[index+1].x){
				break;
			}
			else{
				xGelijk = true;
			}
		}
		for(int teller = 0; teller < points.length -1 ; teller++ ){
			if(points[index].y != points[index+1].y){
				break;
			}
			else{
				yGelijk = true;
			}
		}
		if(!xGelijk && !yGelijk){
			return null;
		}
		// Is dit het eerste woord? En zoja ligt het op het startvak?
		boolean check = false;
		boolean start = false;
		for (HashMap<Point, Stone> woord : woordenLijst) {
			Stone[] woordArray = (Stone[]) woord.values().toArray();
			for (Stone stone : woordArray) {
				if (stone.getLocked()) {
					check = true;
				} else {
					if (spelBord
							.getTile(spelBord.getCoordinat(stone))
							.getType().equals("TYPE_START")) {
						start = true;
					}
				}
			}
		}
		if (!check) {
			if (start) {
				return woordenLijst;
			} else {
				return null;
			}
		}
		return woordenLijst;
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
		if (spelBord.getTile(position).getStone() != null) {
			return spelBord.getTile(p).getStone();
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
				myResultSet = connect2
						.voerSelectQueryUit("SELECT Aktie_type FROM beurt WHERE Spel_ID = "
								+ spel.getSpelId() + " ORDER BY ID DESC LIMIT 3"); // kijken
																					// of
																					// winnaar
																					// is
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
			}
			if (myTurn()) {
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
		if(spelOver){
			int myScore = 0;
			int opScore = 0;
			JFrame popup = null;
			connect2.voerInsertOrUpdateQueryUit("UPDATE spel SET Toestand_type = 'Finished' WHERE ID = " + spel.getSpelId() + ";");
			myResultSet = connect2.voerSelectQueryUit("SELECT SUM(score) FROM beurt WHERE Account_naam = '" + Account.getAccountNaam() + "' AND Spel_ID = " + spel.getSpelId() + ";");
			try{
				if(myResultSet.next()){
					myScore = myResultSet.getInt(1);
				}
			}catch(SQLException e) {}
			myResultSet = connect2.voerSelectQueryUit("SELECT SUM(score) FROM beurt WHERE NOT Account_naam = '" + Account.getAccountNaam() + "' AND Spel_ID = " + spel.getSpelId() + ";");
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
			else{
				JOptionPane.showMessageDialog(popup,
					"Je hebt gelijk gespeeld met beide een score van " + myScore + ".", "Gelijkspel",
					JOptionPane.WARNING_MESSAGE);
				popup = null;
			}
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
