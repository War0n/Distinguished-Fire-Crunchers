package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.sql.Blob;

public class Chat extends Observable{

	private Connectie connect;
	private ResultSet rs;
	
	public Chat(){
		connect = new Connectie();
		connect.closeConnection();
	}
	
	public void addChatLine(int spelID,String accountNaam,String bericht){
		connect.voerInsertQueryUit("INSERT INTO ChatRegels VALUES(NOW(),'" + accountNaam + "',"+ spelID +",'"+ bericht +"')");
	}
	
	public String getChatLines(int spelID){
		rs = connect.voerSelectQueryUit("SELECT * FROM ChatRegels WHERE spel_id = "+ spelID);
		String chatLines = "";
		try{

		while(rs.next())
		{
		 Blob berichtBlob = rs.getBlob("bericht");
		 byte[] bdata = berichtBlob.getBytes(1, (int) berichtBlob.length());
		 String bericht = new String(bdata);
		 chatLines = chatLines + System.lineSeparator() + bericht;
		}

		}catch(SQLException e){
		            System.out.println("Error: " + e);
		}
		
		return chatLines;
	}
}
