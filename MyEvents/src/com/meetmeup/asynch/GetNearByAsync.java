package com.meetmeup.asynch;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import com.meetmeup.adapters.NearByAdapter;
import com.meetmeup.bean.FriendBean;
//import com.meetmeup.fragment.CreateEventFragement.GetNearByListListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;



//This class is used for getting list of the nearBy people's of the current user's.
public class GetNearByAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	GetNearByListListener mListener;
	MultipartEntity multipart;

	public GetNearByAsync(Context ct, GetNearByListListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.GET_NEAR_BY, multipart);
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
						mListener.onError(msg);
				
					} else if (response.equalsIgnoreCase("true")) {
					
						ArrayList<FriendBean> freindList = new ArrayList<FriendBean>();
						JSONArray jsonArray = json.getJSONArray("user_id_list");
						
						for(int i=0;i<jsonArray.length();i++){
							
							FriendBean bean = new FriendBean();
							JSONObject jsonObj = jsonArray.getJSONObject(i);
							bean.setUser_id(jsonObj.getString("user_id"));
							bean.setUser_fname(jsonObj.getString("f_name"));
							bean.setUser_lname(jsonObj.getString("l_name"));
							bean.setImage_url(jsonObj.getString("image_url"));
							bean.setIs_app_user(jsonObj.getString("is_app_user"));
							bean.setFriend_fb_id(jsonObj.getString("fb_id"));
							freindList.add(bean);
							
						}
						
						Collections.reverse(freindList);
						AppConstants.setNearByList(freindList);
						
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
	
	
	public interface GetNearByListListener{
		
		public void onSuccess(ArrayList<FriendBean> list, String msg);
		public void onError(String msg);
		
	}
}