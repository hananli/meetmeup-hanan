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

import com.meetmeup.bean.ChatBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.fragment.ChatFragement.ChatGetListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.HttpRequest;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WebServices;

/**
 * @author administrator
 * this is the async task for getting or sending data to the server occording to WebService
 */
public class GetChatAsync extends AsyncTask<Void, Void, String> {

	
	Context mContext;
	boolean isNetworkError = false;
	ChatGetListener mlistner;
	MultipartEntity multipart;

	public GetChatAsync(Context ct, ChatGetListener mlistner,MultipartEntity multi) {
		mContext = ct;
		multipart = multi;
		this.mlistner = mlistner; 
	}

	@Override
	protected String doInBackground(Void... params) {
		try {
			return HttpRequest.post(WebServices.WEB_GET_CHAT, multipart);
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
						String EventTitle = "()";
						if(json.has("event_title"))
							EventTitle = json.getString("event_title")+"(Chat)";
						else
							EventTitle = "Event's Chat";
						
						
						ArrayList<ChatBean> chatList  = new ArrayList<ChatBean>();
						if(json.has("event_chat_list")){
							
							JSONArray jArray = json.getJSONArray("event_chat_list");
							UserBean user = Utill.getUserPreferance(mContext);
							
							for(int i=0;i<jArray.length();i++){
							
								JSONObject jtemp = jArray.getJSONObject(i);
								ChatBean chatBean = new ChatBean();
								chatBean.setChatId(jtemp.getString("chat_id"));
								chatBean.setSenderId(jtemp.getString("sender_id"));
								chatBean.setSenderName(jtemp.getString("f_name"));
								chatBean.setImageUrl(jtemp.getString("image"));
								chatBean.setMsgTime(jtemp.getString("msg_date_time"));
								chatBean.setMsg(jtemp.getString("chat_message"));
								
								try {
									
									String a = chatBean.getMsgTime();
									String t[] = a.split(" ");
									chatBean.setChatTime(Utill.getDateTime(t[0], t[1]));	
									
								} catch (Exception e) {
									
									e.printStackTrace();
									
								}
								
								// 2015-02-17 05:36:01
							
								
								if(user.getUser_id().equalsIgnoreCase(chatBean.getSenderId())){
									
									chatBean.setType(0);
									chatBean.setSenderName("Me");
									
								}else{
									
									chatBean.setType(1);
									
								}
								
								chatList.add(chatBean);
							}
						}
						
					/*	Collections.sort(chatList,new Comparator<ChatBean>() {
							@Override
							public int compare(ChatBean lhs, ChatBean rhs) {
								return lhs.getChatTime().compareTo(rhs.getChatTime());
							}
						});*/
						/*Collections.sort(chatList, new Comparator<ChatBean>() {
							  public int compare(ChatBean o1, ChatBean o2) {
							      return o1.getChatId().compareTo(o2.getChatId());
							  }
							});*/
						
						Collections.reverse(chatList);
						
						mlistner.onSuccess(EventTitle,chatList);
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