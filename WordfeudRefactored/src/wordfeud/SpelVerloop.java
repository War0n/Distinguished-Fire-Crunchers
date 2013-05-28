package wordfeud;

import java.util.ArrayList;

public class SpelVerloop {
	private Spel spel;
	private ArrayList<Tile> newTiles;
	private ArrayList<ArrayList<Tile>> woordenLijst;
	
	public SpelVerloop(Spel spel){
		this.spel = spel;
		newTiles = spel.getBord().getNewTiles();
		woordenLijst = new ArrayList<ArrayList<Tile>>();
	}
	
	
	public void puntenTeller() {
		int score = 0;
		for (int i = 0; i < woordenLijst.size(); i++) {
			for (int ii = 0; ii < woordenLijst.get(i).size(); ii++) {
				Tile currTile = (Tile) woordenLijst.get(i).get(ii);
				if (currTile.isDoubleLetter()) {
					score = score
							+ (currTile.getStone().getValue(
									new LetterSet("NL")) * 2);
				} else if (currTile.isTripleLetter()) {
					score = score
							+ (currTile.getStone().getValue(spel.getLetterSet()) * 3);
				} else if (currTile.isDoubleWord()) {
					score = (score + currTile.getStone().getValue(spel.getLetterSet()) * 2);
				} else if (currTile.isTripleWord()) {
					score = (score + currTile.getStone().getValue(spel.getLetterSet()) * 3);
				}
			}
		}
	}

	public ArrayList<ArrayList<Tile>> vindWoord() {

		Tile currentTile = newTiles.get(0);
		woordenLijst.add(new ArrayList<Tile>());
		boolean done = false;
		int index = 0;

		// Is er wel gelegd?
		if (newTiles.size() == 0) {
			return null;
		}
		// Is er maar een letter gelegd?
		if (newTiles.size() == 1) {
			if (spel.getBord().getCoordinat(newTiles.get(0))[0] == 7
					&& spel.getBord().getCoordinat(newTiles.get(0))[1] == 7) {
				woordenLijst.get(0).add(newTiles.get(0));
			} else {
				return null;
			}
		}

		// Woorden zoeken
		while (!done) {
			if (!nextTile(currentTile, 'u').equals(null)) {
				currentTile = nextTile(currentTile, 'u');
			} else {
				done = true;
			}
		}
		done = false;
		while (!done) {
			woordenLijst.get(index).add(currentTile);
			if (!nextTile(currentTile, 'd').equals(null)) {
				if (!currentTile.equals(newTiles.get(0))) {
					if (!nextTile(currentTile, 'l').equals(null)
							|| !nextTile(currentTile, 'r').equals(null)
							&& currentTile.getNieuwGelegd()) {
						currentTile = nextTile(currentTile, 'l');
						boolean done2 = false;
						while (!done2) {
							if (!nextTile(currentTile, 'l').equals(null)) {
								currentTile = nextTile(currentTile, 'l');
							} else {
								done2 = true;
							}
							done2 = false;
						}
						woordenLijst.add(woordenLijst.size(), new ArrayList<Tile>());
						while (!done2) {
							if (!nextTile(currentTile, 'r').equals(null)) {
								woordenLijst.get(woordenLijst.size() - 1).add(
										currentTile);
								currentTile = nextTile(currentTile, 'r');
							} else {
								done2 = true;
							}
						}
						currentTile = (Tile) woordenLijst.get(index).get(
								woordenLijst.size());
					}
				}
				currentTile = nextTile(currentTile, 'd');
			} else {
				done = true;
			}
			done = false;
			while (!done) {
				if (!nextTile(currentTile, 'l').equals(null)) {
					currentTile = nextTile(currentTile, 'l');
				} else {
					done = true;
				}
			}
		}
		done = false;
		while (!done) {
			woordenLijst.get(index).add(currentTile);
			if (!nextTile(currentTile, 'r').equals(null)) {
				if (!currentTile.equals(newTiles.get(0))) {
					if (!nextTile(currentTile, 'u').equals(null)
							|| !nextTile(currentTile, 'd').equals(null)
							&& currentTile.getNieuwGelegd()) {
						currentTile = nextTile(currentTile, 'u');
						boolean done2 = false;
						while (!done2) {
							if (!nextTile(currentTile, 'u').equals(null)) {
								currentTile = nextTile(currentTile, 'u');
							} else {
								done2 = true;
							}
							done2 = false;
						}
						woordenLijst.add(woordenLijst.size(), new ArrayList<Tile>());
						while (!done2) {
							if (!nextTile(currentTile, 'd').equals(null)) {
								woordenLijst.get(woordenLijst.size() - 1).add(
										currentTile);
								currentTile = nextTile(currentTile, 'd');
							} else {
								done2 = true;
							}
						}
						currentTile = (Tile) woordenLijst.get(index).get(
								woordenLijst.size());
					}
				}
				currentTile = nextTile(currentTile, 'r');
			} else {
				done = true;
			}
		}
		// controleer of er geen of dubbelwoorden gaten zijn.
		int count = 0;
		for (ArrayList<Tile> woord : woordenLijst) {
			for (Tile tile : woord) {
				for (Tile newTile : newTiles) {
					if (newTile.equals(tile)) {
						count++;
					}
				}
			}
		}
		if (count != newTiles.size()) {
			return null;
		}
		// Is dit het eerste woord?
		boolean check = false;
		boolean start = false;
		for (ArrayList<Tile> woord : woordenLijst) {
			for (Tile tile : woord) {
				if (!tile.getNieuwGelegd()) {
					check = true;
				} else {
					if (spel.getBord().getCoordinat(tile)[0] == 7
							&& spel.getBord().getCoordinat(tile)[1] == 7) {
						start = true;
					}
				}
			}
		}
		if (check) {
			if (start) {
				return woordenLijst;
			} else {
				return null;
			}
		}
		return woordenLijst;
	}

	private Tile nextTile(Tile tile, char direction) {
		int[] coor = spel.getBord().getCoordinat(tile);
		switch (direction) {
		case 'u':
			if (!spel.getBord().getTile(coor[0], coor[1] + 1).getStone().equals(null)) {
				return spel.getBord().getTile(coor[0], coor[1] + 1);
			}
			break;
		case 'd':
			if (!spel.getBord().getTile(coor[0], coor[1] - 1).getStone().equals(null)) {
				return spel.getBord().getTile(coor[0], coor[1] - 1);
			}
			break;
		case 'l':
			if (!spel.getBord().getTile(coor[0] - 1, coor[1]).getStone().equals(null)) {
				return spel.getBord().getTile(coor[0] - 1, coor[1]);
			}
			break;
		case 'r':
			if (!spel.getBord().getTile(coor[0] + 1, coor[1]).getStone().equals(null)) {
				return spel.getBord().getTile(coor[0] + 1, coor[1]);
			}
			break;
		default:
		}
		return null;
	}
}
