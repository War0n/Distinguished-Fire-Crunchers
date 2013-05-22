package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
	
public class ObserverGUI extends JPanel{
		private static final long serialVersionUID = 1L;
		private Spel spel;
		private BordPanel speelVeld; 
		private JPanel leftBottomContainer;
		private JPanel scores;
		private JLabel score;
		private WFButton backButton;
		
		public ObserverGUI() {
			spel = new Spel();
			setMinimumSize(new Dimension(650,750));
			setPreferredSize(getMinimumSize());
			score = new JLabel("Scoreveld hier");
			score.setForeground(Color.white);
			
			
			setBackground(Color.blue);
			setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
			speelVeld = new BordPanel(spel.getBord());
			leftBottomContainer = new JPanel();
			backButton = new WFButton("< Terug");
			scores = new JPanel();
			scores.setLayout(new BorderLayout());
			scores.setBackground(getBackground());
			scores.setMaximumSize(new Dimension(600,50));
			
			leftBottomContainer.setLayout(new BoxLayout(leftBottomContainer,BoxLayout.PAGE_AXIS));
			this.add(speelVeld);
			scores.add(backButton,BorderLayout.WEST);
			scores.add(score,BorderLayout.EAST);
			leftBottomContainer.add(scores);
			initButtons();
			add(leftBottomContainer);
		}
		
		public void setParentContentPane(JPanel contentPane){
			JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
			root.setContentPane(contentPane);
			root.pack();
		}
		
		public void initButtons(){
			
			backButton.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent arg0) {
					// TODO Auto-generated method stub
					setParentContentPane(new CompetitiesMenu());
				}
			});
		}
	}



