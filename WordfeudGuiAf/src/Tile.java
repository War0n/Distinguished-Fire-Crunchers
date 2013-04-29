import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.nio.charset.Charset;
import java.util.Random;

import javax.swing.JLabel;
import javax.swing.JPanel;


public class Tile extends JPanel{

	private Random rnd;
	private JLabel tileLabel;
	private Stone steen;
	
	public Tile(Color bgColor,String label) {
		tileLabel = new JLabel(label);
		tileLabel.setForeground(Color.WHITE);
		tileLabel.setFont(new Font("BabelStone Han",Font.BOLD,18));
		

		setMaximumSize(new Dimension(40,40));
		setMinimumSize(new Dimension(40,40));
		setPreferredSize(getMaximumSize());
		if(bgColor != null){
			setBackground(bgColor);
		}else{
			setBackground(new Color(0,0,150));
		}
		rnd = new Random();
		add(tileLabel);
		addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				System.out.println("Clicked Tile!");				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
		});
//		System.out.println("Tile compiler Done!");
	}
	
	public Stone getStone(){
		return steen;
	}
	
	public void setStone(Stone steen){
		this.steen = steen;
		removeAll();
		add(steen);
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		// TODO Auto-generated method stub
		super.paintComponent(g);
		if(steen != null){
			if(steen.decideImage() != null){
				BufferedImage img = steen.decideImage();
				g.drawImage(img, 0,0,this);
			}
		}
	}
	
}
