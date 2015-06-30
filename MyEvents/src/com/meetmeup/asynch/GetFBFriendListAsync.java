package com.meetmeup.asynch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.bean.EventsBean;
import com.meetmeup.fragment.HomeFragment.GetEventListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;

//This class is used for getting list of all the event's for the current user.
public class GetFBFriendListAsync extends AsyncTask<Void, Void, String> {
//App Secret=e5d715f9e29cbfc6f2a65a7fbadcc856
	Context mContext;
	boolean isNetworkError = false;
	GetEventListener mListener;
	String param;
	public static final String url = "https://graph.facebook.com/me/friends?access_token=";
//	MultipartEntity multipart;

	public GetFBFriendListAsync(Context ct, GetEventListener listener,String p) {
		mContext = ct;
		mListener = listener;
		param = p;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			//return HttpRequest.post(WebServices.GetFBFriendsDirect, multipart);
			String finalUrl = url+param;
			return HttpRequest.post(finalUrl);
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
						JSONArray jsonArray = json.getJSONArray("my_event_list");
						for(int i=0;i<jsonArray.length();i++){
							EventsBean bean = new EventsBean();
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							bean.setEvent_id(jsonObj.getString("event_id"));
							bean.setEvent_title(jsonObj.getString("event_title"));
							bean.setEvent_date(jsonObj.getString("event_date"));
							bean.setEvent_time(jsonObj.getString("event_time"));
							bean.setEvent_owner_name(jsonObj.getString("event_owner_name"));
							bean.setEvent_owner_id(jsonObj.getString("event_owner_id"));
							String date = bean.getEvent_date();
							String time = bean.getEvent_time();
							bean.setEvent_Date_Object(Utill.getDateTime(date, time));
							eventList.add(bean);
						}
						if(json.has("invited_event_list")){
						jsonArray = json.getJSONArray("invited_event_list");
						try{
						for(int i=0;i<jsonArray.length();i++){
							EventsBean bean = new EventsBean();
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							bean.setEvent_id(jsonObj.getString("event_id"));
							bean.setEvent_title(jsonObj.getString("event_title"));
							bean.setEvent_date(jsonObj.getString("event_date"));
							bean.setEvent_time(jsonObj.getString("event_time"));
							bean.setEvent_owner_name(jsonObj.getString("event_owner_name"));
							bean.setEvent_owner_id(jsonObj.getString("event_owner_id"));
							String date = bean.getEvent_date();
							String time = bean.getEvent_time();
							bean.setEvent_Date_Object(Utill.getDateTime(date, time));
							eventList.add(bean);
						}}
						catch (Exception e) {
						}
						}
						
						try {
							Collections.sort(eventList, new Comparator<EventsBean>() {
								public int compare(EventsBean o1, EventsBean o2) {
									return o1.getEvent_Date_Object().compareTo(o2.getEvent_Date_Object());
								}
							});	
						} catch (Exception e) {
							// TODO: handle exception
						}
						
						
						
						try {
							for(int i=0;i<eventList.size();i++){
								EventsBean eveBean = eventList.get(i);
								if(Utill.isEventTimePassed(eveBean.getEvent_date(),eveBean.getEvent_time())){
									Log.e("removed date time tag",eveBean.getEvent_date()+" "+eveBean.getEvent_time()+" "+eveBean.getEvent_title()+" "+i);
									eveBean.setPassed(true);
								//	eventList.remove(eveBean);
								}else{
									eveBean.setPassed(false);
								}
								
							}	
						} catch (Exception e) {
							e.printStackTrace();
						}
						ArrayList<EventsBean> tempnewList = new ArrayList<EventsBean>();
						ArrayList<EventsBean> tempoldList = new ArrayList<EventsBean>();
						for(int i=0;i<eventList.size();i++){
							EventsBean eveBean = eventList.get(i);
							if(!eveBean.isPassed())
								tempnewList.add(eveBean);
							else
								tempoldList.add(eveBean);
						}
						Collections.reverse(tempoldList);
						tempnewList.addAll(tempoldList);
						
						mListener.onSuccess(tempnewList,"Successfull");
						
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