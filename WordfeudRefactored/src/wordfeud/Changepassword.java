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
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class Changepassword extends JFrame {

	private static final long serialVersionUID = 1L;
	private JPasswordField passwordField;
	private JPasswordField registerControle;
	private JPasswordField currentPassword;
	private JPanel changepwPanel;
	private JPanel buttonpanel;
	private JLabel passLabel;
	private JLabel currentPasslabel;
	private JLabel registercontroleLabel;
	private WFButton changepwbutton;
	private JFrame activeFrame;
	
	private WFButton removeAccount;
	JFrame popup = null;

	public Changepassword() {
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		activeFrame = this;
		activeFrame.setMinimumSize(new Dimension(200, 150));
		activeFrame.setResizable(false);
		activeFrame.setLocationRelativeTo(null);
		changepwPanel = (JPanel) activeFrame.getContentPane();
		changepwPanel.setLayout(new BoxLayout(changepwPanel, BoxLayout.Y_AXIS));
		changepwPanel.setBackground(new Color(23, 26, 30));
		changepwPanel.setPreferredSize(new Dimension(200, 175));
		changepwPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		buttonpanel = new JPanel();
		buttonpanel.setLayout(new FlowLayout());
		buttonpanel.setBackground(new Color(23, 26, 30));

		changepwbutton = new WFButton("Verander!");
		removeAccount = new WFButton("Verwijder account!");
		currentPasslabel = new JLabel("huidig wachtwoord:");
		currentPasslabel.setForeground(Color.white);
		currentPasslabel.setAlignmentX(CENTER_ALIGNMENT);
		currentPasslabel.setVisible(true);
		currentPassword = new JPasswordField();
		currentPassword.setVisible(true);
		currentPassword.setAlignmentX(CENTER_ALIGNMENT);
		currentPassword.setMaximumSize(new Dimension(150, 20));
		passwordField = new JPasswordField();
		passwordField.setAlignmentX(CENTER_ALIGNMENT);
		passLabel = new JLabel("Nieuw wachtwoord:");
		passLabel.setForeground(Color.white);
		passLabel.setAlignmentX(CENTER_ALIGNMENT);
		passwordField.setMaximumSize(new Dimension(150, 20));
		registerControle = new JPasswordField();
		registerControle.setVisible(true);
		registerControle.setAlignmentX(CENTER_ALIGNMENT);
		registercontroleLabel = new JLabel("Nieuwe wachtwoord herhalen:");
		registercontroleLabel.setForeground(Color.white);
		registercontroleLabel.setAlignmentX(CENTER_ALIGNMENT);
		registercontroleLabel.setVisible(true);
		registerControle.setMaximumSize(new Dimension(150, 20));

		changepwPanel.add(Box.createVerticalStrut(5));
		changepwPanel.add(currentPasslabel);
		changepwPanel.add(Box.createVerticalStrut(2));
		changepwPanel.add(currentPassword);
		changepwPanel.add(Box.createVerticalStrut(2));
		changepwPanel.add(passLabel);
		changepwPanel.add(Box.createVerticalStrut(2));
		changepwPanel.add(passwordField);
		changepwPanel.add(Box.createVerticalStrut(2));
		changepwPanel.add(registercontroleLabel);
		changepwPanel.add(Box.createVerticalStrut(2));
		changepwPanel.add(registerControle);
		changepwPanel.add(Box.createVerticalStrut(5));
		buttonpanel.add(changepwbutton);
		buttonpanel.add(removeAccount);
		changepwPanel.add(buttonpanel);
		activeFrame.pack();
		this.setVisible(true);
		/*passwordField.addKeyListener(new KeyAdapter() {
			public void KeyPressed(KeyEvent e) {
				if (e.getKeyCode() == (KeyEvent.VK_ENTER)) {
					System.out.println("het werkt ongeveer");
				}
			}
		});*/
		changepwbutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String checkcurrentpassword = new String(currentPassword
						.getPassword());
				Connectie con2 = new Connectie();
				ResultSet rs;
				rs = con2
						.voerSelectQueryUit("SELECT COUNT(naam) AS aantal FROM account WHERE naam = '"
								+ Account.getAccountNaam()
								+ "' AND wachtwoord ='"
								+ checkcurrentpassword
								+ "' ");
				try {
					if (rs.next()) {
						if (rs.getInt("aantal") == 1) {
							final ImageIcon icon = new ImageIcon(
									"src/images/conf.jpg");
							String controlePassword = new String(
									registerControle.getPassword());
							String controlePasswordAgain = new String(
									passwordField.getPassword());

							if (controlePassword.equals("")
									&& controlePasswordAgain.equals("")) {
								JOptionPane.showMessageDialog(popup,
										"Je hebt niks ingevoerd!", "fout",
										JOptionPane.ERROR_MESSAGE);
								popup = null;
							} else {
								if (controlePassword
										.equals(controlePasswordAgain)) {
									Connectie connect = new Connectie();
									String password = new String(passwordField
											.getPassword());
									System.out.println("nars.getint");
									connect.voerInsertOrUpdateQueryUit("UPDATE account SET wachtwoord = '"
											+ password
											+ "' WHERE naam = '"
											+ Account.getAccountNaam() + "'");
									connect.closeConnection();
									JOptionPane.showMessageDialog(popup,
											"Het wachtwoord is veranderd!",
											"Voltooid",
											JOptionPane.INFORMATION_MESSAGE,
											icon);
									popup = null;
									activeFrame.dispose();
								}

								else if (!controlePassword
										.equals(controlePasswordAgain)) {
									JOptionPane
											.showMessageDialog(
													popup,
													"De wachtwoorden komen niet overeen!",
													"Foutje!",
													JOptionPane.ERROR_MESSAGE);
									popup = null;
								}

							}

						} else {
							JOptionPane
							.showMessageDialog(
									popup,
									"Je oude wachtwoord klopt niet!",
									"Foutje!",
									JOptionPane.ERROR_MESSAGE);
					popup = null;
							
						}
					}

				} catch (SQLException e) {
					e.printStackTrace();
				}
				;

			};

		});
		
		removeAccount.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				String checkcurrentpassword = new String(currentPassword
						.getPassword());
				Connectie con2 = new Connectie();
				ResultSet rs;
				rs = con2
						.voerSelectQueryUit("SELECT COUNT(*) AS aantal FROM account WHERE naam = '"
								+ Account.getAccountNaam()
								+ "' AND wachtwoord ='"
								+ checkcurrentpassword
								+ "' ");
				try {
					if (rs.next()) {
						if (rs.getInt("aantal") == 1) {
							con2.doInsertUpdate("DELETE FROM account WHERE naam = '%1$s'", Account.getAccountNaam());
						}
					}
				}
				catch(SQLException e)
				{
					e.printStackTrace();
				}
			}
		});
	}
}