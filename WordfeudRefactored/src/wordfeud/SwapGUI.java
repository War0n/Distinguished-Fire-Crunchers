package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class SwapGUI extends JFrame {

	private Letterbak letters;
	private Spel curSpel;
	private LetterbakPanel lettersView;
	private WFButton swapCommit;
	private WFButton swapCancel;
	private JPanel cp;
	private JPanel trashPanel;
	private int swapTiles = 0;
	private ArrayList<Stone> stenen;
	private DropTarget trashDroptarget;
	
	private DataFlavor flav = new DataFlavor(Stone.class, "java-x-StoneTransfer");

	public SwapGUI(Spel spel) {
		stenen = new ArrayList<Stone>();
		this.letters = spel.getLetterBak();
		this.curSpel = spel;
		this.setTitle("Wissel letters in");
		
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
		cp.setBackground(new Color(23, 26, 30));
		
		trashPanel = new JPanel();
		trashPanel.setBackground(new Color(23,26,30));
		trashPanel.setPreferredSize(new Dimension(40,40));
		trashPanel.add(new JLabel(new ImageIcon("src/images/WFBin.png")));
		trashDroptarget = new DropTarget(trashPanel,new DropTargetAdapter() {
			
			@Override
			public void drop(DropTargetDropEvent event) {
				try 
				{
					Transferable tr = event.getTransferable();
		            GUITile an = (GUITile) tr.getTransferData(flav);
		            
		            if( swapTiles < letters.getTilesLeftInPot())
		            {
		            	if (event.isDataFlavorSupported(flav))
			            {
			            	if(an.getTile().getStone() != null)
			            	{
			            		stenen.add(an.getTile().getStone());
			            		an.getTile().setStone(null);
			            		an.repaint();
			            		swapTiles += 1;
			            		event.dropComplete(true);
			            	}
			            }
		            }
		            event.rejectDrop();
				}
				catch (Exception e) 
		        {
		            e.printStackTrace();
		            event.rejectDrop();
		        }
			}
		});
		
		trashPanel.setDropTarget(trashDroptarget);
		
		lettersView = new LetterbakPanel(this.letters);
		swapCommit = new WFButton("Swap them!");
		swapCancel = new WFButton("Cancel");
		cp.add(lettersView);
		cp.add(trashPanel);
		cp.add(Box.createVerticalStrut(10));
		cp.add(swapCommit);
		cp.add(Box.createHorizontalStrut(10));
		cp.add(swapCancel);
		initButtons();

		this.pack();
		this.setAlwaysOnTop(true);
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public void initButtons() {
		swapCommit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				commitAction();
			}
		});

		swapCancel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				cancelFrame();
			}
		});
	}

	public void cancelFrame() 
	{
		lettersView.addLetters(stenen);
		this.dispose();
	}

	public void commitAction() 
	{
		stenen.clear();
		curSpel.getVerloop().doTurn("Swap", false);
		curSpel.getSpelPanel().getLetterbakPanel().repaint();
		
		cancelFrame();
	}

}
