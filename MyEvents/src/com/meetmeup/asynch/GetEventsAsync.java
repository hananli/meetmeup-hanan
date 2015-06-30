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

import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.fragment.HomeFragment.GetEventListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

//This class is used for getting list of all the event's for the current user.
public class GetEventsAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	GetEventListener mListener;
	MultipartEntity multipart;

	public GetEventsAsync(Context ct, GetEventListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_GET_EVENTS_LIST, multipart);
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
												
						ArrayList<EventsBean> eventList = new ArrayList<EventsBean>();
						if(json.has("my_event_list")){
						 jsonArray = json.getJSONArray("my_event_list");
					
						 for(int i=0;i<jsonArray.length();i++){
							 
							EventsBean bean = new EventsBean();
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							
							bean.setEvent_id(jsonObj.getString("event_id"));
														
							bean.setEvent_title(jsonObj.getString("event_title"));
							bean.setEvent_date(jsonObj.getString("event_date"));
							bean.setEvent_time(jsonObj.getString("event_time"));
							bean.setEvent_owner_name(jsonObj.getString("event_owner_name"));
							bean.setEvent_owner_id(jsonObj.getString("event_owner_id"));
							bean.setEvent_Address(jsonObj.getString("address"));
							bean.setEvent_type(jsonObj.getString("event_type"));                                 
							
							
							bean.setEvent_category(jsonObj.getString("event_category"));
							
							String evtOwnerProfurl = jsonObj.getString("event_owner_profile_url");
							
							bean.setEvent_owner_profile_url(evtOwnerProfurl);
							
							JSONArray acceptedParticipantsList = jsonObj.getJSONArray("accepted_participants_list");
							/*"user_id": "102",
		                    "user_name": "Shahid",
		                    "user_image_profile": "http://graph.facebook.com/766839203384917/picture?type=large"*/
							ArrayList<ParticipantsBean> partList = new ArrayList<ParticipantsBean>();
							for(int j=0;j<acceptedParticipantsList.length();j++){
								ParticipantsBean pBean = new ParticipantsBean();
								pBean.setImage(acceptedParticipantsList.getJSONObject(j).getString("user_image_profile"));
								pBean.setUser_fname(acceptedParticipantsList.getJSONObject(j).getString("user_name"));
								partList.add(pBean);
							}
							
							/*for(int j=0;j<10;j++){
								ParticipantsBean pBean = new ParticipantsBean();
								pBean.setImage(Utill.getUserPreferance(mContext).getProfile_pic_url());
								partList.add(pBean);
							}*/
							
							
							//bean.setJoindEvent("1");
							
							bean.setEventAcceptStatus("1");
							
							bean.setAcceptedParticipantsList(partList);
							
							String date = bean.getEvent_date();
							String time = bean.getEvent_time();
							bean.setEvent_Date_Object(Utill.getDateTime(date, time));
							bean.setMyEvent(true);
							eventList.add(bean);
							
						}}       
						
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
							bean.setEvent_category(jsonObj.getString("event_category"));
							
                            String evtOwnerProfurl = jsonObj.getString("event_owner_profile_url");
                            bean.setEvent_Address(jsonObj.getString("address"));
                            
							bean.setEvent_type(jsonObj.getString("event_type"));
                            //bean.setEvent_type("1");
							bean.setEvent_owner_profile_url(evtOwnerProfurl);
							
							String date = bean.getEvent_date();
							String time = bean.getEvent_time();
							bean.setEvent_Date_Object(Utill.getDateTime(date, time));
							JSONArray acceptedParticipantsList = jsonObj.getJSONArray("accepted_participants_list");
							ArrayList<ParticipantsBean> partList = new ArrayList<ParticipantsBean>();
							for(int j=0;j<acceptedParticipantsList.length();j++){
								ParticipantsBean pBean = new ParticipantsBean();
								pBean.setImage(acceptedParticipantsList.getJSONObject(j).getString("user_image_profile"));
								pBean.setUser_fname(acceptedParticipantsList.getJSONObject(j).getString("user_name"));
								partList.add(pBean);
							}
						/*	for(int j=0;j<10;j++){
								ParticipantsBean pBean = new ParticipantsBean();
								pBean.setImage(Utill.getUserPreferance(mContext).getProfile_pic_url());
								partList.add(pBean);
							}*/
							bean.setAcceptedParticipantsList(partList);
							
							if(jsonObj.has("accept_status")){
								
								bean.setEventAcceptStatus(jsonObj.getString("accept_status"));
								
							}
							bean.setMyEvent(false);
							
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