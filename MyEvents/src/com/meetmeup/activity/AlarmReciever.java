package com.meetmeup.activity;

import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.Utill;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

public class AlarmReciever extends BroadcastReceiver {
	
	
	public static final int SECOND = 1000;
	public static final int MINUTE = 60 * SECOND;
	public static final int HOUR = 60 * MINUTE;
	public static final int DAY = 72 * HOUR;
	
	
	@Override
	public void onReceive(Context context, Intent intent) {
		
		int requestId = (int) System.currentTimeMillis();
		try {
			if (!Utill.getNotification(context)) {
				return;
			}
			
			UserBean user = Utill.getUserPreferance(context);
			if (user.getUser_id() == null) {
			
				return;
			
			} else {
				
				String eid = intent.getStringExtra("eid");
				String eTitel = intent.getStringExtra("etitle");
				String userId = intent.getStringExtra("userid");
				String type = intent.getStringExtra("type");
				/*Date d = (Date) intent.getSerializableExtra("event_time");
				long l = d.getTime();*/
				/*if(l<System.currentTimeMillis()){
					Utill.showToast(context, "Time Expired");
				}
				*/
				
				
				  
				
				if (!user.getUser_id().equalsIgnoreCase(userId))
					return;
				NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				int icon = R.drawable.launcher;
				String title = context.getString(R.string.app_name);
				long when = System.currentTimeMillis();
				Notification notification = new Notification(icon, title, when);
				Intent notificationIntent = new Intent(context, DashBoard.class);
				notificationIntent.putExtra("eid", eid);
				notificationIntent.putExtra("uid", userId);
				notificationIntent.putExtra("type", type);
				notificationIntent.putExtra("notificationId", requestId);
				notificationIntent.putExtra("menuFragment", "favoritesMenuItem");
				notificationIntent.setAction("myString" + requestId);
				PendingIntent contentIntent = PendingIntent.getActivity(context, requestId, notificationIntent, 0);
				notificationIntent.setData((Uri.parse("mystring" + requestId)));
				notification.setLatestEventInfo(context, title, eTitel, contentIntent);
				// testing
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				mNotificationManager.notify(0, notification);
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
