package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUITile extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel tileLabel;
	private Tile tile;
	
	public GUITile(Tile tile) {
		initTile(tile);
	}
	
	public GUITile(TileType type) {
		initTile(new Tile(type));
	}
	
	public void initTile(Tile tile)
	{
		this.tile = tile;
		String label = "";
		Color bgColor = new Color(0,0,150);
		switch(tile.getType())
		{
		case TYPE_NONE:
			bgColor = new Color(44,47,53);
			label = "";
			break;
		case TYPE_DW:
			bgColor = new Color(192,121,33);
			label = "DW";
			break;
		case TYPE_TW:
			bgColor = new Color(156,71,26);
			label = "TW";
			break;
		case TYPE_DL:
			bgColor = new Color(122,162,113);
			label = "DL";
			break;
		case TYPE_TL:
			bgColor = new Color(29,144,160);
			label = "TL";
			break;
		case TYPE_START:
			bgColor = new Color(97,65,93);
			label = "\u2605";
			break;
		default:
			bgColor = new Color(44,47,53);
			label = "";
			break;
		}
		tileLabel = new JLabel(label);
		tileLabel.setForeground(Color.WHITE);
		tileLabel.setFont(new Font("BabelStone Han",Font.BOLD,18));
		

		setMaximumSize(new Dimension(40,40));
		setMinimumSize(new Dimension(40,40));
		setPreferredSize(getMaximumSize());
		setBackground(bgColor);
		add(tileLabel);
	}
	
	public Stone getStone(){
		return tile.getStone();
	}
	
	public void setStone(Stone stone){
		tile.setStone(stone);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		if( tile.getStone() != null )
		{
			g.drawImage(tile.getStone().getImage(), 0, 0, this);
			
			Font tmp = g.getFont();
			LetterSet letterSet = new LetterSet("nl-NL");
			
			//g.drawImage(img, 0, 0, this);
			
			char[] c = new char[1];
			c[0] = tile.getStone().getLetter();
			g.setFont(tmp.deriveFont(32.0f));
			g.drawChars(c, 0, 1, 8, 34);
			g.setFont(tmp);
			g.drawString(""+letterSet.getLetter(c[0]).getWaarde(), 28, 13);
			this.tileLabel.setVisible(false);
		}
		else
			this.tileLabel.setVisible(true);
	}

	public Tile getTile()
	{
		return tile;
	}

	public void setTile(Tile tile)
	{
		this.tile = tile;
	}
	
}