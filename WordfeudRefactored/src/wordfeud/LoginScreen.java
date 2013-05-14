package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class LoginScreen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JTextField usernameField;
	private JTextField passwordField;
	private JPanel content;
	private JLabel userLabel;
	private JLabel passLabel;
	private JLabel regLabel;
	private JButton loginButton;
	private JButton registerButton;
	private JFrame activeFrame;
	private String curUser;
	

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
		passwordField = new JTextField();
		passLabel = new JLabel("Password:");
		passLabel.setForeground(Color.white);
		passwordField.setMaximumSize(new Dimension(200, 20));

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
					if (rs.getInt(1) == 0) {
						connect.voerInsertQueryUit("INSERT INTO `myDBtestding`.`Accounts` (`naam`, `rol`, `geaccepteerd`, `password`) VALUES ('"
								+ usernameField.getText()
								+ "', 'user', '1', '"
								+ passwordField.getText().toString()
								+ "');");
						connect.closeConnection();
						usernameField.setText("");
						passwordField.setText("");
						regLabel.setText("Registratie voltooid!");
					} else {
						regLabel.setText("Joost heeft je naam al gejat");
					}
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}

		});
		// Loginbutton ActionListener
		loginButton.addActionListener(new ActionListener() {
			private CompetitiesMenu competitieView;

			@Override
			public void actionPerformed(ActionEvent arg0) {
				Connectie connect = new Connectie();
				ResultSet rs;
				rs = connect
						.voerSelectQueryUit("SELECT COUNT(naam) FROM Accounts WHERE naam = '"
								+ usernameField.getText()
								+ "' AND password ='"
								+ passwordField.getText().toString() + "' ");
				try {
					rs.next();
					if (rs.getInt(1) == 1) {
						curUser = usernameField.getText();
						Account loggedin = new Account(curUser);
						competitieView = new CompetitiesMenu();
						activeFrame.setContentPane(competitieView);
						activeFrame.pack();
					} else {
						regLabel.setText("Naam of Wachtwoord fouttief!");
					}

				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});

	}
}
