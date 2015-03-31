package com.pirateseas.utils.approach2d;

import java.util.ArrayList;
import java.util.List;

import com.pirateseas.model.canvasmodel.game.objects.Item;

public class ItemManager{
	
	private List<Item> itemList;
	
	public ItemManager(){
		itemList = new ArrayList<Item>();
	}
	
	public void registerItem(Item newItem){
		itemList.add(newItem);
	}
	
	
	
}