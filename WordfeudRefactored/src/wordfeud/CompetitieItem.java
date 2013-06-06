package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

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
	
	public CompetitieItem(int idCompetitie, String eigenaar, String compNaam){
		this.idCompetitie = idCompetitie;
		this.eigenaar = eigenaar;
		this.compNaam = compNaam;
		setMaximumSize(new Dimension(650,80));
		setPreferredSize(this.getMaximumSize());
		setBackground(new Color(44,47,53));
		compTxt = new JLabel(compNaam+ " [id: " + idCompetitie + "]");
		compTxt.setForeground(Color.white);
		add(compTxt);
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
