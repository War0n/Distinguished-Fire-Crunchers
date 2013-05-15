package wordfeud;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class ModeratorPanel extends JPanel{
	
	private JFrame activeFrame;

	public ModeratorPanel(){
		this.setMinimumSize(new Dimension(650,750));
		this.setPreferredSize(getMinimumSize());
	}
}
