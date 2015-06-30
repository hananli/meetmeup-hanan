package com.meetmeup.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Scroller;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetmeup.asynch.GetAddressByLatLongTask;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;

//This class is used for Choosing event address from the google map.

public class ChooseLocationActivity extends FragmentActivity {
	
	public String add = "Add Event Address";
	public double lat = 0;
	public double longi = 0;
	GoogleMap googleMap;
	MarkerOptions markerOptions;
	Marker m;
	static Activity mActivity;
	public static Context mContext;
	TextView Done, Cancel;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.choose_event_location);
			mActivity = this;
			Intent intent = getIntent();
			Done = (TextView) findViewById(R.id.done);
			Cancel = (TextView) findViewById(R.id.cancel);
			Done.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				
					Intent returnIntent = new Intent();
					returnIntent.putExtra("add", add);
					returnIntent.putExtra("lat", lat);
					returnIntent.putExtra("long", longi);
					setResult(RESULT_OK, returnIntent);
					finish();

				}
			});
			
			Cancel.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					finish();
					
				}
				
			});

			mContext = this;
			String Latti = intent.getStringExtra("lat");
			String Longi = intent.getStringExtra("lon");

			String title = "MeetMeUp";
			title = intent.getStringExtra("name");
			if (title != null)
				add = title;

			double Lat = Double.parseDouble(Latti);// 22.719569;
			double Lon = Double.parseDouble(Longi);// 75.857726;
								
			
			lat = Lat;
			longi = Lon;
			
			String urls = ""+lat+","+longi;
			new GetAddressByLatLongTask(mContext, urls, new GetAddressListener()).execute();
			 
			 
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			googleMap = supportMapFragment.getMap();
			LatLng latLng = new LatLng(Lat, Lon);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
			googleMap.animateCamera(cameraUpdate);
			googleMap.setMyLocationEnabled(true);
			markerOptions = new MarkerOptions().position(new LatLng(Lat, Lon)).title(add);
	    			
			
			Marker m = googleMap.addMarker(markerOptions);
//			m.showInfoWindow();

			googleMap.setOnMapClickListener(new OnMapClickListener() {
				
				@Override
				public void onMapClick(LatLng arg0) {
					
					googleMap.clear();
					markerOptions = new MarkerOptions().position(new LatLng(arg0.latitude, arg0.longitude)).title("MeetMeUP");
					
					markerOptions.title(add);
					Marker m = googleMap.addMarker(markerOptions);
					// gettingNormalAddress(arg0.latitude,arg0.longitude);
					m.showInfoWindow();
			//		Utill.showToast(getApplicationContext(), "" + arg0.latitude + " " + arg0.longitude);
					lat = arg0.latitude;
					longi = arg0.longitude;
					 String url = ""+lat+","+longi;
					 showProgress();
					 new GetAddressByLatLongTask(mContext, url, new GetAddressListener()).execute();
					// "http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+longi+"&sensor=true";
					// new getAddressAsync(mContext, new locationListener(),
					// url).execute();
					 
				}
				
			});
			
			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				@Override
				public boolean onMarkerClick(Marker arg0) {
					
					return false;
					
				}
				
			});
			
			googleMap.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {
				@Override
				public void onInfoWindowClick(Marker arg0) {
					
					address(arg0.getTitle());
					
				}
			});
		
		
		} catch (Exception e) {
			
			
       }
		
	}

	private void address(String title) {
		
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.address_popup);
		dialog.setTitle("Enter Event Address.");
		Button ok = (Button) dialog.findViewById(R.id.ok);
		final EditText AddressET = (EditText) dialog.findViewById(R.id.radiusET);
		AddressET.setInputType(InputType.TYPE_CLASS_TEXT);
		AddressET.setLines(3);
		AddressET.setText(title);
		AddressET.setScroller(new Scroller(mContext));
		AddressET.setVerticalScrollBarEnabled(true);
		AddressET.setMovementMethod(new ScrollingMovementMethod());
		AddressET.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View view, MotionEvent event) {
				// TODO Auto-generated method stub
				if (view.getId() == R.id.radiusET) {
					view.getParent().requestDisallowInterceptTouchEvent(true);
					switch (event.getAction() & MotionEvent.ACTION_MASK) {
					case MotionEvent.ACTION_UP:
						view.getParent().requestDisallowInterceptTouchEvent(false);
						break;
					}
				}
				return false;
			}
		});

		ok.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				String radius = AddressET.getText().toString();
				add = radius;
				googleMap.clear();
				Marker m = googleMap.addMarker(markerOptions.title(add));
				m.showInfoWindow();
				dialog.dismiss();
				
			}
		});
		dialog.show();
	}

	public class locationListener {
		public void onSuccess(String msg) {

		}

		public void onError(String msg) {

		}
	}
	ProgressDialog progress;

	// This method will be used for showing progress bar.
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

	// This method will show for hiding progressBar.
	public void hideProgress() {
		
		if (progress != null) {
			
			progress.dismiss();
			
		}
	}
	
	public class GetAddressListener{
		public void onSuccess(String msg){
			
			try{
			
			  hideProgress();
//			Utill.showToast(mContext, msg);
			  add = msg;
			  googleMap.clear();
			  Marker m = googleMap.addMarker(markerOptions.title(msg));
			  m.showInfoWindow();
			
			}catch(Exception e){
				
				e.printStackTrace();
				
				m.showInfoWindow();
				
			}		
		
		}
		public void onError(String msg){
			
			hideProgress();
//			Utill.showToast(mContext, msg);
			
		}
	}
}
