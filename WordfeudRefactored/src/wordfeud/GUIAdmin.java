package wordfeud;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

@SuppressWarnings("serial")
public class GUIAdmin extends JPanel implements Observer, ActionListener {

	private JLabel titel;
	private JPanel head;
	private JPanel functies;
	private JPanel menu;
	private GridLayout myGridLayout;
	private JScrollPane myScroller;
	private WFButton back;
	private WFButton newPlayer;
	private Admin admin;
	private WFButton updateButton;
	private WFButton verwijderAccount;

	private JFrame popup;
	private JPanel rolePanel;
	private JLabel roleLabel;
	private JTextField passwordField;
	private JLabel usernameLabel;
	private JLabel usernameDataLabel;
	private JLabel passwordLabel;

	private ArrayList<JCheckBox> roleArray;

	public GUIAdmin() {
		setMinimumSize(new Dimension(630, 700));
		setPreferredSize(getMinimumSize());
		setBackground(new Color(23, 26, 30));
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		titel = new JLabel("Administrator");
		titel.setForeground(Color.white);
		titel.setFont(new Font("Arial", Font.BOLD, 40));
		titel.setAlignmentX(Component.CENTER_ALIGNMENT);
		head = new JPanel();
		head.setBackground(this.getBackground());
		head.setMaximumSize(new Dimension(650, 50));
		head.setPreferredSize(head.getMaximumSize());
		head.add(titel);
		functies = new JPanel();
		functies.setBackground(new Color(29, 144, 160));
		functies.setMaximumSize(new Dimension(650, 40));
		functies.setPreferredSize(functies.getMaximumSize());
		functies.setLayout(new FlowLayout());
		back = new WFButton("< Terug naar menu");
		newPlayer = new WFButton("Nieuwe speler aanmaken");
		functies.add(back);
		functies.add(newPlayer);
		menu = new JPanel();
		myGridLayout = new GridLayout(0, 1, 0, 10);
		menu.setLayout(myGridLayout);
		menu.setBackground(this.getBackground());
		myScroller = new JScrollPane(menu);
		myScroller
				.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		admin = new Admin();
		admin.addObserver(this);
		back.addActionListener(this);
		newPlayer.addActionListener(this);
		roleArray = new ArrayList<JCheckBox>();

		add(head);
		add(functies);
		admin.getAccounts();
		this.add(myScroller);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void update(Observable arg0, Object arg1) {
		ArrayList<String> namen = (ArrayList<String>) arg1;
		displayNamen(namen);
	}

	public void displayNamen(ArrayList<String> namen) {

		for (String naam : namen) {
			WFButton naamButton = new WFButton(naam);
			naamButton.setMinimumSize(new Dimension(650, 50));
			naamButton.setPreferredSize(naamButton.getMinimumSize());
			naamButton.addActionListener(new ActionListener() {

				@Override
				public void actionPerformed(ActionEvent e) {
					getAccountInfo(e);
				}
			});

			menu.add(naamButton);

		}
		menu.repaint();
	}

	public void getAccountInfo(ActionEvent e) {
		popup = new JFrame();
		JButton srcButton = (JButton) e.getSource();
		JPanel popupPanel = new JPanel();
		String[] data = admin.getInfo(srcButton.getText());

		passwordField = new JTextField();
		usernameLabel = new JLabel("Gebruikersnaam: ");
		usernameDataLabel = new JLabel(data[0]);
		passwordLabel = new JLabel("Wachtwoord: ");
		roleLabel = new JLabel("Rollen: ");

		usernameLabel.setForeground(Color.white);
		usernameDataLabel.setForeground(Color.white);
		passwordLabel.setForeground(Color.white);
		passwordField.setForeground(Color.white);
		roleLabel.setForeground(Color.white);

		popupPanel.add(usernameLabel);
		popupPanel.add(usernameDataLabel);
		passwordField.setText(data[1]);
		passwordField.setBackground(new Color(23, 26, 30));
		passwordField.setFont(new Font("Arial", Font.BOLD, 20));
		popupPanel.add(passwordLabel);
		popupPanel.add(passwordField);
		rolePanel = new JPanel();
		rolePanel.setBackground(this.getBackground());
		rolePanel.setLayout(new BoxLayout(rolePanel, BoxLayout.Y_AXIS));
		roleArray = admin.getAvailableRoles();
		for (JCheckBox role : roleArray) {
			if (admin.checkUserRole(role.getText(), data[0])) {
				role.setSelected(true);
			} else {
				role.setSelected(false);
			}
			role.setBackground(this.getBackground());
			role.setForeground(Color.white);
			rolePanel.add(role);
		}
		popupPanel.add(roleLabel);
		popupPanel.add(rolePanel);

		popup.setTitle(data[0]);
		popup.setMinimumSize(new Dimension(300, 50));

		popup.setResizable(false);
		popupPanel.setBackground(new Color(23, 26, 30));
		popupPanel.setLayout(new GridLayout(4, 2));
		popup.setContentPane(popupPanel);
		popup.pack();
		popup.setVisible(true);
		updateButton = new WFButton("Wijzig gegevens");
		verwijderAccount = new WFButton("Verwijder account");
		verwijderAccount.setMaximumSize(new Dimension(100, 50));
		verwijderAccount.setPreferredSize(getMaximumSize());
		updateButton.setMaximumSize(new Dimension(5, 5));
		updateButton.setPreferredSize(getMaximumSize());
		updateButton.addActionListener(this);
		verwijderAccount.addActionListener(this);

		popupPanel.add(updateButton);
		popupPanel.add(verwijderAccount);
	}

	public void setParentContentPane(JPanel contentPane) {
		JFrame root = (JFrame) SwingUtilities.getWindowAncestor(this);
		root.setContentPane(contentPane);
		root.pack();
	}

	public ArrayList<String> getSelectedRoles() {
		ArrayList<String> selectedRoles = new ArrayList<String>();
		for (JCheckBox roleCheckBox : roleArray) {
			if (roleCheckBox.isSelected()) {
				selectedRoles.add(roleCheckBox.getText());
			}
		}
		return selectedRoles;
	}

	public ArrayList<String> getDeselectedRoles() {
		ArrayList<String> deSelectedRoles = new ArrayList<String>();
		for (JCheckBox roleCheckBox : roleArray) {
			if (!roleCheckBox.isSelected()) {
				deSelectedRoles.add(roleCheckBox.getText());
			}
		}
		return deSelectedRoles;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (arg0.getSource().equals(back)) {
			setParentContentPane(new GUIMenu());
		} else if (arg0.getSource().equals(updateButton)) {
			admin.chancePassword(usernameDataLabel.getText(),
					passwordField.getText());
			admin.changeRoles(getSelectedRoles(), getDeselectedRoles(),
					usernameDataLabel.getText());
			popup.dispose();
			admin.getAccounts();
		} else if (arg0.getSource().equals(verwijderAccount)) {
			admin.removeAccount(usernameDataLabel.getText());
			popup.dispose();
		} else if (arg0.getSource().equals(newPlayer)) {
			JFrame registreren = new JFrame("Speler aanmaken");
			registreren.setContentPane(new LoginScreen(false));
			registreren.setLocationRelativeTo(null);
			registreren.setMinimumSize(new Dimension(200, 200));
			registreren.pack();
			registreren.setResizable(false);
			registreren.setVisible(true);
		}
	}

}