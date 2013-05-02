package wordfeud;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Stone
{
	private char letter;
	private BufferedImage img;
	
	public Stone(char letter)
	{
		this.letter = letter;
		File imageFile = new File("src/images/"+ letter + ".png");
		try {
			img  = ImageIO.read(imageFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
	
	public char getLetter()
	{
		return letter;
	}
	
	public void setLetter(char letter)
	{
		this.letter = letter;
	}
	
	public int getValue(LetterSet letterSet)
	{
		return letterSet.getLetter(letter).getWaarde();
	}
	
	public BufferedImage getImage()
	{
		return img;
	}
}
