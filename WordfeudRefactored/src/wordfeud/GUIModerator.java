package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
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
	private JPanel woordenPanel;
	private JPanel myButtonPanel;
	private JLabel titel;
	private WFButton backToMenu;
	private ArrayList<JCheckBox> reviewWoorden;
	
	public GUIModerator(){
		this.setMinimumSize(new Dimension(650,750));
		this.setPreferredSize(getMinimumSize());
		this.setLayout(new BorderLayout());
		this.setBackground(new Color(23,26,30));
		
		//new Moderator(this);
		
		reviewWoorden = new ArrayList<JCheckBox>();
		
		titel = new JLabel("Moderator scherm");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		titel.setPreferredSize(new Dimension(650,100));
		acceptWord = new WFButton("Accepteer");
		declineWord = new WFButton("Verwerp");
		woordenPanel = new JPanel();
		myButtonPanel = new JPanel();
		backToMenu = new WFButton("Ga terug");

		
		JScrollPane wordScrollPane = new JScrollPane(woordenPanel);
		woordenPanel.setPreferredSize(new Dimension(300, 300));
		woordenPanel.setLayout(new BoxLayout(woordenPanel,BoxLayout.Y_AXIS));
		myButtonPanel.setLayout(new BoxLayout(myButtonPanel, BoxLayout.PAGE_AXIS));
		myButtonPanel.setPreferredSize(new Dimension(120,650));
		woordenPanel.setBackground(new Color(23,26,30));
		myButtonPanel.setBackground(new Color(23,26,30));
		
		this.add(titel, BorderLayout.NORTH);
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
