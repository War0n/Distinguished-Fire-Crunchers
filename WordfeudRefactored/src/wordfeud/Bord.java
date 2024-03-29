package wordfeud;

import java.awt.Point;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class Bord {
	private String name;
	private Tile[][] tiles;
	private Connectie connectie;
	private Spel spel;
	private ResultSet result;
	private BordPanel panel;
	private boolean bObserver;

	public Bord(Spel spel, String name, boolean observer) {
		this.name = name;
		bObserver = observer;
		connectie = new Connectie();
		this.spel = spel;
		setupTiles();
	}

	public void setPanel(BordPanel panel) {
		this.panel = panel;
	}

	public BordPanel getPanel() {

		return panel;
	}

	private void setupTiles() {
		tiles = new Tile[15][15];
		ArrayList<String> soortArray = new ArrayList<String>();

		try {
			result = connectie
					.voerSelectQueryUit("SELECT t.tegelType_soort AS soort FROM spel AS s LEFT JOIN bord AS b ON s.Bord_naam = b.naam LEFT JOIN tegel AS t ON b.naam = t.bord_naam WHERE "
							+ spel.getSpelId() + " LIMIT 0, 225");
			while (result.next()) {
				soortArray.add(result.getString("soort"));
			}
		} catch (SQLException e) {
			System.out
					.println("Er is iets verkeerd gegaam, start het programma opnieuw op, controleer de internetverbinding en probeer het nog eens.");
			e.printStackTrace();
		}

		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				tiles[x][y] = new Tile();
			}
		}
		int x = 0;
		int y = 0;
		for (String type : soortArray) {
			tiles[x][y].setType(TileType.fromValue(type));
			if (x == 14) {
				x = 0;
				y++;
			} else {
				x++;
			}
		}
		if( !bObserver )
			plaatsLetters();
		else
			plaatsLetters(2);
	}

	public void plaatsLetters() {
		try {
			ResultSet result = connectie
					.voerSelectQueryUit("SELECT letter_ID, Tegel_X, Tegel_Y, BlancoLetterKarakter ,LetterType_karakter FROM gelegdeletter AS g LEFT JOIN letter AS l ON g.Letter_ID = l.ID AND l.Spel_ID = g.Spel_ID WHERE l.Spel_ID = "
							+ spel.getSpelId());

			while (result.next()) {
				int xSQL = result.getInt("Tegel_X")-1;
				int ySQL = result.getInt("Tegel_Y")-1;
				if(tiles[xSQL][ySQL].getStone() != null && tiles[xSQL][ySQL].getStone().getLocked() == true)
					continue;
				int idSQL = result.getInt("letter_ID");
				String bString = result.getString("BlancoLetterKarakter");
				String kString = result.getString("LetterType_karakter");

				if (kString.equals("?")) {
					tiles[xSQL][ySQL].setStone(new Stone(kString.toCharArray()[0], idSQL));
					tiles[xSQL][ySQL].getStone().setBlancoLetter(bString.toCharArray()[0]);
				} else {
					tiles[xSQL][ySQL].setStone(new Stone(kString.toCharArray()[0], idSQL));
				}
				tiles[xSQL][ySQL].getStone().setLocked(true);
				if( panel != null )
					panel.repaitTile(xSQL, ySQL);
			}

		} catch (SQLException e) {
			System.out
					.println("Er is iets verkeerd gegaan, start het programma opnieuw op, controleer de internetverbinding en probeer het nog eens.");
			e.printStackTrace();
		}
	}
	
	public void plaatsLetters(int beurt) {
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y].setStone(null);
			}
		}
		if(panel != null)
			panel.repaint();
		try {
			ResultSet result = connectie
					.voerSelectQueryUit("SELECT letter_ID, Tegel_X, Tegel_Y, BlancoLetterKarakter ,LetterType_karakter FROM gelegdeletter AS g LEFT JOIN letter AS l ON g.Letter_ID = l.ID AND l.Spel_ID = g.Spel_ID WHERE l.Spel_ID = "
							+ spel.getSpelId() + " AND Beurt_ID <= " + beurt);

			while (result.next()) {
				int xSQL = result.getInt("Tegel_X")-1;
				int ySQL = result.getInt("Tegel_Y")-1;
				if(tiles[xSQL][ySQL].getStone() != null && tiles[xSQL][ySQL].getStone().getLocked() == true)
					continue;
				int idSQL = result.getInt("letter_ID");
				String bString = result.getString("BlancoLetterKarakter");
				String kString = result.getString("LetterType_karakter");

				if (kString.equals("?")) {
					tiles[xSQL][ySQL].setStone(new Stone(kString.toCharArray()[0], idSQL));
					tiles[xSQL][ySQL].getStone().setBlancoLetter(bString.toCharArray()[0]);
				} else {
					tiles[xSQL][ySQL].setStone(new Stone(kString.toCharArray()[0], idSQL));
				}
				tiles[xSQL][ySQL].getStone().setLocked(true);
				if( panel != null )
					panel.repaitTile(xSQL, ySQL);
			}

		} catch (SQLException e) {
			System.out
					.println("Er is iets verkeerd gegaan, start het programma opnieuw op, controleer de internetverbinding en probeer het nog eens.");
			e.printStackTrace();
		}
	}

	public String getName() {
		return name;
	}

	public Spel getSpel(){
		return spel;
	}
	
	public Tile getTile(Point p) {
		if (p.x < 0 || p.y < 0 || p.x > 14 || p.y > 14){
			return null;
		}
		return tiles[p.x][p.y];
	}

	public Stone getStoneAt(Point p) 
	{
		if (p.x < 0 || p.y < 0 || p.x > 14 || p.y > 14 || tiles[p.x][p.y].getStone()==null){
			return null;
		}
		return tiles[p.x][p.y].getStone();
	}

	public Tile[][] getTiles() {
		return tiles;
	}

	public Point getCoordinate(Stone stoneInput) {
		Point coor = new Point();
		for (int x = 0; x < 15; x++) {
			for (int y = 0; y < 15; y++) {
				if(tiles[x][y].getStone() != null){
					if (tiles[x][y].getStone().equals(stoneInput)) {
						coor.x = x;
						coor.y = y;
						return coor;
					}
				}
			}
		}
		return null;
	}
	public HashMap<Point, Stone> getNewTiles(){
		HashMap<Point ,Stone> newTiles = new HashMap<Point, Stone>();
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				if (tiles[x][y].getStone() != null) {
					if (!tiles[x][y].getStone().getLocked()) {
						Point p = new Point(x, y);
						newTiles.put(p ,tiles[x][y].getStone());
					}
				}
			}
		}
		return newTiles;
	}

	public void lockField() {
		for (int y = 0; y < 15; y++) {
			for (int x = 0; x < 15; x++) {
				tiles[x][y].setLocked(true);
				panel.repaint();
			}
		}
	}
}
