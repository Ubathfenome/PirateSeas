package com.pirateseas.model.canvasmodel.game.objects;

import java.util.List;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class ShopActivity extends Activity{
	
	private List<Item> itemList;
	
	public void onActivity(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		//setContentView(R.layout.activity_shop);
		
		Intent data = getIntent();
		String shopNature = data.getExtras().getString(Constants.ITEMLIST_NATURE, Constants.EMPTY_STRING);
		
		if(shopNature.equals(Constants.SHOP_NATURE)){
			// TODO An ordered list
		} else if (shopNature.equals(Constants.TREASURE_NATURE)){
			// TODO A random list
		}
	}
	
	public void purchaseItem(Item itemPurchased){
		// TODO Add item effects
		// TODO Take item price from player's gold stash
	}
	
}