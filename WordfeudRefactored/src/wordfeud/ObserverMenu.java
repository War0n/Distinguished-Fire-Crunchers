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
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ObserverMenu extends JPanel implements ActionListener, ItemListener {

	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel spellen;
	private JScrollPane scrollPane;
	private WFButton backButton;
	private Connectie connect;

	private JComboBox<String> selectedCompetition = new JComboBox<String>();
	private HashMap<Integer,Integer> existingCompetitions;
	
	private int selectedCompetitionID;
	
	private HashMap<WFButton, String[]> playBtn = new HashMap<WFButton, String[]>();

	public ObserverMenu() {
		existingCompetitions =new HashMap<Integer,Integer>();
		selectedCompetition.addItemListener(this);
		selectedCompetitionID = 1;
		setMinimumSize(new Dimension(630, 700));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
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
		functies.add(selectedCompetition);
		
		add(head);
		add(functies);
		initSpellen();
		
		addExistingCompetitions();
		
		showSpellen();
	}

	public boolean myTurnActiveSpellen(int spelId) {
		boolean myTurn = false;
		Connectie con = new Connectie();
		ResultSet rs;
		String accountNaam = "";
		rs = con.doSelect("SELECT Account_naam FROM beurt WHERE Spel_ID = "
				+ spelId + " ORDER BY ID DESC LIMIT 1");
		try {
			if (rs.next()) {
				accountNaam = rs.getString("Account_naam");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		if (!Account.getAccountNaam().equals(accountNaam)) {
			myTurn = true;
		}
		con.closeConnection();
		return myTurn;
	}

	/**
	 * 
	 */
	
	public void initSpellen(){
		spellen = new JPanel();
		spellen.setLayout(new BoxLayout(spellen, BoxLayout.Y_AXIS));
		spellen.setBackground(this.getBackground());
						
		scrollPane = new JScrollPane(spellen);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
	}
	public void showSpellen() {
		connect = new Connectie();
		ResultSet rs;
		String comp_naam = null;
		// Haal alle spellen op uit de db voor de geselecteerde competitie
		
		rs = connect
				.voerSelectQueryUit("select Account_naam_uitdager, Account_naam_tegenstander, ID from Spel where Competitie_ID = (SELECT ID FROM competitie WHERE ID = '"+ selectedCompetitionID + "')");
		try {
			while (rs.next()) {
				// Wie is de tegenstander in het spel? Degene die je niet zelf
				// bent ofcourse
				String p1 = rs.getString("Account_naam_uitdager");
				String p2 = rs.getString("Account_naam_tegenstander");
				String spelID = ""+rs.getInt("ID");
				JPanel comp = new JPanel();
				comp.setMaximumSize(new Dimension(650, 80));
				comp.setPreferredSize(comp.getMaximumSize());
				comp.setBackground(new Color(44, 47, 53));

				Box vertBox = new Box(BoxLayout.PAGE_AXIS);
				Box lineBoxText = new Box(BoxLayout.LINE_AXIS);
				Box lineBoxControls = new Box(BoxLayout.LINE_AXIS);

				JLabel uitdagingTxt = new JLabel(p1 + " VERSUS " + p2);
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
				spellen.add(comp);
				spellen.add(Box.createVerticalStrut(5));

			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("open spel")) {
			String[] a = playBtn.get(arg0.getSource());
			connect = new Connectie();
			ResultSet rs = connect.voerSelectQueryUit("SELECT ID FROM competitie WHERE omschrijving = '"+ selectedCompetitionID + "'");
			try {
				if(rs.next()){
				setParentContentPane(new CompetitieRanking(rs.getInt("ID"),Integer.parseInt(a[0])));
				}
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//setParentContentPane(new ObserverGUI(Integer.parseInt(a[0])));
		} else if (arg0.getSource().equals(backButton)) {
			setParentContentPane(new GUIMenu());
		}
	}
	
	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	
	public void addExistingCompetitions(){
		connect = new Connectie();
		ResultSet competitieRS = connect.voerSelectQueryUit("SELECT omschrijving, ID FROM competitie");
		try {
			while(competitieRS.next()){
				int i = 0;
				while (competitieRS.next()) {
					existingCompetitions.put(i,competitieRS.getInt("ID"));
					selectedCompetition.insertItemAt(competitieRS.getString("omschrijving")+" (id:"+competitieRS.getInt("ID")+")", i);
					i++;
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		selectedCompetition.setSelectedIndex(0);
		connect.closeConnection();
	}

	@Override
	public void itemStateChanged(ItemEvent e) {
		//tmpID = selectedCompetition.getSelectedItem().toString();
		int tmpID = existingCompetitions.get(selectedCompetition.getSelectedIndex());
		selectedCompetitionID = tmpID;
		remove(scrollPane);
		initSpellen();
		showSpellen();
		revalidate();
		repaint();
	}

}