package com.meetmeup.helper;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.meetmeup.bean.UserBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class Utill {
	
	
	
	public static final String loginUser = "loginuser";

	public static boolean isStringNullOrBlank(String str) {
		if (str == null) {
			
			return true;
			
		} else if (str.equals("null") || str.equals("")) {
			
			return true;
			
		}
		
		return false;
	}

	public static boolean isNetworkAvailable(Context context) {
		
			NetworkInfo localNetworkInfo = ((ConnectivityManager) context
				.getSystemService("connectivity")).getActiveNetworkInfo();
		    return (localNetworkInfo != null) && (localNetworkInfo.isConnected());
	}

	public static final void showToast(Context mContext, String msg) {
		Toast.makeText(mContext, "" + msg, Toast.LENGTH_SHORT).show();
	}

	public static final void showNetworkError(Context mContext) {
		Toast.makeText(mContext, "No network.", Toast.LENGTH_SHORT).show();
	}

	public static void createUserPreference(UserBean user, Context context) {
		SharedPreferences pref = context.getSharedPreferences(loginUser, 0);
		Editor edit = pref.edit();
		edit.putString("fb_id", user.getFb_id());
		edit.putString("email", user.getEmail());
		edit.putString("f_name", user.getF_name());
		edit.putString("l_name", user.getL_name());
		edit.putBoolean("isRegistered", true);
		edit.putString("lattitude", user.getLattitude());
		edit.putString("longitute", user.getLongitute());
		edit.putString("profile_pic_url", user.getProfile_pic_url());
		edit.putString("fb_access_token", user.getFb_access_token());
		edit.putString("fb_token_expire", user.getFb_access_token());
		edit.putString("user_id", user.getUser_id());
		edit.commit();
	}

	public static void setRadius(Context mContext, String rad) {
		// SharedPreferences pref = mContext.getSharedPreferences(Utill.radius,
		// Context.MODE_PRIVATE);
		SharedPreferences pref = mContext.getSharedPreferences("myPref",
				Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putString("radius", rad);
		edit.commit();
	}

	public static String getRadius(Context mContext) {
		SharedPreferences pref = mContext.getSharedPreferences("myPref",
				Context.MODE_PRIVATE);
		String radius = pref.getString("radius", null);
		if (radius == null)
			return "10000";
		return radius;
	}

	public static UserBean getUserPreferance(Context context) {
		UserBean user = new UserBean();
		SharedPreferences pref = context.getSharedPreferences(loginUser, 0);
		boolean isRegistered = pref.getBoolean("isRegistered", false);
		user.setRegistered(isRegistered);
		if (isRegistered) {
			user.setFb_id(pref.getString("fb_id", null));
			user.setEmail(pref.getString("email", null));
			user.setF_name(pref.getString("f_name", null));
			user.setL_name(pref.getString("l_name", null));
			user.setLattitude(pref.getString("lattitude", null));
			user.setLongitute(pref.getString("longitute", null));
			user.setProfile_pic_url(pref.getString("profile_pic_url", null));
			user.setFb_access_token(pref.getString("fb_access_token", null));
			user.setFb_token_expire(pref.getString("fb_token_expire", null));
			user.setUser_id(pref.getString("user_id", null));
		}
		return user;
	}
	

//	public static void removeUserPreference(Context context) {
//		SharedPreferences pref = context.getSharedPreferences(loginUser, 0);
//		Editor edit = pref.edit();
//		UserBean user = new UserBean();
//		edit.putString("fb_id", user.getFb_id());
//		edit.putString("email", user.getEmail());
//		edit.putString("f_name", user.getF_name());
//		edit.putString("l_name", user.getL_name());
//		edit.putBoolean("isRegistered", true);
//		edit.putString("lattitude", user.getLattitude());
//		edit.putString("longitute", user.getLongitute());
//		edit.putString("profile_pic_url", user.getProfile_pic_url());
//		edit.putString("fb_access_token", user.getFb_access_token());
//		edit.putString("fb_token_expire", user.getFb_access_token());
//		edit.putString("user_id", user.getUser_id());
//		edit.commit();
//	}

	
	public static void removeUserPreference(Context context) {
	
		try {

			SharedPreferences pref = context.getSharedPreferences(loginUser, 0);
			Editor edit = pref.edit();
			edit.clear();
			edit.commit();
			
		} catch (Exception e) {

			e.printStackTrace();

		}
		
	}
	
	
	
	
	public static void hideSoftKeyboard(Activity activity) {
		try {
			/*activity.getWindow().setSoftInputMode(
					WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
			InputMethodManager inputMethodManager = (InputMethodManager) activity
					.getSystemService(Activity.INPUT_METHOD_SERVICE);
			inputMethodManager.hideSoftInputFromWindow(activity
					.getCurrentFocus().getWindowToken(), 0);*/
			activity.getWindow().setSoftInputMode(
		            WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static String loadJSONFromAsset(Context mContext) {
		String json = null;
		try {
			InputStream is = mContext.getAssets().open("friendlist.json");
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			json = new String(buffer, "UTF-8");
		} catch (IOException ex) {
			ex.printStackTrace();
			return null;
		}
		return json;

	}

	public static Date getDateTime(String date, String time) {
		//2015-04-03 03:11 PM
		Calendar cal = Calendar.getInstance();
		int year = 0, monthOfYear = 0, dayOfMonth = 0, hourOfDay = 0, minute = 0, sec = 0;
		String d[] = date.split("-");

		year = Integer.parseInt(d[0]);
		monthOfYear = Integer.parseInt(d[1])-1;
		dayOfMonth = Integer.parseInt(d[2]) ;
		String t[] = time.split(" ");
		t = t[0].split(":");
		hourOfDay = Integer.parseInt(t[0]);
		minute = Integer.parseInt(t[1]);
		// sec = Integer.parseInt(t[2]);

		cal.set(Calendar.YEAR, year);
		cal.set(Calendar.MONTH, monthOfYear);
		cal.set(Calendar.DATE, dayOfMonth);
		cal.set(Calendar.HOUR_OF_DAY, hourOfDay);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.SECOND, sec);
		return cal.getTime();
	}

	public static boolean isCorrectDateAndTime(String date, String time) {
				
//		try {

			String d[] = date.split("-");// [19, 1, 2015]
			String s = "";
			SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm",Locale.ENGLISH);
			SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a",Locale.ENGLISH);
			
			try {
				
				Date dat = parseFormat.parse(time);
				System.out.println(parseFormat.format(dat) + " = "
						+ displayFormat.format(dat));
				s = displayFormat.format(dat);

			} catch (Exception e1) {
				
				// TODO Auto-generated catch block
				e1.printStackTrace();
				
			}
			
			String t[] = s.split(":");
			int hr = Integer.parseInt(t[0]);
			int min = Integer.parseInt(t[1]);
			int intyear = Integer.parseInt(d[2]);
			int intmonth = Integer.parseInt(d[1]);
			int intday = Integer.parseInt(d[0]);

			Calendar calendar = Calendar.getInstance();

			calendar.set(intyear, Integer.parseInt(d[1]) - 1,
					Integer.parseInt(d[0]), Integer.parseInt(t[0]),
					Integer.parseInt(t[1]), 0);

			long startTime = calendar.getTimeInMillis();
			Log.e("Selected time", "" + startTime);
			Log.e("Current time", "" + System.currentTimeMillis());
			
			
			if (startTime <= System.currentTimeMillis()) {
				return false;

			} else {
				return true;
			}
			

//		} catch (Exception e) {
//
//			e.printStackTrace();
//
//		}
//		
//		return false;
		

	}

	public static boolean isEventTimePassed(String date, String time) {
		get24HrFormatTime(time);
		String d[] = date.split("-");// [2015, 1,19]
		String s = "";
		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
		try {
			Date dat = parseFormat.parse(time);
			System.out.println(parseFormat.format(dat) + " = "
					+ displayFormat.format(dat));
			s = displayFormat.format(dat);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			
		}
		
		String t[] = s.split(":");
		int hr = Integer.parseInt(t[0]);
		int min = Integer.parseInt(t[1]);
		int intyear = Integer.parseInt(d[0]);
		int intmonth = Integer.parseInt(d[1]);
		int intday = Integer.parseInt(d[2]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(intyear,intmonth-1,intday, hr,min,0);
		
		long startTime = calendar.getTimeInMillis();
		Log.e("Selected time", "" + startTime);
		Log.e("Current time", "" + System.currentTimeMillis());
		
		if (startTime <= System.currentTimeMillis()) {
			
			return true;
		
		} else {
			
			return false;
	 
		}
		
	}
	
	public static void setNotification(Context mContext,boolean status) {
		
		try{
		  
			SharedPreferences pref = mContext.getSharedPreferences("noti",
			Context.MODE_PRIVATE);
		    Editor edit = pref.edit();
		    edit.putBoolean("notification",status);
		    edit.commit();
		
		}catch(Exception e){
		
			e.printStackTrace();
			
	   
		}	
		
	}

	public static boolean getNotification(Context mContext) {
		
		try{
		  
			SharedPreferences pref = mContext.getSharedPreferences("noti",
				Context.MODE_PRIVATE);
		    boolean status = pref.getBoolean("notification",false);
		    return status;
		
		}catch(Exception e){
		  
			e.printStackTrace();
			
		}
		
		return false; 
		
	}
	public static void setSession(Context mContext,boolean status) {
		SharedPreferences pref = mContext.getSharedPreferences("noti",
				Context.MODE_PRIVATE);
		Editor edit = pref.edit();
		edit.putBoolean("notification",status);
		edit.commit();
	}

	public static boolean getSession(Context mContext) {
		SharedPreferences pref = mContext.getSharedPreferences("noti",
				Context.MODE_PRIVATE);
		boolean status = pref.getBoolean("notification",false);
		return status;
	}
	public static  final void shareImage(Activity mActivity,Context mContext,String referralCode) {
		//perfect working 
		Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);  
		shareIntent.setType("text/plain");  
		shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"subject");  
		String msg = "https://market.android.com/search?q=pname:com.ExceptionWeb.HealthOnPhone";
		msg = "";
		msg = "Hi\n"+
		"Install MeetMeUP from https://play.google.com/store/apps/details?id=com.ExceptionWeb.HealthOnPhone \n";
		shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
		shareIntent.putExtra(android.content.Intent.EXTRA_TEMPLATE, msg+"templete");
		shareIntent.putExtra(android.content.Intent.EXTRA_TITLE, msg+"title");
		shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, msg);  
		mActivity.startActivity(Intent.createChooser(shareIntent, "Sharing..."));  
	}
	public static JSONObject getLocationInfo(double lat,double longi) {

        HttpGet httpGet = new HttpGet("http://maps.google.com/maps/api/geocode/json?latlng="+lat+","+longi+"&sensor=true");
        HttpClient client = new DefaultHttpClient();
        HttpResponse response;
        StringBuilder stringBuilder = new StringBuilder();

        try {
            response = client.execute(httpGet);
            HttpEntity entity = response.getEntity();
            InputStream stream = entity.getContent();
            int b;
            while ((b = stream.read()) != -1) {
                stringBuilder.append((char) b);
            }
        } catch (ClientProtocolException e) {
            } catch (IOException e) {
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject = new JSONObject(stringBuilder.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }
	public static int getDayNumber  (String date, String time) {
		
		String d[] = date.split("-");// [2015, 1,19]
		String s = "";
		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
		try {
			Date dat = parseFormat.parse(time);
			System.out.println(parseFormat.format(dat) + " = "
					+ displayFormat.format(dat));
			s = displayFormat.format(dat);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String t[] = s.split(":");
		int hr = Integer.parseInt(t[0]);
		int min = Integer.parseInt(t[1]);
		int intyear = Integer.parseInt(d[0]);
		int intmonth = Integer.parseInt(d[1]);
		int intday = Integer.parseInt(d[2]);
		Calendar calendar = Calendar.getInstance();
		calendar.set(intyear,intmonth-1,intday, hr,min,0);
		
		return calendar.get(Calendar.DAY_OF_WEEK);
	}
	public static int getNumber(Date date){
		date.setMonth(date.getMonth()-1);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
		String s = date.getDate()+" "+date.getMonth()+" "+date.getYear();
		int day = cal.get(Calendar.DAY_OF_WEEK);
		return day;
	}
	public static int get24HrFormatTime(String time){
		SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
		SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
		String s= "";
		try {
			Date dat = parseFormat.parse(time);
			System.out.println(parseFormat.format(dat) + " = "
					+ displayFormat.format(dat));
			s = displayFormat.format(dat);

		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		String t[]=s.split(":");
		int l = Integer.parseInt(t[0]);
		return l;
	}
	
	/*upload the image from server by passing to url and id on which view layout*/
    public static void uploadImage(View v,int id,String url,int defaultImgId){
 	  
	        AQuery aquery2 = new AQuery(v);
//			System.out.println("view uploadImage path == "+url);
			aquery2.id(id).image(url,true, true, 0,defaultImgId, null,
					AQuery.CACHE_DEFAULT,0.0f)
					.visible();
	  
    }
    
    public static void loadImage(Context mContext,DisplayImageOptions opt,ImageLoader image_load,
    		ProgressBar progress, ImageView imageView, String uri){
    	   
    
       image_load.init(ImageLoaderConfiguration.createDefault(mContext));
       image_load.displayImage(uri,imageView, opt, new CustomImageLoader(progress, mContext));
    	
    }
	
	public static String getUTF(String utfst){
		
		String temp = utfst;
		
		try
		{
		    //chatMsg = new String(chatMsg.getBytes(), "UTF-8");
			utfst = URLDecoder.decode(utfst, "UTF-8");
			
			
			
		}
		catch (UnsupportedEncodingException e)
		{
			
		    Log.e("utf8", "conversion", e);
		    
		}
		
	
		if(Utill.isStringNullOrBlank(utfst)){
			
			return temp;
			
		}
				
		return utfst;
		
	}
    
}
