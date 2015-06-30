package com.meetmeup.asynch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.bean.EventTypeCategory;
import com.meetmeup.bean.EventsBean;
//import com.meetmeup.fragment.CreateEventFragement.GetEventTypeCategListener;
import com.meetmeup.fragment.HomeFragment.GetEventListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

public class GetEventTypeCategAsyTask extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	GetEventTypeCategListener mListener;
	MultipartEntity multipart;

	public GetEventTypeCategAsyTask(Context ct, GetEventTypeCategListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.EVENT_TYPE_CATEGORY_LIST, multipart);
		} catch (Exception e) {
			e.printStackTrace();
			isNetworkError = true;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Log.e("result", "result : " + result);
		if (isNetworkError) {
			//Utill.showNetworkError(mContext);
			if(mListener!=null)
				mListener.onError(AppConstants.networkError);
		} else {
			if (Utill.isStringNullOrBlank(result)) {
			//	Utill.showServerError(mContext);
				mListener.onError(AppConstants.networkError);
			
			} else {
				try {
					JSONObject json = new JSONObject(result);
					String response = json.getString("status");
					if (response.equalsIgnoreCase("false")) {
						String msg = json.getString("msg");
						mListener.onError(msg);
					} else if (response.equalsIgnoreCase("true")) {
						JSONArray jsonArray;
						ArrayList<EventTypeCategory> eventList = new ArrayList<EventTypeCategory>();
						
						if(json.has("category_list")){
						 jsonArray = json.getJSONArray("category_list");
						  
						 for(int i=0;i<jsonArray.length();i++){
							
							JSONObject jsonObj = jsonArray.getJSONObject(i);
//							eventList.add(new EventTypeCategory(jsonObj.getString("category_id"),
//									jsonObj.getString("category_name")));
							
//							eventList.add(new EventTypeCategory(jsonObj.getString("category_id").trim(),
//									jsonObj.getString("category_name").trim(),
//									jsonObj.getString("category_image").trim()));
						
							eventList.add(new EventTypeCategory(Utill.getUTF(jsonObj.getString("category_id").trim()),
									Utill.getUTF(jsonObj.getString("category_name").trim()),
									Utill.getUTF(jsonObj.getString("category_image").trim())));
							
							
						  }
						}
						
						mListener.onSuccess(eventList);
						
					} else {
						mListener.onError("error");
					}
				} catch (Exception e) {
					e.printStackTrace();
					mListener.onError("");
				}
			}
		}
	}
	
	
	
	public interface GetEventTypeCategListener{
		
		public void onSuccess(ArrayList<EventTypeCategory> eventList);
		public void onError(String msg);
		
		
	}
	
}