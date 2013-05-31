package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private static String accountNaam;
	
	public Account() // private constructor
	{
	}
		
	public static String getAccountNaam(){
		return accountNaam;
	}
	
	public static void setAccountNaam(String loginNaam){
		accountNaam = loginNaam;
	}
	
	public static boolean checkModerator()
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Moderator")){
					return true;
				}
				else{
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		return false;
	}
	
	public static boolean checkAdmin()
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Administrator")){
					return true;
				}
				else{
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		return false;
	}
	
	public static boolean checkPlayer() // Eigenlijk dubbele code maar tijdnood
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Player")){
					return true;
				}
				else{
					return false;
				}
			}
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
		return false;
	}
	
	public static boolean checkObserver()
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Observer")){
					return true;
				}
				else{
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
