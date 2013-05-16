package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
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
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends JPanel {
	private static String encryptPassword(String password)
	{
	    String sha1 = "";
	    try
	    {
	        MessageDigest crypt = MessageDigest.getInstance("SHA-1");
	        crypt.reset();
	        crypt.update(password.getBytes("UTF-8"));
	        sha1 = byteToHex(crypt.digest());
	    }
	    catch(NoSuchAlgorithmException e)
	    {
	        e.printStackTrace();
	    }
	    catch(UnsupportedEncodingException e)
	    {
	        e.printStackTrace();
	    }
	    return sha1;
	}

	private static String byteToHex(final byte[] hash)
	{
	    Formatter formatter = new Formatter();
	    for (byte b : hash)
	    {
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
	private JPanel content;
	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel regLabel;
	private JButton loginButton;
	private JButton registerButton;
	private JFrame activeFrame;
	private String curUser;
	private GUIMenu myGuiMenu;
	JFrame popup = null;

	public LoginScreen(JFrame frame) {
		setMinimumSize(new Dimension(650, 750));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new FlowLayout(FlowLayout.CENTER));
		activeFrame = frame;

		content = new JPanel();
		content.setPreferredSize(new Dimension(200, 200));
		content.setBackground(new Color(23, 26, 30));
		content.setLayout(new BoxLayout(content, BoxLayout.PAGE_AXIS));

		loginButton = new JButton("Log In");
		registerButton = new JButton("Register");
		usernameField = new JTextField();
		userLabel = new JLabel("Username:");
		userLabel.setForeground(Color.white);
		regLabel = new JLabel("");
		regLabel.setForeground(Color.white);
		usernameField.setMaximumSize(new Dimension(200, 20));
		passwordField = new JPasswordField();
		passLabel = new JLabel("Password:");
		passLabel.setForeground(Color.white);
		passwordField.setMaximumSize(new Dimension(200, 20));
		myGuiMenu = new GUIMenu();

		content.add(Box.createHorizontalGlue());
		content.add(userLabel);
		content.add(usernameField);
		content.add(passLabel);
		content.add(passwordField);
		content.add(registerButton);
		content.add(loginButton);
		content.add(regLabel);

		add(content);
		content.add(Box.createHorizontalGlue());

		registerButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Connectie connect = new Connectie();
				ResultSet rs;
				rs = connect
						.voerSelectQueryUit("SELECT COUNT(naam) FROM Accounts WHERE naam = '"
								+ usernameField.getText() + "'");
				try {
					rs.next();
					if ((usernameField.getText().equals(""))) {
						JOptionPane.showMessageDialog(popup,
								"Je hebt niks ingevuld! ", "Bezet!",
								JOptionPane.WARNING_MESSAGE);
						popup = null;
					} else {
						String password = "";
						for(char c:passwordField.getPassword()){
							password = password + c;
						}
						password = encryptPassword(password);
						if (rs.getInt(1) == 0) {

							connect.voerInsertQueryUit("INSERT INTO `myDBtestding`.`Accounts` (`naam`, `rol`, `geaccepteerd`, `password`) VALUES ('"
									+ usernameField.getText()
									+ "', 'user', '1', '"
									+ password
									+ "');");
							connect.closeConnection();
							usernameField.setText("");
							passwordField.setText("");

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
				for(char c:passwordField.getPassword()){
					password = password + c;
				}
				password = encryptPassword(password);
				rs = connect
						.voerSelectQueryUit("SELECT COUNT(naam) FROM Accounts WHERE naam = '"
								+ usernameField.getText()
								+ "' AND password ='"
								+ password + "' ");
				try {
					if(rs.next()){
						if (rs.getInt(1) == 1) {
							curUser = usernameField.getText();
							//Hier veranderd
							Account.setAccountNaam(curUser);
							menuView = new GUIMenu();
							activeFrame.setContentPane(myGuiMenu);
							activeFrame.pack();
						} else {
							regLabel.setText("Naam of Wachtwoord fouttief!");
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
