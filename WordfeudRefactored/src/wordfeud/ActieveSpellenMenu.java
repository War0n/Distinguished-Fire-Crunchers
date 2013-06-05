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
	private JPanel myTurns;
	private JPanel notMyTurns;
	private JPanel myTurnLabelPanel;
	private JPanel notMyTurnLabelPanel;
	private JLabel myTurnLabel;
	private JLabel notMyTurnLabel;

	private boolean observer;
	private boolean doNotCheckBeurten;
	private JScrollPane scrollPane;
	private WFButton inviteButton;
	private WFButton backButton;
	private Connectie connect;
	private Thread checkBeurtThread;

	private HashMap<WFButton, String[]> playBtn = new HashMap<WFButton, String[]>();

	public ActieveSpellenMenu(boolean observer) {
		this.observer = observer;
		doNotCheckBeurten = false;
		setMinimumSize(new Dimension(630, 700));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
		if(!observer){
		inviteButton = new WFButton("Speler uitdagen");
		inviteButton.addActionListener(this);
		titel = new JLabel("Actieve spellen");
		}
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
		myTurns = new JPanel();
		myTurns.setLayout(new BoxLayout(myTurns, BoxLayout.Y_AXIS));
		myTurns.setBackground(this.getBackground());
		notMyTurns = new JPanel();
		notMyTurns.setLayout(new BoxLayout(notMyTurns, BoxLayout.Y_AXIS));
		notMyTurns.setBackground(this.getBackground());
		
		//Verdeling in myTurn en notMyTurn
		myTurnLabelPanel = new JPanel();
		myTurnLabelPanel.setBackground(new Color(44,47,53));
		myTurnLabel = new JLabel("Jouw beurt");
		myTurnLabel.setForeground(Color.white);
		myTurnLabelPanel.setMaximumSize(new Dimension(650,25));
		myTurnLabelPanel.setPreferredSize(myTurnLabelPanel.getMaximumSize());
		myTurnLabelPanel.add(myTurnLabel);
		myTurns.add(myTurnLabelPanel);
		myTurns.add(Box.createVerticalStrut(5));
		
		notMyTurnLabelPanel = new JPanel();
		notMyTurnLabelPanel.setBackground(new Color(44,47,53));
		notMyTurnLabel = new JLabel("Niet jouw beurt");
		notMyTurnLabel.setForeground(Color.white);
		notMyTurnLabelPanel.setMaximumSize(new Dimension(650,25));
		notMyTurnLabelPanel.setPreferredSize(notMyTurnLabelPanel.getMaximumSize());
		notMyTurnLabelPanel.add(notMyTurnLabel);
		notMyTurns.add(notMyTurnLabelPanel);
		notMyTurns.add(Box.createVerticalStrut(5));
		
		spellen.add(myTurns);
		spellen.add(notMyTurns);
		
		scrollPane = new JScrollPane(spellen);
		scrollPane
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
		checkBeurtThread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				while(!doNotCheckBeurten){
					try {
						Thread.sleep(5000);
						myTurns.removeAll();
						myTurns.add(myTurnLabelPanel);
						myTurns.add(Box.createVerticalStrut(5));
						
						notMyTurns.removeAll();
						notMyTurns.add(notMyTurnLabelPanel);
						notMyTurns.add(Box.createVerticalStrut(5));
						showSpellen();
						System.out.println("in Thread");
						spellen.revalidate();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}	
				}
				
			}
		});
		checkBeurtThread.start();
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
				if (myTurnActiveSpellen(Integer.parseInt(spelID))) {
					comp.setBackground(new Color(122, 162, 113));
					myTurns.add(comp);
					myTurns.add(Box.createVerticalStrut(5));
				} else {
					comp.setBackground(new Color(156, 71, 26));
					notMyTurns.add(comp);
					notMyTurns.add(Box.createVerticalStrut(5));
				}

			}
		} catch (SQLException e) {
		}
		connect.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getActionCommand().equals("open spel")) {
			// addCompetition();
			String[] a = playBtn.get(arg0.getSource());
			System.out.println("Open spel [id: " + a[0] + "]");
			if (!observer) {
				System.out.println(Integer.parseInt(a[0]));
				setParentContentPane(new SpelPanel(Integer.parseInt(a[0])));
			} else {
				setParentContentPane(new ObserverGUI(Integer.parseInt(a[0])));
			}
			doNotCheckBeurten = true;
		} else if (arg0.getSource().equals(backButton)) {
			doNotCheckBeurten = true;
			setParentContentPane(new GUIMenu());
		}
	}
	
	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

}