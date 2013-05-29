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
		namenRS = connect.voerSelectQueryUit("SELECT naam from account");
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
				rij[0] = "Username: "+gegevensRS.getString("naam");
				rij[1]= "Role: "+gegevensRS.getString("rol");
				if(gegevensRS.getString("geaccepteerd").equals("1")){
				rij[2] = "Accepted: true";
				}
				else if(gegevensRS.getString("geaccepteerd").equals("0")){
					rij[2] = "Accepted: false";
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		connect.closeConnection();
		return rij;
	}

}
