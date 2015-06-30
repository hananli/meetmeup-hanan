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
import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.fragment.HomeFragment.GetEventListener;
import com.meetmeup.fragment.PreferencesFragment.GetPreferencesListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

public class GetPreferencesListAsyTask extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	GetPreferencesListener mListener;
	MultipartEntity multipart;

	public GetPreferencesListAsyTask(Context ct, GetPreferencesListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		
		try {
			return HttpRequest.post(WebServices.GET_PREFERENCES_LIST, multipart);
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
						
						JSONObject jsonListObj = json.getJSONObject("list");
						String user_id = jsonListObj.getString("user_id");
												
						JSONArray jsonArray;
						
						ArrayList<PreferenceBean> prefArrayList = new ArrayList<PreferenceBean>();
						
						
						if(jsonListObj.has("category_list")){
						 
							jsonArray = jsonListObj.getJSONArray("category_list");
					 
						   for(int i=0;i<jsonArray.length();i++){							
							   
							   JSONObject listjsonobj = jsonArray.getJSONObject(i);
							   
//							   prefArrayList.add(new PreferenceBean(listjsonobj.getString("category_id"),
//									   listjsonobj.getString("category_name"),
//									   listjsonobj.getString("status")));
							   					 
							 
							   prefArrayList.add(new PreferenceBean(listjsonobj.getString("category_id"),
									   listjsonobj.getString("category_name"),
									   listjsonobj.getString("status"),
									   listjsonobj.getString("category_image")));
						   }
					  }
									
					  mListener.onSuccess(prefArrayList,user_id);
					 
					} else {
						
						mListener.onError("error");
						
					}
				} catch (Exception e) {
					e.printStackTrace();
					mListener.onError("Error in json parsing");
				}
			}
		}
	}
}