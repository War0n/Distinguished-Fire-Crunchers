package wordfeud;

public class Bord
{
	private String name;
	private Tile[][] tiles;
	
	public Bord()
	{
		this.name = "";
		setupTiles();
	}
	
	public Bord(String name)
	{
		this.name = name;
		setupTiles();
	}
	
	private void setupTiles()
	{
		tiles = new Tile[15][15];
		
		// De informatie over de tiles en hun type moet uit de database komen.
		// Dit is nog niet geïmplementeerd, dus een leeg bord.
		
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y] = new Tile();
			}
		}
		
		// Triple word
		tiles[0][4].setType(TileType.TYPE_TW);
		tiles[0][10].setType(TileType.TYPE_TW);
		tiles[4][0].setType(TileType.TYPE_TW);
		tiles[4][14].setType(TileType.TYPE_TW);
		tiles[10][0].setType(TileType.TYPE_TW);
		tiles[10][14].setType(TileType.TYPE_TW);
		tiles[14][4].setType(TileType.TYPE_TW);
		tiles[14][10].setType(TileType.TYPE_TW);
		
		// Double word
		tiles[2][2].setType(TileType.TYPE_DW);
		tiles[12][2].setType(TileType.TYPE_DW);
		tiles[7][3].setType(TileType.TYPE_DW);
		tiles[4][4].setType(TileType.TYPE_DW);
		tiles[10][4].setType(TileType.TYPE_DW);
		tiles[3][7].setType(TileType.TYPE_DW);
		tiles[11][7].setType(TileType.TYPE_DW);
		tiles[4][10].setType(TileType.TYPE_DW);
		tiles[10][10].setType(TileType.TYPE_DW);
		tiles[7][11].setType(TileType.TYPE_DW);
		tiles[2][12].setType(TileType.TYPE_DW);
		tiles[12][12].setType(TileType.TYPE_DW);

		// Triple letter
		tiles[0][0].setType(TileType.TYPE_TL);
		tiles[14][0].setType(TileType.TYPE_TL);
		tiles[5][1].setType(TileType.TYPE_TL);
		tiles[9][1].setType(TileType.TYPE_TL);
		tiles[3][3].setType(TileType.TYPE_TL);
		tiles[11][3].setType(TileType.TYPE_TL);
		tiles[1][5].setType(TileType.TYPE_TL);
		tiles[5][5].setType(TileType.TYPE_TL);
		tiles[9][5].setType(TileType.TYPE_TL);
		tiles[13][5].setType(TileType.TYPE_TL);
		tiles[1][9].setType(TileType.TYPE_TL);
		tiles[5][9].setType(TileType.TYPE_TL);
		tiles[9][9].setType(TileType.TYPE_TL);
		tiles[13][9].setType(TileType.TYPE_TL);
		tiles[3][11].setType(TileType.TYPE_TL);
		tiles[11][11].setType(TileType.TYPE_TL);
		tiles[5][13].setType(TileType.TYPE_TL);
		tiles[9][13].setType(TileType.TYPE_TL);
		tiles[0][14].setType(TileType.TYPE_TL);
		tiles[14][14].setType(TileType.TYPE_TL);

		// Double letter
		tiles[7][0].setType(TileType.TYPE_DL);
		tiles[1][1].setType(TileType.TYPE_DL);
		tiles[13][1].setType(TileType.TYPE_DL);
		tiles[6][2].setType(TileType.TYPE_DL);
		tiles[8][2].setType(TileType.TYPE_DL);
		tiles[6][4].setType(TileType.TYPE_DL);
		tiles[8][4].setType(TileType.TYPE_DL);
		tiles[2][6].setType(TileType.TYPE_DL);
		tiles[4][6].setType(TileType.TYPE_DL);
		tiles[10][6].setType(TileType.TYPE_DL);
		tiles[12][6].setType(TileType.TYPE_DL);
		tiles[0][7].setType(TileType.TYPE_DL);
		tiles[14][7].setType(TileType.TYPE_DL);
		tiles[2][8].setType(TileType.TYPE_DL);
		tiles[4][8].setType(TileType.TYPE_DL);
		tiles[10][8].setType(TileType.TYPE_DL);
		tiles[12][8].setType(TileType.TYPE_DL);
		tiles[6][10].setType(TileType.TYPE_DL);
		tiles[8][10].setType(TileType.TYPE_DL);
		tiles[6][12].setType(TileType.TYPE_DL);
		tiles[8][12].setType(TileType.TYPE_DL);
		tiles[1][13].setType(TileType.TYPE_DL);
		tiles[13][13].setType(TileType.TYPE_DL);
		tiles[7][14].setType(TileType.TYPE_DL);
		
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
}
