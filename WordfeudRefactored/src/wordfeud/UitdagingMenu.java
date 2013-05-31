package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class UitdagingMenu extends JPanel implements ActionListener{

	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies; 
	private JPanel uitdagingen;
	private JScrollPane scrollPane;
	private WFButton inviteButton;
	private WFButton backButton;
	private Connectie connect;
	private String alleEigenaren;
	private int aantalCompetities;
	private boolean observerMode;
	private JFrame popup = null;
	
	
	
	private JPanel challengePlayer;
	private JTextField playerName;
	private JTextField selectedCompetition;
	private WFButton challengeButton;
	private WFButton challengeCancelButton;
	
	private HashMap<WFButton, String[]> acceptBtn = new HashMap<WFButton, String[]>();
	private HashMap<WFButton, String[]> declineBtn = new HashMap<WFButton, String[]>();
	
	public UitdagingMenu() {
		
		
		// TODO Auto-generated constructor stub
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		backButton = new WFButton("< Terug naar menu");
		backButton.addActionListener(this);
		inviteButton = new WFButton("Speler uitdagen");
		inviteButton.addActionListener(this);
		titel = new JLabel("Uitdagingen");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		head =  new JPanel();
		head.setBackground(this.getBackground());
		head.setMaximumSize(new Dimension(650,50));
		head.setPreferredSize(head.getMaximumSize());
		head.add(titel);
		functies = new JPanel();
		functies.setBackground(new Color(29,144,160));
		functies.setMaximumSize(new Dimension(650,40));
		functies.setPreferredSize(functies.getMaximumSize());
		functies.setLayout(new FlowLayout());
		functies.add(backButton);
		functies.add(inviteButton);
		
		
		add(head);
		add(functies);
		
		setUitdagingenList();
		
		aantalCompetities = 0;
		alleEigenaren = "";
		
		showUitdagingen();
	}
	
	public void setUitdagingenList(){
		uitdagingen = new JPanel();
		uitdagingen.setLayout(new BoxLayout(uitdagingen,BoxLayout.Y_AXIS));
		uitdagingen.setBackground(this.getBackground());
		scrollPane = new JScrollPane(uitdagingen);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		add(scrollPane);
	}
	
	public void showUitdagingen(){
		connect = new Connectie();
		ResultSet rs;
		ResultSet rs2;
		int idCompetitie = 0;
		String comp_naam = null;
		String uitdager = null;
		
		//Haal alle uitdagingen op uit de db
		rs = connect.voerSelectQueryUit("select * from Spel where Account_naam_tegenstander ='"+Account.getAccountNaam()+"' AND Toestand_type ='Request' AND Reaktie_type = 'Unknown'");
		try {
			while(rs.next())
			{
				
				idCompetitie = rs.getInt("Competitie_ID");
				rs2 = connect.voerSelectQueryUit("select * from Competitie where ID ='"+idCompetitie+"'");
				try {
					rs2.next();
					comp_naam = rs2.getString("omschrijving");
				}
				catch (SQLException e) {
					// TODO Auto-generated catch block
					System.out.println("Error: " + e);
				}
				uitdager = rs.getString("Account_naam_uitdager");
				String spelID = rs.getString("ID");
				JPanel comp = new JPanel();
				comp.setMaximumSize(new Dimension(650,80));
				comp.setPreferredSize(comp.getMaximumSize());
				comp.setBackground(new Color(44,47,53));
				
				Box vertBox = new Box(BoxLayout.PAGE_AXIS);
				Box lineBoxText = new Box(BoxLayout.LINE_AXIS);
				Box lineBoxControls = new Box(BoxLayout.LINE_AXIS);
				
				JLabel uitdagingTxt = new JLabel(uitdager + " || " + comp_naam);
				uitdagingTxt.setForeground(Color.white);
				lineBoxText.add(uitdagingTxt);
				
				String[] challengeSpecs = {uitdager,comp_naam, spelID};
				//accept 
				WFButton accept = new WFButton("accept");
				accept.addActionListener(this);				
				acceptBtn.put(accept, challengeSpecs);
				//decline
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
				if(rs.isLast()){
					aantalCompetities = idCompetitie;
				}
				alleEigenaren += comp_naam + ";";
				
				//comp.addMouseListener(this);				
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}
	
	public void addUitdaging(){
		inviteButton.setVisible(false);
		this.remove(scrollPane);
		challengePlayer = new JPanel();
		challengePlayer.setLayout(new BoxLayout(challengePlayer,BoxLayout.Y_AXIS));
		challengePlayer.setBackground(this.getBackground());
		
		JLabel chalName = new JLabel("Naam speler:");
		chalName.setForeground(Color.white);
		JLabel chalComp = new JLabel("Competitie ID:");
		chalComp.setForeground(Color.white);
		challengeButton = new WFButton("Uitdagen");
		challengeButton.addActionListener(this);
		challengeCancelButton = new WFButton("Annuleren");
		challengeCancelButton.addActionListener(this);
		playerName = new JTextField();
		
		selectedCompetition = new JTextField();
		
		
		
		challengePlayer.add(chalName);
		challengePlayer.add(playerName);
		playerName.setMaximumSize(new Dimension(200,20));
		challengePlayer.add(chalComp);
		challengePlayer.add(selectedCompetition);
		selectedCompetition.setMaximumSize(new Dimension(200,20));
		Box lineBox = new Box(BoxLayout.LINE_AXIS);
		lineBox.add(challengeButton);
		lineBox.add(challengeCancelButton);
		challengePlayer.add(lineBox);
		this.add(challengePlayer);
		System.out.println("Speler uitdagen2");
		this.validate();
		repaint();
	}
	
	public void extendUitdaging(){
		String naam = Account.getAccountNaam();
		String compID = selectedCompetition.getText();
		String naam2 = playerName.getText();
		String toestand = "Request";
		String reaktie = "Unknown";
		String letterset = "NL";
		String bord = "Standard";
		String q = "INSERT INTO Spel (Competitie_ID, Toestand_type, Account_naam_uitdager, Account_naam_tegenstander, moment_uitdaging, Reaktie_type, Bord_naam, LetterSet_naam) VALUES('"+compID+"','"+toestand+"','"+naam+"','"+naam2+"',NOW(),'"+reaktie+"','"+bord+"','"+letterset+"')";
		System.out.println(q);
		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit(q);
		connect.closeConnection();
		this.remove(challengePlayer);
		inviteButton.setVisible(true);
		setUitdagingenList();
		showUitdagingen();
		this.validate();
		repaint();
	}
	
	
	public void acceptUitdaging(String[] a){
		System.out.println("ACCEPTING > game_id: "+a[2]);
		String q = "UPDATE Spel SET Toestand_type = 'Playing', Reaktie_type = 'Accepted', moment_reaktie = now() WHERE ID='"+a[2]+"'";
		
		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit(q);
		connect.closeConnection();
		
		this.remove(scrollPane);
		setUitdagingenList();
		showUitdagingen();
		this.validate();
		repaint();
	}
	
	public void declineUitdaging(String[] a){
		System.out.println("DECLINING > game_id: "+a[2]);
		String q = "UPDATE Spel SET Reaktie_type = 'Rejected', moment_reaktie = now() WHERE ID='"+a[2]+"'";
		
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
		if(arg0.getSource().equals(inviteButton)){
			//addCompetition();
			addUitdaging();
			System.out.println("Speler uitdagen");
		} else if(arg0.getSource().equals(backButton)){
			setParentContentPane(new GUIMenu());
		} else if(arg0.getSource().equals(challengeCancelButton)){
			this.remove(challengePlayer);
			inviteButton.setVisible(true);
			setUitdagingenList();
			showUitdagingen();
			this.validate();
			repaint();
		} else if(arg0.getSource().equals(challengeButton)){
			extendUitdaging();
		} else if(arg0.getActionCommand().equals("accept")){
			//accepteer uitdaging
			String[] a = acceptBtn.get(arg0.getSource());
			acceptUitdaging(a);
			
		} else if(arg0.getActionCommand().equals("decline")){
			//decline uitdaging
			String[] a = declineBtn.get(arg0.getSource());
			declineUitdaging(a);
		}
		
		
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

}
