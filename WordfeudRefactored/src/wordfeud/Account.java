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
	
	public static boolean checkAdmin()
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Administrator")){
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
	
	public static boolean checkPlayer()
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Player")){
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
	
	public static boolean checkObserver()
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals("Observer")){
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
