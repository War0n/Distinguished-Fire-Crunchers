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
	private Integer spel_id;
	private Format formatter;
	
	public Chat(int spel_id){
		this.spel_id = spel_id;
		formatter = new SimpleDateFormat("dd-MM-yy KK:mm");
		Thread chatThread = new Thread(this,"ChatThread");
		chatThread.start();
	}
	
	public void addChatLine(int spel_id,String accountNaam,String bericht){
		connect = new Connectie();
		connect.voerInsertOrUpdateQueryUit("INSERT INTO chatregel VALUES(NOW(),'" + accountNaam + "',"+ spel_id +",'"+ bericht +"')");
		connect.closeConnection();
	}
	
	public String getChatLines(int spel_id){
		connect = new Connectie();
		rs = connect.voerSelectQueryUit("SELECT * FROM chatregel WHERE spel_id = "+ spel_id);
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
				getChatLines(spel_id);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
