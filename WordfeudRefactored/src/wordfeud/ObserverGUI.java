package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class ObserverGUI extends JPanel implements ActionListener {
	private static final long serialVersionUID = 1L;
	private Spel spel;
	private BordPanel speelVeld;
	private JPanel scores;
	private JPanel filler;
	private JLabel score;
	private WFButton backButton;

	private WFButton prevTurn, nextTurn;
	private JLabel beurtLabel;
	private JPanel beurtPanel;
	private JLabel beurtVan;

	private int beurt = 2;

	private LetterbakPanel letterbak;

	public ObserverGUI(int observableSpelId) {
		spel = new Spel(observableSpelId); // TODO verander in logisch nummer
		setMinimumSize(new Dimension(630, 710));
		setPreferredSize(getMinimumSize());
		score = new JLabel();
		score.setBackground(Color.red);
		score.setForeground(Color.white);

		setBackground(Color.blue);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		speelVeld = new BordPanel(spel, true);
		spel.getBord().setPanel(speelVeld);
		backButton = new WFButton("< Terug");
		scores = new JPanel();
		filler = new JPanel();

		scores.setLayout(new BorderLayout());
		scores.setBackground(new Color(23, 26, 30));
		filler.setBackground(new Color(23, 26, 30));
		scores.setMaximumSize(new Dimension(800, 20));

		this.add(speelVeld);
		scores.add(backButton, BorderLayout.WEST);
		scores.add(score, BorderLayout.EAST);
		backButton.addActionListener(this);

		letterbak = new LetterbakPanel(spel);

		beurtPanel = new JPanel();
		beurtPanel.setBackground(filler.getBackground());
		beurtLabel = new JLabel("Beurt: " + (beurt - 1));
		prevTurn = new WFButton("Vorige");
		nextTurn = new WFButton("Volgende");
		beurtVan = new JLabel("Beurt van: " + getBeurtNaam());

		beurtLabel.setForeground(Color.white);
		beurtVan.setForeground(Color.white);

		spel.getLetterBak().laadStenenVoorBeurt(beurt);
		if (letterbak != null) {
			letterbak.repaint();
		}

		prevTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (beurt > 2)
					beurt--;
				spel.getBord().plaatsLetters(beurt);
				spel.getLetterBak().laadStenenVoorBeurt(beurt);
				if (letterbak != null) {
					letterbak.repaint();
				}
				updateBeurtLabel();
				updateScoreLabel();
			}

		});
		nextTurn.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Connectie con = new Connectie();
				try {
					ResultSet rs = con
							.doSelect(
									"SELECT MAX(ID) FROM beurt WHERE Spel_ID = %1$d AND Aktie_type != 'End'",
									spel.getSpelId());
					if (rs.next()) {
						if (beurt < rs.getInt(1))
							beurt++;
					}

					spel.getBord().plaatsLetters(beurt);
					spel.getLetterBak().laadStenenVoorBeurt(beurt);
					if (letterbak != null) {
						letterbak.repaint();
					}
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				con.closeConnection();
				updateBeurtLabel();
				updateScoreLabel();
			}

		});

		beurtPanel.add(score);
		beurtPanel.add(backButton);
		beurtPanel.add(prevTurn);
		beurtPanel.add(beurtLabel);
		beurtPanel.add(nextTurn);
		beurtPanel.add(beurtVan);
		
		updateScoreLabel();

		add(letterbak);
		add(beurtPanel);

	}

	public String getBeurtNaam() {
		String ret = "???";
		Connectie con = new Connectie();
		try {
			ResultSet rs = con
					.doSelect(
							"SELECT Account_naam FROM beurt WHERE Spel_ID = %1$d AND ID = %2$d",
							spel.getSpelId(), beurt - 1);
			if (rs.next()) {
				ret = rs.getString(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		con.closeConnection();
		return ret;
	}

	public void updateBeurtLabel() {
		beurtLabel.setText("Beurt: " + (beurt - 1));
		beurtVan.setText("Beurt van: " + getBeurtNaam());
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	public void actionPerformed(ActionEvent arg0) {
		setParentContentPane(new ObserverMenu());
	}

	public void setScore(String text) {
		score.setText(text);
		repaint();
	}
	
	public void updateScoreLabel() {
		String ret = "";
		Connectie con = new Connectie();
		try {
			ResultSet rs = con
					.doSelect(
							"SELECT Account_naam, SUM(score) FROM wordfeud.beurt WHERE Spel_ID = %1$d AND ID <= %2$d GROUP BY Account_naam ORDER BY Account_naam ASC;",
							spel.getSpelId(), beurt);
			while (rs.next()) {
				ret += (rs.getString(1) + ": " + rs.getInt(2) + "        ");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		setScore(ret.trim());
	}

}