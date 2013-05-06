package wordfeud;

import java.sql.Connection;
import java.sql.DriverManager;

public class Connectie {

	private Connection con;
	
	public Connectie(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://db.den-networks.net/myDBtestding", "WF", "avansWF");
	
		}catch(Exception ex){
			System.out.println("Error: " + ex);
		}
	}
	
}