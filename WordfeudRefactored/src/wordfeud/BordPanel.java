package wordfeud;

import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;


public class BordPanel extends JPanel 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Bord speelVeld;
	GUITile[][] tiles;
	
	public BordPanel(Bord speelVeld) {
		this.setSpeelVeld(speelVeld);
		setLayout(new GridLayout(15,15,2,2));
		setBackground(new Color(23,26,30));
		
		tiles = new GUITile[15][15];
		
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y] = new GUITile(speelVeld.getTile(x,y));
				add(tiles[x][y]);
				//System.out.println("Added tile at X:" + x + " Y:" + y);
			}
		}	
	}

	public Bord getSpeelVeld()
	{
		return speelVeld;
	}

	public void setSpeelVeld(Bord speelVeld)
	{
		this.speelVeld = speelVeld;
	}
}
