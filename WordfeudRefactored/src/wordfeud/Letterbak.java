package wordfeud;
     
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
     
    public class Letterbak
    {
            private ArrayList<Tile> stenen;
            private Spel curSpel;
            private boolean bObserver;
           
            public Letterbak(Spel currentSpel, boolean bObserver)
            {
            	this.bObserver = bObserver;
                stenen = new ArrayList<Tile>();
                curSpel = currentSpel;
                int spelId = curSpel.getSpelId();
                for(int i = 0; i < 7; i++)
                {
                	stenen.add(new Tile());
                	//stenen.get(i).setStone(new Stone('A', i));
                }
                
                if( !bObserver )
                {
                	Connectie con = new Connectie();
                	ResultSet rs = con.doSelect("SELECT lb.Spel_ID, lb.Beurt_ID, l.ID, l.LetterType_LetterSet_code, l.LetterType_karakter FROM letterbakjeletter AS lb JOIN letter as l ON lb.Letter_ID = l.ID AND lb.Spel_ID = l.Spel_ID WHERE lb.Spel_ID = %1$d AND lb.Beurt_ID = (SELECT MAX(ID) FROM Beurt WHERE Spel_ID = %1$d AND Account_Naam = '%2$s') LIMIT 7;", spelId, Account.getAccountNaam());
                	int i = 0;
                	try
                	{
                		while(rs.next())
                		{
                			stenen.get(i).setStone(new Stone(rs.getString("LetterType_karakter").charAt(0), rs.getInt("ID")));
                			i++;
                		}
                	}
                	catch(SQLException io)
                	{
                		io.printStackTrace();
                	}
                	con.closeConnection();
                }
            }
            
            public void laadStenenVoorBeurt(int beurtId)
            {
            	for(int i = 0; i < 7; i++)
            	{
            		stenen.get(i).setStone(null);
            	}
            	Connectie con = new Connectie();
            	ResultSet rs = con.doSelect("SELECT lb.Spel_ID, lb.Beurt_ID, l.ID, l.LetterType_LetterSet_code, l.LetterType_karakter FROM letterbakjeletter AS lb JOIN letter as l ON lb.Letter_ID = l.ID AND lb.Spel_ID = l.Spel_ID WHERE lb.Spel_ID = %1$d AND lb.Beurt_ID = %3$d LIMIT 7;", curSpel.getSpelId(), Account.getAccountNaam(), beurtId-1);
            	int i = 0;
            	try
            	{
            		while(rs.next())
            		{
            			stenen.get(i).setStone(new Stone(rs.getString("LetterType_karakter").charAt(0), rs.getInt("ID")));
            			i++;
            		}
            	}
            	catch(SQLException io)
            	{
            		io.printStackTrace();
            	}
            	con.closeConnection();
            }
          
            public ArrayList<Tile> getTiles()
            {
                    return stenen;
            }
           
            public Tile getTile(int i)
            {
                    return stenen.get(i);
            }
            
            public int getTilesInLetterbak()
            {
            	int numTiles = 0;
        		Connectie con = new Connectie();
        		try
        		{
        			ResultSet rs = con.doSelect("SELECT COUNT(*) FROM letterbakjeletter WHERE Spel_ID = %1$d AND Beurt_ID = (SELECT MAX(ID) FROM beurt WHERE Account_naam = '%2$s' AND Spel_ID = %1$d);", curSpel.getSpelId(), Account.getAccountNaam());
        		
        			if(rs.next())
        			{
        				numTiles = rs.getInt(1);
        			}
        		} 
        		catch (SQLException e) 
        		{
        			e.printStackTrace();
        		}
        		con.closeConnection();
        		return numTiles;
            }
           
            public int getTilesLeftInPot()
        	{
        		int numTiles = 0;
        		Connectie con = new Connectie();
        		try
        		{
        			ResultSet rs = con.voerSelectQueryUit("SELECT COUNT(*) FROM pot");
        		
        			if(rs.next())
        			{
        				numTiles = rs.getInt(1);
        			}
        		} 
        		catch (SQLException e) 
        		{
        			e.printStackTrace();
        		}
        		con.closeConnection();
        		return numTiles;
        	}
            
            public int getNumberOfStones()
            {
                    int numStones = 0;
                    for(int i = 0; i < 7; i++)
                    {
                            if(getTile(i).getStone() != null)
                            {
                                    numStones++;
                            }
                    }
                    return numStones;
            }
           
            public void lockButtons(){
            	//locked buttons als er nog tiles op het bord liggen die niet zijn gespeeld
            	if(getNumberOfStones() < getTilesInLetterbak()){
            		curSpel.getSpelPanel().getSwapButton().setEnabled(false);
            		curSpel.getSpelPanel().getSkipButton().setEnabled(false);
            		curSpel.getSpelPanel().getPlayButton().setEnabled(true);
            		curSpel.getSpelPanel().getClearButton().setEnabled(true);
            	} else{
            		curSpel.getSpelPanel().getSwapButton().setEnabled(true);
            		curSpel.getSpelPanel().getSkipButton().setEnabled(true);
            		curSpel.getSpelPanel().getPlayButton().setEnabled(false);
            		curSpel.getSpelPanel().getClearButton().setEnabled(false);
            	}
            }

			public boolean isObserver() 
			{
				return bObserver;
			}
    }

