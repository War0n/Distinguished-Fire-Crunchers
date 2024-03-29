package wordfeud;

import java.awt.Dimension;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginScreen loginScreen;
	private CompetitiesMenu competitieView;
	private Competitie competition;
	private ImageIcon image;

	public GUI() {
		this.setMinimumSize(new Dimension(200, 200));
		loginScreen = new LoginScreen(true);
		setCompetitieView(new CompetitiesMenu(true));
		image = new ImageIcon("src/images/wordfeud.png");
		setIconImage(image.getImage());
		this.setContentPane(loginScreen);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Woordje Leggen Niemand Zeggen");
		this.setResizable(false);

		setCompetitie(new Competitie());
		this.validate();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public Competitie getCompetitie() {
		return competition;
	}

	public void setCompetitie(Competitie competition) {
		this.competition = competition;
	}

	public CompetitiesMenu getCompetitieView() {
		return competitieView;
	}

	public void setCompetitieView(CompetitiesMenu competitieView) {
		this.competitieView = competitieView;
	}
}
