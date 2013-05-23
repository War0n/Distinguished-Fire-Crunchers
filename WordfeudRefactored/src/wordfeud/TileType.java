package wordfeud;

import java.util.HashMap;

public enum TileType 
{
	TYPE_START("*"),
	TYPE_NONE("--"),
	TYPE_DL("DL"),
	TYPE_TL("TL"), 
	TYPE_DW("DW"), 
	TYPE_TW("TW");

	private static final HashMap<String, TileType> typesByValue = new HashMap<String, TileType>();

	static 
	{
		for (TileType type : TileType.values())
		{
			typesByValue.put(type.value, type);
		}
	}

	private final String value;

	private TileType(String value)
	{
		this.value = value;
	}

	public static TileType fromValue(String value)
	{
		return typesByValue.get(value);
	}
}
