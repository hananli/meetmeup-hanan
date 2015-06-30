package com.meetmeup.activity;

import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.widget.Toast;

import com.meetmeup.asynch.GetEventTypeCategAsyTask;
import com.meetmeup.asynch.GetEventTypeCategAsyTask.GetEventTypeCategListener;
import com.meetmeup.bean.EventCategoryList;
import com.meetmeup.bean.EventTypeCategory;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.Utill;



//This is the splash class which shows splash image for 3 seconds,if user is logged in previously then redirect to the home screen other wise login screen will be shown.
public class SplashActivity extends Activity {

	
	Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		
		mContext = this;
		
//		 Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
////			
//	     startActivity(intent);
		
		getEventTypeCategory();
		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		getMenuInflater().inflate(R.menu.splash, menu);
		return true;
	
	}
	
	
	public void hideSplash(){
		
		Thread t = new Thread() {
			@Override
			public void run() {
				super.run();
				try {
					Thread.sleep(3000);
					startActivity();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		};
		t.start();
				
		
		if(Utill.isNetworkAvailable(SplashActivity.this))
		{
		    Intent I = new Intent(SplashActivity.this,BackgroundService.class);
		    startService(I);
		}
		else
		{
			Toast.makeText(SplashActivity.this, "Please Check Internet Connection", Toast.LENGTH_LONG).show();
		}
		
		
	}
	
	
	
	void startActivity(){
			Intent intent = null;
			UserBean mUser = Utill.getUserPreferance(this);
			
			if (mUser.getUser_id() == null) {
				intent = new Intent(SplashActivity.this,LoginActivity.class);
		
			} else {
				
				intent = new Intent(SplashActivity.this,DashBoard.class);
			
			}
			finish();
			startActivity(intent);
	}
	
	
	public void getEventTypeCategory(){
		

		if (Utill.isNetworkAvailable(mContext)) {
			
			MultipartEntity multipart = new MultipartEntity();
			
			try {
				
				new GetEventTypeCategAsyTask(mContext, new GetEventTypeCategListener() {
					
					@Override
					public void onSuccess(ArrayList<EventTypeCategory> eventList) {
												
						if(eventList.isEmpty()){
							
							return;
						}
													
						EventCategoryList.setEventCategoryList(eventList);
						
						hideSplash();
						
					}
					
					@Override
					public void onError(String msg) {
						
						Toast.makeText(mContext,""+msg,Toast.LENGTH_LONG).show();
						
					}
				}, multipart).execute();
			
			} catch (Exception e) {
				
				e.printStackTrace();
				
			}
			
		} else {
			
			Utill.showNetworkError(mContext);
			
		}
		
		
	}

}
