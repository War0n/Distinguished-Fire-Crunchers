package wordfeud;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;

public class StoneTransfer implements Transferable 
{
	private DataFlavor flav = new DataFlavor(Stone.class, "java-x-StoneTransfer");
	private GUITile transferableTile;
	
    public StoneTransfer(GUITile transferableTile) {
        this.transferableTile = transferableTile;
    } 

    @Override
    public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[] { flav };
    }

    @Override
    public boolean isDataFlavorSupported(DataFlavor flavor) {
        return flavor.equals(flav);
    }

    @Override
    public Object getTransferData(DataFlavor flavor)
        throws UnsupportedFlavorException, IOException {

        if (flavor.equals(flav)){
            return transferableTile;
        } else {
            throw new UnsupportedFlavorException(flavor);
        }
    }
}