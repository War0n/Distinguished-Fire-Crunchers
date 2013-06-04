package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CompetitieRanking extends JPanel implements ActionListener {
	
	WFButton backButton;
	
	public CompetitieRanking(){
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		
		backButton = new WFButton("< terug naar competities");
		backButton.addActionListener(this);
		
		//iets met ranking
		
		this.add(backButton);
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource().equals(backButton)){
			setParentContentPane(new CompetitiesMenu(true));
		}
	}
}
