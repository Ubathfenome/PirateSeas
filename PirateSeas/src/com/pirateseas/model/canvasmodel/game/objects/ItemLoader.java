package com.pirateseas.model.canvasmodel.game.objects;

import java.util.ArrayList;
import java.util.List;

public class ItemLoader{
	
	List<Item> itemList;
	
	public ItemLoader(){
		itemList = new ArrayList<Item>();
	}
	
	public List<Item> loadDefault(){
		// TODO
		/*
		Item repairman = new Item();
		Item nest = new Item();
		Item materials = new Item();
		Item mapPiece = new Item();
		Item blackPowder = new Item();
		*/
		return itemList;
	}
	
	
}