package com.meetmeup.asynch;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.meetmeup.adapters.FriendAdapter;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.FriendBean;
//import com.meetmeup.fragment.CreateEventFragement.GetFriendListListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;


//This class is used for getting list of all facebook friends.
public class GetFriendListAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	GetFriendListListener mListener;
	MultipartEntity multipart;

	public GetFriendListAsync(Context ct, GetFriendListListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.GET_Fb_Friends, multipart);
		} catch (Exception e) {
			e.printStackTrace();
			isNetworkError = true;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		try {
		//	result = Utill.loadJSONFromAsset(mContext);
			super.onPostExecute(result);	
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		Log.e("result", "result : " + result);
		if (isNetworkError) {
			//Utill.showNetworkError(mContext);
			if(mListener!=null)
				mListener.onError(AppConstants.networkError);
		} else {
			if (Utill.isStringNullOrBlank(result)) {
			//	Utill.showServerError(mContext);
				if(mListener!=null)
				mListener.onError(AppConstants.networkError);
			} else {
				try {
					JSONObject json = new JSONObject(result);
					String response = json.getString("status");
					if (response.equalsIgnoreCase("false")) {
						String msg = json.getString("msg");
						if(mListener!=null)
						mListener.onError("No friend found.");
					} else if (response.equalsIgnoreCase("true")) {
						ArrayList<FriendBean> freindList = new ArrayList<FriendBean>();
						JSONArray jsonArray = json.getJSONArray("friend_list");
						for(int i=0;i<jsonArray.length();i++){
							FriendBean bean = new FriendBean();
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							bean.setUser_id(jsonObj.getString("user_id"));
							bean.setUser_fname(jsonObj.getString("user_fname"));
							bean.setUser_lname(jsonObj.getString("user_lname"));
							bean.setImage_url(jsonObj.getString("image_url"));
							bean.setFriend_fb_id(jsonObj.getString("friend_fb_id"));
							bean.setIs_app_user(jsonObj.getString("is_app_user"));
							freindList.add(bean);
						}
						Collections.reverse(freindList);
						AppConstants.setFacebookFriendList(freindList);
						if(mListener!=null)
						mListener.onSuccess(freindList,"Successfull");
					} else {
						if(mListener!=null)
						mListener.onError("error");
					}
				} catch (Exception e) {
					e.printStackTrace();
					if(mListener!=null)
					mListener.onError("");
				}
			}
		}
	}
	
	public interface GetFriendListListener{
		
		public void onSuccess(ArrayList<FriendBean> list, String msg);
		public void onError(String msg);
		
	}
	
}