import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.nio.charset.Charset;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;


public class Spel extends JPanel{

	private Bord speelVeld; 
	private FunctionPanel functiepanel;
	private Letterbak letterbak;
	private ButtonPanel buttons;
	private JButton swapButton;
	private JButton skipButton;
	private JButton playButton;
	
	public Spel() {
		// TODO Auto-generated constructor stub
		swapButton = new JButton("Swap");
		skipButton = new JButton("Skip");
		playButton = new JButton("Play");
		swapButton.setBackground(new Color(44,47,53));
		swapButton.setForeground(Color.white);
		skipButton.setBackground(new Color(44,47,53));
		skipButton.setForeground(Color.white);
		playButton.setBackground(new Color(44,47,53));
		playButton.setForeground(Color.white);
		
		
		letterbak = new Letterbak();
		buttons = new ButtonPanel();
		
		buttons.add(playButton);
		buttons.add(skipButton);
		buttons.add(swapButton);
		
		setBackground(Color.blue);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		speelVeld = new Bord();
		functiepanel = new FunctionPanel();
		this.add(speelVeld);
		functiepanel.add(letterbak);
		functiepanel.add(buttons);
		this.add(functiepanel);
//		System.out.println("Spel Compiler Done!");
	}
}
