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

//import com.meetmeup.activity.ShowAllNearByUsersActivity.NearByListener;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.fragment.NearByEventsFragement.NearByListener;
//import com.meetmeup.fragment.NearByEventsFragement.NearByListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

//This class is used for getting list of all the event's for the current user.
public class GetNearByEventsAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	NearByListener mListener;
	MultipartEntity multipart;

	public GetNearByEventsAsync(Context ct, NearByListener listener, MultipartEntity multi) {
		
		mContext = ct;
		mListener = listener;
		multipart = multi;
		
	}

	@Override
	protected String doInBackground(Void... params) {
	
		try {
			
			return HttpRequest.post(WebServices.GET_NEAR_EVENETS, multipart);
			
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
						
						ArrayList<EventsBean> eventList = new ArrayList<EventsBean>();
						JSONArray jsonArray = json.getJSONArray("nearby_event_list");
						
						for(int i=0;i<jsonArray.length();i++){

							EventsBean bean = new EventsBean();
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							bean.setEvent_id(jsonObj.getString("event_id"));
							bean.setEvent_title(jsonObj.getString("event_title"));
							bean.setEvent_date(jsonObj.getString("event_date"));
							bean.setEvent_time(jsonObj.getString("event_time"));
							bean.setEvent_owner_name(jsonObj.getString("event_owner_name"));
							bean.setEvent_owner_id(jsonObj.getString("event_owner_id"));
							bean.setEvent_description(jsonObj.getString("event_description"));
							bean.setEvent_lat(jsonObj.getString("event_latitude"));
							bean.setEvent_eventLong(jsonObj.getString("event_longitude"));
							bean.setMin_participants(jsonObj.getString("max_participant"));
							bean.setEvent_type(jsonObj.getString("event_type"));
							bean.setCollect_money_from_participants(jsonObj.getString("collect_money_from_particaipant"));
							bean.setEvent_Address(jsonObj.getString("address"));
							
							bean.setEvent_owner_profile_url(jsonObj.getString("event_owner_profile_url"));
														
							String date = bean.getEvent_date();
							String time = bean.getEvent_time();
							bean.setEvent_Date_Object(Utill.getDateTime(date, time));
							
							bean.setCategory_image(jsonObj.getString("category_active_image").trim());
														
							eventList.add(bean);
														
						}
						
						try {
							
							Collections.sort(eventList, new Comparator<EventsBean>() {
								public int compare(EventsBean o1, EventsBean o2) {
									return o1.getEvent_Date_Object().compareTo(o2.getEvent_Date_Object());
								}
							});	
							
						} catch (Exception e) {
							
							e.printStackTrace();
							
						}
						
						//Collections.reverse(eventList);
						mListener.onSuccess(eventList,"Successfull");
						
						
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
}