package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class GUIMenu extends JPanel {

	private JLabel titel;
	private JPanel head;
	private JPanel menu;
	private GUIMenuItem itemCompetitie;
	private GUIMenuItem itemObserver;
	private GUIMenuItem itemPWChange;
	private GUIMenuItem itemExit;
	
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
		head.setMaximumSize(new Dimension(650,50));
		head.setPreferredSize(head.getMaximumSize());
		head.add(titel);
		menu = new JPanel();
		menu.setLayout(new BoxLayout(menu,BoxLayout.Y_AXIS));
		menu.setBackground(this.getBackground());
		makeMenu();
		
		add(titel);
		add(head);
		add(menu);
	}
	
	public void makeMenu(){
		itemCompetitie	= new GUIMenuItem();
		itemObserver 	= new GUIMenuItem();
		itemPWChange	= new GUIMenuItem();
		itemExit 		= new GUIMenuItem();
		
		JLabel txtcomp = new JLabel("Competie overzicht");
		txtcomp.setForeground(Color.white);
		itemCompetitie.add(txtcomp);
		
		JLabel txtobs = new JLabel("Obeserver");
		txtcomp.setForeground(Color.white);
		itemObserver.add(txtobs);
		
		JLabel txtpwc = new JLabel("Wijzig wachtwoord");
		txtcomp.setForeground(Color.white);
		itemPWChange.add(txtpwc);
	
		JLabel txtex = new JLabel("Exit");
		txtcomp.setForeground(Color.white);
		itemExit.add(txtex);
		
		menu.add(itemCompetitie);
		menu.add(itemObserver);
		menu.add(itemPWChange);
		menu.add(itemExit);
	}


	
}
