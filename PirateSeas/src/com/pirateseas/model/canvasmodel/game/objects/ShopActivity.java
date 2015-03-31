package com.pirateseas.model.canvasmodel.game.objects;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

/**
 * 
 * @author p7166421
 * @see: http://developer.android.com/guide/topics/ui/layout/listview.html
 * @see: http://developer.android.com/reference/android/app/ListActivity.html
 *
 */
public class ShopActivity extends ListActivity{
	
	String mNature = "";
	
	public void onActivity(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_shop);
		
		Intent data = getIntent();
		mNature = data.getExtras().getString(Constants.ITEMLIST_NATURE, Constants.EMPTY_STRING);
		
		if(mNature.equals(Constants.SHOP_NATURE)){
			// TODO An ordered list
		} else if (mNature.equals(Constants.TREASURE_NATURE)){
			// TODO A random list
		}
	}
	
	public void purchaseItem(Item itemPurchased){
		// TODO Add item effects
		// TODO Take item price from player's gold stash
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		super.onListItemClick(l, v, position, id);
	}
	
}