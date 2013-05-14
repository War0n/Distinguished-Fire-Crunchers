package wordfeud;

public class Account {

	private static String accountNaam;
	
	public  Account(String naam)
	{
		accountNaam = naam;
	}
	
	public static String getAccountNaam(){
		return accountNaam;
	}
	
}
