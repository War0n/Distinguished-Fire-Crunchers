package wordfeud;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
	private static String encryptPassword(String password) {
		String sha1 = "";
		try {
			MessageDigest crypt = MessageDigest.getInstance("SHA-1");
			crypt.reset();
			crypt.update(password.getBytes("UTF-8"));
			sha1 = byteToHex(crypt.digest());
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return sha1;
	}

	private static String byteToHex(final byte[] hash) {
		Formatter formatter = new Formatter();
		for (byte b : hash) {
			formatter.format("%02x", b);
		}
		String result = formatter.toString();
		formatter.close();
		return result;
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JPasswordField passwordField;
	private JTextField registerField;
	private JPasswordField registerpassField;
	private JPanel content;
	private JPanel loginpanel;
	private JPanel registerpanel;
	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel registerLabel;
	private JLabel registerpassLabel;
	private WFButton loginButton;
	private WFButton registerButton;
	private JFrame activeFrame;
	private String curUser;
	private GUIMenu myGuiMenu;
	JFrame popup = null;

	public LoginScreen(JFrame frame) {
		setMinimumSize(new Dimension(650, 750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		activeFrame = frame;

		loginpanel = new JPanel();
		loginpanel.setLayout(new BoxLayout(loginpanel, BoxLayout.Y_AXIS));
		loginpanel.setBackground(new Color(23, 26, 30));
		loginpanel.setPreferredSize(new Dimension(150, 150));
		loginpanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		registerpanel = new JPanel();
		registerpanel.setLayout(new BoxLayout(registerpanel, BoxLayout.Y_AXIS));
		registerpanel.setBackground(new Color(23, 26, 30));
		registerpanel.setPreferredSize(new Dimension(150, 150));
		registerpanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		content = new JPanel();
		content.setAlignmentX(JPanel.CENTER_ALIGNMENT);
		content.setAlignmentY(JPanel.CENTER_ALIGNMENT);
		content.setPreferredSize(new Dimension(200, 200));
		content.setBackground(new Color(23, 26, 30));
		content.setLayout(new BoxLayout(content, BoxLayout.X_AXIS));

		loginButton = new WFButton("Log In");
		registerButton = new WFButton("Register");
		usernameField = new JTextField();
		registerField = new JTextField();

		registerLabel = new JLabel("Username:");
		registerLabel.setForeground(Color.white);
		userLabel = new JLabel("Username:");
		userLabel.setForeground(Color.white);
		usernameField.setMaximumSize(new Dimension(200, 20));
		registerField.setMaximumSize(new Dimension(200, 20));
		passwordField = new JPasswordField();
		passLabel = new JLabel("Password:");
		passLabel.setForeground(Color.white);
		passwordField.setMaximumSize(new Dimension(200, 20));
		registerpassField = new JPasswordField();
		registerpassLabel = new JLabel("Password:");
		registerpassLabel.setForeground(Color.white);
		registerpassField.setMaximumSize(new Dimension(200, 20));
		myGuiMenu = new GUIMenu();

		content.add(Box.createHorizontalGlue());
		content.add(Box.createVerticalGlue());
		loginpanel.add(userLabel);
		loginpanel.add(usernameField);
		loginpanel.add(passLabel);
		loginpanel.add(passwordField);
		loginpanel.add(Box.createVerticalStrut(5));
		loginpanel.add(loginButton);

		registerpanel.add(registerLabel);
		registerpanel.add(registerField);
		registerpanel.add(registerpassLabel);
		registerpanel.add(registerpassField);
		registerpanel.add(Box.createVerticalStrut(5));
		registerpanel.add(registerButton);

		content.add(loginpanel);
		content.add(registerpanel);

		add(content);
		content.add(Box.createHorizontalGlue());

		registerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Connectie connect = new Connectie();
				ResultSet rs;
				rs = connect
						.voerSelectQueryUit("SELECT COUNT(naam) FROM Accounts WHERE naam = '"
								+ registerField.getText() + "'");
				try {
					rs.next();
					if ((registerField.getText().equals(""))) {
						JOptionPane.showMessageDialog(popup,
								"Je hebt niks ingevuld! ", "Leeg!",
								JOptionPane.WARNING_MESSAGE);
						popup = null;
					} else {
						String password = "";
						for (char c : registerpassField.getPassword()) {
							password = password + c;
						}
						password = encryptPassword(password);
						if (rs.getInt(1) == 0) {

							connect.voerInsertQueryUit("INSERT INTO `myDBtestding`.`Accounts` (`naam`, `rol`, `geaccepteerd`, `password`) VALUES ('"
									+ registerField.getText()
									+ "', 'user', '1', '" + password + "');");
							connect.closeConnection();
							registerField.setText("");
							registerpassField.setText("");

							JOptionPane
									.showMessageDialog(
											popup,
											"Je bent geregistreerd! Je kunt nu inloggen!",
											"Voltooid",
											JOptionPane.PLAIN_MESSAGE);
							popup = null;
						} else {
							JOptionPane.showMessageDialog(popup,
									"Deze naam is al bezet! kies een andere!",
									"Bezet!", JOptionPane.WARNING_MESSAGE);
							popup = null;
						}
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});

		// Loginbutton ActionListener
		loginButton.addActionListener(new ActionListener() {
			private GUIMenu menuView;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Connectie connect = new Connectie();
				ResultSet rs;
				String password = "";
				for (char c : passwordField.getPassword()) {
					password = password + c;
				}
				password = encryptPassword(password);
				rs = connect
						.voerSelectQueryUit("SELECT COUNT(naam) FROM Accounts WHERE naam = '"
								+ usernameField.getText()
								+ "' AND password ='"
								+ password + "' ");
				try {
					if (rs.next()) {
						if (rs.getInt(1) == 1) {
							curUser = usernameField.getText();
							// Hier veranderd
							Account.setAccountNaam(curUser);
							menuView = new GUIMenu();
							activeFrame.setContentPane(myGuiMenu);
							activeFrame.pack();
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
}
