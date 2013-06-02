package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ActieveSpellenMenu extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel spellen;
	private boolean observer;
	private JScrollPane scrollPane;
	private WFButton inviteButton;
	private WFButton backButton;
	private Connectie connect;
	private Spel spel;

	private HashMap<WFButton, String[]> playBtn = new HashMap<WFButton, String[]>();

	public ActieveSpellenMenu(boolean observer) {
		this.observer = observer;
		spel = new Spel(5);
		setMinimumSize(new Dimension(650, 750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
		inviteButton = new WFButton("Speler uitdagen");
		inviteButton.addActionListener(this);
		titel = new JLabel("Actieve spellen");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial", Font.BOLD, 30));
		head = new JPanel();
		head.setBackground(this.getBackground());
		head.setMaximumSize(new Dimension(650, 50));
		head.setPreferredSize(head.getMaximumSize());
		head.add(titel);
		functies = new JPanel();
		functies.setBackground(new Color(29, 144, 160));
		functies.setMaximumSize(new Dimension(650, 40));
		functies.setPreferredSize(functies.getMaximumSize());
		functies.setLayout(new FlowLayout());
		functies.add(backButton);
		// functies.add(inviteButton);

		add(head);
		add(functies);

		spellen = new JPanel();
		spellen.setLayout(new BoxLayout(spellen, BoxLayout.Y_AXIS));
		spellen.setBackground(this.getBackground());
		scrollPane = new JScrollPane(spellen);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);

		showSpellen();
	}

	public void showSpellen() {
		connect = new Connectie();
		ResultSet rs;
		ResultSet rs2;
		int idCompetitie = 0;
		String comp_naam = null;

		// Haal alle uitdagingen op uit de db
		rs = connect
				.voerSelectQueryUit("select * from Spel where (Account_naam_tegenstander ='"
						+ Account.getAccountNaam()
						+ "' OR Account_naam_uitdager='"
						+ Account.getAccountNaam()
						+ "') AND Toestand_type = 'Playing'");
		try {
			while (rs.next()) {

				idCompetitie = rs.getInt("Competitie_ID");
				rs2 = connect
						.voerSelectQueryUit("select * from Competitie where ID ='"
								+ idCompetitie + "'");
				try {
					rs2.next();
					comp_naam = rs2.getString("omschrijving");
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error: " + e);
				}
				// Wie is de tegenstander in het spel? Degene die je niet zelf
				// bent ofcourse
				String p1 = rs.getString("Account_naam_uitdager");
				String p2 = rs.getString("Account_naam_tegenstander");
				String tegenstander = (p1.equals(Account.getAccountNaam())) ? p2
						: p1;

				String spelID = rs.getString("ID");
				JPanel comp = new JPanel();
				comp.setMaximumSize(new Dimension(650, 80));
				comp.setPreferredSize(comp.getMaximumSize());
				comp.setBackground(new Color(44, 47, 53));

				Box vertBox = new Box(BoxLayout.PAGE_AXIS);
				Box lineBoxText = new Box(BoxLayout.LINE_AXIS);
				Box lineBoxControls = new Box(BoxLayout.LINE_AXIS);
				// check of het jou beurt is moet hier.
				// JLabel myTurn = new JLabel("Mijn beurt: ");
				JLabel uitdagingTxt = new JLabel(tegenstander + " ["
						+ comp_naam + "]");
				uitdagingTxt.setForeground(Color.white);
				lineBoxText.add(uitdagingTxt);

				String[] challengeSpecs = { spelID, comp_naam };
				// accept
				WFButton accept = new WFButton("open spel");
				accept.addActionListener(this);
				playBtn.put(accept, challengeSpecs);

				lineBoxControls.add(accept);
				vertBox.add(lineBoxText);
				vertBox.add(Box.createVerticalStrut(5));
				vertBox.add(lineBoxControls);
				comp.add(vertBox);
				spellen.add(Box.createVerticalStrut(5));
				spellen.add(comp);

			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getActionCommand().equals("open spel")) {
			// addCompetition();
			String[] a = playBtn.get(arg0.getSource());
			System.out.println("Open spel [id: " + a[0] + "]");
			if (!observer) {
				setParentContentPane(new SpelPanel(Integer.parseInt(a[0])));
			} else {
				setParentContentPane(new ObserverGUI(Integer.parseInt(a[0])));
			}
		} else if (arg0.getSource().equals(backButton)) {
			setParentContentPane(new GUIMenu());
		}
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

}
