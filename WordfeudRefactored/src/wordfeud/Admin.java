package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;

public class Admin extends Observable {
	
	private Connectie connect;
	private ResultSet namenRS;
	private ResultSet gegevensRS;
	private ArrayList<String> namen;
	
	public Admin(){
		namen = new ArrayList<String>();
		connect = new Connectie();
		
	}
	
	public void getAccounts(){
		namenRS = connect.voerSelectQueryUit("SELECT naam from Accounts");
		try {
			while(namenRS.next()){
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
	
	public String[] getInfo(String naam){
		connect = new Connectie();
		gegevensRS = connect.voerSelectQueryUit("SELECT * from Accounts WHERE naam = '" + naam+"'");
		String[] rij = new String[3];
		try {
			while(gegevensRS.next()){
				rij[0] = gegevensRS.getString("naam");
				rij[1]= gegevensRS.getString("rol");
				rij[2] = gegevensRS.getString("geaccepteerd");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect.closeConnection();
		return rij;
	}

}
