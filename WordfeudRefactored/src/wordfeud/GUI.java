package wordfeud;

import javax.swing.ImageIcon;
import javax.swing.JFrame;


public class GUI extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private LoginScreen loginScreen;
	private CompetitiesMenu competitieView;
	private CreateCompetition createCompetition;
	private ImageIcon image;
	
	public GUI()
	{
		loginScreen = new LoginScreen(this);
		setCompetitieView(new CompetitiesMenu());
		image = new ImageIcon("src/images/wordfeud.png");
		setIconImage(image.getImage());
		this.setContentPane(loginScreen);
		//this.setContentPane(competitieView);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setTitle("Woordje Leggen Niemand Zeggen");
		this.setResizable(false);
		
		setCreateCompetition(new CreateCompetition());
		//this.setContentPane(createCompetition);
		
		this.validate();
		this.pack();
		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}

	public CreateCompetition getCreateCompetition()
	{
		return createCompetition;
	}

	public void setCreateCompetition(CreateCompetition createCompetition)
	{
		this.createCompetition = createCompetition;
	}

	public CompetitiesMenu getCompetitieView()
	{
		return competitieView;
	}

	public void setCompetitieView(CompetitiesMenu competitieView)
	{
		this.competitieView = competitieView;
	}
}

