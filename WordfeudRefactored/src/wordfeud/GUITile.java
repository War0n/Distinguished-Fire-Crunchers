package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUITile extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel tileLabel;
	private Tile tile;
	
	private boolean bOverrideLock;
	
	public GUITile(Tile tile) {
		initTile(tile);
	}
	
	public GUITile(TileType type) {
		initTile(new Tile(type));
	}
	
	public void initTile(Tile tile)
	{
		this.tile = tile;
		String label = "";
		Color bgColor = new Color(0,0,150);
		switch(tile.getType())
		{
		case TYPE_NONE:
			bgColor = new Color(44,47,53);
			label = "";
			break;
		case TYPE_DW:
			bgColor = new Color(192,121,33);
			label = "DW";
			break;
		case TYPE_TW:
			bgColor = new Color(156,71,26);
			label = "TW";
			break;
		case TYPE_DL:
			bgColor = new Color(122,162,113);
			label = "DL";
			break;
		case TYPE_TL:
			bgColor = new Color(29,144,160);
			label = "TL";
			break;
		case TYPE_START:
			bgColor = new Color(97,65,93);
			label = "\u2605";
			break;
		default:
			bgColor = new Color(44,47,53);
			label = "";
			break;
		}
		tileLabel = new JLabel(label);
		tileLabel.setForeground(Color.WHITE);
		tileLabel.setFont(new Font("BabelStone Han",Font.BOLD,18));
		

		setMaximumSize(new Dimension(40,40));
		setMinimumSize(new Dimension(40,40));
		setPreferredSize(getMaximumSize());
		setBackground(bgColor);
		this.addMouseListener(new MouseAdapter() {
			
			@Override
			public void mousePressed(MouseEvent e) {
				// TODO Auto-generated method stub
				super.mousePressed(e);
				if(getStone() != null){
				try {
					AudioInputStream stream = AudioSystem.getAudioInputStream(new File("src/sounds/click.wav"));
				    AudioFormat format = stream.getFormat();
				    Info info = new DataLine.Info(Clip.class, format);
				    Clip clip = (Clip) AudioSystem.getLine(info);
				    clip.open(stream);
				    clip.start();
				} catch (MalformedURLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (UnsupportedAudioFileException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (LineUnavailableException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				}
				
			}
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				super.mouseReleased(arg0);
			}
			
		});
		add(tileLabel);
	}
	
	public Stone getStone(){
		return tile.getStone();
	}
	
	public void setStone(Stone stone){
		tile.setStone(stone);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		if( tile.getStone() != null )
		{
			g.drawImage(tile.getStone().getImage(isOverride()), 0, 0, this);
			
			Font tmp = g.getFont();
			LetterSet letterSet = new LetterSet("NL");
			
			//g.drawImage(img, 0, 0, this);
			
			char[] c = new char[1];
			c[0] = tile.getStone().getLetter();
			g.setFont(tmp.deriveFont(32.0f));
			g.drawChars(c, 0, 1, 8, 34);
			g.setFont(tmp);
			g.drawString(""+letterSet.getLetter(c[0]).getWaarde(), 28, 13);
			this.tileLabel.setVisible(false);
		}
		else
			this.tileLabel.setVisible(true);
	}

	public Tile getTile()
	{
		return tile;
	}

	public void setTile(Tile tile)
	{
		this.tile = tile;
	}

	public boolean isOverride() {
		return bOverrideLock;
	}

	public void setOverride(boolean bOverrideLock) {
		this.bOverrideLock = bOverrideLock;
	}
	
}