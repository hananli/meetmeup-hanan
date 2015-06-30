package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.ChatAdapter;
import com.meetmeup.asynch.GetChatAsync;
import com.meetmeup.asynch.SendChatAsync;
import com.meetmeup.bean.ChatBean;
import com.meetmeup.bean.ChatInfoBean;
import com.meetmeup.helper.Utill;
import com.meetmeup.interfac.RefreshChatGroup;

//This class is used for Group chat for a perticular event.
public class ChatFragement extends Fragment implements RefreshChatGroup {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	static DashBoard dashBoard;
	ListView listView;
	EditText messageET;
	ImageView sendBtn;
	ArrayList<ChatBean> ChatList;
	ChatAdapter adapter;
	static Activity mActivity;
	public static ChatInfoBean chatInfoBean;
	public static String eventID;
	public static String eventName;
	static boolean bottom = false;
	TextView eventTitleTV;

	// boolean status = false;

	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		dashBoard = (DashBoard) mContext;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new ChatFragement();
		}
		initializeChatObj();
		return mfFragment;
		
	}

	static void initializeChatObj() {
		
		chatInfoBean = new ChatInfoBean();
		chatInfoBean.setEventID(eventID);
		chatInfoBean.setUserID(Utill.getUserPreferance(mContext).getUser_id());
		
		chatInfoBean.setEventName(eventName);
		
	}

	@Override
	public void onStart() {
		
		if (DashBoard.actionBar != null) {
			
			if(eventName != null && !eventName.isEmpty()){
			
				DashBoard.resetActionBarTitle(eventName,-2);
				
			}else{
		     
				DashBoard.resetActionBarTitle("Chating",-2);
				
			}
			
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}

		super.onStart();

	}

	Handler mHandler;
	Runnable mUpdateResults;
	Timer timer;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.chat, container, false);
		initializeView(view);
		setOnClickeListeners();
		mHandler = new Handler();
		// Create runnable for posting
		mUpdateResults = new Runnable() {
			public void run() {
				getChat();
			}
		};
		int delay = 1000; // delay for 1 sec.
		int period = 6000; // repeat every 6 sec.
		timer = new Timer();
		showProgress();
		timer.scheduleAtFixedRate(new TimerTask() {
			public void run() {
				mHandler.post(mUpdateResults);
			}
		}, delay, period);
		bottom = true;
		return view;
	}

	void getChat() {
		if (Utill.isNetworkAvailable(mContext)) {
			try {
				MultipartEntity multi = new MultipartEntity();
				multi.addPart("event_id", new StringBody(eventID));
				new GetChatAsync(mContext, new ChatGetListener(), multi)
						.execute();
			} catch (Exception e) {

			}
		} else {
			Utill.showNetworkError(mContext);
		}
	}

	void initializeView(View view) {
		listView = (ListView) view.findViewById(R.id.list);
		messageET = (EditText) view.findViewById(R.id.chat_message);
		sendBtn = (ImageView) view.findViewById(R.id.send_btn);
		ChatList = new ArrayList<ChatBean>();
		adapter = new ChatAdapter(mContext, ChatList);
		listView.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}

	public void setOnClickeListeners() {
		listView.setOnTouchListener(new OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				Utill.hideSoftKeyboard(mActivity);
				return false;
			}
		});
		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentManager.getBackStackEntryCount() > 0) {
					mFragmentManager.popBackStack();
				}
			}
		});

		sendBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String chatMsg = messageET.getText().toString();
				if (Utill.isNetworkAvailable(mContext)) {
					if (!Utill.isStringNullOrBlank(chatMsg)) {
						ChatBean bean = new ChatBean();
						try
						{
						//    chatMsg = new String(chatMsg.getBytes(), "UTF-8");
						    chatMsg = URLDecoder.decode(chatMsg, "UTF-8");
						}
						catch (Exception e)
						{
						    Log.e("utf8", "conversion", e);
						}
						bean.setMsg(chatMsg);
						bean.setImageUrl(Utill.getUserPreferance(mContext)
								.getProfile_pic_url());
						bean.setSenderName("Me");
						bean.setChatId("");
						ChatList.add(bean);
						adapter.notifyDataSetChanged();
						sendToServer();
						messageET.setText("");
						
//						Utill.hideSoftKeyboard(mActivity);
						
					} else {
						Utill.showToast(mContext, "Please text something.");
					}
				} else {
					Utill.showNetworkError(mContext);
				}
			}
		});

	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
	
/*	@SuppressLint("NewApi") private void sendChatMessage(int opponentId){
		MultipartEntity multipartEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName(HTTP.UTF_8));
		try {
			multipartEntity.addPart("customer_id", new StringBody(customer_id));
			multipartEntity.addPart("sh_r_id", new StringBody(sh_r_id1));
			multipartEntity.addPart("sh_id", new StringBody(""+SessionManager.getSignIn(mContext).getId()));
			multipartEntity.addPart("send_user_type", new StringBody(""+SessionManager.getSignIn(mContext).getUsertype()));
			multipartEntity.addPart("message", new StringBody(inputbox.getText().toString(),Charset.forName("UTF-8"))); 

			AppInfoMessage.active_customer = Integer.valueOf(customer_id); 

			if(!cd.isConnectingToInternet()){
				AppInfoMessage.ShowToastMessage(AppInfoMessage.internet_error, mContext);
			}else{
				new SendMessageToShopkeeperAsyTask(mContext, multipartEntity,new ChatToCustomer(1)).executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
			}

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(); 
		}
	} */
	

	void sendToServer() {
		if (Utill.isNetworkAvailable(mContext)) {
		//	MultipartEntity multi = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName(HTTP.UTF_8));
			MultipartEntity multi = new MultipartEntity();
			try {
				multi.addPart("sender_id", new StringBody(Utill
						.getUserPreferance(mContext).getUser_id()));
				multi.addPart("event_id", new StringBody(eventID));
				String chatMsg = messageET.getText().toString();
				try
				{
				    //chatMsg = new String(chatMsg.getBytes(), "UTF-8");
				    chatMsg = URLDecoder.decode(chatMsg, "UTF-8");
				}
				catch (UnsupportedEncodingException e)
				{
				    Log.e("utf8", "conversion", e);
				}
				
				multi.addPart("chat_message", new StringBody(chatMsg,Charset.forName("UTF-8"))); 
		//		multi.addPart("chat_message", new StringBody(chatMsg));
				
				
				// status = true;
				new SendChatAsync(mContext, new ChatSentListener(), multi)
						.execute();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			Utill.showNetworkError(mContext);
		}
	}

	public class ChatSentListener {
		public void onSuccess(String msg) {
			// status =false;
			getChat();
		}

		public void onError(String msg) {
			// status =false;
		}
	}

	public class ChatGetListener {
	
		public void onSuccess(String msg, ArrayList<ChatBean> list) {
			
//			DashBoard.resetActionBarTitle(msg);
			
			boolean scroll = false;
			int index = listView.getFirstVisiblePosition();
			View v = listView.getChildAt(0);
			int top = (v == null) ? 0 : v.getTop();
			try {
				String previous = ChatList.get(ChatList.size() - 1).getChatId();
				String current = list.get(list.size() - 1).getChatId();
				if (!ChatList
						.get(ChatList.size() - 1)
						.getChatId()
						.equalsIgnoreCase(list.get(list.size() - 1).getChatId())) {
					scroll = true;
					
				}
			} catch (Exception e) {
				// TODO: handle exception
			
			}
			
			ChatList = list;

			ChatList = list;
			adapter = new ChatAdapter(mContext, ChatList);
			listView.setAdapter(adapter);
			adapter.notifyDataSetChanged();

			
//			if (scroll || bottom){
//				
//				scrollMyListViewToBottom();
//				bottom = false;
//				
//			}
//			else {
//
//				listView.setSelectionFromTop(index, top);
//				
//			}

			hideProgress();
		}

		public void onError(String msg) {
			hideProgress();
		}
	}

	ProgressDialog progress;

	// This method is used for showing progress bar
	public void showProgress() {
		try {
			if (progress == null)
				progress = new ProgressDialog(mActivity);
			progress.setMessage("Please Wait..");
			progress.setCancelable(false);
			progress.show();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				progress = new ProgressDialog(mActivity);
				progress.setMessage("Please Wait..");
				progress.setCancelable(false);
				progress.show();
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
	}

	// This method is used for hiding progress bar.
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

	private void scrollMyListViewToBottom() {
		listView.post(new Runnable() {
			@Override
			public void run() {
				// Select the last row so it will scroll into view...
				listView.setSelection(adapter.getCount() - 1);

			}
		});
	}

	@Override
	public void onDestroy() {
		mHandler.removeCallbacks(mUpdateResults);
		timer.cancel();
		chatInfoBean = null;
		super.onDestroy();
	}

	@Override
	public void onDetach() {
		mHandler.removeCallbacks(mUpdateResults);
		timer.cancel();
		chatInfoBean = null;
		super.onDetach();
	}

	@Override
	public void onDestroyView() {
		mHandler.removeCallbacks(mUpdateResults);
		timer.cancel();
		chatInfoBean = null;
		super.onDestroyView();
	}

	@Override
	public void onChatRefresh() {
		initializeChatObj();
		bottom = true; 
		showProgress();
		getChat();
	}
	@Override
	public void onPause() {
		chatInfoBean = null;
		super.onPause();
	}
	@Override
	public void onResume() {
		initializeChatObj();
		super.onResume();
	}
	

}
