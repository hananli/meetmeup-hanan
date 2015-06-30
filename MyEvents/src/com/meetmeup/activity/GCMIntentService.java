package com.meetmeup.activity;

import static com.meetmeup.helper.CommonUtilities.SENDER_ID;
import static com.meetmeup.helper.CommonUtilities.displayMessage;

import org.json.JSONObject;

import android.R.string;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.meetmeup.fragment.ChatFragement;
import com.meetmeup.fragment.HomeFragment;
import com.meetmeup.helper.Utill;

public class GCMIntentService extends GCMBaseIntentService {

	static int requestId;
	
	// public String msg, status, request_id, notification_id, msg_id;
	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		
		super(SENDER_ID);
		
	}
	
	
	/**
	 * Method called on device registered
	 **/
	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		displayMessage(context, "Your device registred with GCM");
	}

	/**
	 * Method called on device un registred
	 * */
	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
	}

	/**
	 * Method called on Receiving a new message
	 * */

	@Override
	protected void onMessage(Context context, Intent intent) {
	
		Log.i(TAG, "Received message");
		String message1 = intent.getExtras().getString("price");
		Log.v("Notification", message1);// {"message":"hello"}
		// displayMessage(context, message1);
		// notifies user
		
        if (Utill.getNotification(context)) {
			
			return;
			
		}
		
		generateNotification(context, message1);

	}

	/**
	 * Method called on receiving a deleted message
	 * */
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
		
        if (!Utill.getNotification(context)) {
			
			return;
			
		}
		
		generateNotification(context, message);
		
	}

	/**
	 * Method called on Error
	 * */
	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */

	// {"event_id":"64","user_id":"85","type":"chat"}
	@SuppressWarnings("deprecation")
	private void generateNotification(Context context, String msg) {
	
//		int requestId = (int) System.currentTimeMillis();
		
		requestId = (int) System.currentTimeMillis();
		
		try {
			/*
			 * "event_id": "44", "event_title": "h6", "event_date":
			 * "2015-02-10", "event_time": "03:50 PM", "event_owner_name":
			 * "Anil", "event_owner_id": "85"
			 */

			JSONObject json = new JSONObject(msg);
			
			if(json.has("notificationState")){
				
				
				try{
					
					
					String notificationState = json.getString("notificationState");
					
					
					if(notificationState.equals("4")){
						
						
						String event_id = json.getString("event_id");
						String event_title = json.getString("event_title");
						String event_date = json.getString("event_date");
						String event_time = json.getString("event_time");
						String event_owner_name = json.getString("event_owner_name");
						String event_owner_id = json.getString("event_owner_id");

						NotificationManager mNotificationManager = (NotificationManager) context
								.getSystemService(Context.NOTIFICATION_SERVICE);
						int icon = R.drawable.launcher;
						String title = context.getString(R.string.app_name);
						long when = System.currentTimeMillis();
						Notification notification = new Notification(icon, title, when);
						Intent notificationIntent = new Intent(context, DashBoard.class);

						notificationIntent.putExtra("notificationId", requestId);
						notificationIntent
								.putExtra("menuFragment", "favoritesMenuItem");
						notificationIntent.putExtra("type", "delete");
						notificationIntent.putExtra("eid", event_id);
						notificationIntent.putExtra("uid", event_owner_id);

						notificationIntent.setAction("myString" + requestId);

						PendingIntent contentIntent = PendingIntent.getActivity(
								context, requestId, notificationIntent, 0);
						notificationIntent.setData((Uri.parse("mystring" + requestId)));
						notification.setLatestEventInfo(context, title, event_title
								+ " Deleted by " + event_owner_name, contentIntent);

						notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
								| Intent.FLAG_ACTIVITY_SINGLE_TOP);
						notification.flags |= Notification.FLAG_AUTO_CANCEL;
						notification.defaults |= Notification.DEFAULT_SOUND;
						notification.defaults |= Notification.DEFAULT_VIBRATE;
						mNotificationManager.notify(requestId, notification);
					
						HomeFragment.isRefreshnNotify = true;						
						
					  return;
						
					}
										
					
				}catch(Exception e){
					
					e.printStackTrace();
					
				}								
				
			}
			
			
			
			if (json.has("type")) {
				
				
			//	{"event_id":"10","user_id":"91","chat_message":"hello","event_title":"Part 2015","type":"chat"}
				
				String event_id = json.getString("event_id");
				String user_id = json.getString("user_id");
				String type = json.getString("type");
				if(type.equalsIgnoreCase("chat")){
					if(json.has("accept_status")){
						if(!json.getString("accept_status").equalsIgnoreCase("1")){
							return;
						}
					}
				}
				
				String eventType = json.getString("event_title");
				String chatMsg = json.getString("chat_message");
				
				
				if(ChatFragement.chatInfoBean!=null){
					if(ChatFragement.chatInfoBean.getEventID()!=null && event_id.equalsIgnoreCase(ChatFragement.chatInfoBean.getEventID())){
						return;
					}
				}
				
				NotificationManager mNotificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				int icon = R.drawable.launcher;
				String title = context.getString(R.string.app_name);
				
				long when = System.currentTimeMillis();
				Notification notification = new Notification(icon, title, when);
				Intent notificationIntent = new Intent(context, DashBoard.class);

				notificationIntent.putExtra("notificationId", requestId);
				notificationIntent
						.putExtra("menuFragment", "favoritesMenuItem");
				
				notificationIntent.putExtra("type",type);
				notificationIntent.putExtra("eid", event_id);
				
				notificationIntent.putExtra("event_title",eventType);
				notificationIntent.putExtra("chat_msg",chatMsg);
				
				
			

				notificationIntent.setAction("myString" + requestId);

				PendingIntent contentIntent = PendingIntent.getActivity(
						context, requestId, notificationIntent, 0);
				notificationIntent.setData((Uri.parse("mystring" + requestId)));
				notification.setLatestEventInfo(context, title,chatMsg,contentIntent);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				mNotificationManager.notify(0, notification);
				
			}
			else {
				
				String event_id = json.getString("event_id");
				String event_title = json.getString("event_title");
				String event_date = json.getString("event_date");
				String event_time = json.getString("event_time");
				String event_owner_name = json.getString("event_owner_name");
				String event_owner_id = json.getString("event_owner_id");

				NotificationManager mNotificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				int icon = R.drawable.launcher;
				String title = context.getString(R.string.app_name);
				long when = System.currentTimeMillis();
				Notification notification = new Notification(icon, title, when);
				Intent notificationIntent = new Intent(context, DashBoard.class);

				notificationIntent.putExtra("notificationId", requestId);
				notificationIntent
						.putExtra("menuFragment", "favoritesMenuItem");
				notificationIntent.putExtra("type", "detail");
				notificationIntent.putExtra("eid", event_id);
				notificationIntent.putExtra("uid", event_owner_id);

				notificationIntent.setAction("myString" + requestId);

				PendingIntent contentIntent = PendingIntent.getActivity(
						context, requestId, notificationIntent, 0);
				notificationIntent.setData((Uri.parse("mystring" + requestId)));
				notification.setLatestEventInfo(context, title, event_title
						+ " By " + event_owner_name, contentIntent);

				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_SOUND;
				notification.defaults |= Notification.DEFAULT_VIBRATE;
				mNotificationManager.notify(requestId, notification);
			
				HomeFragment.isRefreshnNotify = true;
				
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
