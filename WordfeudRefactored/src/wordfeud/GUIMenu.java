package wordfeud;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class GUIMenu extends JPanel implements ActionListener{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private JLabel titel;
	private JLabel message;
	private JPanel head;
	private JPanel menu;
	private JPanel blankSpace;
	private WFButton competitieButton;
	private WFButton competitieAanmaken;
	private WFButton observerButton;
	private WFButton wachtwoordWijzigen;
	private WFButton uitnodigingButton;
	private WFButton adminButton;
	private WFButton modButton;
	private WFButton stopButton;
	private ArrayList<WFButton> myButtons;
	private GridLayout myGridLayout;
	private CompetitiesMenu myCompetitiesMenu;
	
	public GUIMenu(){
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		titel = new JLabel("Menu");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,40));
		titel.setAlignmentX(Component.CENTER_ALIGNMENT);
		message = new JLabel("");
		message.setAlignmentX(Component.CENTER_ALIGNMENT);
		message.setForeground(Color.RED);
		head =  new JPanel();
		head.setBackground(this.getBackground());
		head.setMinimumSize(new Dimension(650,125));
		head.setPreferredSize(head.getMinimumSize());
		head.add(titel);
		menu = new JPanel();
		menu.setPreferredSize(new Dimension(650,500));
		myGridLayout = new GridLayout(0,1,0,20);
		menu.setLayout(myGridLayout);
		menu.setBackground(this.getBackground());
		blankSpace = new JPanel();
		blankSpace.setPreferredSize(new Dimension(650,125));
		blankSpace.setBackground(this.getBackground());
				
		competitieButton = new WFButton("Competities openen");
		competitieAanmaken = new WFButton("Competitie aanmaken");
		uitnodigingButton = new WFButton("Uitnodigingen bekijken");
		observerButton = new WFButton("Observeren");
		wachtwoordWijzigen = new WFButton("Wachtwoord wijzigen");
		stopButton = new WFButton("Spel Stoppen");
		adminButton = new WFButton("Administrator scherm bekijken");
		modButton = new WFButton("Moderator scherm bekijken");
		myButtons = new ArrayList<WFButton>();
		
		myButtons.add(competitieButton);
		myButtons.add(competitieAanmaken);
		myButtons.add(uitnodigingButton);
		myButtons.add(observerButton);
		myButtons.add(adminButton);
		myButtons.add(modButton);
		myButtons.add(wachtwoordWijzigen);
		myButtons.add(stopButton);

		for(int i = 0; i < myButtons.size(); i++)
		{
			myButtons.get(i).addActionListener(this);
			menu.add(myButtons.get(i));
		}
				
		add(titel);
		add(message);
		add(head);
		add(menu);
		add(blankSpace);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(competitieButton)){
			myCompetitiesMenu = new CompetitiesMenu(false);
			setParentContentPane(myCompetitiesMenu);
		}
		if(arg0.getSource().equals(observerButton)){
			setParentContentPane(new CompetitiesMenu(true));
		}
		if(arg0.getSource().equals(modButton)){
            if(Account.checkModerator(Account.getAccountNaam()))
            {
            	GUIModerator myGUIModerator = new GUIModerator();
				Moderator myModerator = new Moderator(myGUIModerator);
				setParentContentPane(myGUIModerator);
            }else{
            	message.setText("Je hebt geen moderator rechten!");
            }
    }
		if(arg0.getSource().equals(wachtwoordWijzigen)){
			
		}
		if(arg0.getSource().equals(uitnodigingButton)){
			
		}
		if(arg0.getSource().equals(stopButton)){
			System.exit(0);
		}	
		if(arg0.getSource().equals(adminButton)){
			if(Account.checkAdmin(Account.getAccountNaam())){
				setParentContentPane(new GUIAdmin());
			}
			else{
				message.setText("Je hebt geen admin rechten!");
			}
		}
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
}
