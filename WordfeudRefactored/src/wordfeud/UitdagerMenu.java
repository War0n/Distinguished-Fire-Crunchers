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

public class UitdagerMenu extends JPanel implements ActionListener {

	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel uitdagingen;
	private JScrollPane scrollPane;
	private WFButton backButton;
	private Connectie connect;

	private HashMap<WFButton, String[]> declineBtn = new HashMap<WFButton, String[]>();

	public UitdagerMenu() {
		setMinimumSize(new Dimension(630, 700));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
		titel = new JLabel("Uitgedaagde spelers");
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
				.voerSelectQueryUit("select * from Spel where Account_naam_uitdager ='"
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
					System.out.println("Error: " + e);
				}
				uitdager = rs.getString("Account_naam_tegenstander");
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
				// decline
				WFButton decline = new WFButton("Annuleer uitdaging");
				declineBtn.put(decline, challengeSpecs);
				decline.addActionListener(this);

				lineBoxControls.add(decline);
				vertBox.add(lineBoxText);
				vertBox.add(Box.createVerticalStrut(5));
				vertBox.add(lineBoxControls);
				comp.add(vertBox);
				uitdagingen.add(Box.createVerticalStrut(5));
				uitdagingen.add(comp);

			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
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
		if (arg0.getSource().equals(backButton)) {
			setParentContentPane(new UitdagingMenu());
		} else if (arg0.getActionCommand().equals("Annuleer uitdaging")) {
			// decline uitdaging
			String[] a = declineBtn.get(arg0.getSource());
			declineUitdaging(a);
		}

	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

}
