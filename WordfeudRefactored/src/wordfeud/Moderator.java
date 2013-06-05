package wordfeud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JCheckBox;

import com.mysql.jdbc.UpdatableResultSet;

public class Moderator extends Observable implements ActionListener {

	private Connectie connect;
	private ResultSet myResultSet;
	private ResultSet prevResultSet;
	private ArrayList<JCheckBox> reviewWoorden;
	private GUIModerator myGUIModerator;
	private GUIMenu myGuiMenu;
	private Thread checkReviewWoorden;

	private boolean refresh;

	public Moderator(GUIModerator gui) {
		refresh = true;
		this.myGUIModerator = gui;
		reviewWoorden = new ArrayList<JCheckBox>();
		this.addObserver(myGUIModerator);
		this.refreshList();
		myGUIModerator.addButtonMethods(this);
	}

	public void acceptWord() {
		connect = new Connectie();

		for (int i = 0; i < reviewWoorden.size(); i++) {
			if (reviewWoorden.get(i).isSelected()) { // voor alle geselecteerde
														// woorden

				ResultSet myResultSet = connect
						.voerSelectQueryUit("SELECT * FROM woordenboek WHERE woord = '"
								+ reviewWoorden.get(i).getText() + "'");
				try {
					if ((myResultSet.next())) {
						connect.voerInsertOrUpdateQueryUit("UPDATE  `rcollard_db2`.`woordenboek` SET  `status` =  'Accepted' WHERE  `woordenboek`.`woord` =  '"
								+ reviewWoorden.get(i).getText() + "';");
					}
				} catch (SQLException e) {
					System.out.println("Error: " + e);
				}
			}
		}
		connect.closeConnection();
		refreshList();
	}

	public void declineWord() {
		connect = new Connectie();
		for (int i = 0; i < reviewWoorden.size(); i++) {
			if (reviewWoorden.get(i).isSelected()) { // verander status van
														// geselecteerde woorden
														// in denied
				connect.voerInsertOrUpdateQueryUit("UPDATE  `rcollard_db2`.`woordenboek` SET  `status` =  'Denied' WHERE  `woordenboek`.`woord` =  '"
						+ reviewWoorden.get(i).getText() + "';");
			}
		}
		connect.closeConnection();
		refreshList();
	}

	public void refreshList() {
		connect = new Connectie();
		myResultSet = connect
				.voerSelectQueryUit("SELECT * FROM woordenboek WHERE status = 'Pending'");
		myGUIModerator.clearWoordenPanel();
		try {
			while (myResultSet.next()) {
				reviewWoorden
						.add(new JCheckBox(myResultSet.getString("Woord")));
			}
			setChanged();
			notifyObservers(reviewWoorden);
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
		case "Accepteer":
			acceptWord();
			refreshList();
			break;
		case "Verwerp":
			declineWord();
			refreshList();
			break;
		case "Ga terug":
			myGuiMenu = new GUIMenu();
			refresh = false;
			myGUIModerator.setParentContentPane(myGuiMenu);
			break;
		}
	}
}
