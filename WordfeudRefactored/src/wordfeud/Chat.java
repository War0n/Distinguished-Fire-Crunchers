package wordfeud;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Observable;
import java.sql.Blob;
import java.text.Format;
import java.text.SimpleDateFormat;

public class Chat extends Observable implements Runnable{

	private Connectie connect;
	private ResultSet rs;
	private String chatLines;
	private Integer spelID;
	private Format formatter;
	
	public Chat(int spelID){
		this.spelID = spelID;
		formatter = new SimpleDateFormat("dd-MM-yy KK:mm");
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
		chatLines = "<html style=\"margin:1px;\"><body style=\"color:white; font-family:Arial,verdana;\">";
		try{

		
		while(rs.next())
		{
		String date = formatter.format(rs.getDate("datumtijd"));
		String account=rs.getString("account_naam");
		 Blob berichtBlob = rs.getBlob("bericht");
		 byte[] bdata = berichtBlob.getBytes(1, (int) berichtBlob.length());
		 String bericht = new String(bdata);
		 chatLines = chatLines + date+ " <b>" + account + "</b>: " + bericht + "<br/>" ;
		}

		}catch(SQLException e){
		            System.out.println("Error: " + e);
		}
		chatLines = chatLines + "</body></html>";
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
