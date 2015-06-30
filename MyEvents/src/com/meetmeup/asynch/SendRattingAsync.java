package com.meetmeup.asynch;

import org.apache.http.entity.mime.MultipartEntity;
import org.json.JSONObject;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.meetmeup.fragment.SendFeedBackFragement.RattingListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

/**
 * @author administrator
 * this is the async task for getting or sending data to the server occording to WebService
 */
public class SendRattingAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	RattingListener mlistner;
	MultipartEntity multipart;

	public SendRattingAsync(Context ct, RattingListener mlistner,MultipartEntity multi) {
		mContext = ct;
		multipart = multi;
		this.mlistner = mlistner; 
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_RATE_PARTICIPANTS, multipart);
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
			if(mlistner!=null)
				mlistner.onError(AppConstants.networkError);
		} else {
			if (Utill.isStringNullOrBlank(result)) {
				//	Utill.showServerError(mContext);
				mlistner.onError(AppConstants.networkError);
			} else {
				try {
					JSONObject json = new JSONObject(result);
					String response = json.getString("status");
					if (response.equalsIgnoreCase("false")) {
						String msg = json.getString("msg");
						mlistner.onError(msg);
					} else if (response.equals("true")) {

						mlistner.onSuccess("Successfull");
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