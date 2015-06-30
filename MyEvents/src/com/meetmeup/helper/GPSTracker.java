package com.meetmeup.helper;


import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;

public class GPSTracker extends Service implements LocationListener {
 
    private final Context mContext;
 
    // flag for GPS status
    boolean isGPSEnabled = false;
 
    // flag for network status
    boolean isNetworkEnabled = false;
 
    // flag for GPS status
    boolean canGetLocation = false;
 
    Location location = null; // location
    double latitude; // latitude
    double longitude; // longitude
 
    // The minimum distance to change Updates in meters
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters
 
    // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 1000 * 60 * 2; // 1 minute
 
    // Declaring a Location Manager
    protected LocationManager locationManager;
 
    public GPSTracker(Context context) {
     
    	this.mContext = context;
        getLocation();
        AppConstants.setLattitude(new String(""+getLatitude()));
        AppConstants.setLogitude(new String(""+getLongitude()));
        
        AppConstants.setCurrentLatitude(getLatitude());
    	AppConstants.setCurrentLatitude(getLongitude());
        
        //String loc =  getAddress(HOPApllication.getUserLatitude(),HOPApllication.getUserLongitude());
    //    Log.e("location : ",loc);
    }
 
    
    
    public Location getLocation() {
    	
        try {
        	
//        	LocationClient
        	
            locationManager = (LocationManager) mContext
                    .getSystemService(LOCATION_SERVICE);
 
            // getting GPS status
            isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);
 
            // getting network status
            isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
 
            if (!isGPSEnabled && !isNetworkEnabled) {
            	  this.canGetLocation = false;
           
            } else {
            	
                this.canGetLocation = true;
               
                if (isNetworkEnabled) {
                	
                    locationManager.requestLocationUpdates(
                             LocationManager.NETWORK_PROVIDER,
                             MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                    Log.d("Network", "Network Enabled");
                    
                    if (locationManager != null) {
                    	
                        location = locationManager
                                .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        
                        if(location == null){
                        	
                        	 Log.i ("TAG", "getLastKnownLocation returned null, NETWORK_PROVIDER requesting updates when available");
                             locationManager.requestLocationUpdates (LocationManager.NETWORK_PROVIDER, 0, 0,this);
                        }
                        
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            return location;
                        }
                        
                   }
                    
                }
                
                // if GPS Enabled get lat/long using GPS Services
                if (isGPSEnabled) {
                	
                    if (location == null) {
                    	
                        locationManager.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                MIN_TIME_BW_UPDATES,
                                MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        Log.d("GPS", "GPS Enabled");
                      
                        if (locationManager != null) {
                        	
                            location = locationManager
                                    .getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            
                            if(location == null){
                            	
                           	  Log.i ("TAG", "getLastKnownLocation returned null, GPS_PROVIDER requesting updates when available");
                                locationManager.requestLocationUpdates (LocationManager.GPS_PROVIDER, 0, 0,this);
                           }
                            
                            
                            
                            if (location != null) {
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                                
                                return location;
                            }
                            
                        }
                    }
                }
            }
            
 
        } catch (Exception e) {
        	
            e.printStackTrace();
            
        }
 
        return location;
        
     }
 
    /**
     * Stop using GPS listener Calling this function will stop using GPS in your
     * app
     * */
    public void stopUsingGPS() {
        if (locationManager != null) {
            locationManager.removeUpdates(GPSTracker.this);
        }
    }
 
    /**
     * Function to get latitude
     * */
    public double getLatitude() {
        if (location != null) {
            latitude = location.getLatitude();
        }
 
        // return latitude
        return latitude;
    }
 
    /**
     * Function to get longitude
     * */
    public double getLongitude() {
        if (location != null) {
            longitude = location.getLongitude();
        }
 
        // return longitude
        return longitude;
    }
 
    /**
     * Function to check GPS/wifi enabled
     *
     * @return boolean
     * */
    public boolean canGetLocation() {
        return this.canGetLocation;
    }
 
    /**
     * Function to show settings alert dialog On pressing Settings button will
     * lauch Settings Options
     * */
    public void showSettingsAlert() {
    	
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
 
        // Setting Dialog Title
        alertDialog.setTitle("GPS is settings");
 
        // Setting Dialog Message
        alertDialog
                .setMessage("GPS is not enabled. Do you want to go to settings menu?");
 
        // On pressing Settings button
        alertDialog.setPositiveButton("Settings",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(
                                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        mContext.startActivity(intent);
                    }
                });
 
        // on pressing cancel button
        alertDialog.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
 
        // Showing Alert Message
        alertDialog.show();
        
    }
 
    @Override
    public void onLocationChanged(Location location) {
    	    	
    	AppConstants.setCurrentLatitude(location.getLatitude());
    	AppConstants.setCurrentLongitude(location.getLongitude());
    	
    }
 
    @Override
    public void onProviderDisabled(String provider) {
    	
    }
 
    @Override
    public void onProviderEnabled(String provider) {
    	
    }
 
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    	
    	
    }
 
    @Override
    public IBinder onBind(Intent arg0) {
    	
        return null;
 
    }
	private String getAddress(double latitude, double longitude) {
		
	//	AppConstant.isIndian = false;
        StringBuilder result = new StringBuilder();
      
        try {
        	
        	Log.e("inside getAddress","inside getaddress");
        	
            Geocoder geocoder = new Geocoder(mContext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            Log.e("add",":"+addresses.size());
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                result.append(address.getLocality()).append("\n");
                result.append(address.getCountryName());
                result.append(address.getCountryCode());
                Log.e("inside getAddress","inside getaddress :"+result);
                if(address.getCountryName().equalsIgnoreCase("india")){
        //        	AppConstant.isIndian = true;
                }
            }
        } catch (IOException e) {
         
        	Log.e("tag", e.getMessage());
        
        }
   //     Toast.makeText(mContext,result, Toast.LENGTH_SHORT).show();
        return result.toString();
        
    }
	
}