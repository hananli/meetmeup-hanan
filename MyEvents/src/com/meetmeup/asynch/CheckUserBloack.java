package com.meetmeup.asynch;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.activity.DashBoard.CheckUserListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

/**
 * @author administrator
 * this is the async task for getting or sending data to the server occording to WebService
 */
public class CheckUserBloack extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	CheckUserListener mlistner;
	MultipartEntity multipart;

	public CheckUserBloack(Context ct, CheckUserListener mlistner,MultipartEntity multi) {
		mContext = ct;
		multipart = multi;
		this.mlistner = mlistner; 
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_CHECK_USER, multipart);
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
			if(mlistner!=null)
				mlistner.onError(AppConstants.networkError);
		} else {
			if (Utill.isStringNullOrBlank(result)) {
				mlistner.onError(AppConstants.networkError);
			} else {
				try {
					JSONObject json = new JSONObject(result);

					String response = json.getString("status");
					if (response.equalsIgnoreCase("false")) {
						String msg = json.getString("msg");
						mlistner.onError(msg);
					} else if (response.equals("true")) {
					    String userStatus = json.getString("active_status");
						if(userStatus.equalsIgnoreCase("0")){
							mlistner.onSuccess("Successfull");	
						}else if(userStatus.equalsIgnoreCase("1")){
							String msg = json.getString("msg");
							String activeDatedate = json.getString("date_of_activation");
							mlistner.onBloack(msg,activeDatedate);	
						}else{
							mlistner.onError("error");
						}
					} else {
						mlistner.onError("error");
					}
				} catch (Exception e) {
					e.printStackTrace();
					mlistner.onError("");
					//	new SendErrorAsync(mContext, e).execute();
				}
			}
		}
	}
}