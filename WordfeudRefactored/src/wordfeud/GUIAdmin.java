package wordfeud;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
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
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class GUIAdmin extends JPanel implements Observer{
	
	private JLabel titel;
	private JLabel message;
	private JPanel head;
	private JPanel menu;
	private JPanel blankSpace;
	private GridLayout myGridLayout;
	private Admin admin;
	
	public GUIAdmin(){
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		titel = new JLabel("Administrator");
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
		
		admin = new Admin();
		admin.addObserver(this);
		
		
		add(titel);
		add(message);
		add(head);
//		add(menu);
//		add(blankSpace);
		
	}


	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) {
		System.out.println("BUTTON TOEGEVOEGD");
		ArrayList<String> namen = (ArrayList<String>)arg1;
		displayNamen(namen);
	}
	
	public void displayNamen(ArrayList<String> namen){

		for(String naam:namen){
		WFButton naamButton = new WFButton(naam);
		naamButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
					JFrame popup = new JFrame();	
					JButton srcButton = (JButton) e.getSource();
					String[] data = admin.getInfo(srcButton.getText());
					String verzamelData = "";
					for(String gegeven:data){
						verzamelData = verzamelData + gegeven + System.lineSeparator();
					}
					
					JOptionPane.showMessageDialog(popup,
							verzamelData, "Gegevens van: " + data[0],
							JOptionPane.QUESTION_MESSAGE);
			}
		});
		
		this.add(naamButton);

		}
	}

}
