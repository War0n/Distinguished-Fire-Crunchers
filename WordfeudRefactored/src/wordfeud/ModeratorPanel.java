package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;

public class ModeratorPanel extends JPanel implements ActionListener{
	
	private JButton acceptWord;
	private JButton declineWord;
	private Connectie connect;
	private ResultSet myResultSet;
	private ArrayList<JCheckBox> reviewWoorden;
	private JPanel woordenPanel;
	private JPanel myButtonPanel;
	private JLabel titel;
	private JButton backToMenu;
	private GUIMenu myGuiMenu;

	public ModeratorPanel(){
		this.setMinimumSize(new Dimension(650,750));
		this.setPreferredSize(getMinimumSize());
		this.setLayout(new BorderLayout());
		
		titel = new JLabel("Moderator scherm");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial",Font.BOLD,30));
		titel.setPreferredSize(new Dimension(650,100));
		acceptWord = new JButton("Accepteer");
		declineWord = new JButton("Verwerp");
		connect = new Connectie();
		reviewWoorden = new ArrayList<JCheckBox>();
		woordenPanel = new JPanel();
		myButtonPanel = new JPanel();
		backToMenu = new JButton("Ga terug");
		
		JScrollPane wordScrollPane = new JScrollPane(woordenPanel);
		woordenPanel.setPreferredSize(new Dimension(300, 300));
		woordenPanel.setLayout(new BoxLayout(woordenPanel,BoxLayout.Y_AXIS));
		acceptWord.addActionListener(this);
		declineWord.addActionListener(this);
		backToMenu.addActionListener(this);
		myButtonPanel.setLayout(new BoxLayout(myButtonPanel, BoxLayout.PAGE_AXIS));
		myButtonPanel.setPreferredSize(new Dimension(120,650));
		
		myResultSet = connect.voerSelectQueryUit("select * from WoordenVoorReview");
		
		try {
			while(myResultSet.next())
			{
				reviewWoorden.add(new JCheckBox(myResultSet.getString("Woord")));
				for(int i = 0; i < reviewWoorden.size(); i++){
					woordenPanel.add(reviewWoorden.get(i));
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		this.add(titel, BorderLayout.NORTH);
		this.add(wordScrollPane, BorderLayout.CENTER);
		myButtonPanel.add(acceptWord);
		myButtonPanel.add(declineWord);
		myButtonPanel.add(backToMenu);
		this.add(myButtonPanel, BorderLayout.LINE_END);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(acceptWord)){
			
		}
		if(arg0.getSource().equals(declineWord)){
			
		}
		if(arg0.getSource().equals(backToMenu)){
			myGuiMenu = new GUIMenu();
			JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
			root.setContentPane(myGuiMenu);
			root.pack();
		}
	}
}
