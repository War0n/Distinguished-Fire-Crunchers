package wordfeud;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;

public class Changepassword extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPasswordField passwordField;
	private JPasswordField registerControle;
	private JPanel changepwPanel;
	private JPanel buttonpanel;
	private JLabel passLabel;
	private JLabel registercontroleLabel;
	private WFButton changepwbutton;
	private JFrame activeFrame;
	private String curUser;
	JFrame popup = null;

	public Changepassword() {
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		activeFrame = this;
		activeFrame.setMinimumSize(new Dimension(200, 100));
		activeFrame.setResizable(false);
		activeFrame.setLocationRelativeTo(null);
		changepwPanel = (JPanel) activeFrame.getContentPane();
		changepwPanel.setLayout(new BoxLayout(changepwPanel, BoxLayout.Y_AXIS));
		changepwPanel.setBackground(new Color(23, 26, 30));
		changepwPanel.setPreferredSize(new Dimension(150, 150));
		changepwPanel.setAlignmentX(JPanel.CENTER_ALIGNMENT);

		buttonpanel = new JPanel();
		buttonpanel.setLayout(new FlowLayout());
		buttonpanel.setBackground(new Color(23, 26, 30));

		changepwbutton = new WFButton("Verander!");

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

		changepwPanel.add(passLabel);
		changepwPanel.add(Box.createVerticalStrut(2));
		changepwPanel.add(passwordField);
		changepwPanel.add(registercontroleLabel);
		changepwPanel.add(registerControle);
		changepwPanel.add(Box.createVerticalStrut(5));
		buttonpanel.add(changepwbutton);
		changepwPanel.add(buttonpanel);
		activeFrame.pack();
		this.setVisible(true);
		passwordField.addKeyListener(new KeyAdapter() {
			public void KeyPressed(KeyEvent e) {
				if (e.getKeyCode() == (KeyEvent.VK_ENTER)) {
					System.out.println("het werkt ongeveer");
				}
			}
		});
		changepwbutton.addActionListener(new ActionListener() {

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
					connect.voerInsertOrUpdateQueryUit("UPDATE account SET wachtwoord = '"
							+ password
							+ "' WHERE naam = '"
							+ Account.getAccountNaam() + "'");

					connect.closeConnection();
					JOptionPane.showMessageDialog(popup,
							"Het wachtwoord is veranderd!", "Voltooid",
							JOptionPane.PLAIN_MESSAGE);
					popup = null;
					activeFrame.dispose();

				} else if (!controlePassword.equals(password2)) {
					JOptionPane.showMessageDialog(popup,
							"De wachtwoorden komen niet overeen!", "Foutje!",
							JOptionPane.WARNING_MESSAGE);
					popup = null;
				}
			}
		});

	}

}