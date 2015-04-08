package com.pirateseas.model.canvasmodel.game.objects;

import java.util.List;

import com.pirateseas.R;
import com.pirateseas.global.Constants;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
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
	
	List<Item> itemList;
	
	public void onActivity(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_shop);
		
		Intent data = getIntent();
		mNature = data.getExtras().getString(Constants.ITEMLIST_NATURE, Constants.EMPTY_STRING);
		
		ItemLoader loader = new ItemLoader();
		
		// TODO Display items depending on their recommended level over the player level
		if(mNature.equals(Constants.NATURE_SHOP)){
			// An ordered list of items with their price
			itemList = loader.loadDefault();
		} else if (mNature.equals(Constants.NATURE_TREASURE)){
			// A random list of free items
			itemList = loader.loadRandom();
		}
		
		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
		    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				// TODO Are you sure you want to buy this?
				// Affirmative: purchaseItem()
				// Negative: Nothing
		        return true;
		    }
		});
	}
	
	public void purchaseItem(Item itemPurchased){
		// TODO Add item effects
		// TODO Take item price from player's gold stash
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Update item description on TextView
	}
	
}