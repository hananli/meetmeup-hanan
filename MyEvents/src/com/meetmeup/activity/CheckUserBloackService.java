package com.meetmeup.activity;

import com.meetmeup.helper.Utill;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

public class CheckUserBloackService extends Service{
Handler handler;
	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		handler = new Handler();
		handler.post(runnable);
		return 0;
	}
	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			Utill.showToast(getApplicationContext(),"Check for application bloack or naot.");
			handler.postDelayed(this,1000);
		}
	};
	
	

}
