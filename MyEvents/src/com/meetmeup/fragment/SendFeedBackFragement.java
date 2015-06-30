package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.FeedBackAdapter;
import com.meetmeup.adapters.RattingAdapter;
import com.meetmeup.asynch.SendRattingAsync;
import com.meetmeup.asynch.SendYellowCardAsync;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.helper.Utill;

//This class is used for sending yello card,to those invities who have accepted event invitation for three times and they not appear int the event.
public class SendFeedBackFragement extends Fragment {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	// public static ArrayList<ParticipantsBean> participantsList;
	public static EventsBean eventBean;
	// public static String eventName = "";
	TextView rattingTV, feedBackTV, eventTitle;
	Button sendBTN;
	ListView listView;
	public static Activity mActivity;
	static DashBoard dashBoard;

	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new SendFeedBackFragement();
		}
		return mfFragment;
	}

	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("Event Feed Back",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		eventTitle.setText(eventBean.getEvent_title());
		super.onStart();
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.event_feedback_view, container, false);
		dashBoard = (DashBoard) mActivity;
		initializeView(view);
		setOnClickeListeners();
		rattingTV.performClick();
//		setAdapterAccordingToId();
		return view;
	}

	
	
	void initializeView(View view) {
		
		rattingTV = (TextView) view.findViewById(R.id.ratting);
		feedBackTV = (TextView) view.findViewById(R.id.feedback);
		eventTitle = (TextView) view.findViewById(R.id.event_title);
		sendBTN = (Button) view.findViewById(R.id.send);
		listView = (ListView) view.findViewById(R.id.list);
	}

	public void setOnClickeListeners() {
		
		rattingTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
//				rattingTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selected_bg));
//				feedBackTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.unselect_bg));
				LIST_ID = RATTING;
				setAdapterAccordingToId();
				
			}
		});
		feedBackTV.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				feedBackTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.selected_bg));
//				rattingTV.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.unselect_bg));
				LIST_ID = FEEDBACK;
				setAdapterAccordingToId();
			}
		});
		eventTitle.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

			}
		});
		;
		sendBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (LIST_ID == RATTING) {
					sendRatting();
				} else if (LIST_ID == FEEDBACK) {
					sendFeedBack();
				}
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
	}

	
	// this is participants dialogue
	 void showRatting(EventsBean eventBean) {
				
				try {
									
					ArrayList<ParticipantsBean> ptlist = eventBean.getParticipantsList();
					
					if(ptlist != null){
					
					 RattingAdapter.rattingArray = new float[eventBean.getParticipantsList().size()];
					 for (int i = 0; i < eventBean.getAcceptedParticipantsList().size(); i++) {
						String ratStr = eventBean.getAcceptedParticipantsList().get(i).getRating();
						if (!Utill.isStringNullOrBlank(ratStr)) {
							float rat = Float.parseFloat(ratStr);
							RattingAdapter.rattingArray[i] = rat;
						}
					 }

					  RattingAdapter adapter = new RattingAdapter(mContext, eventBean.getAcceptedParticipantsList(), 0);
					  listView.setAdapter(adapter);
					  
//					  listView.setAdapter(adapter);
					 
					  adapter.notifyDataSetChanged();
					  
					}
//					else{
//					  
//						Toast.makeText(mContext, "No Rating available",Toast.LENGTH_SHORT).show();
//					   
//				   }

				   sendBTN.setText("SEND RATTING");	
				  
					
				} catch (Exception e) {
					
					e.printStackTrace();
					
				}
				
			}
	
	
	
	int LIST_ID = 0;
	public static final int RATTING = 1;
	public static final int FEEDBACK = 2;

	void setAdapterAccordingToId() {
		if (LIST_ID == RATTING) {
			rattingTV.setBackgroundColor(getResources().getColor(com.meetmeup.activity.R.color.selectedd));
			feedBackTV.setBackgroundColor(getResources().getColor(com.meetmeup.activity.R.color.unselectedd));
			
			showRatting(eventBean);
			
//			if(eventBean.getAcceptedParticipantsList() != null){
//			
//				RattingAdapter adapter = new RattingAdapter(mContext,eventBean.getAcceptedParticipantsList(), 0);
//
////			  if(eventBean.getFeedBackStatus() != null && eventBean.getFeedBackStatus().equalsIgnoreCase("1")){
//				
//				if(RattingAdapter.rattingArray != null){
//			
//					for(int i = 0;i<RattingAdapter.rattingArray.length;i++){
//					String ratStr = eventBean.getAcceptedParticipantsList().get(i).getRating();
//					if(!Utill.isStringNullOrBlank(ratStr))
//						RattingAdapter.rattingArray[i] = Float.parseFloat(ratStr);
//					
//				 }
//					
////			   }
//			}
			
//			listView.setAdapter(adapter);
//			sendBTN.setText("SEND RATTING");
//			adapter.notifyDataSetChanged();
//		  }
			
		} else if (LIST_ID == FEEDBACK) {
			
			if( eventBean.getAcceptedParticipantsList() != null){
			
				rattingTV.setBackgroundColor(getResources().getColor(com.meetmeup.activity.R.color.unselectedd));
			    feedBackTV.setBackgroundColor(getResources().getColor(com.meetmeup.activity.R.color.selectedd));
			    FeedBackAdapter adapter = new FeedBackAdapter(mContext, eventBean.getAcceptedParticipantsList());
			    listView.setAdapter(adapter);
			    sendBTN.setText("SEND YELLOW CARD");
			    adapter.notifyDataSetChanged();
			
		   }
			
		}
	}

	void sendFeedBack() {
		if (Utill.isStringNullOrBlank(getYellowCardsId())) {
			Utill.showToast(mContext, "No particpant selected.");
			return;
		}
		Utill.showToast(mContext, "send feedback");
		if (Utill.isNetworkAvailable(mContext)) {
			MultipartEntity multi = new MultipartEntity();
			try {
				multi.addPart("event_id", new StringBody(eventBean.getEvent_id()));
				multi.addPart("user_list", new StringBody(getYellowCardsId()));
				showProgress();
				new SendYellowCardAsync(mContext, new YellowCardListener(), multi).execute();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			
			Utill.showNetworkError(mContext);
			
		}
	}

	void sendRatting() {
		
		String participants_rating = getParticipantsRatting();
		
		if (Utill.isNetworkAvailable(mContext)) {
			
			MultipartEntity multi = new MultipartEntity();
			try {
				multi.addPart("event_id", new StringBody(eventBean.getEvent_id()));
				multi.addPart("user_list", new StringBody(participants_rating));
				showProgress();
				new SendRattingAsync(mContext, new RattingListener(), multi).execute();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}

		} else {
			
			Utill.showNetworkError(mContext);
			
		}
	}

	String getYellowCardsId() {
		JSONArray jArray = new JSONArray();
		if (FeedBackAdapter.cheackStatus != null) {
			for (int i = 0; i < FeedBackAdapter.cheackStatus.length; i++) {
				JSONObject jObject = new JSONObject();
				try {
					if(FeedBackAdapter.cheackStatus[i]){
						jObject.put("id", eventBean.getAcceptedParticipantsList().get(i).getUser_id());
						jArray.put(jObject);
					}
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jArray.toString();
	
	}

	String getParticipantsRatting() {
		JSONArray jArray = new JSONArray();
		if (RattingAdapter.rattingArray != null) {
			for (int i = 0; i < RattingAdapter.rattingArray.length; i++) {
				JSONObject jObject = new JSONObject();
				try {
					jObject.put("id", eventBean.getAcceptedParticipantsList().get(i).getUser_id());
					jObject.put("rating", RattingAdapter.rattingArray[i]);
					jArray.put(jObject);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return jArray.toString();
	}

	public class RattingListener {
		public void onSuccess(String msg) {
			Utill.showToast(mContext, "Rated successfully.");
			hideProgress();
		//	dashBoard.backStackPress();
			dashBoard.popFromBackStack();
		}

		public void onError(String msg) {
			hideProgress();
		}
	}

	
	//This is the listener class for notifiy about yellow card sent or not.
	public class YellowCardListener {
		public void onSuccess(String msg) {
			Utill.showToast(mContext, "Yellow card sent.");
			hideProgress();
			dashBoard.popFromBackStack();
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

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
}
