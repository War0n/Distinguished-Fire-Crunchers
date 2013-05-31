package wordfeud;

public class Tile {
	// privates <:
	private TileType type;
	private Stone steen;

	// Constructor
	public Tile(TileType type) {
		this.type = type;
		steen = null;
	}

	public Tile() {
		this.type = TileType.TYPE_NONE;
		steen = null;
	}

	// Getters en setters
	public Stone getStone() {
		return steen;
	}

	public void setStone(Stone steen) {
		this.steen = steen;
	}

	public TileType getType() {
		return type;
	}

	public void setType(TileType type) {
		this.type = type;
	}

	// Speciale Tile Types.
	public boolean isDoubleLetter() {
		return type.equals(TileType.TYPE_DL);
	}

	public boolean isTripleLetter() {
		return type.equals(TileType.TYPE_TL);
	}

	public boolean isDoubleWord() {
		return type.equals(TileType.TYPE_DW);
	}

	public boolean isTripleWord() {
		return type.equals(TileType.TYPE_TW);
	}

	public boolean isStart() {
		return type.equals(TileType.TYPE_START);
	}

	// locked
	public boolean getLocked() {
		if (steen != null) {
			return steen.getLocked();
		}
		return false;
	}

	public void setLocked(boolean locked) {
		if (steen != null) {
			steen.setLocked(locked);
		}
	}
}
