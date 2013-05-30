package wordfeud;

import java.sql.Array;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

public class Bord
{
	private String name;
	private Tile[][] tiles;
	private Connectie connectie;
	private Spel spel;
	
	private BordPanel panel;
	
	public Bord(Spel spel)
	{
		this.name = "";
		setupTiles();
		connectie = new Connectie();	
		this.spel = spel;
	}
	
	public Bord(String name)
	{
		this.name = name;
		setupTiles();
	}
	
	public void setPanel(BordPanel panel)
	{
		this.panel = panel;//
	}
	
	public BordPanel getPanel(){
		
		return panel;
	}
	
	private void setupTiles()
	{
		tiles = new Tile[15][15];
		//int[] xArray = null;
		//int[] yArray = null;
		String[] soortArray = null;
		
		ResultSet result = connectie.voerSelectQueryUit("SELECT s.ID, b.naam, t.x,  t.y, t.tegelType_soort AS soort FROM spel AS s LEFT JOIN bord AS b ON s.Bord_naam = b.naam LEFT JOIN tegel AS t ON b.naam = t.bord_naam WHERE s.ID = " + spel.getSpelId());
		try{
			Array soortSQLArray = result.getArray("soort");
			soortArray = (String[])soortSQLArray.getArray();	
			}
		catch(SQLException e){
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
		
	
		// Start
		tiles[7][7].setType(TileType.TYPE_START);
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
		for(int x = 0; x < 16; x++){
			for(int y = 0; y < 16; y++){
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
				if(tile.getNieuwGelegd()){
					newTiles.add(tile);
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
