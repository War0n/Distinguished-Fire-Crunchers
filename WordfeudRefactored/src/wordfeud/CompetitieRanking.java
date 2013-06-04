package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

public class CompetitieRanking extends JPanel implements ActionListener {
	
	WFButton backButton;
	int idCompetitie;
	private JLabel titel;
	private JPanel rankings;
	private JPanel head;
	private JPanel functies;
	
	public CompetitieRanking(int idCompetitie){
		this.idCompetitie = idCompetitie;
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		titel = new JLabel("Ranking");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		head =  new JPanel();
		head.setBackground(this.getBackground());
		head.setMaximumSize(new Dimension(650,50));
		head.setPreferredSize(head.getMaximumSize());
		head.add(titel);
		backButton = new WFButton("< terug naar competities");
		backButton.addActionListener(this);
		functies = new JPanel();
		functies.setBackground(new Color(29,144,160));
		functies.setMaximumSize(new Dimension(650,40));
		functies.setPreferredSize(functies.getMaximumSize());
		functies.setLayout(new FlowLayout());
		rankings = new JPanel();
		rankings.setBackground(new Color(23,26,30));
		rankings.setLayout(new GridLayout(0,4));
		JLabel plaatsHead = new JLabel("Plaats");
		JLabel competitieIDHead = new JLabel("Competitie ID");
		JLabel accountNaamHead = new JLabel("Speler Naam");
		JLabel ratingHead = new JLabel("Rating");
		plaatsHead.setForeground(Color.WHITE);
		competitieIDHead.setForeground(Color.WHITE);
		accountNaamHead.setForeground(Color.WHITE);
		ratingHead.setForeground(Color.WHITE);
		rankings.add(plaatsHead);
		rankings.add(competitieIDHead);
		rankings.add(accountNaamHead);
		rankings.add(ratingHead);
		
		this.idCompetitie = idCompetitie;
		this.add(head);
		this.add(functies);
		head.add(titel);
		functies.add(backButton);
		showRanking();
		this.add(rankings);
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	
	public void showRanking(){
		
		int index = 1;
		Connectie connect = new Connectie();
		ResultSet rs = connect.voerSelectQueryUit("SELECT * FROM ranking WHERE Competitie_ID = " + idCompetitie);
		try {
			while(rs.next()){
				JLabel plaats = new JLabel();
				JLabel competitieID = new JLabel();
				JLabel accountNaam = new JLabel();
				JLabel rating= new JLabel();
				
				plaats.setText(index + ".");
				competitieID.setText(rs.getString("Competitie_ID"));
				accountNaam.setText(rs.getString("account_naam"));
				rating.setText(rs.getString("bayesian_rating"));
				
				plaats.setForeground(Color.WHITE);
				competitieID.setForeground(Color.WHITE);
				accountNaam.setForeground(Color.WHITE);
				rating.setForeground(Color.WHITE);
				
				rankings.add(plaats);
				rankings.add(competitieID);
				rankings.add(accountNaam);
				rankings.add(rating);
				index++;
			}	
		} catch (SQLException e){e.printStackTrace();}
		connect.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(backButton)){
			setParentContentPane(new CompetitiesMenu(true));
		}
	}
}
