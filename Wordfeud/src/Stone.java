import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;


@SuppressWarnings("serial")
public class Stone extends JPanel{

	private char letter;
	private int value;
	private BufferedImage image;
	
	public Stone(char letter,int value) {
		this.letter = letter;
		this.value = value;
		setVisible(false);
	}
	
	public BufferedImage decideImage(){
		File imageFile = new File("src/images/"+ letter + ".png");
		try {
			image  = ImageIO.read(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		return image;
	}
}
