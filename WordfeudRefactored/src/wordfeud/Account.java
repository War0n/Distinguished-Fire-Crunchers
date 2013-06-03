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
		if(checkRole("Moderator")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkAdmin()
	{
		if(checkRole("Administrator")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkPlayer()
	{
		if(checkRole("Player")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkObserver()
	{
		if(checkRole("Observer")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static boolean checkRole(String rol)
	{
		Connectie connect = new Connectie();
		ResultSet myResultSet;
		
		myResultSet = connect.voerSelectQueryUit("SELECT Rol_type From accountrol WHERE Account_naam = '" + getAccountNaam() + "'");
		
		try {
			while(myResultSet.next())
			{
				if(myResultSet.getString("Rol_type").equals(rol)){
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
