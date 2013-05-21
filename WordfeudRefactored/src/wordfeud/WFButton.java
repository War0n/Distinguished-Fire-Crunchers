package wordfeud;

import java.awt.Color;

import javax.swing.JButton;

public class WFButton extends JButton {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public WFButton(String text){
		super();
		this.setText(text);
		this.setBackground(new Color(44,47,53));
		this.setForeground(Color.white);
	}
}
