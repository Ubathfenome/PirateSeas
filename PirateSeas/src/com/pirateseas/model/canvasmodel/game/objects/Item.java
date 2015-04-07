package com.pirateseas.model.canvasmodel.game.objects;

import java.util.Comparator;

public class Item implements Comparable<Item> {
	
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
	
	public int compareTo(Item other){
		if (level > other.level)
			return 1;
		else if (level == other.level)
			return 0;
		else
			return -1;
	}
	
	public static Comparator<Item> ItemComparator = new Comparator<Item>(){
		public int compare(Item item1, Item item2){
			if (item1.price > item2.price)
				return 1;
			else if (item1.price == item2.price)
				return 0;
			else
				return -1;
		}
	};

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the level
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * @param level the level to set
	 */
	public void setLevel(int level) {
		this.level = level;
	}

	/**
	 * @return the price
	 */
	public int getPrice() {
		return price;
	}

	/**
	 * @param price the price to set
	 */
	public void setPrice(int price) {
		this.price = price;
	}

	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}

	/**
	 * @return the recommendedLevel
	 */
	public int getRecommendedLevel() {
		return recommendedLevel;
	}

	@Override
	public String toString() {
		return "Item [id=" + id + ", name=" + name + ", description="
				+ description + ", recommendedLevel=" + recommendedLevel
				+ ", level=" + level + ", price=" + price + "]";
	}
	
	
}