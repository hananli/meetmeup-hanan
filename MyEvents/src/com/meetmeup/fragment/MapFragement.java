package com.meetmeup.fragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.PlaceAdapter;
import com.meetmeup.asynch.SerachNearByLocationTask;
import com.meetmeup.bean.Place;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;

//This class is Selecting location for the event.firstly it will show some nearby location of the user,and also user can serch by tag,whatever he want location.
public class MapFragement extends Fragment {

	EditText location_searchtxt;
	ListView location_lv;
	EditText search;
	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	ProgressDialog progress;

	// This method is used for instantiating class object.
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new MapFragement();
		}
		return mfFragment;
	}

	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("Select Location", -2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
			// DashBoard.chatIcon.setVisibility(View.GONE);
		}
		location_searchtxt.setText("");
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.location_view, container, false);
		initializeView(view);
		setOnClickeListeners();
		Utill.hideSoftKeyboard(mActivity);
		searchByLoc();
		return view;
	}

	// This method is used for initializing view.
	void initializeView(View view) {
		location_searchtxt = (EditText) view.findViewById(R.id.enter_text);
		location_lv = (ListView) view.findViewById(R.id.list);
		search = (EditText) view.findViewById(R.id.search);
	}

	// This method is used for set Click onClick listenr to all the Views.
	public void setOnClickeListeners() {
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
		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentManager.getBackStackEntryCount() > 0) {
					mFragmentManager.popBackStack();
				}
			}
		});
	}

	public static Activity mActivity;

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}

	// This method is used for serching location accorging to user,s current
	// location.
	public void searchByLoc() {
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
		/*
		 * new SerachNearByLocationTask(mContext, requestParams, new
		 * NearPlacesListener(), 2).execute();
		 */
	}

	// This is the listener class which is used for notifying,is nearPlacess
	// successfully got or not.
	public class NearPlacesListener {
		public void onSuccess(final ArrayList<Place> nearbyList) {
			Utill.hideSoftKeyboard(mActivity);
			if (nearbyList.size() > 0) {
				location_lv.setAdapter(new PlaceAdapter(mContext, nearbyList));

			}
			hideProgress();
			location_lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
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
					CreateEventFragement.eventLat = new String(""
							+ nearbyList.get(position).getLat());
					CreateEventFragement.eventLong = new String(""
							+ nearbyList.get(position).getLonge());

					Intent intent = new Intent();
					intent.putExtra("address", fullAddress);
					intent.putExtra("lat", ""
							+ nearbyList.get(position).getLat());
					intent.putExtra("lng", ""
							+ nearbyList.get(position).getLonge());
					// intent.putExtra("country",
					// addresses.get(0).getCountryName());
					// intent.putExtra("state",
					// addresses.get(0).getAdminArea());
					// intent.putExtra("city",addresses.get(0).getSubAdminArea());
					if (mFragmentManager.getBackStackEntryCount() > 0) {
						mFragmentManager.popBackStack();
					}
					intent.putExtra("country", "India");
					intent.putExtra("state", "MP");
					intent.putExtra("city", "Indore");

					// setResult(13, intent);
					// finish();
					// }
					// } catch (IOException e) {
					// e.printStackTrace();
					// }

				}
			});

		}

		public void onError(String message) {
			hideProgress();
			Utill.hideSoftKeyboard(mActivity);
		}
	}

	// This is the handle object,used for sending request occording to location
	// Tag.
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			String search = msg.getData().getString("search");
			searchByText(search);
		};
	};

	// This method is used for getting location's according to Tags.
	public void searchByText(String searchTag) {
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
			/*
			 * new SerachNearByLocationTask(mContext, requestParams, new
			 * NearPlacesListener(), 1).execute();
			 */
		} else {
			Utill.showNetworkError(mContext);
		}
	}

	// This method is used for showing progress bar.
	public void showProgress() {
		try {
			if (progress == null)
				progress = new ProgressDialog(mActivity);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				progress = new ProgressDialog(mActivity);
				progress.setMessage("Please Wait..");
				progress.setCancelable(false);
				progress.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// This method is used for hiding progress bar.
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}
}
