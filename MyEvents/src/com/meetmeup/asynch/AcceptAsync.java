package com.meetmeup.asynch;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.internal.m;
import com.meetmeup.fragment.EventDetailFragement.AcceptRejectListner;
import com.meetmeup.fragment.HomeFragment.AcceptRejectListnerHome;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

/**
 * @author administrator this is the async task for getting or sending data to
 *         the server occording to WebService
 */
public class AcceptAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	AcceptRejectListner mlistner;
	AcceptRejectListnerHome nListener;
	MultipartEntity multipart;

	public AcceptAsync(Context ct, AcceptRejectListner mlistner, MultipartEntity multi) {
		
		mContext = ct;
		multipart = multi;
		this.mlistner = mlistner;
		
	}

	public AcceptAsync(Context ct, AcceptRejectListnerHome mlistner, MultipartEntity multi) {
		
		mContext = ct;
		multipart = multi;
		this.nListener = mlistner;
		
	}

	@Override
	protected String doInBackground(Void... params) {
	
		try {
			
			return HttpRequest.post(WebServices.Accept_Reject, multipart);
			
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
			if (mlistner != null)
				mlistner.onError(AppConstants.networkError);
			else if (nListener != null)
				nListener.onError(AppConstants.networkError);
		} else {
			if (Utill.isStringNullOrBlank(result)) {
				// Utill.showServerError(mContext);
				if (mlistner != null)
					mlistner.onError(AppConstants.networkError);
				else if (nListener != null)
					nListener.onError(AppConstants.networkError);
			
			} else {
				
				try {
					
					JSONObject json = new JSONObject(result);
					String response = json.getString("status");
					
					if (response.equalsIgnoreCase("false")) {
						
						String msg = json.getString("msg");
						if (mlistner != null)
							mlistner.onError(msg);
						else if (nListener != null)
							nListener.onError(msg);
					
					} else if (response.equals("true")) {
						
						String msg = json.getString("msg");
						
						if (mlistner != null)
							mlistner.onSuccess(msg);
//							mlistner.onSuccess("Successfully Joined");
						
						else if (nListener != null)
							nListener.onSuccess(msg);
//							nListener.onSuccess("Successfull Joined");
						
						
					} else {
						
						if (mlistner != null)
							mlistner.onError("error");
						else if (nListener != null)
							nListener.onError("error");
						
				   }
					
					
				} catch (Exception e) {
					
					e.printStackTrace();
					if (mlistner != null)
						mlistner.onError("");
					else if (nListener != null)
						nListener.onError("");
					// new SendErrorAsync(mContext, e).execute();
					
			  
			   }
			
			}
		
		}
	
	}
	
}