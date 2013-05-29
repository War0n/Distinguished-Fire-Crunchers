package wordfeud;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Observable;
import javax.swing.JCheckBox;

public class Moderator extends Observable implements ActionListener{
	
	
	private Connectie connect;
	private ResultSet myResultSet;
	private ArrayList<JCheckBox> reviewWoorden;
	private GUIModerator myGUIModerator;
	private GUIMenu myGuiMenu;

	public Moderator(GUIModerator gui){

		this.myGUIModerator = gui;
		this.addObserver(myGUIModerator);
		this.refreshList();
		myGUIModerator.addButtonMethods(this);
	}

	public void acceptWord(){
		connect = new Connectie();
		
		for(int i = 0; i < reviewWoorden.size(); i++){
			if(reviewWoorden.get(i).isSelected()){ // voor alle geselecteerde woorden
				
				ResultSet myResultSet = connect.voerSelectQueryUit("select * from woordenboek where woord = '" + reviewWoorden.get(i).getText() + "'");
				try {
					if(!(myResultSet.next())){
						connect.voerInsertOrUpdateQueryUit("insert into woordenboek (woord) values ('" + reviewWoorden.get(i).getText() + "') ");
						connect.voerInsertOrUpdateQueryUit("DELETE FROM WoordenVoorReview WHERE Woord = '" + reviewWoorden.get(i).getText() + "'");
					}
					else{
						connect.voerInsertOrUpdateQueryUit("DELETE FROM WoordenVoorReview WHERE Woord = '" + reviewWoorden.get(i).getText() + "'");
					}
				} catch (SQLException e) {
					System.out.println("Error: " + e);
				}
			}
		}
		connect.closeConnection();
		refreshList();
	}
	
	public void declineWord(){
		connect = new Connectie();
		for(int i = 0; i < reviewWoorden.size(); i++){
			if(reviewWoorden.get(i).isSelected()){
				connect.voerInsertOrUpdateQueryUit("DELETE FROM WoordenVoorReview WHERE Woord = '" + reviewWoorden.get(i).getText() + "';");
			}
		}
		connect.closeConnection();
		refreshList();
	}
	
	public void refreshList(){
		connect = new Connectie();
		myResultSet = connect.voerSelectQueryUit("select * from WoordenVoorReview");
		reviewWoorden = new ArrayList<JCheckBox>();
		myGUIModerator.clearWoordenPanel();
		try {	
			while(myResultSet.next())
			{
				reviewWoorden.add(new JCheckBox(myResultSet.getString("Woord")));
			}
			setChanged();
			notifyObservers(reviewWoorden);
		} catch (SQLException e) {
			System.out.println("Error: " + e);
		}
		connect.closeConnection();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch(e.getActionCommand()){
			case "Accepteer":
				acceptWord();
			break;
			case "Verwerp":
				declineWord();
			break;
			case "Ga terug":
				myGuiMenu = new GUIMenu();
				myGUIModerator.setParentContentPane(myGuiMenu);
			break;
		}
	}
}
