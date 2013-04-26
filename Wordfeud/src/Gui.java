import java.awt.GraphicsConfiguration;
import java.awt.HeadlessException;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class Gui extends JFrame {

	private Spel gameOfWordfeud;
	private ImageIcon image;
	
	public Gui(){
		gameOfWordfeud = new Spel();
		image = new ImageIcon("images/wordfeud.jpg");
		setIconImage(image.getImage());
		this.setContentPane(gameOfWordfeud);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Wordfeud");
		
		
		this.setVisible(true);
		this.validate();
		this.pack();
	}
}
