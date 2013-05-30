package wordfeud;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Stone
{
	private char letter;
	private char iederLetter;
	private BufferedImage lockedImg;
	private BufferedImage unlockedImg;
	private boolean bLocked;
	
	public Stone(char letter)
	{
		this.letter = letter;
		File imageFile = new File("src/images/tile.png");//
		File imageFile2 = new File("src/images/unlockedtile.png");//
		try {
			lockedImg  = ImageIO.read(imageFile);
			unlockedImg  = ImageIO.read(imageFile2);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public Stone(char letter, char iederLetter)
	{
		this.letter = letter;
		this.setIederLetter(iederLetter);
		File imageFile = new File("src/images/tile.png");//
		File imageFile2 = new File("src/images/unlockedtile.png");//
		try {
			lockedImg  = ImageIO.read(imageFile);
			unlockedImg  = ImageIO.read(imageFile2);
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
		if( getLocked() == true )
			return lockedImg;
		else
			return unlockedImg; 
	}
	
	public BufferedImage getImage(boolean bOverrideLock)
	{
		if( bOverrideLock == true )
			return lockedImg;
		
		if( getLocked() == true )
			return lockedImg;
		else
			return unlockedImg;
	}
	
	public boolean getLocked()
	{
		return bLocked;
	}
	
	public void setLocked(boolean locked)
	{
		bLocked = locked;
	}
	public char getIederLetter() {
		return iederLetter;
	}
	public void setIederLetter(char iederLetter) {
		this.iederLetter = iederLetter;
	}
}
