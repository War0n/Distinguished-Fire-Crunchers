package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UitdagingMenu extends JPanel implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel uitdagingen;
	private JScrollPane scrollPane;
	private WFButton inviteButton;
	private WFButton backButton;
	private Connectie connect;
	private JFrame popup = null;
	private HashMap<Integer,Integer> existingCompetitions;

	private JPanel challengePlayer;
	private JTextField playerName;
	private JComboBox<String> selectedCompetition;
	private JComboBox<String> selectedPlayer;
	
	private WFButton challengeButton;
	private WFButton challengeCancelButton;

	private HashMap<WFButton, String[]> acceptBtn = new HashMap<WFButton, String[]>();
	private HashMap<WFButton, String[]> declineBtn = new HashMap<WFButton, String[]>();

	public UitdagingMenu() {
		existingCompetitions =new HashMap<Integer,Integer>();
		// TODO Auto-generated constructor stub
		setMinimumSize(new Dimension(630, 700));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
		inviteButton = new WFButton("Speler uitdagen");
		inviteButton.addActionListener(this);
		titel = new JLabel("Uitdagingen");
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
		functies.add(inviteButton);

		add(head);
		add(functies);

		setUitdagingenList();

		showUitdagingen();
	}

	public void setUitdagingenList() {
		uitdagingen = new JPanel();
		uitdagingen.setLayout(new BoxLayout(uitdagingen, BoxLayout.Y_AXIS));
		uitdagingen.setBackground(this.getBackground());
		scrollPane = new JScrollPane(uitdagingen);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
	}

	public void showUitdagingen() {
		connect = new Connectie();
		ResultSet rs;
		ResultSet rs2;
		int idCompetitie = 0;
		String comp_naam = null;
		String uitdager = null;

		// Haal alle uitdagingen op uit de db
		rs = connect
				.voerSelectQueryUit("select * from Spel where Account_naam_tegenstander ='"
						+ Account.getAccountNaam()
						+ "' AND Toestand_type ='Request' AND Reaktie_type = 'Unknown'");
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
				uitdager = rs.getString("Account_naam_uitdager");
				String spelID = rs.getString("ID");
				JPanel comp = new JPanel();
				comp.setMaximumSize(new Dimension(650, 80));
				comp.setPreferredSize(comp.getMaximumSize());
				comp.setBackground(new Color(44, 47, 53));

				Box vertBox = new Box(BoxLayout.PAGE_AXIS);
				Box lineBoxText = new Box(BoxLayout.LINE_AXIS);
				Box lineBoxControls = new Box(BoxLayout.LINE_AXIS);

				JLabel uitdagingTxt = new JLabel(uitdager + " || " + comp_naam);
				uitdagingTxt.setForeground(Color.white);
				lineBoxText.add(uitdagingTxt);

				String[] challengeSpecs = { uitdager, comp_naam, spelID };
				// accept
				WFButton accept = new WFButton("accept");
				accept.addActionListener(this);
				acceptBtn.put(accept, challengeSpecs);
				// decline
				WFButton decline = new WFButton("decline");
				declineBtn.put(decline, challengeSpecs);
				decline.addActionListener(this);

				lineBoxControls.add(accept);
				lineBoxControls.add(decline);
				vertBox.add(lineBoxText);
				vertBox.add(Box.createVerticalStrut(5));
				vertBox.add(lineBoxControls);
				comp.add(vertBox);
				uitdagingen.add(Box.createVerticalStrut(5));
				uitdagingen.add(comp);

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}

	public void addUitdaging() {

		inviteButton.setVisible(false);
		this.remove(scrollPane);

		JLabel chalName = new JLabel("Naam speler:");
		chalName.setForeground(Color.white);
		JLabel chalComp = new JLabel("Competitie ID:");
		chalComp.setForeground(Color.white);
		challengeButton = new WFButton("Uitdagen");
		challengeButton.addActionListener(this);
		challengeCancelButton = new WFButton("Annuleren");
		challengeCancelButton.addActionListener(this);
		playerName = new JTextField();
		selectedCompetition = new JComboBox<String>();
		selectedPlayer = new JComboBox<String>();
		selectedPlayer.addItem("Selecteer competitie");
		selectedCompetition.addItemListener(this);
		playerName.setMaximumSize(new Dimension(200, 20));
		selectedCompetition.setMaximumSize(new Dimension(200, 20));
		selectedPlayer.setMaximumSize(new Dimension(200, 20));
		addExistingCompetitions();

		challengePlayer = new JPanel();
		challengePlayer.setMaximumSize(new Dimension(650, 160));
		challengePlayer.setPreferredSize(challengePlayer.getMaximumSize());
		challengePlayer.setBackground(new Color(44, 47, 53));

		Box superBox = new Box(BoxLayout.PAGE_AXIS);

		Box inputBox = new Box(BoxLayout.PAGE_AXIS);
		chalName.setAlignmentX(RIGHT_ALIGNMENT);
		chalComp.setAlignmentX(RIGHT_ALIGNMENT);
		
		//inputBox.add(playerName);

		inputBox.add(chalComp);
		inputBox.add(selectedCompetition);
		inputBox.add(Box.createVerticalStrut(10));
		inputBox.add(chalName);
		inputBox.add(selectedPlayer);
		inputBox.add(Box.createVerticalStrut(10));
		superBox.add(inputBox);
		superBox.add(Box.createVerticalStrut(10));
		Box lineBox = new Box(BoxLayout.LINE_AXIS);
		lineBox.add(challengeButton);
		lineBox.add(challengeCancelButton);
		superBox.add(lineBox);

		challengePlayer.add(superBox);

		this.add(challengePlayer);
		this.validate();
		repaint();
	}

	public void extendUitdaging() {
		String naam = Account.getAccountNaam();
		
		int compID = existingCompetitions.get(selectedCompetition.getSelectedIndex());
		String naam2 = selectedPlayer.getSelectedItem().toString();
		String toestand = "Request";
		String reaktie = "Unknown";
		String letterset = "NL";
		String bord = "Standard";
		final ImageIcon icon = new ImageIcon("src/images/conf.jpg");

		String q = "INSERT INTO Spel (Competitie_ID, Toestand_type, Account_naam_uitdager, Account_naam_tegenstander, moment_uitdaging, Reaktie_type, moment_reaktie, Bord_naam, LetterSet_naam) VALUES("
				+ compID
				+ ",'"
				+ toestand
				+ "','"
				+ naam
				+ "','"
				+ naam2
				+ "',NOW(),'"
				+ reaktie
				+ "',NULL,'"
				+ bord
				+ "','"
				+ letterset
				+ "')";
		connect = new Connectie();

		try {
			ResultSet rss = connect.doSelect(
					"SELECT COUNT(*) FROM Account WHERE naam = '%1$s'", naam2);
			if (rss.next()) {
				if (rss.getInt(1) == 0) {
					connect.closeConnection();
					JOptionPane.showMessageDialog(popup, naam2
							+ " bestaat niet", "Foutje!",
							JOptionPane.WARNING_MESSAGE);
					popup = null;
					return;
				}

			}
			rss = connect.doSelect(
					"SELECT COUNT(*) FROM Competitie WHERE ID = %1$s", compID);
			if (rss.next()) {
				if (rss.getInt(1) == 0) {
					connect.closeConnection();
					return;
				}
			}
		} catch (SQLException e2) {
			e2.printStackTrace();
		}

		connect.voerInsertOrUpdateQueryUit(q);
		connect.doInsertUpdate(
				"INSERT INTO beurt (ID, Spel_ID, Account_naam, score, Aktie_type) VALUES (1, (SELECT MAX(ID) FROM Spel), '%1$s', 0, 'Begin')",
				Account.getAccountNaam());

		ResultSet rs = connect
				.voerSelectQueryUit("SELECT karakter, aantal FROM lettertype WHERE LetterSet_code = 'NL'");
		try {
			int j = 1;
			while (rs.next()) {
				for (int i = 0; i < rs.getInt(2); i++) {
					connect.doInsertUpdate(
							"INSERT INTO letter (ID, Spel_ID, LetterType_LetterSet_code, LetterType_karakter) VALUES (%1$d, (SELECT MAX(ID) FROM Spel), 'NL', '%2$s')",
							j, rs.getString(1));
					j++;
				}
			}
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		ResultSet rs2 = connect
				.voerSelectQueryUit("SELECT Letter_ID FROM pot WHERE Spel_ID = (SELECT MAX(ID) FROM Spel) ORDER BY RAND() LIMIT 7");
		try {
			while (rs2.next()) {

				connect.doInsertUpdate(
						"INSERT INTO letterbakjeletter (Spel_ID, Letter_ID, Beurt_ID) VALUES ((SELECT MAX(ID) FROM Spel), %1$d, 1)",
						rs2.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect.closeConnection();
		this.remove(challengePlayer);
		inviteButton.setVisible(true);
		setUitdagingenList();
		showUitdagingen();
		this.validate();
		repaint();
		JOptionPane.showMessageDialog(popup, "Je hebt " + naam2
				+ " uitgedaagd!", "Uitdaging.",
				JOptionPane.INFORMATION_MESSAGE, icon);
		popup = null;

	}

	public void acceptUitdaging(String[] a) {
		String q = "UPDATE Spel SET Toestand_type = 'Playing', Reaktie_type = 'Accepted', moment_reaktie = now() WHERE ID='"
				+ a[2] + "'";

		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit(q);

		connect.doInsertUpdate(
				"INSERT INTO beurt (ID, Spel_ID, Account_naam, score, Aktie_type) VALUES (2, %1$s, '%2$s', 0, 'Begin')",
				a[2], Account.getAccountNaam());

		ResultSet rs2 = connect
				.doSelect(
						"SELECT Letter_ID FROM pot WHERE Spel_ID = %1$s ORDER BY RAND() LIMIT 7",
						a[2]);
		try {
			while (rs2.next()) {

				connect.doInsertUpdate(
						"INSERT INTO letterbakjeletter (Spel_ID, Letter_ID, Beurt_ID) VALUES (%1$s, %2$d, 2)",
						a[2], rs2.getInt(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		connect.closeConnection();

		this.remove(scrollPane);
		setUitdagingenList();
		showUitdagingen();
		this.validate();
		repaint();
	}

	public void declineUitdaging(String[] a) {
		String q = "UPDATE Spel SET Reaktie_type = 'Rejected', moment_reaktie = now() WHERE ID='"
				+ a[2] + "'";

		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit(q);
		connect.closeConnection();

		this.remove(scrollPane);
		setUitdagingenList();
		showUitdagingen();
		this.validate();
		repaint();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if (arg0.getSource().equals(inviteButton)) {
			addUitdaging();
		} else if (arg0.getSource().equals(backButton)) {
			setParentContentPane(new GUIMenu());
		} else if (arg0.getSource().equals(challengeCancelButton)) {
			this.remove(challengePlayer);
			inviteButton.setVisible(true);
			setUitdagingenList();
			showUitdagingen();
			this.validate();
			repaint();
		} else if (arg0.getSource().equals(challengeButton)) {
			extendUitdaging();
		} else if (arg0.getActionCommand().equals("accept")) {
			// accepteer uitdaging
			String[] a = acceptBtn.get(arg0.getSource());
			acceptUitdaging(a);

		} else if (arg0.getActionCommand().equals("decline")) {
			// decline uitdaging
			String[] a = declineBtn.get(arg0.getSource());
			declineUitdaging(a);
		}

	}

	public void addExistingCompetitions() {
		connect = new Connectie();
		ResultSet competitieRS = connect
				.voerSelectQueryUit("SELECT ID,omschrijving FROM competitie");
		try {
			int i = 0;
			while (competitieRS.next()) {
				existingCompetitions.put(i,competitieRS.getInt("ID"));
				selectedCompetition.insertItemAt(competitieRS.getString("omschrijving"), i);
				i++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		int curCompID = existingCompetitions.get(selectedCompetition.getSelectedIndex());
		String q = "SELECT Account_naam FROM Deelnemer WHERE Competitie_ID ="+curCompID+"";
		connect = new Connectie();
		ResultSet rs = connect.voerSelectQueryUit(q);
		selectedPlayer.removeAllItems();
		try {
			while(rs.next()){
				selectedPlayer.addItem(rs.getString(1));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
