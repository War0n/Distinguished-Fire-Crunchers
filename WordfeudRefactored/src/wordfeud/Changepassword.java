package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Formatter;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class Changepassword extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JPasswordField registerControle;
	private JPanel content;
	private JPanel loginpanel;
	private JPanel buttonpanel;
	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel registercontroleLabel;
	private WFButton loginButton;
	private WFButton registerButton;
	private JFrame activeFrame;
	private String curUser;
	JFrame popup = null;

	public Changepassword(JFrame frame) {
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		activeFrame = frame;
		activeFrame.setMinimumSize(new Dimension(200, 200));

		loginpanel = new JPanel();
		loginpanel.setLayout(new BoxLayout(loginpanel, BoxLayout.Y_AXIS));
		loginpanel.setBackground(new Color(23, 26, 30));
		loginpanel.setPreferredSize(new Dimension(150, 150));
		loginpanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		buttonpanel = new JPanel();
		buttonpanel.setLayout(new FlowLayout());
		buttonpanel.setBackground(new Color(23, 26, 30));

		content = new JPanel();
		content.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		content.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		content.setBackground(new Color(23, 26, 30));
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

		loginButton = new WFButton("Log In");
		registerButton = new WFButton("Register");
		usernameField = new JTextField();

		passwordField = new JPasswordField();
		passLabel = new JLabel("Nieuw wachtwoord:");
		passLabel.setForeground(Color.white);
		passLabel.setAlignmentX(RIGHT_ALIGNMENT);
		passwordField.setMaximumSize(new Dimension(150, 20));
		registerControle = new JPasswordField();
		registerControle.setVisible(false);
		registercontroleLabel = new JLabel("Nieuwe wachtwoord herhalen herhalen:");
		registercontroleLabel.setForeground(Color.white);
		registercontroleLabel.setAlignmentX(CENTER_ALIGNMENT);
		registercontroleLabel.setVisible(false);
		registerControle.setMaximumSize(new Dimension(150, 20));


		loginpanel.add(passLabel);
		loginpanel.add(Box.createVerticalStrut(2));
		loginpanel.add(passwordField);
		loginpanel.add(registercontroleLabel);
		loginpanel.add(registerControle);
		loginpanel.add(Box.createVerticalStrut(5));
		buttonpanel.add(registerButton);
		loginpanel.add(buttonpanel);
		content.add(loginpanel);
		add(content);
		activeFrame.pack();
		passwordField.addKeyListener(new KeyAdapter() {
			public void KeyPressed(KeyEvent e) {
				if (e.getKeyCode() == (KeyEvent.VK_ENTER)) {
					System.out.println("het werkt ongeveer");
					clickLogin();
				}
			}
		});
		registerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String controlePassword = new String(registerControle
						.getPassword());
				String password2 = new String(passwordField.getPassword());
				System.out.println("1");
				if (controlePassword.equals(password2)) {
					Connectie connect = new Connectie();
						String password = new String(passwordField.getPassword());
							System.out.println("nars.getint");
							connect.voerInsertOrUpdateQueryUit("UPDATE account SET wachtwoord = '"+password2+"' WHERE naam = '" + Account.getAccountNaam()+"'");
							
							connect.closeConnection();
							passwordField.setText("");
							registerControle.setVisible(false);
							registercontroleLabel.setVisible(false);
							JOptionPane
									.showMessageDialog(
											popup,
											"Het wachtwoord is veranderd!",
											"Voltooid",
											JOptionPane.PLAIN_MESSAGE);
							popup = null;

				} else if (registerControle.isVisible() == false) {
					registerControle.setVisible(true);
					registercontroleLabel.setVisible(true);

				} else if (!controlePassword.equals(password2)) {
					JOptionPane.showMessageDialog(popup,
							"De wachtwoorden komen niet overeen!", "Foutje!",
							JOptionPane.WARNING_MESSAGE);
					popup = null;
				}
			}
		});

	}

	public void clickLogin() {

		loginButton.doClick();

	}
}
