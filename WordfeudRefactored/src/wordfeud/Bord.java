package wordfeud;

import java.sql.Array;
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
		

		result = connectie.voerSelectQueryUit("SELECT t.tegelType_soort AS soort FROM spel AS s LEFT JOIN bord AS b ON s.Bord_naam = b.naam LEFT JOIN tegel AS t ON b.naam = t.bord_naam WHERE " + spel.getSpelId() + " LIMIT 0, 225");
		try {
			while(result.next()){
				soortArray.add(result.getString("soort"));
			}
		} catch (SQLException e) {
			System.out.println(e);
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
		int[] xArray = null;
		int[] yArray = null;
		char[] bArray = null;
		char[] kArray = null;
		ResultSet result = connectie.voerSelectQueryUit("SELECT letter_ID, Tegel_X, Tegel_Y, BlancoLetterKarakter LetterType_karakter FROM gelegdeletter AS g LEFT JOIN letter AS l ON g.Letter_ID = l.ID WHERE l.spel_ID = " + spel.getSpelId());
		try {
			Array xSQLArray = result.getArray("Tegel_X");
			xArray = (int[])xSQLArray.getArray();
			Array ySQLArray = result.getArray("Tegel_Y");
			yArray = (int[])ySQLArray.getArray();
			Array bSQLArray = result.getArray("BlancoLetterKarakter");
			bArray = (char[])bSQLArray.getArray();
			Array kSQLArray = result.getArray("LetterType_karakter");
			kArray = (char[])kSQLArray.getArray();
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		int index = 0;
		while(index < kArray.length){
			if(kArray[index] != '?'){
				tiles[xArray[index]][yArray[index]].setStone(new Stone(kArray[index]));
				tiles[xArray[index]][yArray[index]].getStone().setLocked(false);
			}
			else{
				tiles[xArray[index]][yArray[index]].setStone(new Stone(kArray[index], bArray[index]));
				tiles[xArray[index]][yArray[index]].getStone().setLocked(false);
			}
			index++;
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
