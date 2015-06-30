package com.meetmeup.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

//This class is used for Showing google map

public class MapActivity extends FragmentActivity {

	GoogleMap googleMap;
	MarkerOptions markerOptions;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_map);
			Intent intent = getIntent();
			String Latti = intent.getStringExtra("lat");
			String Longi = intent.getStringExtra("lon");
			String title = "MeetMeUp";
			title = intent.getStringExtra("name");
			double Lat = Double.parseDouble(Latti);// 22.719569;
			double Lon = Double.parseDouble(Longi);// 75.857726;
			SupportMapFragment supportMapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
			googleMap = supportMapFragment.getMap();
			LatLng latLng = new LatLng(Lat, Lon);
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
			googleMap.animateCamera(cameraUpdate);
			googleMap.setMyLocationEnabled(true);
			//MarkerOptions marker = new MarkerOptions().position(new LatLng(Lat, Lon)).title(title);
			Marker m = googleMap.addMarker(new MarkerOptions().position(new LatLng(Lat, Lon)).title(title));
			m.showInfoWindow();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
