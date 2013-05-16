package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;

public class ChatGUI  extends JFrame implements Observer{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Container cp;
	private JPanel statusBar;
	private JPanel chatFrame;
	private JPanel sendPanel;
	private JLabel chatStatus;
	private JTextPane chatHistory;
	private JTextField tekstVeld;
	private JButton sendButton;
	private Chat chat;
	private Spel game;
	
	public ChatGUI(Spel game){
		this.setTitle("Chatvenster");
		this.game = game;
		statusBar = new JPanel();
		chatFrame = new JPanel();
		sendPanel = new JPanel();
		chatStatus = new JLabel("Chatten met: USERNAME");
		chatHistory = new JTextPane();
		chatHistory.setContentType("text/html");
		tekstVeld = new JTextField();
		sendButton = new JButton("Stuur");
		chat = new Chat(game.getSpelId());
		
		sendPanel.setLayout(new GridLayout(1,2));
		sendPanel.add(tekstVeld);
		sendPanel.add(sendButton);
		
		chatFrame.setLayout(new BorderLayout());
		chatFrame.add(sendPanel,BorderLayout.SOUTH);
		chatHistory.setForeground(Color.white);
		chatHistory.setBackground(new Color(23,26,30));
		chatFrame.add(chatHistory, BorderLayout.CENTER);
		
		statusBar.setMaximumSize(new Dimension(300,50));
		statusBar.setPreferredSize(statusBar.getMaximumSize());
		statusBar.setBackground(new Color(44,47,53));
		chatStatus.setForeground(Color.white);
				
		cp = getContentPane();
		cp.setBackground(new Color(23,26,30));
		cp.setLayout(new BoxLayout(cp, BoxLayout.Y_AXIS));
		cp.setPreferredSize(new Dimension(300,500));
		this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
		this.setResizable(false);
		chatFrame.setBackground(cp.getBackground());
		
		statusBar.add(chatStatus);
		cp.add(statusBar);
		cp.add(chatFrame);
		pack();
		chatHistory.setMinimumSize(chatHistory.getSize());
		chatHistory.setPreferredSize(getMinimumSize());
		setVisible(true);
		
		initActions();
	}
	
	public void initActions(){
		tekstVeld.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				chat.addChatLine(game.getSpelId(), Account.getAccountNaam(), tekstVeld.getText());
				tekstVeld.setText("");
			}
		});
		
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				chat.addChatLine(game.getSpelId(), Account.getAccountNaam(), tekstVeld.getText());	
				tekstVeld.setText("");
			}
		});
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		// TODO Auto-generated method stub
		chatHistory.setText((String) arg1);
	}
}
