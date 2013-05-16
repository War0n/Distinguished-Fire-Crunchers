package wordfeud;

public class MainClass
{

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		System.setProperty("file.encoding", "UTF-8");
		//start program
		GUI WFGui = new GUI();
		WFGui.setVisible(true);
	}

}
