package wordfeud;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GUIAdmin extends JPanel implements Observer, ActionListener{
	
	private JLabel titel;
	private JPanel head;
	private JPanel functies; 
	private JPanel menu;
	private JPanel blankSpace;
	private GridLayout myGridLayout;
	private JScrollPane myScroller;
	private WFButton back;
	private Admin admin;
	private WFButton wwWijzig;
	private WFButton verwijderAccount;
	private String chancedPassword;
	
	JFrame popup;
	JTextField passwordField;
	JLabel usernameLabel;
	JLabel usernameDataLabel;
	JLabel passwordLabel;
	
	public GUIAdmin(){
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		titel = new JLabel("Administrator");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,40));
		titel.setAlignmentX(Component.CENTER_ALIGNMENT);
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
		back = new WFButton("< Terug naar menu");
		functies.add(back);
		menu = new JPanel();
		myGridLayout = new GridLayout(0,1,0,10);
		menu.setLayout(myGridLayout);
		menu.setBackground(this.getBackground());
		blankSpace = new JPanel();
		blankSpace.setPreferredSize(new Dimension(650,50));
		blankSpace.setBackground(this.getBackground());
		myScroller = new JScrollPane(menu);
		myScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		admin = new Admin();
		admin.addObserver(this);
		back.addActionListener(this);
		
		add(head);
		add(functies);
		admin.getAccounts();
		this.add(myScroller);
	}


	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) {
		ArrayList<String> namen = (ArrayList<String>)arg1;
		displayNamen(namen);
	}
	
	public void displayNamen(ArrayList<String> namen){

		for(String naam:namen){
		WFButton naamButton = new WFButton(naam);
		naamButton.setMinimumSize(new Dimension(650,50));
		naamButton.setPreferredSize(naamButton.getMinimumSize());
		naamButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					getAccountInfo(e);
			}
		});
		
		menu.add(naamButton);

		}
		menu.repaint();
	}
	
	public void getAccountInfo(ActionEvent e){
		popup = new JFrame();	
		JButton srcButton = (JButton) e.getSource();
		JPanel popupPanel = new JPanel();
		String[] data = admin.getInfo(srcButton.getText());	
		
		passwordField = new JTextField();
		usernameLabel = new JLabel("Gebruikersnaam: ");
		usernameDataLabel = new JLabel(data[0]);
		passwordLabel = new JLabel("Wachtwoord: ");
		
		usernameLabel.setForeground(Color.white);
		usernameDataLabel.setForeground(Color.white);
		passwordLabel.setForeground(Color.white);
		
		popupPanel.add(usernameLabel);
		popupPanel.add(usernameDataLabel);
		passwordField.setText(data[1]);
		popupPanel.add(passwordLabel);
		popupPanel.add(passwordField);

		popup.setTitle(data[0]);
		popup.setMinimumSize(new Dimension(300,50));
		
		popup.setResizable(false);
		popupPanel.setBackground(new Color(23,26,30));
		popupPanel.setLayout(new GridLayout(3,2));			
		popup.setContentPane(popupPanel);
		popup.pack();
		popup.setVisible(true);
		wwWijzig = new WFButton("Wijzig gegevens");
		verwijderAccount = new WFButton("Verwijder account");
		verwijderAccount.setMaximumSize(new Dimension(100,50));
		verwijderAccount.setPreferredSize(getMaximumSize());
		wwWijzig.setMaximumSize(new Dimension(5,5));
		wwWijzig.setPreferredSize(getMaximumSize());
		wwWijzig.addActionListener(this);
		verwijderAccount.addActionListener(this);
		
		popupPanel.add(wwWijzig);
		popupPanel.add(verwijderAccount);
	}
	
	public void insertPassword(){
		JTextField myPassword = new JTextField();
		myPassword.setSize(new Dimension(200,50));
		chancedPassword = myPassword.getText();
	}

	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if(arg0.getSource().equals(back)){
			setParentContentPane(new GUIMenu());
		}
		else if(arg0.getSource().equals(wwWijzig)){
			admin.chancePassword(usernameDataLabel.getText(), passwordField.getText());
			popup.dispose();
			admin.getAccounts();
		}
		else if(arg0.getSource().equals(verwijderAccount)){
			admin.removeAccount(usernameDataLabel.getText());
			popup.dispose();
		}
	}

}
