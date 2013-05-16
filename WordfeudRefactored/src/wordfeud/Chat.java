package wordfeud;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.sql.Blob;

public class Chat extends Observable implements Runnable{

	private Connectie connect;
	private ResultSet rs;
	private String chatLines;
	private Integer spelID;
	
	public Chat(int spelID){
		this.spelID = spelID;
		Thread chatThread = new Thread(this,"ChatThread");
		chatThread.start();
	}
	
	public void addChatLine(int spelID,String accountNaam,String bericht){
		connect = new Connectie();
		connect.voerInsertQueryUit("INSERT INTO ChatRegels VALUES(NOW(),'" + accountNaam + "',"+ spelID +",'"+ bericht +"')");
		connect.closeConnection();
	}
	
	public String getChatLines(int spelID){
		connect = new Connectie();
		rs = connect.voerSelectQueryUit("SELECT * FROM ChatRegels WHERE spel_id = "+ spelID);
		chatLines = "";
		try{

		
		while(rs.next())
		{
			String date = rs.getDate("datumtijd").toString();
		String account=rs.getString("account_naam");
		 Blob berichtBlob = rs.getBlob("bericht");
		 byte[] bdata = berichtBlob.getBytes(1, (int) berichtBlob.length());
		 String bericht = new String(bdata);
		 chatLines = chatLines + System.lineSeparator() + date+ " " + account + ": " + bericht;
		}

		}catch(SQLException e){
		            System.out.println("Error: " + e);
		}
		connect.closeConnection();
		
		this.setChanged();
		this.notifyObservers(chatLines);
		return chatLines;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true){
			try {
				Thread.sleep(500);
				getChatLines(spelID);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
