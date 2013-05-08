package wordfeud;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.GridLayout;
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
		setLayout(new GridLayout(15,15,2,2));
		setBackground(new Color(23,26,30));
		
		tiles = new GUITile[15][15];
		
		for(int y = 0; y < 15; y++)
		{
			for(int x = 0; x < 15; x++)
			{
				tiles[x][y] = new GUITile(speelVeld.getTile(x,y));
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
	
	public class MyDragGestureListener implements DragGestureListener 
	{

	    @Override
	    public void dragGestureRecognized(DragGestureEvent event) 
	    {
	        Cursor cursor = null;
	        GUITile panel = (GUITile) event.getComponent();

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
	                event.acceptDrop(DnDConstants.ACTION_MOVE);
	                this.tile.getTile().setStone(an.getTile().getStone());
	                an.getTile().setStone(null);
	                an.validate();
	                an.repaint();
	                event.dropComplete(true);
	                this.tile.validate();
	                this.tile.repaint();
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
