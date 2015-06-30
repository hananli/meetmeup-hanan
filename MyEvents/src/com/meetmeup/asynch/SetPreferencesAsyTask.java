package com.meetmeup.asynch;

import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.fragment.PreferencesFragment.GetPreferencesListener;
import com.meetmeup.fragment.PreferencesFragment.SetPreferencesListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

public class SetPreferencesAsyTask extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	SetPreferencesListener mListener;
	MultipartEntity multipart;

	public SetPreferencesAsyTask(Context ct, SetPreferencesListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		
		try {
			return HttpRequest.postMultipartEntity(WebServices.SET_PREFERENCES_LIST, multipart);
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
								
						String msg = json.getString("msg");
					    mListener.onSuccess(msg);
					 
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