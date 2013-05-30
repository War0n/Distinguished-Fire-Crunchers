package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class SwapGUI extends JFrame implements Observer {

	private Letterbak letters;
	private Spel curSpel;
	private LetterbakPanel lettersView;
	private WFButton swapCommit;
	private WFButton swapCancel;
	private JPanel cp;

	public SwapGUI(Spel spel) {
		this.letters = spel.getLetterBak();
		this.curSpel = spel;
		this.setTitle("Wissel letters in");
		cp = (JPanel) this.getContentPane();
		cp.setLayout(new BoxLayout(cp, BoxLayout.X_AXIS));
		cp.setBackground(new Color(23, 26, 30));
		lettersView = new LetterbakPanel(this.letters);
		swapCommit = new WFButton("Swap them!");
		swapCancel = new WFButton("Cancel");
		cp.add(lettersView);
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

	private void commitAction() {
		curSpel.getVerloop().skipTurn();
		cancelFrame();
	}

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub

	}

}
