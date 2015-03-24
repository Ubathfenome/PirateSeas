package com.pirateseas.model.canvasmodel.game.objects;

public class Item {
	
	private int id;
	private String name;
	private String description;
	private int recommendedLevel;
	private int level;
	private int price;
	
	public Item(String name, String description, int level, int price){
		this.id = name.hashCode();
		this.name = name;
		this.description = description;
		this.level = level;
		this.recommendedLevel = level - 1;
		this.price = price;
	}
	
	private class LevelComparator implements Comparable<Item>{
		@Override
		public int compareTo(Item other) {
			if (level > other.level)
				return 1;
			else if (level == other.level)
				return 0;
			else
				return -1;
		}
	}
	
	private class PriceComparator implements Comparable<Item>{
		@Override
		public int compareTo(Item other) {
			if (price > other.price)
				return 1;
			else if (price == other.price)
				return 0;
			else
				return -1;
		}
	}
	
}