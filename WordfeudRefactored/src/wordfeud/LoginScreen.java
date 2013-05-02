package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;


public class LoginScreen extends JPanel{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPanel content;
	private JLabel userLabel;
	private JLabel passLabel;
	private JButton loginButton;
	private JFrame activeFrame;

	public LoginScreen(JFrame frame) {
		setMinimumSize(new Dimension(650,750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23,26,30));
		setLayout(new FlowLayout(FlowLayout.CENTER));
		activeFrame = frame;
		
		content = new JPanel();
		content.setPreferredSize(new Dimension(200,200));
		content.setBackground(new Color(23,26,30));
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));
				
		loginButton = new JButton("Log In");
		usernameField = new JTextField();
		userLabel = new JLabel("Username:");
		userLabel.setForeground(Color.white);
		usernameField.setMaximumSize(new Dimension(200,20));
		passwordField =new JPasswordField();
		passLabel = new JLabel("Password:");
		passLabel.setForeground(Color.white);
		passwordField.setMaximumSize(new Dimension(200,20));
		
		
		content.add(Box.createHorizontalGlue());
		content.add(userLabel);
		content.add(usernameField);
		content.add(passLabel);
		content.add(passwordField);
		content.add(loginButton);
		add(content);
		content.add(Box.createHorizontalGlue());
		
		loginButton.addActionListener(new ActionListener() {
			private CompetitiesMenu competitieView;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				getParent();
				competitieView = new CompetitiesMenu();
				activeFrame.setContentPane(competitieView);
				activeFrame.pack();
			}
		});
	}
	

}
