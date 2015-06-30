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
import com.meetmeup.bean.ProfileBean;
import com.meetmeup.fragment.MyProfileFragement.ProfileListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

//This class is used for Getting Detail of a perticular event according to event id.
public class GetUserProfileAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	ProfileListener mListener;
	MultipartEntity multipart;

	public GetUserProfileAsync(Context ct, ProfileListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_USER_PROFILE, multipart);
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
						ProfileBean bean = new ProfileBean();
						bean.setAccept_event(json.getString("accept_event"));
						bean.setFb_id(json.getString("fb_id"));
						bean.setImage_url(json.getString("image_url"));
						bean.setTotal_user_ratting(json.getString("total_user_ratting"));
						bean.setUser_fname(json.getString("user_fname"));
						bean.setUser_id(json.getString("user_id"));
						bean.setUser_lname(json.getString("user_lname"));
						bean.setYellow_card_status(json.getString("yellow_card_status"));
						if(mListener!=null)
							mListener.onSuccess(bean);
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
}