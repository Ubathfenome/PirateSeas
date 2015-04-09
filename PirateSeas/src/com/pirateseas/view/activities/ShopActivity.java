package com.pirateseas.view.activities;

import java.util.List;

import com.pirateseas.R;
import com.pirateseas.controller.androidGameAPI.Player;
import com.pirateseas.exceptions.NotEnoughGoldException;
import com.pirateseas.exceptions.SaveGameException;
import com.pirateseas.global.Constants;
import com.pirateseas.model.canvasmodel.game.entity.Ship;
import com.pirateseas.model.canvasmodel.game.objects.Item;
import com.pirateseas.model.canvasmodel.game.objects.ItemLoader;
import com.pirateseas.utils.approach2d.GameHelper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * @author p7166421
 * @see: http://developer.android.com/guide/topics/ui/layout/listview.html
 * @see: http://developer.android.com/reference/android/app/ListActivity.html
 *
 */
public class ShopActivity extends ListActivity{
	private static final String TAG = "ShopActivity";
	
	String mNature = "";
	
	Player dummyPlayer;
	Ship dummyShip;
	
	List<Item> itemList;
	ListView listView;
	ListAdapter mAdapter;
	
	TextView txtDescription;
	
	Button btnAcceptAll;
	Button btnCancel;
	
	public void onActivity(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_shop);
		
		Intent data = getIntent();
		mNature = data.getExtras().getString(Constants.ITEMLIST_NATURE, Constants.EMPTY_STRING);
		
		ItemLoader loader = new ItemLoader();
		
		GameHelper.loadGame(this, dummyPlayer, dummyShip);
		dummyPlayer = GameHelper.helperPlayer;
		dummyShip = GameHelper.helperShip;
		
		if(mNature.equals(Constants.NATURE_SHOP)){
			// An ordered list of items with their price
			itemList = loader.loadDefault(dummyPlayer.getLevel());
		} else if (mNature.equals(Constants.NATURE_TREASURE)){
			// A random list of free items
			itemList = loader.loadRandom();
		}
		
		listView = (ListView) findViewById(R.id.listItems);
		
		// Assign loaded itemList to ListView Adapter
		mAdapter = new ArrayAdapter<Item>(this, R.layout.list_item_layout, itemList);
		listView.setAdapter(mAdapter);
				
		this.getListView().setLongClickable(true);
		this.getListView().setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
		    public boolean onItemLongClick(AdapterView<?> parent, View v, int position, long id) {
				// Are you sure you want to buy this?
				// Affirmative: Purchase Item
				// Negative: Nothing
				PurchaseItemDialogFragment purchaseDialog = new PurchaseItemDialogFragment();
				purchaseDialog.show(getFragmentManager(), "ConfirmItemBuyDialog");
				
		        return true;
		    }
		});
		
		txtDescription = (TextView) findViewById(R.id.txtItemDescription);
		
		// Accept all
		btnAcceptAll = (Button) findViewById(R.id.btnReceiveAll);
		if(mNature.equals(Constants.NATURE_SHOP))
			btnAcceptAll.setVisibility(View.GONE);
		btnAcceptAll.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				for(int i = 0, size = itemList.size(); i < size; i++){
					purchaseItem(itemList.get(i));
				}				
			}
		});
		
		// Cancel
		btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!itemList.isEmpty()){
					LeaveActivityDialogFragment exitShopDialog = new LeaveActivityDialogFragment();
					exitShopDialog.show(getFragmentManager(), "ExitShopDialog");
				} else {
					finish();
				}
				
			}
		});
	}
	
	public void purchaseItem(Item itemPurchased){
		// Take item price from player's gold stash
		try {
			dummyPlayer.useGold(this, itemPurchased.getPrice());
			
			// Add item effects
			switch(itemPurchased.getName()){
				case "Crew":
					dummyShip.gainHealth(5);
					break;
				case "Repairman":
					dummyShip.gainHealth(15);
					break;
				case "Nest":
					dummyShip.addRange(1.15f);
					break;
				case "Materials":
					dummyShip.setMaxHealth(dummyShip.getMaxHealth() + 10);
					break;
				case "Map Piece":
					dummyPlayer.addMapPiece();
					break;
				case "Map":
					dummyPlayer.giveCompleteMap(true);
					break;
				case "BlackPowder":
					dummyShip.addPower(0.5f);
					break;
				case "Valuable":
					dummyPlayer.addGold(100);
					break;
			}
			
			// Remove item from itemList
			itemList.remove(itemPurchased);
		} catch (NotEnoughGoldException e) {
			Log.e(TAG, e.getMessage());
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
		}
	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// Update item description on TextView
		txtDescription.setText(itemList.get(position).getDescription());
	}
	
	private class LeaveActivityDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Activity dummyActivity = getActivity();
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
			builder.setTitle(
					getResources().getString(R.string.exit_dialog_title))
					.setMessage(R.string.exit_shop_dialog_message)
					.setPositiveButton(R.string.exit_dialog_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									if (GameHelper.saveGame(dummyActivity, dummyPlayer, dummyShip))
										Log.v(TAG, "Game saved");
									else
										try {
											throw new SaveGameException(getResources().getString(
													R.string.exception_save));
										} catch (NotFoundException e) {
											Log.e(TAG, e.getMessage());
										} catch (SaveGameException e) {
											Log.e(TAG, e.getMessage());
											Toast.makeText(dummyActivity, e.getMessage(), Toast.LENGTH_SHORT).show();
										}
									dummyActivity.finish();
								}
							})
					.setNegativeButton(R.string.exit_dialog_negative,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancels the dialog
								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
	
	private class PurchaseItemDialogFragment extends DialogFragment {
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			final Activity dummyActivity = getActivity();
			// Use the Builder class for convenient dialog construction
			AlertDialog.Builder builder = new AlertDialog.Builder(dummyActivity);
			builder.setTitle(
					getResources().getString(R.string.shop_purchase_dialog_title))
					.setMessage(R.string.shop_purchase_dialog_message)
					.setPositiveButton(R.string.shop_purchase_dialog_positive,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									purchaseItem(null);
								}
							})
					.setNegativeButton(R.string.exit_dialog_negative,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int id) {
									// User cancels the dialog
								}
							});
			// Create the AlertDialog object and return it
			return builder.create();
		}
	}
	
}