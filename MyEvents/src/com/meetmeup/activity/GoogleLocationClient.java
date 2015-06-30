package com.meetmeup.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.GPSTracker;

public class GoogleLocationClient implements
		GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationListener {

	// locations objects
	LocationClient mLocationClient;
	Location mCurrentLocation;
	LocationRequest mLocationRequest;
	Context mcontext;

//	TextView txtLong, txtLat;

	
	public GoogleLocationClient(Context ctx){
		
		this.mcontext = ctx;
		onCreate();
				
		
	}
	
	protected void onCreate() {
//		super.onCreate(savedInstanceState);
		// 1. setContnetView
//		setContentView(R.layout.activity_main);

		// 2. get reference to TextView
//		txtLong = (TextView) findViewById(R.id.txtLong);
//		txtLat = (TextView) findViewById(R.id.txtLat);

		// 3. create LocationClient
		mLocationClient = new LocationClient(mcontext, this, this);

		// 4. create & set LocationRequest for Location update
		mLocationRequest = LocationRequest.create();
		mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		// Set the update interval to 5 seconds
		mLocationRequest.setInterval(1000 * 5);
		// Set the fastest update interval to 1 second
		mLocationRequest.setFastestInterval(1000 * 1);
		
	}


	public void onStart() {
		
		// 1. connect the client.
		
		try{
		 
			if(mLocationClient != null)
		    mLocationClient.connect();
			
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}

	
	public void onStop() {
	
		// 1. disconnecting the client invalidates it.
		try{
			 
			if(mLocationClient != null)
		      mLocationClient.disconnect();
		
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
	}

	// GooglePlayServicesClient.OnConnectionFailedListener
	@Override
	public void onConnectionFailed(ConnectionResult connectionResult) {
		
		Toast.makeText(mcontext, "Connection Failed", Toast.LENGTH_SHORT).show();
		
	}
	
//	public LocationClient getLocationclient(){
//		
//		return mLocationClient;
//							
//	}
	
	
	// GooglePlayServicesClient.ConnectionCallbacks
	@Override
	public void onConnected(Bundle arg0) {

		if (mLocationClient != null)
			mLocationClient.requestLocationUpdates(mLocationRequest,this);

//		Toast.makeText(mcontext, "Connected", Toast.LENGTH_SHORT).show();
		Log.i("location Connected","location Connected");

		if (mLocationClient != null) {
			// get location
			mCurrentLocation = mLocationClient.getLastLocation();
			try {

				// set TextView(s)
//				txtLat.setText(mCurrentLocation.getLatitude() + "");
//				txtLong.setText(mCurrentLocation.getLongitude() + "");
				
				 AppConstants.setLattitude(new String(""+mCurrentLocation.getLatitude()));
			     AppConstants.setLogitude(new String(""+mCurrentLocation.getLongitude()));
			        
			     AppConstants.setCurrentLatitude(mCurrentLocation.getLatitude());
			     AppConstants.setCurrentLongitude(mCurrentLocation.getLongitude());

			} catch (NullPointerException npe) {

//				Toast.makeText(mcontext, "Failed to Connect", Toast.LENGTH_SHORT)
//						.show();
				try{
				
				  Log.i("Failed to Connect location ","Failed to Connect location");
				 
				try{
 								 
					new GPSTracker(mcontext);
				  
				}catch(Exception e){
					  
					  e.printStackTrace();
				}
				// switch on location service intent
//				  Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//			
//				  mcontext.startActivity(intent);
				
			     
				}catch(Exception e){
					
					e.printStackTrace();
					
				}
				
				
			}
		}

	}

	@Override
	public void onDisconnected() {
		Toast.makeText(mcontext, "Disconnected.", Toast.LENGTH_SHORT).show();

	}

	// LocationListener
	@Override
	public void onLocationChanged(Location location) {
//		Toast.makeText(mcontext, "Location changed.", Toast.LENGTH_SHORT).show();
		Log.i("locationchanged",location.toString());

		try {

			mCurrentLocation = mLocationClient.getLastLocation();

			AppConstants.setLattitude(new String(""
					+ mCurrentLocation.getLatitude()));
			AppConstants.setLogitude(new String(""
					+ mCurrentLocation.getLongitude()));

			AppConstants.setCurrentLatitude(mCurrentLocation.getLatitude());
			AppConstants.setCurrentLongitude(mCurrentLocation.getLongitude());

		} catch (Exception e) {

			e.printStackTrace();
		}

		// txtLat.setText(mCurrentLocation.getLatitude() + "");
		//
		// txtLong.setText(mCurrentLocation.getLongitude() + "");
	}

}