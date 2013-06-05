package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ObserverGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Spel spel;
	private BordPanel speelVeld;
	private JPanel leftBottomContainer;
	private JPanel scores;
	private JPanel filler;
	private JLabel score;
	private WFButton backButton;
	
	public ObserverGUI(int observableSpelId) {
		spel = new Spel(observableSpelId); // TODO verander in logisch nummer
		setMinimumSize(new Dimension(630, 700));
		setPreferredSize(getMinimumSize());
		score = new JLabel("Scoreveld hier");
		score.setForeground(Color.white);

		setBackground(Color.blue);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		speelVeld = new BordPanel(spel);
		leftBottomContainer = new JPanel();
		backButton = new WFButton("< Terug");
		scores = new JPanel();
		filler = new JPanel();
		scores.setLayout(new BorderLayout());
		scores.setBackground(new Color(23, 26, 30));
		filler.setBackground(new Color(23, 26, 30));
		scores.setMaximumSize(new Dimension(800, 20));

		leftBottomContainer.setLayout(new BoxLayout(leftBottomContainer,
				BoxLayout.PAGE_AXIS));
		this.add(speelVeld);
		scores.add(backButton, BorderLayout.WEST);
		scores.add(score, BorderLayout.EAST);
		leftBottomContainer.add(scores);
		backButton.addActionListener(this);

		add(filler);
		add(leftBottomContainer);
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	public void actionPerformed(ActionEvent arg0) {
		setParentContentPane(new ObserverMenu());
	}
	
	
}