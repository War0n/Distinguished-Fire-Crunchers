package wordfeud;

import java.awt.Color;
import java.awt.Cursor;
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
import java.util.ArrayList;

import javax.swing.JPanel;

public class LetterbakPanel extends JPanel
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Letterbak letterbak;
	private ArrayList<GUITile> tiles;
	
	private DataFlavor flav = new DataFlavor(Stone.class, "java-x-StoneTransfer");//
	
	public LetterbakPanel(Letterbak letterbak) 
	{
		this.setLetterbak(letterbak);
		tiles = new ArrayList<GUITile>();
		for(int i = 0; i < 7; i++)
		{
			tiles.add(new GUITile(TileType.TYPE_NONE));
			add(tiles.get(i));
			tiles.get(i).setStone(letterbak.getStones().get(i));
			
			DragSource ds = new DragSource();
		    ds.createDefaultDragGestureRecognizer(tiles.get(i), DnDConstants.ACTION_MOVE, new MyDragGestureListener());

		    new MyDropTargetListener(tiles.get(i));
		}
		setBackground(new Color(23,26,30));
		repaint();
	}
	
	public void setLetter(int index, Stone stone){
		tiles.get(index).setStone(stone);
	}

	public Letterbak getLetterbak()
	{
		return letterbak;
	}

	public void setLetterbak(Letterbak letterbak)
	{
		this.letterbak = letterbak;
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

