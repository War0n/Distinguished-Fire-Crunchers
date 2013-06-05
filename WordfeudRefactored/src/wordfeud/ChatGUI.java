package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import javax.swing.text.DefaultCaret;

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
	private JTextArea chatHistory;
	private JTextField tekstVeld;
	private JScrollPane scroll;
	private JButton sendButton;
	private Chat chat;
	private Spel game;
	
	public ChatGUI(Spel game){
		this.setTitle("Chatvenster");
		this.game = game;
		statusBar = new JPanel();
		chatFrame = new JPanel();
		sendPanel = new JPanel();
		chatStatus = new JLabel("Chatten met: "+getOpponent());
		chatHistory = new JTextArea();
		//chatHistory.setEditable(false);
		chatHistory.setLineWrap(true);
		chatHistory.setWrapStyleWord(true);
		tekstVeld = new JTextField();
		sendButton = new JButton("Stuur");
		chat = new Chat(game.getSpelId());
		
		sendPanel.setLayout(new GridLayout(1,2));
		sendPanel.add(tekstVeld);
		sendPanel.add(sendButton);
		
		DefaultCaret caret = (DefaultCaret)chatHistory.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		
		chatFrame.setLayout(new BorderLayout());
		chatFrame.add(sendPanel,BorderLayout.SOUTH);
		chatHistory.setForeground(Color.white);
		chatHistory.setBackground(new Color(23,26,30));
		scroll = new JScrollPane(chatHistory);
		scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		scroll.getVerticalScrollBar().setBackground(new Color(23,26,30));
		scroll.setAutoscrolls(true);
		chatHistory.setCaretPosition(chatHistory.getDocument().getLength());
		chatFrame.add(scroll, BorderLayout.CENTER);
		
		
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
		scroll.setPreferredSize(getMinimumSize());
		setVisible(true);
		
		initActions();
	}
	
	public String getOpponent() 
	{
		Connectie connect = new Connectie();
		try
		{
			ResultSet rs;
	        rs = connect.voerSelectQueryUit("SELECT * FROM Spel WHERE ID = " + game.getSpelId());
			if(rs.next())
			{
	        		String p1 = rs.getString("Account_naam_uitdager");
	        		String p2 = rs.getString("Account_naam_tegenstander");    
				connect.closeConnection();
		        	return (p1.equals(Account.getAccountNaam()) ? p2 : p1 );
			}
		}
		catch(SQLException ex)
		{
			ex.printStackTrace();
		}
		connect.closeConnection();
		return "";
	}
	
	public void initActions(){
		tekstVeld.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				chat.addChatLine(game.getSpelId(), Account.getAccountNaam(), tekstVeld.getText());
				tekstVeld.setText("");
			}
		});
		
		sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				chat.addChatLine(game.getSpelId(), Account.getAccountNaam(), tekstVeld.getText());	
				tekstVeld.setText("");
			}
		});
	}
	

	@Override
	public void update(Observable arg0, Object arg1) {
		if(!chatHistory.getText().equals((String) arg1)){
			chatHistory.setText((String) arg1);
		}
	}
}
