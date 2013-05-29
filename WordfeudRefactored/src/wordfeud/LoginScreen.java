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

public class LoginScreen extends JPanel {

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
	private GUIMenu myGuiMenu;
	JFrame popup = null;

	public LoginScreen(JFrame frame) {
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

		userLabel = new JLabel("Username:");
		userLabel.setForeground(Color.white);
		userLabel.setAlignmentX(RIGHT_ALIGNMENT);
		usernameField.setMaximumSize(new Dimension(150, 20));
		passwordField = new JPasswordField();
		passLabel = new JLabel("Password:");
		passLabel.setForeground(Color.white);
		passLabel.setAlignmentX(RIGHT_ALIGNMENT);
		passwordField.setMaximumSize(new Dimension(150, 20));
		registerControle = new JPasswordField();
		registerControle.setVisible(false);
		registercontroleLabel = new JLabel("Password herhalen:");
		registercontroleLabel.setForeground(Color.white);
		registercontroleLabel.setAlignmentX(CENTER_ALIGNMENT);
		registercontroleLabel.setVisible(false);
		registerControle.setMaximumSize(new Dimension(150, 20));
		myGuiMenu = new GUIMenu();

		loginpanel.add(userLabel);
		loginpanel.add(Box.createVerticalStrut(2));
		loginpanel.add(usernameField);
		loginpanel.add(Box.createVerticalStrut(2));
		loginpanel.add(passLabel);
		loginpanel.add(Box.createVerticalStrut(2));
		loginpanel.add(passwordField);
		loginpanel.add(registercontroleLabel);
		loginpanel.add(registerControle);
		loginpanel.add(Box.createVerticalStrut(5));
		buttonpanel.add(loginButton);
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

				if (registerControle.isVisible()
						&& controlePassword.equals(password2)) {
					Connectie connect = new Connectie();
					ResultSet rs;
					rs = connect
							.voerSelectQueryUit("SELECT COUNT(naam) FROM Account WHERE naam = '"
									+ usernameField.getText() + "'");
					try {
						rs.next();
						if ((usernameField.getText().equals(""))) {
							JOptionPane.showMessageDialog(popup,
									"Je hebt niks ingevuld! ", "Leeg!",
									JOptionPane.WARNING_MESSAGE);
							popup = null;
						} else {
							String password = new String(passwordField.getPassword());
							if (rs.getInt(1) == 0) {

								connect.voerInsertOrUpdateQueryUit("INSERT INTO `Account` (`naam`, `wachtwoord`) VALUES ('"
										+ usernameField.getText()
										+ "', '"
										+ password
										+ "');");
								
								connect.voerInsertOrUpdateQueryUit("INSERT INTO `accountrol` (`Account_naam`, `Rol_type`) VALUES ('"
										+ usernameField.getText()
										+ "', 'Player');");
								
								connect.closeConnection();
								usernameField.setText("");
								passwordField.setText("");
								registerControle.setVisible(false);
								registercontroleLabel.setVisible(false);

								JOptionPane
										.showMessageDialog(
												popup,
												"Je bent geregistreerd! Je kunt nu inloggen!",
												"Voltooid",
												JOptionPane.PLAIN_MESSAGE);
								popup = null;
							} else {
								JOptionPane
										.showMessageDialog(
												popup,
												"Deze naam is al bezet! kies een andere!",
												"Bezet!",
												JOptionPane.WARNING_MESSAGE);
								popup = null;
							}
						}
					} catch (SQLException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

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

		// Loginbutton ActionListener//
		loginButton.addActionListener(new ActionListener() {
			// private GUIMenu menuView;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Connectie connect = new Connectie();
				ResultSet rs;
				String password = new String(passwordField.getPassword());
				rs = connect
						.voerSelectQueryUit("SELECT COUNT(naam) FROM account WHERE naam = '"
								+ usernameField.getText()
								+ "' AND wachtwoord ='"
								+ password + "' ");
				try {
					if (rs.next()) {
						if (rs.getInt(1) == 1) {
							curUser = usernameField.getText();
							// Hier veranderd
							Account.setAccountNaam(curUser);
							// menuView = new GUIMenu();
							new GUIMenu();
							activeFrame.setContentPane(myGuiMenu);
							activeFrame.pack();
							activeFrame.setLocationRelativeTo(null);
						} else {
							JOptionPane.showMessageDialog(popup,
									"Onjuiste inlognaam of wachtwoord",
									"Foutje!", JOptionPane.WARNING_MESSAGE);
							popup = null;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}

	public void clickLogin() {

		loginButton.doClick();

	}
}
