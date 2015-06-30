package com.meetmeup.asynch;

import android.content.Context;
import android.os.AsyncTask;
import com.meetmeup.activity.ChooseLocationActivity.locationListener;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;

//This class is used for Getting Detail of a perticular event according to event id.
public class getAddressAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	locationListener mListener;
	String mainUrl;

	public getAddressAsync(Context ct, locationListener listener, String url) {
		mContext = ct;
		mListener = listener;
		mainUrl = url;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(mainUrl);
		} catch (Exception e) {
			e.printStackTrace();
			isNetworkError = true;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		Utill.showToast(mContext, ""+result);
		/*
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
							bean.setEvent_title(json.getString("event_title"));
							String tempDate = json.getString("date");
							bean.setEvent_date(changeFormate(tempDate));
							
							bean.setEvent_time(json.getString("time"));
							if(Utill.isCorrectDateAndTime(bean.getEvent_date(),bean.getEvent_time())){
								bean.setPassed(false);
							}else{
								bean.setPassed(true);
							}
								
							bean.setEvent_description(json.getString("event_description"));
							bean.setEvent_type(json.getString("event_type"));
							bean.setEvent_createdby(json.getString("created_by"));
							bean.setEvent_Address(json.getString("event_address"));
							bean.setEvent_lat(json.getString("lat"));
							bean.setEvent_eventLong(json.getString("long"));
							bean.setEvent_owner_id(json.getString("event_owner_id"));
							bean.setMin_participants(json.getString("max_participants"));
							bean.setCollect_money_from_participants(json.getString("collect_money_from_participants"));
							if(json.has("participants_list")){
								ArrayList<ParticipantsBean> plist = new ArrayList<ParticipantsBean>();
								JSONArray tempArray = json.getJSONArray("participants_list");
								for(int i=0;i<tempArray.length();i++){
									JSONObject jObj = tempArray.getJSONObject(i);
									ParticipantsBean pBean = new ParticipantsBean();
									pBean.setUser_id(jObj.getString("user_id"));
									pBean.setUser_fname(jObj.getString("user_fname"));
									pBean.setUser_lname(jObj.getString("user_lname"));
									pBean.setImage(jObj.getString("image"));
									pBean.setStatus(jObj.getString("status"));
									plist.add(pBean);
								}
								bean.setParticipantsList(plist);
							}
							
							
							bean.setEvent_owner_name(jsonObj.getString("event_owner_name"));
							bean.setEvent_owner_id(jsonObj.getString("event_owner_id"));
							
						
						mListener.onSuccess("Successfull");
					} else {
						mListener.onError("error");
					}
				} catch (Exception e) {
					e.printStackTrace();
					mListener.onError("");
				}
			}
		}
	*/}
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
}