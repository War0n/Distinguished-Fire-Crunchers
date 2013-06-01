package wordfeud;

public class MainClass
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.setProperty("file.encoding", "UTF-8");
		//start programma
		try{
			GUI WFGui = new GUI();
			WFGui.setVisible(true);
		}
		catch(Exception e){
			System.out.println("Er is iets verkeerd gegaan, controleer de internet verbinding en herstart het programma.\n___\n" + e);
		}
	}

}
