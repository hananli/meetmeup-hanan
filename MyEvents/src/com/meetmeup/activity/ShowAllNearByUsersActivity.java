package com.meetmeup.activity;

import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.fragment.NearByEventsFragement;
import com.meetmeup.helper.AppConstants;

//This class is used for Showing google map
public class ShowAllNearByUsersActivity extends FragmentActivity {

	GoogleMap googleMap;
	MarkerOptions markerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		try {
			
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_map);
			String title = "MeetMeUp";
			double Lat =  Double.parseDouble(AppConstants.getLattitude());
			double Lon =  Double.parseDouble(AppConstants.getLogitude());
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			googleMap = supportMapFragment.getMap();
			LatLng latLng = new LatLng(Lat, Lon);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 12);
			googleMap.animateCamera(cameraUpdate);
			googleMap.setMyLocationEnabled(true);

			for(int i= 0;i<NearByEventsFragement.mainList.size();i++){
				
				EventsBean bean = NearByEventsFragement.mainList.get(i);
				double l = Double.parseDouble(bean.getEvent_lat());
				double lon = Double.parseDouble(bean.getEvent_eventLong());
				String t = bean.getEvent_title();
				Marker m = googleMap.addMarker(new MarkerOptions().position(new LatLng(l, lon)).title(t));
		//		m.showInfoWindow();
			
			}
			
			
			googleMap.setOnMarkerClickListener(new OnMarkerClickListener() {
				
				@Override
				public boolean onMarkerClick(Marker arg0) {
					String id = arg0.getId();
					id = id.substring(1,id.length());
					int index = Integer.parseInt(id);
					Intent returnIntent = new Intent();
					returnIntent.putExtra("index",index);
					setResult(RESULT_OK,returnIntent);
					finish();
					return false;
				}
			});
			
			//googleMap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lon)).title(title));
			
		} catch (Exception e) {
			
			// TODO: handle exception
			
		}
	}
}
