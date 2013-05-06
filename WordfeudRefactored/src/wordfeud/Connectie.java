package wordfeud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Connectie {

	private Connection con;
	private PreparedStatement pr;
	private ResultSet result;
	
	public Connectie(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://db.den-networks.net/myDBtestding", "WF", "avansWF");
	
		}catch(Exception ex){
			System.out.println("Error: " + ex);
		}
	}
	
	public Connection getConnection(){
		return con;
	}
	
	public ResultSet voerSelectQueryUit(String myQuery){
		try {
			pr = con.prepareStatement(myQuery);
			result = pr.executeQuery();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
			return null;
		}
	}
	
	public void voerInsertQueryUit(String myQuery){
		 try {
			pr = (PreparedStatement) con.createStatement();
			pr.executeUpdate(myQuery);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void closeConnection()
	{
		try {
			con.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
		}
	}
}