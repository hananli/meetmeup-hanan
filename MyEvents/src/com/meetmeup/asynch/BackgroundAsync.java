package com.meetmeup.asynch;

import org.apache.http.entity.mime.MultipartEntity;

import android.content.Context;
import android.os.AsyncTask;

import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.WebServices;



// This class is used for updating user position in a fixed time interval.
public class BackgroundAsync extends AsyncTask<Void, Void, String> {

	Context mContext;
	boolean isNetworkError = false;
	MultipartEntity multipart;

	public BackgroundAsync(Context ct, MultipartEntity multipart) {
		mContext = ct;
		this.multipart = multipart;
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest
					.post(WebServices.USER_CURRENT_LATLONG, multipart);
		} catch (Exception e) {
			e.printStackTrace();
			isNetworkError = true;
		}
		return null;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
	}
}