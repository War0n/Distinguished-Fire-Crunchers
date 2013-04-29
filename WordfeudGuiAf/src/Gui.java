import java.awt.Dimension;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class Gui extends JFrame {

	private LoginScreen loginScreen;
	private CompetitiesMenu competitieView;
	private CreateCompetition createCompetition;
	private ImageIcon image;
	
	public Gui(){
		loginScreen = new LoginScreen(this);
		competitieView = new CompetitiesMenu();
		image = new ImageIcon("src/images/wordfeud.png");
		setIconImage(image.getImage());
		//this.setContentPane(competitieView);
		//this.setContentPane(loginScreen);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Woordje Leggen Niemand Zeggen");
		this.setResizable(false);
		
		createCompetition = new CreateCompetition();
		this.setContentPane(createCompetition);
		
		this.validate();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
}
