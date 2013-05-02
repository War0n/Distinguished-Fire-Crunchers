package wordfeud;

import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;


public class LetterbakPanel extends JPanel
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Letterbak letterbak;

	ArrayList<GUITile> tiles;
	public LetterbakPanel(Letterbak letterbak) 
	{
		this.setLetterbak(letterbak);
		tiles = new ArrayList<GUITile>();
		for(int i = 0; i < 7; i++)
		{
			tiles.add(new GUITile(TileType.TYPE_NONE));
			add(tiles.get(i));
			tiles.get(i).setStone(letterbak.getStones().get(i));
		}
		setBackground(new Color(23,26,30));
		repaint();
	}
	
	public void setLetter(int index, Stone stone){
		tiles.get(index).setStone(stone);
	}

	public Letterbak getLetterbak()
	{
		return letterbak;
	}

	public void setLetterbak(Letterbak letterbak)
	{
		this.letterbak = letterbak;
	}

}

