package wordfeud;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Tile {
	// privates <:
	private TileType type;
	private Stone steen;

	// Constructor
	public Tile(TileType type) {
		this.type = type;
		steen = null;
	}

	public Tile() {
		this.type = TileType.TYPE_NONE;
		steen = null;
	}

	// Getters en setters
	public Stone getStone() {
		return steen;
	}

	public void setStone(Stone steen) {
		this.steen = steen;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	// Speciale Tile Types.
	public boolean isDoubleLetter() {
		return type.equals(TileType.TYPE_DL);
	}

	public boolean isTripleLetter() {
		return type.equals(TileType.TYPE_TL);
	}

	public boolean isDoubleWord() {
		return type.equals(TileType.TYPE_DW);
	}

	public boolean isTripleWord() {
		return type.equals(TileType.TYPE_TW);
	}

	public boolean isStart() {
		return type.equals(TileType.TYPE_START);
	}
	
	public TileType getTileType(){
		return type;
	}

	// locked
	public boolean getLocked() {
		if (steen != null) {
			return steen.getLocked();
		}
		return false;
	}

	public void setLocked(boolean locked) {
		if (steen != null) {
			steen.setLocked(locked);
		}
	}
	
	public BufferedImage getImageWithLetter(Spel spel)
	{
		BufferedImage retImg = new BufferedImage(
	            40, 40, BufferedImage.TYPE_INT_ARGB);;
		if(getStone() != null)
		{
			Graphics2D g = retImg.createGraphics();
			
			g.drawImage(getStone().getImage(true), 0, 0, null);
			g.setPaint(Color.black);
	        g.setFont(new Font("Arial", Font.PLAIN, 32));
			LetterSet letterSet = spel.getLetterSet();
			
			char[] c = new char[1];
			c[0] = getStone().getLetter();
			g.drawChars(c, 0, 1, 8, 34);
			g.setFont(new Font("Arial", Font.PLAIN, 8));
			g.drawString(""+letterSet.getLetter(getStone().getLetter()).getWaarde(), 28, 13);
			g.dispose();			
		}
		return retImg;
	}
}
