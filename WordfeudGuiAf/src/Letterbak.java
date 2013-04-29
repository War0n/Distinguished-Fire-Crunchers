import java.awt.Color;
import java.util.ArrayList;

import javax.swing.JPanel;


public class Letterbak extends JPanel{
	
	private ArrayList<Tile> letters;

	
	public Letterbak() {
		letters = new ArrayList<Tile>();
		letters.add(new Tile(Color.black,"1"));
		letters.add(new Tile(Color.black,"2"));
		letters.add(new Tile(Color.black,"3"));
		letters.add(new Tile(Color.black,"4"));
		letters.add(new Tile(Color.black,"5"));
		letters.add(new Tile(Color.black,"6"));
		letters.add(new Tile(Color.black,"7"));
		for(Tile t:letters){
			setLetter(new Stone('A',5));
			add(t);
		}
		setBackground(new Color(23,26,30));
		repaint();
	}
	
	public void setLetter(Stone stone){
		for(Tile t:letters){
			if(t.getStone() == null){
				t.setStone(stone);
				break;
			}
		}
	}

}
