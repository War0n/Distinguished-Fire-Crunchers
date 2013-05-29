package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private static String accountNaam;
	
	private Account() // private constructor
	{
	}
		
	public static String getAccountNaam(){
		return accountNaam;
	}
	
	public static void setAccountNaam(String loginNaam){
		accountNaam = loginNaam;
	}
	
	public static boolean checkModerator(String accountNaam)
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From Accountrol WHERE Account_naam = '" + accountNaam + "';");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Moderator")){
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
	
	public static boolean checkAdmin(String accountNaam)
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From Accountrol WHERE Account_naam = '" + accountNaam + "';");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("rol_type").equals("Administrator")){
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
