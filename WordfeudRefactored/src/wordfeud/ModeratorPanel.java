package wordfeud;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ModeratorPanel extends JPanel implements ActionListener{
	
	JButton acceptWord;
	JButton declineWord;
	Connectie connect;
	ResultSet myResultSet;
	ArrayList<JCheckBox> reviewWoorden;
	JPanel woordenPanel;

	public ModeratorPanel(){
		this.setMinimumSize(new Dimension(650,750));
		this.setPreferredSize(getMinimumSize());
		
		acceptWord = new JButton("Accepteer");
		declineWord = new JButton("Verwerp");
		connect = new Connectie();
		reviewWoorden = new ArrayList<JCheckBox>();
		JPanel woordenPanel = new JPanel();
		
		JScrollPane wordScrollPane = new JScrollPane(woordenPanel);
		woordenPanel.setPreferredSize(new Dimension(300, 300));
		woordenPanel.setLayout(new BoxLayout(woordenPanel,BoxLayout.Y_AXIS));
		acceptWord.addActionListener(this);
		declineWord.addActionListener(this);
		
		myResultSet = connect.voerSelectQueryUit("select * from WoordenVoorReview");
		
		try {
			while(myResultSet.next())
			{
				reviewWoorden.add(new JCheckBox(myResultSet.getString("Woord")));
				for(int i = 0; i < reviewWoorden.size(); i++){
					woordenPanel.add(reviewWoorden.get(i)); // lol
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		this.add(wordScrollPane);
		this.add(acceptWord);
		this.add(declineWord);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		if(arg0.getSource().equals(acceptWord)){
			
		}
		if(arg0.getSource().equals(declineWord)){
			
		}
	}
}
