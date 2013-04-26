import java.awt.Color;

import javax.swing.JPanel;


public class Letterbak extends JPanel{

	public Letterbak() {
		add(new Tile(Color.black,"1"));
		add(new Tile(Color.black,"2"));
		add(new Tile(Color.black,"3"));
		add(new Tile(Color.black,"4"));
		add(new Tile(Color.black,"5"));
		add(new Tile(Color.black,"6"));
		add(new Tile(Color.black,"7"));
		setBackground(new Color(23,26,30));
	}

}
