package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;


public class SpelPanel extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Spel spel;
	private BordPanel speelVeld; 
	private FunctionPanel functiepanel;
	private LetterbakPanel letterbak;
	private ChatGUI cg;
	private Chat chat;
	private ButtonPanel buttons;
	private JButton swapButton;
	private JButton skipButton;
	private JButton placeButton;
	private JButton chatButton;
	
	public SpelPanel() {
		chat = new Chat();
		spel = new Spel();
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		swapButton = new JButton("Swap");
		skipButton = new JButton("Skip");
		placeButton = new JButton("Play");
		chatButton = new JButton("Chat");
		swapButton.setBackground(new Color(44,47,53));
		swapButton.setForeground(Color.white);
		skipButton.setBackground(new Color(44,47,53));
		skipButton.setForeground(Color.white);
		placeButton.setBackground(new Color(44,47,53));
		placeButton.setForeground(Color.white);
		chatButton.setBackground(new Color(44,47,53));
		chatButton.setForeground(Color.white);
		cg = new ChatGUI();
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
		this.add(speelVeld);
		functiepanel.add(letterbak);
		functiepanel.add(buttons);
		this.add(functiepanel);
//		System.out.println("Spel Compiler Done!");
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
	}
}
