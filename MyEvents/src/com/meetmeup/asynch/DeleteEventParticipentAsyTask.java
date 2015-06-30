package com.meetmeup.asynch;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.fragment.HomeFragment.DeleteEventParticipentListener;
import com.meetmeup.fragment.PreferencesFragment.SetPreferencesListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;


/*this class hide the event of prticipents from dashboard list*/
public class DeleteEventParticipentAsyTask extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	DeleteEventParticipentListener mListener;
	MultipartEntity multipart;

	public DeleteEventParticipentAsyTask(Context ct, DeleteEventParticipentListener listener, MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		
		try {
			
			return HttpRequest.postMultipartEntity(WebServices.DELETE_EVENT_PARTICIPENT, multipart);
			
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
