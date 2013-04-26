import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.JPanel;


public class Bord extends JPanel{
	
	private HashMap<Integer,Tile> tegels;
	private ArrayList<Integer> tripleWordValues;
	private ArrayList<Integer> doubleWordValues;
	private ArrayList<Integer> tripleCharValues;
	private ArrayList<Integer> doubleCharValues;
	
	public Bord() {
		setLayout(new GridLayout(15,15,2,2));
		setBackground(new Color(23,26,30));
		tripleWordValues = new ArrayList<Integer>();
		doubleWordValues = new ArrayList<Integer>();
		tripleCharValues = new ArrayList<Integer>();
		doubleCharValues = new ArrayList<Integer>();
		
		tegels = new HashMap<Integer,Tile>();
		initSpecialTiles();
		Tile addTile = new Tile(null,null);
		for (int i = 1; i<=225;i++){
			if(tripleWordValues.contains(i)){
				addTile = new Tile(new Color(156,71,26),"TW");
			}else if(doubleWordValues.contains(i)){
				addTile = new Tile(new Color(192,121,33),"DW");
			}else if(tripleCharValues.contains(i)){
				addTile = new Tile(new Color(29,144,160),"TL");
			}else if(doubleCharValues.contains(i)){
				addTile = new Tile(new Color(122,162,113),"DL");
			}else if(i == 113){
				addTile = new Tile(new Color(97,65,93), "\u2605");
			} else{
				addTile = new Tile(new Color(44,47,53), "");
			}
			
			tegels.put(i,addTile);
			add(addTile);
			System.out.println("Added tile " + i);
		}		
	}
	
	private void initSpecialTiles(){
		//TW
		tripleWordValues.add(5);
		tripleWordValues.add(11);
		tripleWordValues.add(61);
		tripleWordValues.add(75);
		tripleWordValues.add(151);
		tripleWordValues.add(165);
		tripleWordValues.add(215);
		tripleWordValues.add(221);
		
		//DW
		doubleWordValues.add(33);
		doubleWordValues.add(43);
		doubleWordValues.add(53);
		doubleWordValues.add(65);
		doubleWordValues.add(71);
		doubleWordValues.add(109);
		doubleWordValues.add(117);
		doubleWordValues.add(155);
		doubleWordValues.add(161);
		doubleWordValues.add(173);
		doubleWordValues.add(183);
		doubleWordValues.add(193);
		
		//TL
		tripleCharValues.add(1);
		tripleCharValues.add(15);
		tripleCharValues.add(21);
		tripleCharValues.add(25);
		tripleCharValues.add(49);
		tripleCharValues.add(57);
		tripleCharValues.add(77);
		tripleCharValues.add(81);
		tripleCharValues.add(85);
		tripleCharValues.add(89);
		tripleCharValues.add(137);
		tripleCharValues.add(141);
		tripleCharValues.add(145);
		tripleCharValues.add(149);
		tripleCharValues.add(169);
		tripleCharValues.add(177);
		tripleCharValues.add(201);
		tripleCharValues.add(205);
		tripleCharValues.add(211);
		tripleCharValues.add(225);
		
		//DL
		doubleCharValues.add(8);
		doubleCharValues.add(17);
		doubleCharValues.add(29);
		doubleCharValues.add(37);
		doubleCharValues.add(39);
		doubleCharValues.add(67);
		doubleCharValues.add(69);
		doubleCharValues.add(93);
		doubleCharValues.add(95);
		doubleCharValues.add(101);
		doubleCharValues.add(103);
		doubleCharValues.add(106);
		doubleCharValues.add(120);
		doubleCharValues.add(123);
		doubleCharValues.add(125);
		doubleCharValues.add(131);
		doubleCharValues.add(133);
		doubleCharValues.add(157);
		doubleCharValues.add(159);
		doubleCharValues.add(187);
		doubleCharValues.add(189);
		doubleCharValues.add(197);
		doubleCharValues.add(209);
		doubleCharValues.add(218);
		
	}

}
