package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;


public class CompetitiesMenu extends JPanel implements MouseListener, ActionListener{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies; 
	private JPanel competities;
	private JScrollPane scrollPane;
	private JButton inviteButton;
	private Connectie connect;
	private String alleEigenaren;
	private int aantalCompetities;
	private JFrame popup = null;
	
	public CompetitiesMenu() {
		// TODO Auto-generated constructor stub
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		inviteButton = new JButton("Competitie aanmaken");
		inviteButton.addActionListener(this);
		titel = new JLabel("Competitieoverzicht");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		head =  new JPanel();
		head.setBackground(this.getBackground());
		head.setMaximumSize(new Dimension(650,50));
		head.setPreferredSize(head.getMaximumSize());
		head.add(titel);
		functies = new JPanel();
		functies.setBackground(new Color(156,71,26));
		functies.setMaximumSize(new Dimension(650,40));
		functies.setPreferredSize(functies.getMaximumSize());
		functies.setLayout(new FlowLayout());
		functies.add(inviteButton);
		competities = new JPanel();
		competities.setLayout(new BoxLayout(competities,BoxLayout.Y_AXIS));
		competities.setBackground(this.getBackground());
		scrollPane = new JScrollPane(competities);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		
		add(head);
		add(functies);
		add(scrollPane);
		aantalCompetities = 0;
		alleEigenaren = "";
		showCompetitions();
	}
	
	public void showCompetitions(){
		
		connect = new Connectie();
		ResultSet rs;
		int idCompetitie = 0;
		String eigenaar = null;
		
		//Haal alle competities op uit de db
		rs = connect.voerSelectQueryUit("select * from Competities");
		try {
			while(rs.next())
			{
				
				idCompetitie = rs.getInt("idCompetitie");
				eigenaar = rs.getString("eigenaar");
				
				JPanel comp = new JPanel();
				comp.setMaximumSize(new Dimension(650,80));
				comp.setPreferredSize(comp.getMaximumSize());
				comp.setBackground(new Color(44,47,53));
				JLabel compTxt = new JLabel("Competitie " + idCompetitie + " Eigenaar: " + eigenaar);
				compTxt.setForeground(Color.white);
				comp.add(compTxt);
				competities.add(Box.createVerticalStrut(5));
				competities.add(comp);
				if(rs.isLast()){
					aantalCompetities = idCompetitie;
				}
				alleEigenaren += eigenaar + ";";
				
				comp.addMouseListener(this);				
			}
		}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}
	
	public void addCompetition()
	{
		//Kijken of de eigenaar al een competitie heeft
		//Zo niet maak een competitie aan met id onderste rij id + 1
		//en de eigenaar is curUser
		
		String eigenaar = Account.getAccountNaam();
		Connectie connect = new Connectie();
		
		if(!(alleEigenaren.contains(eigenaar + ";"))){
			connect.voerInsertQueryUit("INSERT INTO `myDBtestding`.`Competities` (`idCompetitie`, `eigenaar`) VALUES ('" + (aantalCompetities + 1) + "', '" + eigenaar + "');");
			connect.closeConnection();
			alleEigenaren += eigenaar;
			JOptionPane.showMessageDialog(popup,
					"Je Competitie is aangemaakt!", "",
					JOptionPane.WARNING_MESSAGE);
			popup = null;
			JPanel comp = new JPanel();
			comp.setMaximumSize(new Dimension(650,80));
			comp.setPreferredSize(comp.getMaximumSize());
			comp.setBackground(new Color(44,47,53));
			JLabel compTxt = new JLabel("Competitie " + (aantalCompetities + 1) + " Eigenaar: " + eigenaar);
			compTxt.setForeground(Color.white);
			comp.add(compTxt);
			competities.add(Box.createVerticalStrut(5));
			competities.add(comp);
			aantalCompetities++;
			alleEigenaren += eigenaar + ";";
			
			comp.addMouseListener(this);
			revalidate();
			repaint();
		}else{
			JOptionPane.showMessageDialog(popup,
					"Je hebt al een competitie!", "",
					JOptionPane.WARNING_MESSAGE);
			popup = null;
		}
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		// TODO Auto-generated method stub
		SpelPanel gameOfWordfeud = new SpelPanel();
		setParentContentPane(gameOfWordfeud);
	}
	
	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		addCompetition();
	}

}
