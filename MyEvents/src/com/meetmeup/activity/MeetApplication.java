package com.meetmeup.activity;

import com.meetmeup.helper.GPSTracker;

import android.app.Application;


//This is the application class which executes first whenver application starts.
public class MeetApplication extends Application{
	@Override
	public void onCreate() {
//		new GPSTracker(getApplicationContext());
		new GoogleLocationClient(getApplicationContext()).onStart();
		
		super.onCreate();
	}
		
	
}
