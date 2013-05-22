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
	
	public void getInfo(String naam){
		connect = new Connectie();
		gegevensRS = connect.voerSelectQueryUit("SELECT * from Accounts WHERE naam = " + naam);
		
	}

}
