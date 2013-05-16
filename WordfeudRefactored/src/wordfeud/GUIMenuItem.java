package wordfeud;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;


public class GUIMenuItem extends JPanel{
	
	public GUIMenuItem() {
		setMaximumSize(new Dimension(650,80));
		setPreferredSize(this.getMaximumSize());
		setBackground(new Color(44,47,53));
	}

}
