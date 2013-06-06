package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class CompetitiesMenu extends JPanel implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel competities;
	private JScrollPane scrollPane;
	private WFButton inviteButton;
	private WFButton backButton;
	private WFButton deleteButton;
	private Connectie connect;

	private JPanel newCompPanel;
	private JTextField compDesc;
	private JTextField startDate;
	private JTextField endDate;
	private WFButton newCompButton;
	private WFButton cancelButton;

	public CompetitiesMenu(boolean observerMode) {
		setMinimumSize(new Dimension(650, 750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
		deleteButton = new WFButton("Delete");
		deleteButton.addActionListener(this);
		inviteButton = new WFButton("Competitie aanmaken");
		inviteButton.addActionListener(this);
		titel = new JLabel("Competitieoverzicht");
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
		initShow();
		showCompetitions();
	}

	public void initShow() {
		competities = new JPanel();
		competities.setLayout(new BoxLayout(competities, BoxLayout.Y_AXIS));
		competities.setBackground(this.getBackground());
		scrollPane = new JScrollPane(competities);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
	}

	public void showCompetitions() {

		connect = new Connectie();
		ResultSet rs;
		int idCompetitie = 0;
		String eigenaar = null;

		// Haal alle competities op uit de database
		rs = connect.voerSelectQueryUit("SELECT * FROM Competitie");
		try {
			while (rs.next()) {

				idCompetitie = rs.getInt("ID");
				eigenaar = rs.getString("Account_naam_eigenaar");
				String compNaam = rs.getString("omschrijving");
				String start = rs.getString("start").substring(0,10);
				String einde = rs.getString("einde").substring(0,10);
				
				
				/*
				check of competitie al is afgelopen
				
				java.sql.Date dt1 = new java.sql.Date(System.currentTimeMillis());
				String dt1Text = dt1.toString();
				Date date = new SimpleDateFormat("yyyy-M-d", Locale.ENGLISH).parse(einde);
				if
				 */

				CompetitieItem compItem = new CompetitieItem(idCompetitie,eigenaar, compNaam, start, einde);

				competities.add(Box.createVerticalStrut(5));
				competities.add(compItem);

			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}

	public void newCompetition() {
		this.remove(scrollPane);
		JLabel compNaam = new JLabel("Competitienaam:");
		compNaam.setForeground(Color.white);
		JLabel compStart = new JLabel("Competitie start (yyyy-mm-dd)");
		compStart.setForeground(Color.white);
		JLabel compEind = new JLabel("Competitie einde (yyyy-mm-dd)");
		compEind.setForeground(Color.white);
		newCompButton = new WFButton("Competitie maken");
		newCompButton.addActionListener(this);
		cancelButton = new WFButton("Annuleren");
		cancelButton.addActionListener(this);
		compDesc = new JTextField();
		startDate = new JTextField();
		endDate = new JTextField();

		compDesc.setMaximumSize(new Dimension(200, 20));
		startDate.setMaximumSize(new Dimension(200, 20));
		endDate.setMaximumSize(new Dimension(200, 20));

		newCompPanel = new JPanel();
		newCompPanel.setMaximumSize(new Dimension(650, 160));
		newCompPanel.setPreferredSize(newCompPanel.getMaximumSize());
		newCompPanel.setBackground(new Color(44, 47, 53));

		Box superBox = new Box(BoxLayout.PAGE_AXIS);

		Box inputBox = new Box(BoxLayout.PAGE_AXIS);
		inputBox.add(compNaam);
		inputBox.add(compDesc);
		inputBox.add(Box.createVerticalStrut(5));
		inputBox.add(compStart);

		startDate.setText(startDate());
		inputBox.add(startDate);
		inputBox.add(Box.createVerticalStrut(5));
		inputBox.add(compEind);
		endDate.setText(endDate());
		inputBox.add(endDate);
		inputBox.add(Box.createVerticalStrut(10));
		superBox.add(inputBox);

		Box lineBox = new Box(BoxLayout.LINE_AXIS);
		lineBox.add(newCompButton);
		lineBox.add(cancelButton);
		superBox.add(lineBox);

		newCompPanel.add(superBox);
		this.add(newCompPanel);
		this.validate();
		repaint();
	}

	public String startDate() {
		java.sql.Date dt1 = new java.sql.Date(System.currentTimeMillis());
		String dt1Text = dt1.toString();
		return dt1Text;
	}

	public String endDate() {
		java.sql.Date dt1 = new java.sql.Date(
				System.currentTimeMillis() + 604800000);
		String dt1Text = dt1.toString();
		return dt1Text;
	}

	public void addCompetition() {

		String eigenaar = Account.getAccountNaam();
		Connectie connect = new Connectie();
		try
		{
			ResultSet rs = connect.doSelect("SELECT COUNT(*) FROM Competitie WHERE Account_naam_eigenaar = '%1$s'", eigenaar);
			if(rs.next())
			{
				if( rs.getInt(1) == 0)
				{
					String q = "INSERT INTO `Competitie` (`Account_naam_eigenaar`, `start`, `einde`, `omschrijving`) VALUES ('"
							+ eigenaar
							+ "','"
							+ startDate.getText()
							+ "','"
							+ endDate.getText() + "','" + compDesc.getText() + "')";
					connect.voerInsertOrUpdateQueryUit(q);
					
					connect.doInsertUpdate("INSERT INTO deelnemer (Account_naam, Competitie_ID) VALUES ('%1$s', (SELECT MAX(ID) FROM competitie))", Account.getAccountNaam());
					
					JOptionPane.showConfirmDialog(null,
							"Je competitie is aangemaakt", "Success!",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{
					JOptionPane.showConfirmDialog(null,
							"Je bent al eigenaar van een competitie!", "Sorry!",
							JOptionPane.OK_CANCEL_OPTION,
							JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
		catch(SQLException e)
		{
			e.printStackTrace();
		}
		connect.closeConnection();
	}
	
	public void deleteCompetition(){
		//connect.voerInsertOrUpdateQueryUit("DELETE FROM deelnemer WHERE Competitie_ID = "+ +");
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(inviteButton)) {
			newCompetition();
		} else if (arg0.getSource().equals(backButton)) {
			setParentContentPane(new GUIMenu());
		} else if (arg0.getSource().equals(newCompButton)) {
			addCompetition();
			this.remove(newCompPanel);
			initShow();
			showCompetitions();
			revalidate();
			repaint();
		} else if (arg0.getSource().equals(cancelButton)) {
			this.remove(newCompPanel);
			initShow();
			showCompetitions();
			revalidate();
			repaint();
		} else if (arg0.getSource().equals(deleteButton)){
						
		}
	}
}
