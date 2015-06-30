package com.meetmeup.asynch;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.fragment.CreateEventFragement.CreateEventListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;
//This class is used for sending newly create event data to the server.
public class CreateEventAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	CreateEventListener mListener;
	MultipartEntity multipart;

	public CreateEventAsync(Context ct, CreateEventListener listener,
			MultipartEntity multi) {
		mContext = ct;
		mListener = listener;
		multipart = multi;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_CREATE_EVENT, multipart);
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
			// Utill.showNetworkError(mContext);
			if (mListener != null)
				mListener.onError(AppConstants.networkError);
	
		} else {
			
			if (Utill.isStringNullOrBlank(result)) {
				// Utill.showServerError(mContext);
				mListener.onError(AppConstants.networkError);
			
			} else {
				
				try {
					
					JSONObject json = new JSONObject(result);
					
					String response = json.getString("status");
					
					if (response.equalsIgnoreCase("false")) {
					
						String msg = json.getString("msg");
						mListener.onError(msg);
						
					} else if(response.equalsIgnoreCase("true")) {
						
						mListener.onSuccess("Successfull");
						
					} else {
						
						mListener.onError("error");
						
				   }
					
				} catch (Exception e) {
					
					e.printStackTrace();
					mListener.onError(""+e);
					
				}
			}
		}
	}
}