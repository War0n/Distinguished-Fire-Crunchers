package wordfeud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Connectie {

	private Connection con;
	private PreparedStatement pr;
	private ResultSet result;
	private Statement st;
	
	public Connectie(){
		try{
			Class.forName("com.mysql.jdbc.Driver");
			
			con = DriverManager.getConnection("jdbc:mysql://databases.aii.avans.nl/wordfeud", "rcollard", "Ab12345");
	
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
	
	public void voerInsertOrUpdateQueryUit(String myQuery){
		 try {
			st = con.createStatement();
			st.executeUpdate(myQuery);
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
	
	public void doInsertUpdate(String query, Object ... args)
	{
		try {
			st = con.createStatement();
			st.executeUpdate(String.format(query, args));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public ResultSet doSelect(String query, Object ... args)
	{
		try {
			pr = con.prepareStatement(String.format(query, args));
			result = pr.executeQuery();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			System.out.println("Error: " + e);
			return null;
		}
	}
}