package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


public class CreateCompetition extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel options;
	
	public CreateCompetition(){
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new BoxLayout(this,BoxLayout.Y_AXIS));
		titel = new JLabel("Competitie aanmaken");
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
		
		options = new JPanel();
		options.setLayout(new BoxLayout(options,BoxLayout.Y_AXIS));
		options.setBackground(this.getBackground());
		
		add(head);
		add(functies);
		add(options);
		showOptions();
		
	}
	
	public void showOptions(){
		JPanel option1 = new JPanel();
		option1.setMaximumSize(new Dimension(650,80));
		option1.setPreferredSize(option1.getMaximumSize());
		option1.setBackground(new Color(44,47,53));
		JLabel option1txt = new JLabel("Woordje Leggen Niemand Zeggen vriend");
		option1txt.setForeground(Color.white);
		option1.add(option1txt);
		
		options.add(Box.createVerticalStrut(5));
		options.add(option1);
		
		
		JPanel option2 = new JPanel();
		option2.setMaximumSize(new Dimension(650,80));
		option2.setPreferredSize(option2.getMaximumSize());
		option2.setBackground(new Color(44,47,53));
		JLabel option2txt = new JLabel("Vind vriend");
		option2txt.setForeground(Color.white);
		option2.add(option2txt);
		
		options.add(Box.createVerticalStrut(5));
		options.add(option2);
		
	
	}
	
	public void showFriends(){
		//alle bekende vrienden
		options.removeAll();
		for(int i=1; i <= 10;i++){
			JPanel comp = new JPanel();
			comp.setMaximumSize(new Dimension(650,80));
			comp.setPreferredSize(comp.getMaximumSize());
			comp.setBackground(new Color(44,47,53));
			JLabel compTxt = new JLabel("Vriend " + i);
			compTxt.setForeground(Color.white);
			comp.add(compTxt);
			options.add(Box.createVerticalStrut(5));
			options.add(comp);
		}
	}
	
	public void showSearch(){
		options.removeAll();
		JLabel name = new JLabel("Zoeken");
		name.setForeground(Color.white);
		name.setFont(new Font("Arial",Font.BOLD,20));
		JTextField searchField = new JTextField();
		searchField.setMaximumSize(new Dimension(200,20));
		JButton search = new JButton("Zoek");
		search.setMaximumSize(new Dimension(100,20));
		
		options.setLayout(new BoxLayout(options, BoxLayout.Y_AXIS));
		options.add(name);
		options.add(searchField);
		options.add(search);
		
	}

}
