package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private ArrayList<Tile> swapTiles;
	private DropTarget trashDroptarget;

	public SwapGUI(Spel spel) {
		this.letters = spel.getLetterBak();
		this.curSpel = spel;
		this.setTitle("Wissel letters in");
		swapTiles = new ArrayList<Tile>();
		
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
		cp.setBackground(new Color(23, 26, 30));
		
		trashPanel = new JPanel();
		trashPanel.setBackground(new Color(23,26,30));
		trashPanel.setPreferredSize(new Dimension(40,40));
		trashPanel.add(new JLabel(new ImageIcon("src/images/WFBin.png")));
		trashDroptarget = new DropTarget(trashPanel,new DropTargetAdapter() {
			
			@Override
			public void drop(DropTargetDropEvent dtde) {
				
				swapTiles.add(/*verlepen tiles toevoegen aan de arraylist*/ null);
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

	public void cancelFrame() {
		this.dispose();
	}

	public void commitAction() {
		curSpel.getVerloop().skipTurn();
		
		cancelFrame();
	}

}
