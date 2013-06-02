package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private static String accountNaam;
	
	public Account() // private constructor
	{
	}
		
	public String getAccountNaam(){
		return accountNaam;
	}
	
	public void setAccountNaam(String loginNaam){
		accountNaam = loginNaam;
	}
	
	public boolean checkModerator()
	{
		if(checkRol("Moderator")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkAdmin()
	{
		if(checkRol("Administrator")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkPlayer()
	{
		if(checkRol("Player")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkObserver()
	{
		if(checkRol("Observer")){
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean checkRol(String rol)
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
