package com.meetmeup.asynch;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.meetmeup.activity.SelectAddressBYTagActivity.NearPlacesListener;
import com.meetmeup.bean.Place;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;



//This class is used for searching location based on lat,long or based on tag. 
public class SerachNearByLocationTask extends AsyncTask<Void, Void, String>{ 

	Context mContext;
	String requestParams;
	boolean isNetworkError = false;
	NearPlacesListener mListener;
	int requestType;
	String response;

	//constructor for intializing data.
	public SerachNearByLocationTask(Context ct,String urlData,NearPlacesListener listener,int type){
		mContext = ct;
		requestParams = urlData;
		mListener = listener;
		requestType = type;
	}

	
	//This method will be send a request to the server.
	@Override
	protected String doInBackground(Void... params) {
		try {
			String url;
			if(requestType==1){
				url = WebServices.SERACH_NEAR_PLACES_BYTAG;
			}
			else{
				url = WebServices.SERACH_NEAR_PLACES;
			}
			String mURL = url+requestParams;
			return HttpRequest.post(mURL);
		} catch (Exception e) {
			e.printStackTrace();
			isNetworkError = true;
		}
		return null;
	}

//This method will be called on server operation completed.
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result); 
		Log.e("location response",""+result);
		if(result!=null){
			if(!isNetworkError){

				try { 
					
					JSONObject json = new JSONObject(result);
					JSONArray jArray = json.getJSONArray("results");
					ArrayList<Place> nearbyList = new ArrayList<Place>(); 
					for(int m=0;m<jArray.length();m++){
						
					   try{
						
						Place place = new Place();
						JSONObject locationJson = jArray.getJSONObject(m);

						JSONObject geomatery = locationJson.getJSONObject("geometry");
						JSONObject location = geomatery.getJSONObject("location");

						if(locationJson.has("vicinity")){
							
							place.setFormattendAddress(locationJson.optString("vicinity"));
							
						}else
						{
							place.setFormattendAddress(locationJson.optString("formatted_address"));
						}
						place.setLat(location.getDouble("lat"));
						place.setLonge(location.getDouble("lng"));
						place.setIconUrl(locationJson.getString("icon"));
						place.setName(locationJson.getString("name"));
						place.setId(locationJson.getString("id"));
						nearbyList.add(place);
						
					 }catch(Exception e){
						 
						 e.printStackTrace();
						 
					 }
						
					}
					if(jArray.length() ==0){
//						Utill.showToast(mContext, "No Asset has found.");
						mListener.onError("");
					}
					else if(mListener!=null){
						mListener.onSuccess(nearbyList);  
					}
				} catch (JSONException e) {
					e.printStackTrace();
					if(mListener!=null){
						mListener.onError("internate error");
					}
				}catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
			}else
			{
				if(mListener!=null){
					
					mListener.onError("internet error");
					
				}
				
			}
		}
	}
	

	
}