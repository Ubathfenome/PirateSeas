package com.pirateseas.model.canvasmodel.game.objects;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemLoader{
	
	List<Item> itemList, defaultList;
	List<Item> levelOne = new ArrayList<Item>();
	List<Item> levelTwo = new ArrayList<Item>();
	List<Item> levelThree = new ArrayList<Item>();
	
	private int[] levelProbabilities;
	
	Item crew;
	Item repairman;
	Item nest;
	Item materials;
	Item mapPiece;
	Item map;
	Item blackPowder;
	Item valuable;
	
	private static final float ITEM_TIER1_PERCENT = 80;
	private static final float ITEM_TIER2_PERCENT = 15;
	private static final float ITEM_TIER3_PERCENT = 5;
	
	public ItemLoader(){
		itemList = new ArrayList<Item>();
		
		crew = new Item("Crew", "Restores 5 health points", 1, 5);
		repairman = new Item("Repairman", "Restores 15 health points", 1, 15);
		nest = new Item("Nest", "Increases the effective range by 1.15", 3, 35);
		materials = new Item("Materials", "Increases the max health in 10 units", 2, 25);
		mapPiece = new Item("Map Piece", "1/6 th section of a map", 2, 15);
		map = new Item("Map", "Shows the closest treasure", 3, 40);
		blackPowder = new Item("BlackPowder", "Increases the firepower by 0.5", 3, 35);
		
		defaultList = loadAll();
		
		levelProbabilities = new int[100];
		for(int i = 0; i < 100; i++){
			if(i % ITEM_TIER1_PERCENT == 0)
				levelProbabilities[i] = 1;
			else if (i % ITEM_TIER2_PERCENT == 0)
				levelProbabilities[i] = 2;
			else if (i % ITEM_TIER3_PERCENT == 0)
				levelProbabilities[i] = 3;
		}
	}
	
	public List<Item> loadAll(){
		itemList.clear();
		
		itemList.add(crew);
		itemList.add(repairman);
		itemList.add(nest);
		itemList.add(materials);
		itemList.add(mapPiece);
		itemList.add(map);
		itemList.add(blackPowder);
		
		Arrays.sort(itemList.toArray());
		
		return itemList;
	}
	
	public List<Item> loadEmpty(){
		itemList.clear();
		return itemList;
	}
	
	public List<Item> loadDefault(){
		itemList.clear();
		itemList.add(crew);
		return itemList;
	}
	
	public List<Item> loadRandom(){		
		for(Item item : defaultList){
			if(item.getLevel() == 1)
				levelOne.add(item);
			else if (item.getLevel() == 2)
				levelTwo.add(item);
			else
				levelThree.add(item);
		}
		
		itemList.clear();
		
		itemList.add(getRandomItem());
		itemList.add(getRandomItem());
		
		Arrays.sort(itemList.toArray());
		
		return itemList;
	}
	
	private Item getRandomItem(){
		int randomProbability = (int) Math.random() * 100;
		Item item = null;
		
		switch(levelProbabilities[randomProbability]){
			case 1:
				item = levelOne.get((int) Math.random() * (levelOne.size() - 1));
				break;
			case 2:
				item = levelTwo.get((int) Math.random() * (levelTwo.size() - 1));
				break;
			case 3:
				item = levelThree.get((int) Math.random() * (levelThree.size() - 1));
				break;
		}
		
		return item;
	}
}