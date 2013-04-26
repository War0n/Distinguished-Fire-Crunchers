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
		image = new ImageIcon("src/images/wordfeud.png");
		setIconImage(image.getImage());
		this.setContentPane(gameOfWordfeud);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Woordje Leggen Niemand Zeggen");
		
		
		this.setVisible(true);
		this.validate();
		this.pack();
	}
}
