package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;


public class SpelPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Spel spel;
	private BordPanel speelVeld; 
	private JPanel leftBottomContainer;
	private JPanel scores;
	private JLabel score;
	private FunctionPanel functiepanel;
	private LetterbakPanel letterbak;
	private ChatGUI cg;
	private Chat chat;
	private ButtonPanel buttons;
	private WFButton swapButton;
	private WFButton skipButton;
	private WFButton placeButton;
	private WFButton chatButton;
	private WFButton backButton;
	
	public SpelPanel() {
		spel = new Spel();
		chat = new Chat(spel.getSpelId());
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		swapButton = new WFButton("Swap");
		skipButton = new WFButton("Skip");
		placeButton = new WFButton("Play");
		chatButton = new WFButton("Chat");
		backButton = new WFButton("< Terug");
		score = new JLabel("Scoreveld hier");
		score.setForeground(Color.white);
		
		cg = new ChatGUI(spel);
		cg.setVisible(false);
		chat.addObserver(cg);
		
		letterbak = new LetterbakPanel(spel.getLetterBak());
		buttons = new ButtonPanel();
		
		buttons.add(placeButton);
		buttons.add(skipButton);
		buttons.add(swapButton);
		buttons.add(chatButton);
		
		initButtons();
		setBackground(Color.blue);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		speelVeld = new BordPanel(spel.getBord());
		functiepanel = new FunctionPanel();
		leftBottomContainer = new JPanel();
		scores = new JPanel();
		scores.setLayout(new BorderLayout());
		scores.setBackground(letterbak.getBackground());
		scores.setMaximumSize(new Dimension(600,50));
		
		leftBottomContainer.setLayout(new BoxLayout(leftBottomContainer,BoxLayout.PAGE_AXIS));
		this.add(speelVeld);
		scores.add(backButton,BorderLayout.WEST);
		scores.add(score,BorderLayout.EAST);
		leftBottomContainer.add(letterbak);
		leftBottomContainer.add(scores);
		functiepanel.add(leftBottomContainer);
		functiepanel.add(buttons);
		this.add(functiepanel);
//		System.out.println("Spel Compiler Done!");
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	
	public void initButtons(){
		chatButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				cg.setVisible(true);
				cg.setState(JFrame.NORMAL);
			}
		});
		
		placeButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				speelVeld.lockField();
			}
		});
		
		
		backButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setParentContentPane(new CompetitiesMenu());
			}
		});
	}
}
