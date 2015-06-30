package com.meetmeup.asynch;

import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.bean.UserBean;

import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

//This class is used for Getting Detail of a perticular event according to event id.
public class GetEventDetialAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	GetEventDetailListener mListener;
	MultipartEntity multipart;

	public GetEventDetialAsync(Context ct, GetEventDetailListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_GET_EVENT_DETAIL, multipart);
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
											
						
							EventsBean bean = new EventsBean();
							 
							bean.setEvent_id(json.getString("event_id"));
							
							bean.setEvent_category(json.getString("event_category"));
							
							bean.setEvent_title(json.getString("event_title"));
							String tempDate = json.getString("date");
							bean.setEvent_date(changeFormate(tempDate));
							
							bean.setEvent_time(json.getString("time"));
							
							String time = bean.getEvent_time();
							bean.setEvent_Date_Object(Utill.getDateTime(tempDate, time));
												
							try{
							
							  if (Utill.isCorrectDateAndTime(
									bean.getEvent_date(), bean.getEvent_time())) {

								bean.setPassed(false);

							  } else {

								bean.setPassed(true);

							  }
							
							}catch(Exception e){
								
								e.printStackTrace();
							}
								
							bean.setEvent_description(json.getString("event_description"));
							
							bean.setEvent_type(json.getString("event_type"));
							
							bean.setEvent_createdby(json.getString("created_by"));
							bean.setEvent_Address(json.getString("event_address"));
							bean.setEvent_lat(json.getString("lat"));
							bean.setEvent_eventLong(json.getString("long"));
							
//							bean.setEvent_owner_name(event_owner_name);
							
							bean.setEvent_owner_id(json.getString("event_owner_id"));
							if(json.has("event_owner_profile_url"))
								bean.setEvent_owner_profile_url(json.getString("event_owner_profile_url"));
							else
								bean.setEvent_owner_profile_url("1232");
							
							bean.setMin_participants(json.getString("min_participants"));
							bean.setMax_participants(json.getString("max_participants"));
							bean.setFeedBackStatus(json.getString("feedback_status"));
							
							UserBean user = Utill.getUserPreferance(mContext);
							if(user.getUser_id().equalsIgnoreCase(bean.getEvent_owner_id())){
							
								bean.setMyEvent(true);
							
							}else{
								
								bean.setMyEvent(false);
								
							}
							
							bean.setEvent_chat_mode(json.getString("event_chat_mode"));
							
							
							bean.setCollect_money_from_participants(json.getString("collect_money_from_participants"));
//							if(bean.getCollect_money_from_participants().equalsIgnoreCase("1")){
								bean.setAmount(json.getString("amount"));
								bean.setPaypalId(json.getString("paypalid"));
//							}
								
							if(json.has("participants_list")){
								
								ArrayList<ParticipantsBean> plist = new ArrayList<ParticipantsBean>();
								ArrayList<ParticipantsBean> acceptPList = new ArrayList<ParticipantsBean>();
								JSONArray tempArray = json.getJSONArray("participants_list");
								for(int i=0;i<tempArray.length();i++){
									JSONObject jObj = tempArray.getJSONObject(i);
									ParticipantsBean pBean = new ParticipantsBean();
									pBean.setUser_id(jObj.getString("user_id"));
									pBean.setUser_fname(jObj.getString("user_fname"));
									pBean.setUser_lname(jObj.getString("user_lname"));
									pBean.setImage(jObj.getString("image"));
									pBean.setStatus(jObj.getString("status"));
									pBean.setRating(jObj.getString("rating"));
									pBean.setEvent_transaction_status(jObj.getString("event_transaction_status"));
									plist.add(pBean);
									if(pBean.getStatus().equalsIgnoreCase("1")){
										acceptPList.add(pBean);
									}
								}
								
								bean.setParticipantsList(plist);
								bean.setAcceptedParticipantsList(acceptPList);
								
							}
							
							
							/*bean.setEvent_owner_name(jsonObj.getString("event_owner_name"));
							bean.setEvent_owner_id(jsonObj.getString("event_owner_id"));*/
							
						
						mListener.onSuccess(bean,"Successfull");
						
						
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
	String changeFormate(String date){
		String revDate ="";
		try {
			String s[] = date.split("-");
			revDate = s[2]+"-"+s[1]+"-"+s[0];
			return revDate;
		} catch (Exception e) {
			
		}
		return date;
	}
	
	
	public interface GetEventDetailListener{
	
		public void onSuccess(EventsBean bean, String msg);
		public void onError(String msg);
		
	}
	
}