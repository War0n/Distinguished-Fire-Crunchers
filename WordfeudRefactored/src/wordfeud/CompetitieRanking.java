package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CompetitieRanking extends JPanel implements ActionListener {
	
	WFButton backButton;
	int idCompetitie;
	private JLabel titel;

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
		
		this.idCompetitie = idCompetitie;

		
		showRanking();
		
		this.add(head);
		this.add(functies);
		head.add(titel);
		functies.add(backButton);
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	
	public void showRanking(){
		Connectie connect = new Connectie();
		//ResultSet rs = connect.voerSelectQueryUit("Select * From");
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(backButton)){
			setParentContentPane(new CompetitiesMenu(true));
		}
	}
}
