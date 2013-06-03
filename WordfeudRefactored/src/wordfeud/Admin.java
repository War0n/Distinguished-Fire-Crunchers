package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

import javax.swing.JCheckBox;

public class Admin extends Observable {

	private Connectie connect;
	private ResultSet namenRS;
	private ResultSet gegevensRS;
	private ResultSet rolRS;
	private ArrayList<String> namen;

	public Admin() {
		namen = new ArrayList<String>();
		connect = new Connectie();

	}

	public void getAccounts() {
		connect = new Connectie();
		namenRS = connect.voerSelectQueryUit("SELECT naam from account");
		try {
			while (namenRS.next()) {
				namen.add(namenRS.getString("naam"));

			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setChanged();
		notifyObservers(namen);
		connect.closeConnection();
	}

	public String[] getInfo(String naam) {
		connect = new Connectie();
		gegevensRS = connect
				.voerSelectQueryUit("SELECT * from account WHERE naam = '"
						+ naam + "'");
		String[] rij = new String[3];
		try {
			while (gegevensRS.next()) {
				rij[0] = gegevensRS.getString("naam");
				rij[1] = gegevensRS.getString("wachtwoord");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		rolRS = connect
				.voerSelectQueryUit("SELECT Rol_type from accountrol WHERE Account_naam = '"
						+ naam + "'");
		try {
			rij[2] = "Rol: onbekend";
			while (rolRS.next()) {
				rij[2] = "Rol: " + rolRS.getString("Rol_type");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		connect.closeConnection();
		return rij;
	}

	public void removeAccount(String naam) {
		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit("DELETE FROM accountrol WHERE Account_naam = '"
				+ naam + "'");
		connect.voerInsertOrUpdateQueryUit("DELETE FROM account WHERE naam = '"
				+ naam + "'");
		connect.closeConnection();
	}

	public void chancePassword(String naam, String wachtwoord) {
		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit("UPDATE account SET wachtwoord = '"
				+ wachtwoord + "' WHERE naam = '" + naam + "'");
		connect.closeConnection();
	}

	public void changeRoles(ArrayList<String> selected, ArrayList<String> notSelected ,String username) {
		Connectie roleInsertConn = new Connectie();
		for (String selectedRole : selected) {
			if (!checkUserRole(selectedRole, username)) {

				roleInsertConn
						.voerInsertOrUpdateQueryUit("INSERT INTO accountrol VALUES ('"
								+ username + "','" + selectedRole + "')");
			}
		}
		for (String deSelectedRole : notSelected){
			if (checkUserRole(deSelectedRole, username)) {

				roleInsertConn
						.voerInsertOrUpdateQueryUit("DELETE FROM accountrol WHERE Account_naam = '"+username+"' AND Rol_type = '"+deSelectedRole+"'");
			}
		}
		roleInsertConn.closeConnection();
	}

	public ArrayList<JCheckBox> getAvailableRoles() {
		connect = new Connectie();
		ResultSet roles = connect.voerSelectQueryUit("SELECT * FROM rol");
		ArrayList<JCheckBox> checkboxes = new ArrayList<JCheckBox>();
		try {
			while (roles.next()) {
				checkboxes.add(new JCheckBox(roles.getString("type")));
			}
			return checkboxes;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect.closeConnection();
		return null;
	}

	public boolean checkUserRole(String rol, String givenAccountnaam) {
		connect = new Connectie();
		ResultSet myResultSet;

		myResultSet = connect
				.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '"
						+ givenAccountnaam + "'");

		try {
			while (myResultSet.next()) {
				if (myResultSet.getString("Rol_type").equals(rol)) {
					connect.closeConnection();
					return true;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		return false;
	}

}
