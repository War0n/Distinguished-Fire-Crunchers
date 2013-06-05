package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GUIModerator extends JPanel implements Observer{
	
	private WFButton acceptWord;
	private WFButton declineWord;
	private JPanel headPanel;
	private JPanel woordenPanel;
	private JPanel myButtonPanel;
	private JPanel functies;
	private JLabel titel;
	private WFButton backToMenu;
	private ArrayList<JCheckBox> reviewWoorden;
	private JScrollPane wordScrollPane;
	
	public GUIModerator(){
		this.setMinimumSize(new Dimension(650,700));
		this.setPreferredSize(getMinimumSize());
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(23,26,30));
		
		//new Moderator(this);
		
		reviewWoorden = new ArrayList<JCheckBox>();
		
		headPanel = new JPanel();
		headPanel.setForeground(Color.white);
		headPanel.setBackground(new Color(23,26,30));
		titel = new JLabel("Moderatorscherm");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		functies = new JPanel();
		functies.setBackground(new Color(29,144,160));
		functies.setMaximumSize(new Dimension(650,40));
		functies.setPreferredSize(functies.getMaximumSize());
		functies.setLayout(new FlowLayout());
		
		acceptWord = new WFButton("Accepteer");
		acceptWord.setMinimumSize(new Dimension(120,25));
		acceptWord.setPreferredSize(acceptWord.getMinimumSize());
		declineWord = new WFButton("Verwerp");
		declineWord.setMinimumSize(acceptWord.getMinimumSize());
		declineWord.setPreferredSize(declineWord.getMinimumSize());
		woordenPanel = new JPanel();
		myButtonPanel = new JPanel();
		backToMenu = new WFButton("Ga terug");
		backToMenu.setMinimumSize(acceptWord.getMinimumSize());
		backToMenu.setPreferredSize(backToMenu.getMinimumSize());

		//scrollend
		wordScrollPane = new JScrollPane(woordenPanel);
		wordScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		woordenPanel.setLayout(new BoxLayout(woordenPanel,BoxLayout.Y_AXIS));
		myButtonPanel.setLayout(new FlowLayout());
		myButtonPanel.setPreferredSize(new Dimension(120,650));
		woordenPanel.setBackground(new Color(23,26,30));
		myButtonPanel.setBackground(new Color(23,26,30));
		
		headPanel.add(titel);
		headPanel.add(functies);
		this.add(headPanel, BorderLayout.NORTH);
		this.add(wordScrollPane, BorderLayout.CENTER);
		myButtonPanel.add(acceptWord);
		myButtonPanel.add(declineWord);
		myButtonPanel.add(backToMenu);
		this.add(myButtonPanel, BorderLayout.LINE_END);
	}

	public void clearWoordenPanel(){
		woordenPanel.removeAll();
		reviewWoorden.clear();
		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) {
		reviewWoorden = (ArrayList<JCheckBox>) arg1;
		for(int i = 0; i < reviewWoorden.size(); i++){
			woordenPanel.add(reviewWoorden.get(i));
			reviewWoorden.get(i).setBackground(new Color(23,26,30));
			reviewWoorden.get(i).setForeground(Color.WHITE);
			
		}
		revalidate();
		repaint();
	}
	public void addButtonMethods(ActionListener controller){
        acceptWord.addActionListener(controller);
        declineWord.addActionListener(controller);
        backToMenu.addActionListener(controller);
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	

}