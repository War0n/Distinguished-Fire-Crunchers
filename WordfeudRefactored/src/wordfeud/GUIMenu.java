package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class GUIMenu extends JPanel {

	private JLabel titel;
	private JPanel head;
	private JPanel menu;
	private WFButton competitieButton;
	private WFButton observerButton;
	private WFButton wachtwoordWijzigen;
	private WFButton uitnodigingButton;
	private WFButton stopButton;
	private ArrayList<WFButton> myButtons;
	
	public GUIMenu(){
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		titel = new JLabel("Menu");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		head =  new JPanel();
		head.setBackground(this.getBackground());
		head.setMinimumSize(new Dimension(650,50));
		head.setPreferredSize(head.getMinimumSize());
		head.add(titel);
		menu = new JPanel();
		menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));
		menu.setBackground(this.getBackground());
		menu.setMinimumSize(new Dimension(650,700));
		menu.setPreferredSize(menu.getMinimumSize());
		
		
		
		add(titel);
		add(head);
		add(menu);
	}
	
}
