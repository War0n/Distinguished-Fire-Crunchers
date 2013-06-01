package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Bord
{
	private String name;
	private Tile[][] tiles;
	private Connectie connectie;
	private Spel spel;
	private ResultSet result;
	private BordPanel panel;
	
	public Bord(Spel spel, String name)
	{
		this.name = name;
		connectie = new Connectie();
		this.spel = spel;
		setupTiles();
	}
	
	public void setPanel(BordPanel panel)
	{
		this.panel = panel;
	}
	
	public BordPanel getPanel(){
		
		return panel;
	}
	
	private void setupTiles()
	{
		tiles = new Tile[15][15];
		//int[] xArray = null;
		//int[] yArray = null;
		ArrayList<String> soortArray = new ArrayList<String>();
		
		try{
			result = connectie.voerSelectQueryUit("SELECT t.tegelType_soort AS soort FROM spel AS s LEFT JOIN bord AS b ON s.Bord_naam = b.naam LEFT JOIN tegel AS t ON b.naam = t.bord_naam WHERE " + spel.getSpelId() + " LIMIT 0, 225");
			while(result.next()){
				soortArray.add(result.getString("soort"));
			}
		} catch (SQLException e) {
			System.out.println("Er is iets verkeerd gegaam, start het programma opnieuw op, controleer de internetverbinding en probeer het nog eens.");
			e.printStackTrace();
		}

		
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y] = new Tile();
			}
		}
		int x = 0;
		int y = 0;
		for(String type : soortArray){
			switch(type){
			case "*":
				tiles[x][y].setType(TileType.TYPE_START);
			break;
			case "DL":
				tiles[x][y].setType(TileType.TYPE_DL);
			break;
			case "TL":
				tiles[x][y].setType(TileType.TYPE_TL);
			break;
			case "DW":
				tiles[x][y].setType(TileType.TYPE_DW);
			break;
			case "TW":
				tiles[x][y].setType(TileType.TYPE_TW);
			break;
			default:
			break;
			}
			if(x == 14){
				x = 0;
				y++;
			}
			else{
				x++;
			}
		}
	}
	public void plaatsLetters(){
		try{
			ResultSet result = connectie.voerSelectQueryUit("SELECT letter_ID, Tegel_X, Tegel_Y, BlancoLetterKarakter LetterType_karakter FROM gelegdeletter AS g LEFT JOIN letter AS l ON g.Letter_ID = l.ID WHERE l.spel_ID = " + spel.getSpelId());
			
			while(result.next()){
				int xSQL = result.getInt("Tegel_X");
				int ySQL = result.getInt("Tegel_Y");
				int idSQL = result.getInt("letter_ID");
				String bString = result.getString("BlancoLetterKarakter");
				String kString = result.getString("LetterType_karakter");
				
				if(kString.equals("?")){
					tiles[xSQL][ySQL].setStone(new Stone(kString.toCharArray()[0], idSQL));
				}
				else{
					tiles[xSQL][ySQL].setStone(new Stone(kString.toCharArray()[0], bString.toCharArray()[0], idSQL));
					tiles[xSQL][ySQL].getStone().setBlancoLetter(kString.toCharArray()[0]);
				}
				tiles[xSQL][ySQL].getStone().setLocked(true);
			}

		} catch (SQLException e) {
			System.out.println("Er is iets verkeerd gegaam, start het programma opnieuw op, controleer de internetverbinding en probeer het nog eens.");
			e.printStackTrace();
		}
	}
	
	public String getName()
	{
		return name;
	}
	
	public Tile getTile(int x, int y)
	{
		if(x < 0 || y < 0 || x > 15 || y > 15)
			return null;
		return tiles[x][y];
	}

	public Tile[][] getTiles() {
		return tiles;
	}
	
	public int[] getCoordinat(Tile tileInput){
		int[] coor = new int[2];
		for(int x = 0; x < 15; x++){
			for(int y = 0; y < 15; y++){
				if(tiles[x][y].equals(tileInput)){
					coor[0] = x;
					coor[1] = y;
					return coor;
				}
			}
		}
		return null;
	}
	public ArrayList<Tile> getNewTiles(){
	ArrayList<Tile> newTiles = new ArrayList<Tile>();
		for( Tile[] tileList : tiles){
			for( Tile tile : tileList){
				if(tile.getStone() != null){
					if(!tile.getStone().getLocked()){
						newTiles.add(tile);
					}
				}
			}
		}
		return newTiles;
	}
	
	public void lockField(){
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y].setLocked(true);
				panel.repaint();
			}
		}	
	}
}
