import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;

import javax.swing.JFrame;


public class Gui extends JFrame {

	private Spel gameOfWordfeud;
	public Gui(){
		gameOfWordfeud = new Spel();
		this.setContentPane(gameOfWordfeud);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Wordfeud");
		
		
		this.setVisible(true);
		this.validate();
		this.pack();
	}
}
