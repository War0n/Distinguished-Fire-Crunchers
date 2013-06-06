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
	private WFButton refreshBtn;
	private JPanel contentPanel;
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
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		this.setBackground(new Color(23,26,30));
		
		//new Moderator(this);
		
		reviewWoorden = new ArrayList<JCheckBox>();
		
		contentPanel = new JPanel();
		contentPanel.setBackground(new Color(23,26,30));
		contentPanel.setLayout(new BorderLayout());
		titel = new JLabel("Moderatorscherm");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		titel.setAlignmentX(CENTER_ALIGNMENT);
		functies = new JPanel();
		functies.setBackground(new Color(29,144,160));
		functies.setMinimumSize(new Dimension(650,40));
		functies.setPreferredSize(functies.getMinimumSize());
		functies.setLayout(new FlowLayout());
		
		acceptWord = new WFButton("Accepteer");
		acceptWord.setMinimumSize(new Dimension(120,25));
		acceptWord.setPreferredSize(acceptWord.getMinimumSize());
		declineWord = new WFButton("Verwerp");
		declineWord.setMinimumSize(acceptWord.getMinimumSize());
		declineWord.setPreferredSize(declineWord.getMinimumSize());
		woordenPanel = new JPanel();
		myButtonPanel = new JPanel();
		refreshBtn = new WFButton("Ververs lijst");
		backToMenu = new WFButton("Ga terug");
		backToMenu.setMinimumSize(acceptWord.getMinimumSize());
		backToMenu.setPreferredSize(backToMenu.getMinimumSize());
		functies.add(refreshBtn);

		//scrollend
		wordScrollPane = new JScrollPane(woordenPanel);
		wordScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		woordenPanel.setLayout(new BoxLayout(woordenPanel,BoxLayout.Y_AXIS));
		myButtonPanel.setLayout(new FlowLayout());
		myButtonPanel.setPreferredSize(new Dimension(120,610));
		woordenPanel.setBackground(new Color(23,26,30));
		myButtonPanel.setBackground(new Color(23,26,30));
		
		this.add(titel);
		this.add(functies);
		contentPanel.add(wordScrollPane,BorderLayout.CENTER);
		myButtonPanel.add(acceptWord);
		myButtonPanel.add(declineWord);
		myButtonPanel.add(backToMenu);
		contentPanel.add(myButtonPanel, BorderLayout.LINE_END);
		this.add(contentPanel);
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
        refreshBtn.addActionListener(controller);
        backToMenu.addActionListener(controller);
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	

}