package wordfeud;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

public class CompetitieItem extends JPanel implements MouseListener {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	int idCompetitie;
	String eigenaar;
	String compNaam;
	JLabel compTxt;
	
	public CompetitieItem(int idCompetitie, String eigenaar, String compNaam, String start, String einde, boolean finished){
		this.idCompetitie = idCompetitie;
		this.eigenaar = eigenaar;
		this.compNaam = compNaam;
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		setMaximumSize(new Dimension(650,80));
		setPreferredSize(this.getMaximumSize());
		setBackground(new Color(44,47,53));
		compTxt = new JLabel(compNaam+ " [id: " + idCompetitie + "]");
		compTxt.setForeground(Color.white);
		
		JLabel compTimeHead = new JLabel("competitie looptijd:");
		compTimeHead.setForeground(Color.white);
		JLabel compTime = new JLabel();
		if(finished){
			compTime = new JLabel("Geeindigd op "+einde);
			compTime.setForeground(Color.white);			
		}else{
			compTime = new JLabel(start +" -- "+einde);
			compTime.setForeground(Color.white);
		}
		//add(compTxt);
		Box superBox = new Box(BoxLayout.PAGE_AXIS);
		Box superBox2 = new Box(BoxLayout.PAGE_AXIS);
		superBox2.add(Box.createHorizontalGlue());
		superBox2.add(compTxt);
		superBox.add(Box.createVerticalStrut(15));
		superBox.add(compTimeHead);
		superBox.add(compTime);
		compTxt.setAlignmentX(Component.CENTER_ALIGNMENT);
		compTimeHead.setAlignmentX(Component.CENTER_ALIGNMENT);
		compTime.setAlignmentX(Component.CENTER_ALIGNMENT);
		add(superBox2);
		add(superBox);
		addMouseListener(this);
	}
	
	public void setParentContentPane(JPanel contentPane){
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}
	
	public void setIdCompetitie(int idCompetitie){
		this.idCompetitie = idCompetitie;
	}
	
	public int getIdCompetitie(){
		return idCompetitie;
	}
	
	public void setEigenaar(String eigenaar){
		this.eigenaar = eigenaar;
	}
	
	public String getEigenaar(){
		return eigenaar;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
		setParentContentPane(new CompetitieRanking(idCompetitie,0));
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
	}

}
