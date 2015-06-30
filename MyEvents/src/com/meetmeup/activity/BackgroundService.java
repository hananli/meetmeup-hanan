package com.meetmeup.activity;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import com.meetmeup.asynch.BackgroundAsync;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.GPSTracker;
import com.meetmeup.helper.Utill;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;


//This is the service class which used in background.it will be call automatically.in a fix time period.
public class BackgroundService extends Service {

	Handler handler;
//	GoogleLocationClient googleLocationClient;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
			
		super.onCreate();
//		new GPSTracker(getApplicationContext());
//		googleLocationClient = new GoogleLocationClient(getApplicationContext());
		
	}
	
    private void onstop() {
	// TODO Auto-generated method stub

//    	googleLocationClient.onStop();
    	
   }
	
	@Override
	public void onStart(Intent intent, int startId) {
//		
//		googleLocationClient.onStart();
		
		super.onStart(intent, startId);
		
	}
	
		

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		super.onStartCommand(intent, flags, startId);

		handler = new Handler();
		handler.post(runnable);
		return 0;

	}

	private Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Log.e("", "start Service");
		/*	Toast.makeText(getApplicationContext(), "start Service",
					Toast.LENGTH_SHORT).show();*/
			MultipartEntity multipart = new MultipartEntity();
			try {
				UserBean user = Utill.getUserPreferance(BackgroundService.this);
				String user_id = user.getUser_id();
				Log.v("User_Id", "User Id  = " + user_id);
				if (user_id != null) {
					multipart.addPart("user_id", new StringBody(user_id));
					multipart.addPart("lat",
							new StringBody(AppConstants.getLattitude()));
					multipart.addPart("long",
							new StringBody(AppConstants.getLogitude()));

					Log.e("Lat", "Lat = " + AppConstants.getLattitude());
					Log.e("Lat", "Long = " + AppConstants.getLogitude());

					new BackgroundAsync(BackgroundService.this, multipart)
							.execute();
				}
			} catch (Exception e) {

			}

			handler.postDelayed(this, 1000 * 60 * 1);
		}
	};
	
	
}
