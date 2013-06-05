package wordfeud;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.sound.sampled.DataLine.Info;
import javax.swing.JPanel;

public class BordPanel extends JPanel
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Bord speelVeld;
	private GUITile[][] tiles;
	
	
	private DataFlavor flav = new DataFlavor(Stone.class, "java-x-StoneTransfer");
	
	public BordPanel(Bord speelVeld) {
		this.setSpeelVeld(speelVeld);
		speelVeld.setPanel(this);
		setLayout(new GridLayout(15,15,2,2));
		setBackground(new Color(23,26,30));
		setMaximumSize(new Dimension(630,630));
		setPreferredSize(getMaximumSize());
		tiles = new GUITile[15][15];
		
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y] = new GUITile(speelVeld.getTile(new Point(x,y)));
				add(tiles[x][y]);
				
				DragSource ds = new DragSource();
			    ds.createDefaultDragGestureRecognizer(tiles[x][y], DnDConstants.ACTION_MOVE, new MyDragGestureListener());

			    new MyDropTargetListener(tiles[x][y]);
			}
		}	
	}

	public Bord getSpeelVeld()
	{
		return speelVeld;
	}

	public void setSpeelVeld(Bord speelVeld)
	{
		this.speelVeld = speelVeld;
	}
	
	public void lockField() // implementeer hier de check of het wel echt woorden zijn!
	{
		speelVeld.lockField();
	}
	
	public void repaitTile(int x, int y)
	{
		tiles[x][y].repaint();
	}
	
	public ArrayList<Stone> clearField() //
	{
		ArrayList<Stone> stenen = new ArrayList<Stone>();
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				if(tiles[x][y].getTile().getLocked() == false)
				{
					stenen.add(tiles[x][y].getTile().getStone());
					tiles[x][y].getTile().setStone(null);
					tiles[x][y].repaint();
				}
			}
		}	
		return stenen;
	}
	
	public class MyDragGestureListener implements DragGestureListener 
	{

	    @Override
	    public void dragGestureRecognized(DragGestureEvent event) 
	    {
	        Cursor cursor = null;
	        GUITile panel = (GUITile) event.getComponent();
	        if(panel.getTile().getLocked() == true)
	        {
	        	return; // niet laten slepen als steen gepinned is.
	        }

	        if (event.getDragAction() == DnDConstants.ACTION_MOVE) 
	        {
	            cursor = DragSource.DefaultMoveDrop;
	        }
	        event.startDrag(cursor, new StoneTransfer(panel));
	    }
	}

	private class MyDropTargetListener extends DropTargetAdapter implements DropTargetListener 
	{
	    private GUITile tile;

	    public MyDropTargetListener(GUITile tile) 
	    {
	        this.tile = tile;
	        new DropTarget(tile, DnDConstants.ACTION_MOVE, this, true, null);
	    }

	    @Override
	    public void drop(DropTargetDropEvent event) 
	    {
	        try 
	        {
	            Transferable tr = event.getTransferable();
	            GUITile an = (GUITile) tr.getTransferData(flav);

	            if (event.isDataFlavorSupported(flav)) 
	            {
	            	if(this.tile.getTile().getStone() == null) // geen steen, dus leggen
	            	{
		                event.acceptDrop(DnDConstants.ACTION_MOVE);
		                this.tile.getTile().setStone(an.getTile().getStone());
		                an.getTile().setStone(null);
		                an.validate();
		                an.repaint();
		                event.dropComplete(true);
		                speelVeld.getSpel().getLetterBak().lockButtons();
		                this.tile.validate();
		                this.tile.repaint();
	            	}
	            	else // ligt al een steen, dus swappen? 
	            	{
	            		if( this.tile.getTile().getLocked() == false )
	            		{
	            			Stone tmp = this.tile.getTile().getStone();
	            			this.tile.getTile().setStone(an.getTile().getStone());
			                an.getTile().setStone(tmp);
			                an.validate();
			                an.repaint();
			                event.dropComplete(true);
			                this.tile.validate();
			                this.tile.repaint();
	            		}
	            	}
	                return;
	                
	            }
	            event.rejectDrop();
	        }
	        catch (Exception e) 
	        {
	            e.printStackTrace();
	            event.rejectDrop();
	        }
	    }
	    
	}
}
