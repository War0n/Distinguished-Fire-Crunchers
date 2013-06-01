	

    package wordfeud;
     
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.util.ArrayList;
     
    public class Letterbak
    {
            private ArrayList<Tile> stenen;
            private Spel curSpel;
            private int beurt;
           
            public Letterbak(Spel currentSpel)
            {
                    stenen = new ArrayList<Tile>();
                    curSpel = currentSpel;
                    int spelId = curSpel.getSpelId();
                    beurt = curSpel.getVerloop().getBeurt();
                    for(int i = 0; i < 7; i++)
                    {
                            stenen.add(new Tile());
                            //stenen.get(i).setStone(new Stone('A', i));
                    }
                    Connectie con = new Connectie();
                    ResultSet rs = con.doSelect("SELECT lb.Spel_ID, lb.Beurt_ID, l.ID, l.LetterType_LetterSet_code, l.LetterType_karakter FROM rcollard_db2.letterbakjeletter AS lb JOIN letter as l ON lb.Letter_ID = l.ID AND lb.Spel_ID = l.Spel_ID WHERE lb.Spel_ID = %1$d AND lb.Beurt_ID = (SELECT MAX(ID) FROM Beurt WHERE Spel_ID = %1$d AND Account_Naam = '%2$s') LIMIT 7;", spelId, Account.getAccountNaam());
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
           
            //public void
    }

