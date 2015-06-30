package com.meetmeup.activity;

import java.util.ArrayList;

import com.meetmeup.adapters.PlaceAdapter;
import com.meetmeup.adapters.PlacesAutoCompleteAdapter;
import com.meetmeup.asynch.SerachNearByLocationTask;
import com.meetmeup.bean.Place;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

//This class is used for Selecting address by nearby or search by tag.

public class SelectAddressBYTagActivity extends FragmentActivity {

	AutoCompleteTextView location_searchtxt;
	ListView location_lv;
	EditText search;
	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	ProgressDialog progress;
	LinearLayout backLinear;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			mContext = getApplicationContext();
			setContentView(R.layout.location_view);
			initializeView();
			setOnClickeListeners();
			Utill.hideSoftKeyboard(this);
			searchByLoc();
		
	}
	//This method is used for initializing view.
	void initializeView() {
		
		location_searchtxt = (AutoCompleteTextView) findViewById(R.id.enter_text);
		
		location_searchtxt.setAdapter(new PlacesAutoCompleteAdapter(mContext,R.id.tvName));
		
		location_searchtxt.setOnItemClickListener(new OnItemClickListener() {
		    public void onItemClick(AdapterView<?> parent, View view, int position, long rowId) {
		      
		    	Log.i("autocompleteTextAtposition", ""+((Place)parent.getItemAtPosition(position)).getFormattendAddress());
		    		   
		    	location_searchtxt.setText(((Place)parent.getItemAtPosition(position)).getFormattendAddress());
		    
		    	if (location_searchtxt.getText().toString().length() != 0) {
					Message msgObj = handler.obtainMessage();
					Bundle b = new Bundle();
					b.putString("search",location_searchtxt.getText().toString().toString());
					msgObj.setData(b);
					handler.sendMessage(msgObj);
				}
		    	
		    }
		});
		
		
		location_lv = (ListView) findViewById(R.id.list);
		
//		location_lv.setAdapter(new PlacesAutoCompleteAdapter(mContext,R.id.tvName));
		
		search = (EditText) findViewById(R.id.search);
		backLinear = (LinearLayout) findViewById(R.id.backLinear);
		
		
	}
	//This method is used for set Click onClick listenr to all the Views.
	public void setOnClickeListeners() {
		backLinear.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
		search.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String location = location_searchtxt.getText().toString()
						.trim();
				if (location.length() != 0) {
					Message msgObj = handler.obtainMessage();
					Bundle b = new Bundle();
					b.putString("search", location.toString());
					msgObj.setData(b);
					handler.sendMessage(msgObj);
				}
			}
		});
		
	}

	

	//This method is used for serching location accorging to user,s current location.
	public void searchByLoc() {
	
		try {

			Double latitude = Double.parseDouble(AppConstants.getLattitude());// WelcomePageActivity.latitude;
			Double longitude = Double.parseDouble(AppConstants.getLogitude()); // WelcomePageActivity.longitude;
			// latitude = 22.719569;
			// longitude = 75.857726;
			String location = "location=" + latitude + "," + longitude;
			String radius = "radius=50000";
			String sensor = "sensor=true";
			// String types = "types=";
			String key = mContext.getResources().getString(
					R.string.key_for_google_places_api);// "AIzaSyANiRynL9Mbzr6aPZzsOG2gsOLib3Q7rFs";
			String apiKey = "key=" + key;

			String requestParams = location + "&" + radius + "&" + sensor + "&"
					+ apiKey;

			showProgress();

			new SerachNearByLocationTask(mContext, requestParams,
					new NearPlacesListener(), 2).execute();

		} catch (Exception e) {

			e.printStackTrace();

		}
	}

	
	//This is the listener class which is used for notifying,is nearPlacess successfully got or not.
	public class NearPlacesListener {
		public void onSuccess(final ArrayList<Place> nearbyList) {
		
			try{
			
			Utill.hideSoftKeyboard(SelectAddressBYTagActivity.this);
			
			if (nearbyList.size() > 0) {
				
				location_lv.setAdapter(new PlaceAdapter(mContext, nearbyList));
				

			}
			
			hideProgress();
			location_lv.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Place  place = nearbyList.get(position);
					Intent returnIntent = new Intent();
					returnIntent.putExtra("add", place.getName()+","+place.getFormattendAddress());
					returnIntent.putExtra("lat", place.getLat());
					returnIntent.putExtra("long",place.getLonge());
					setResult(RESULT_OK, returnIntent);
					finish();

				
					
					/*
					Geocoder gcd = new Geocoder(mContext, Locale.getDefault());
					List<Address> addresses;
					String fullAddress = "<html><head></head><body><h5>"
							+ nearbyList.get(position).getName()
							+ "</hr><br><h5>"
							+ nearbyList.get(position).getFormattendAddress()
							+ "</h5><br><h5>"
							+ nearbyList.get(position).getLat() + ","
							+ nearbyList.get(position).getLonge()
							+ "</h5></body></html>";
					
					CreateEventFragement.EventAddress = nearbyList
							.get(position).getFormattendAddress();
					CreateEventFragement.eventLat = new String(""+nearbyList.get(position).getLat()); 
					CreateEventFragement.eventLong = new String(""+nearbyList.get(position).getLonge());
					
					Intent intent = new Intent();
					intent.putExtra("address", fullAddress);
					intent.putExtra("lat", ""
							+ nearbyList.get(position).getLat());
					intent.putExtra("lng", ""
							+ nearbyList.get(position).getLonge());
					if (mFragmentManager.getBackStackEntryCount() > 0) {
						mFragmentManager.popBackStack();
					}
					intent.putExtra("country", "India");
					intent.putExtra("state", "MP");
					intent.putExtra("city", "Indore");
				*/}
				
				
			});

			}catch(Exception e){
				
				e.printStackTrace();
				
			}
		}

		public void onError(String message) {
			hideProgress();
			Utill.hideSoftKeyboard(SelectAddressBYTagActivity.this);
		}
	}

	
	//This is the handle object,used for sending request occording to location Tag.
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String search = msg.getData().getString("search");
			searchByText(search);
		};
	};

	
	//This method is used for getting location's according to Tags.
	public void searchByText(String searchTag) {

		try {

			Double latitude = 22.724014;// WelcomePageActivity.latitude;
			Double longitude = 75.888308; // WelcomePageActivity.longitude;

			String location = "location=" + latitude + "," + longitude;
			String radius = "radius=50000";
			String sensor = "sensor=true";
			String name = "query=" + searchTag.replace(" ", "+");
			// String keyword = "keyword="+searchTag.replace(" ", "+");
			String key = mContext.getResources().getString(
					R.string.key_for_google_places_api);// AIzaSyAA6Eoui8pu3cjSO-1IpL8W9_jgB-S8Jz0
														// //"AIzaSyANiRynL9Mbzr6aPZzsOG2gsOLib3Q7rFs";
			String apiKey = "key=" + key;
			// String rankby = "rankby=distance";
			String requestParams = location + "&" + radius + "&" + sensor + "&"
					+ name + "&" + apiKey; // "&"+keyword
			if (Utill.isNetworkAvailable(mContext)) {
				showProgress();
				new SerachNearByLocationTask(mContext, requestParams,
						new NearPlacesListener(), 1).execute();

			} else {

				Utill.showNetworkError(mContext);

			}

		} catch (Exception e) {

			e.printStackTrace();

		}
		
	}
	
	
	//This method is used for showing progress bar.
	public void showProgress() {
		try {
			if (progress == null)
				progress = new ProgressDialog(SelectAddressBYTagActivity.this);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				progress = new ProgressDialog(SelectAddressBYTagActivity.this);
				progress.setMessage("Please Wait..");
				progress.setCancelable(false);
				progress.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	//This method is used for hiding progress bar.
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

}
