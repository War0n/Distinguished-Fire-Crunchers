package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private static String accountNaam;
		
	public static String getAccountNaam(){
		return accountNaam;
	}
	
	public static void setAccountNaam(String loginNaam){
		Account.accountNaam = loginNaam;
	}
	
	public boolean checkModerator(String accountNaam)
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT rol From Accounts WHERE naam = '" + accountNaam + "';");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("rol").equals("mod")){
					connect.closeConnection();
					return true;
				}
				else{
					connect.closeConnection();
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		return false;
	}
	
}
