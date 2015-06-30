package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint.Join;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract.EventsEntity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.meetmeup.activity.AlarmReciever;
import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.MapActivity;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.EventAdapter;
import com.meetmeup.adapters.ParticipantAdapter;
import com.meetmeup.adapters.RattingAdapter;
import com.meetmeup.asynch.AcceptAsync;
import com.meetmeup.asynch.DeleteAsync;
import com.meetmeup.asynch.GetEventDetialAsync;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.meetmeup.interfac.RefreshEventDetail;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//This class is used for showing event detail.

@SuppressLint("NewApi")
public class EventDetailFragement extends Fragment implements RefreshEventDetail {
	
//	RoundedImageView profile_pic;
	DisplayImageOptions opt;
	ImageLoader image_load;
	ProgressBar progressOnProfilePic;
	public static String eventID;
	public static int index;
	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	public static Activity mActivity;
	DashBoard dashBoard;
	EventAdapter adapter;
	LinearLayout guestBTN, reminderBTN, declineBTN, paidBTN;

	ImageView homeIV, settingIV, createEventIV, myEventsIV, nearByEventsIV;

	
	LinearLayout layout_eventdetail_main;
	
	RelativeLayout chatRelative;

	TextView event_name, event_address, event_date, event_time, event_owner , event_nearby;
	TextView event_description, event_createdby, event_type, accept_btn, reject_btn, total, setAlarm, sendFeedBack;
	View view;
	String currentDateTimeString;
	RelativeLayout accept_reject_btn, relativeJoin;
	TextView sendMoneyBtn;
	ImageButton editBT, deleteBT, participantsBT, chatBT;

	// This method is used for instantiating current class object.
	public static Fragment getInstance(Context ct, FragmentManager fm) {

		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			
			mfFragment = new EventDetailFragement();
			
		}
		// eventID = null;
		return mfFragment;

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.new_event_detail, container, false);
		dashBoard = (DashBoard) mActivity;
		initializeView();
		setOnClickeListeners();
		getEventDetail();

		Calendar cal = Calendar.getInstance();
		currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
		// setAlarm();
		// scheduleAlarm(view);
		return view;

	}

	// This method is used for initializing Views.
	void initializeView() {

		layout_eventdetail_main = (LinearLayout)view.findViewById(R.id.layout_eventdetail_main);
		
		chatRelative = (RelativeLayout) view.findViewById(R.id.relativeChat);

		relativeJoin = (RelativeLayout) view.findViewById(R.id.relativeJoin);
		event_name = (TextView) view.findViewById(R.id.event_name);
		event_description = (TextView) view.findViewById(R.id.event_description);
		event_createdby = (TextView) view.findViewById(R.id.created_by);
		event_address = (TextView) view.findViewById(R.id.event_address);

		event_nearby = (TextView) view.findViewById(R.id.event_nearby);
//		event_guest = 
				
		event_owner = (TextView) view.findViewById(R.id.created_by);
		event_date = (TextView) view.findViewById(R.id.date);
		event_time = (TextView) view.findViewById(R.id.time);
		event_type = (TextView) view.findViewById(R.id.event_type);

		sendMoneyBtn = (TextView) view.findViewById(R.id.money_collection);

		guestBTN = (LinearLayout) view.findViewById(R.id.guestIV);
		reminderBTN = (LinearLayout) view.findViewById(R.id.reminderIV);
		declineBTN = (LinearLayout) view.findViewById(R.id.declineIV);
		paidBTN = (LinearLayout) view.findViewById(R.id.paidIV);

		/*
		 * accept_btn = (TextView) view.findViewById(R.id.accept); reject_btn =
		 * (TextView) view.findViewById(R.id.reject); sendMoneyBtn = (Button)
		 * view.findViewById(R.id.send_money); accept_reject_btn =
		 * (RelativeLayout) view.findViewById(R.id.accept_reject); total =
		 * (TextView) view.findViewById(R.id.total); editBT = (ImageButton)
		 * view.findViewById(R.id.edit); deleteBT = (ImageButton)
		 * view.findViewById(R.id.delete); participantsBT = (ImageButton)
		 * view.findViewById(R.id.participants); chatBT = (ImageButton)
		 * view.findViewById(R.id.chat); setAlarm = (TextView)
		 * view.findViewById(R.id.set_alarm); sendFeedBack = (TextView)
		 * view.findViewById(R.id.sendFeedBack);
		 * 
		 * profile_pic = (RoundedImageView) view.findViewById(R.id.profile);
		 * progressOnProfilePic = (ProgressBar)
		 * view.findViewById(R.id.progressbar);
		 */
	}
	

	// This method is used for set Click listenrs on the views.
	/**
	 * 
	 */
	OnClickListener allInOneClickListener  = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.relativeChat:
				
				break;

			default:
				break;
			}
		}
	};
	
	
	void setOnClickeListeners() {
		
		sendMoneyBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (eventBean != null) {
					
					SendMoneyFragement.eventBean = eventBean;
					dashBoard.swithFragment(DashBoard.FRAGMENT_SEND_MONEY);
				
				} else {
					
					Utill.showToast(mContext, "Error.");
					
			   }
			}
		});
		
		reminderBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				openCalendar();
				
			}
		});
		
		
		 event_address.setOnClickListener(new OnClickListener() {
			
			  @Override public void onClick(View v) { 
				
				  
				  if (eventBean != null && !Utill.isStringNullOrBlank(eventBean.getEvent_Address())) {
			    
					  try{
						  
			              Intent intent = new Intent(mActivity, MapActivity.class);
			              intent.putExtra("lat", eventBean.getEvent_lat());
			              intent.putExtra("lon", eventBean.getEvent_eventLong());
			              intent.putExtra("name", eventBean.getEvent_title());
			  
			              startActivity(intent);
					  
					  }catch(Exception e){
						  
						  e.printStackTrace();
						  
					  }
			  
	             }else{
	            	 
	            	 Toast.makeText(mContext,"The Event Owner Didn't Enter Any Address to this Event", Toast.LENGTH_LONG).show();
	             }
		     }
			  
		 });
		
		 event_nearby.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
		
				dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT);
				
			}
		});

		/*
		 * setAlarm.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { openCalendar(); //
		 * SetReminderPopUp(); } });
		 * 
		 * 
		 * event_address.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (eventBean != null) {
		 * Intent intent = new Intent(mActivity, MapActivity.class);
		 * intent.putExtra("lat", eventBean.getEvent_lat());
		 * intent.putExtra("lon", eventBean.getEvent_eventLong());
		 * intent.putExtra("name", eventBean.getEvent_title());
		 * 
		 * startActivity(intent);
		 * 
		 * } 
		 * else { Utill.showToast(mContext, "Some Error in Address."); } } });
		 * sendMoneyBtn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (eventBean != null) {
		 * SendMoneyFragement.eventBean = eventBean;
		 * dashBoard.swithFragment(DashBoard.FRAGMENT_SEND_MONEY); } else {
		 * Utill.showToast(mContext, "Error."); } } });
		 * DashBoard.leftButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if
		 * (mFragmentManager.getBackStackEntryCount() > 0) {
		 * mFragmentManager.popBackStack(); } } });
		 * DashBoard.rightButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // Utill.showToast(mContext,
		 * "Participants."); if (eventBean != null) { if
		 * (eventBean.getParticipantsList() != null &&
		 * eventBean.getParticipantsList().size() > 0) showParticipants(); else
		 * Utill.showToast(mContext, "No participants invited."); } else {
		 * Utill.showToast(mContext, "No participants invited."); }
		 * 
		 * } });
		 */

		// DashBoard.chatIcon.setOnClickListener(new OnClickListener() {
		// @Override
		// public void onClick(View v) {
		// Utill.showToast(mContext, "Group Chat.");
		// }
		// });

		 
		
		 relativeJoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				UserBean user = Utill.getUserPreferance(mContext);
				
				if (Utill.isNetworkAvailable(mContext)) {
					
					MultipartEntity multipart = new MultipartEntity();
					
					try {

						Log.e("Current date", "current date " + currentDateTimeString);
						
//						String event_id = HomeFragment.mainEventList.get(index).getEvent_id();
						
						String event_id = eventID;
						
						multipart.addPart("user_id", new StringBody(user.getUser_id()));
						multipart.addPart("event_id", new StringBody(event_id));
						multipart.addPart("current_date", new StringBody(currentDateTimeString));
						String status = "1";
						
						
						/*
						 * if (eventBean!=null &&
						 * eventBean.getEvent_type().equalsIgnoreCase("1")) {
						 * status = "2"; }else if (eventBean!=null &&
						 * eventBean.getEvent_type().equalsIgnoreCase("2")) {
						 * status = "1"; }
						 */multipart.addPart("status", new StringBody(status));

//						showProgress();
						new AcceptAsync(mContext, new AcceptRejectListner(), multipart).execute();
						
					} catch (UnsupportedEncodingException e) {
					
						e.printStackTrace();
						
					}catch (Exception e) {

						e.printStackTrace();
						
					}
					
				} else {
					
					Utill.showNetworkError(mContext);
									
			   }
			
			}
			
		});
		
		 
		
		chatRelative.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
//				Utill.showToast(mContext, "Chat");

				if (eventBean != null) {
					// ChatFragement.eventBean = eventBean;
					
					if (!ableTochat) {
						
						Utill.showToast(mContext, "Please accept event first.");
						return;
					
					}else if(eventBean.getEvent_chat_mode().equalsIgnoreCase("0")){
						
						Utill.showToast(mContext, "Event is Ended Before 24 or More hours");
						return;
						
					}
					
					ChatFragement.eventID = eventBean.getEvent_id();
					ChatFragement.eventName = eventBean.getEvent_title();
					dashBoard.swithFragment(DashBoard.FRAGMENT_CHAT);
					
					
				} else {
					
					Utill.showToast(mContext, "Error.");
				
				}
			}
			
		});
		
		guestBTN.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				try{
								   
				   if (eventBean.getParticipantsList() != null && eventBean.getParticipantsList().size() > 0) {
					
					   showParticipants();
			
				   }else {
					
					   Utill.showToast(mContext, "No participants invited.");
					
				  }
					
				}catch(Exception e){
					
					e.printStackTrace();
					
				}
				
			}
		
		});
		
		
		declineBTN.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				
				UserBean user = Utill.getUserPreferance(mContext);
				
				if (Utill.isNetworkAvailable(mContext)) {
					
					MultipartEntity multipart = new MultipartEntity();
					
					try {

						Log.e("Current date", "current date " + currentDateTimeString);
						
//						String event_id = HomeFragment.mainEventList.get(index).getEvent_id();
						
						String event_id = eventID;
						
						multipart.addPart("user_id", new StringBody(user.getUser_id()));
						multipart.addPart("event_id", new StringBody(event_id));
						multipart.addPart("current_date", new StringBody(currentDateTimeString));
						
						String status = "2";
						
						
						/*
						 * if (eventBean!=null &&
						 * eventBean.getEvent_type().equalsIgnoreCase("1")) {
						 * status = "2"; }else if (eventBean!=null &&
						 * eventBean.getEvent_type().equalsIgnoreCase("2")) {
						 * status = "1"; }
						 */multipart.addPart("status", new StringBody(status));

//						showProgress();
						new AcceptAsync(mContext, new AcceptRejectListner(), multipart).execute();
						
					} catch (UnsupportedEncodingException e) {
					
						e.printStackTrace();
						
					}catch (Exception e) {

						e.printStackTrace();
						
					}
					
				} else {
					
					Utill.showNetworkError(mContext);
					
				
			   }
				
				
			}
		});
		
		

		/*
		 * reject_btn.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { UserBean user =
		 * Utill.getUserPreferance(mContext); if
		 * (Utill.isNetworkAvailable(mContext)) { MultipartEntity multipart =
		 * new MultipartEntity(); try {
		 * 
		 * Log.e("Current date", "current date " + currentDateTimeString);
		 * String event_id =
		 * HomeFragment.mainEventList.get(index).getEvent_id();
		 * multipart.addPart("user_id", new StringBody(user.getUser_id()));
		 * multipart.addPart("event_id", new StringBody(event_id));
		 * multipart.addPart("current_date", new
		 * StringBody(currentDateTimeString)); multipart.addPart("status", new
		 * StringBody("2"));
		 * 
		 * showProgress(); new AcceptAsync(mContext, new AcceptRejectListner(),
		 * multipart).execute(); } catch (UnsupportedEncodingException e) {
		 * e.printStackTrace(); } } else { Utill.showNetworkError(mContext); }
		 * 
		 * } });
		 * 
		 * editBT.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { EditEventFragement.dataBean =
		 * eventBean; // Utill.showToast(mContext, "Edit"); if (eventBean !=
		 * null) { if (eventBean.isPassed()) { Utill.showToast(mContext,
		 * "Event time finished.can not Edit."); } else {
		 * dashBoard.swithFragment(DashBoard.FRAGMENT_EDIT_EVENT); } } else {
		 * Utill.showToast(mContext, "No data."); } } });
		 * 
		 * deleteBT.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * ConfirmDeleteDialogue();
		 * 
		 * }
		 * 
		 * });
		 * 
		 * participantsBT.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * openCaledar(); if (true) return;
		 * 
		 * // Utill.showToast(mContext, "Participants."); if (eventBean != null)
		 * { if (eventBean.getParticipantsList() != null &&
		 * eventBean.getParticipantsList().size() > 0) showParticipants(); else
		 * Utill.showToast(mContext, "No participants invited."); } else {
		 * Utill.showToast(mContext, "No participants invited."); }
		 * 
		 * } }); chatBT.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // Utill.showToast(mContext,
		 * "Chat");\
		 * 
		 * if (eventBean != null) { // ChatFragement.eventBean = eventBean; if
		 * (!ableTochat) { Utill.showToast(mContext,
		 * "Please accept event first."); return; }
		 * 
		 * ChatFragement.eventID = eventBean.getEvent_id();
		 * dashBoard.swithFragment(DashBoard.FRAGMENT_CHAT); } else {
		 * Utill.showToast(mContext, "Error."); } } });
		 * 
		 * sendFeedBack.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { if (eventBean != null &&
		 * eventBean.getAcceptedParticipantsList() != null &&
		 * eventBean.getAcceptedParticipantsList().size() != 0) { if
		 * (sendFeedBack.getText().toString().equalsIgnoreCase("View Feedback"))
		 * { showRatting(); // Utill.showToast(mContext, "Not yet rated."); }
		 * else { SendFeedBackFragement.eventBean = eventBean;
		 * dashBoard.swithFragment(DashBoard.FRAGMENT_FEEDBACK); } } else {
		 * Utill.showToast(mContext, "No participant Accept event."); }
		 * 
		 * } });
		 */
		
		

	}

	// this method show's confirm Dialgue to ask for Do you want to Delete or
	// not.
	private void ConfirmDeleteDialogue() {

		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.final_logout);
		dialog.setTitle("Do you want to Delete?");
		Button logout_yes = (Button) dialog.findViewById(R.id.logout_yes);
		Button logout_no = (Button) dialog.findViewById(R.id.logout_no);

		logout_yes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

				if (Utill.isNetworkAvailable(mContext)) {
					UserBean user = Utill.getUserPreferance(mContext);
					MultipartEntity multi = new MultipartEntity();
					try {
						multi.addPart("user_id", new StringBody(user.getUser_id()));
						multi.addPart("event_id", new StringBody(eventBean.getEvent_id()));
						showProgress();
						new DeleteAsync(mContext, new DeleteListener(), multi).execute();
					} catch (UnsupportedEncodingException e) {

						e.printStackTrace();
					}
				} else {
					Utill.showNetworkError(mContext);
				}

				dialog.dismiss();
			}
		});

		logout_no.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	public class DeleteListener {

		public void onSuccess(String msg) {
			hideProgress();
//			Utill.showToast(mContext, msg);
			if (mFragmentManager.getBackStackEntryCount() > 0) {
				mFragmentManager.popBackStack();
			}
		}

		public void onError(String msg) {
			hideProgress();
//			Utill.showToast(mContext, msg);
		}

	}

	// This method is used for getting Detail for a perticular Event.
	void getEventDetail() {
		UserBean user = Utill.getUserPreferance(mContext);
		if (user.getUser_id() == null) {
			Utill.showToast(mContext, "Error");
			return;
		}
		if (Utill.isNetworkAvailable(mContext)) {
			
			MultipartEntity multipart = new MultipartEntity();
		
			try {
				
				String event_id = eventID;// HomeFragment.filteredList.get(index).getEvent_id();
				multipart.addPart("user_id", new StringBody(user.getUser_id()));
				multipart.addPart("event_id", new StringBody(event_id));
				
				if(!isRefreshview)
				 showProgress();
				
				
				new GetEventDetialAsync(mContext, new GetEventDetailListener(), multipart).execute();
				
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();		
				
			}
		
		 } else {
			
			Utill.showNetworkError(mContext);
			
		}
		
	}

	EventsBean eventBean = null;

	// This is the Listener Class used for Notify after getting response from
	// the server.
	public boolean ableTochat = false;

	public class GetEventDetailListener implements com.meetmeup.asynch.GetEventDetialAsync.GetEventDetailListener{

		public void onSuccess(EventsBean bean, String msg) {
						
			layout_eventdetail_main.setVisibility(View.VISIBLE);
			
			eventBean = bean;
			
			if (bean.isMyEvent()) {
			{
					ableTochat = true;
					event_name.setText(bean.getEvent_title());
					event_date.setText(bean.getEvent_date());
					event_time.setText(bean.getEvent_time());
					event_type.setText(bean.getEvent_type());
					event_createdby.setText(bean.getEvent_createdby());
					
//					if(Utill.isStringNullOrBlank(bean.getEvent_Address())){
//						
//						event_address.setVisibility(View.GONE);
//						
//					}else{
					
						event_address.setText(bean.getEvent_Address());
						
//					}
					
						
					event_owner.setText(bean.getEvent_createdby());
					event_description.setText(bean.getEvent_description());
					
					declineBTN.setVisibility(View.GONE);
					paidBTN.setVisibility(View.GONE);
					relativeJoin.setVisibility(View.INVISIBLE);
					

					sendMoneyBtn.setVisibility(View.GONE);
					if (bean.getEvent_type().equalsIgnoreCase("1")) {
						event_type.setText("Public Event");
					} else {
						event_type.setText("Private Event");
					}
					hideProgress();

					if (true)
						return;

					{
						accept_reject_btn.setVisibility(View.GONE);
						accept_btn.setVisibility(View.GONE);
						reject_btn.setVisibility(View.GONE);
						editBT.setVisibility(View.VISIBLE);
						deleteBT.setVisibility(View.VISIBLE);
						sendMoneyBtn.setVisibility(View.GONE);

						// enable/disable of option for send review and
						// feedBack.
						if (bean.isPassed()) {
							
							sendFeedBack.setVisibility(View.VISIBLE);
							
						} else {
							
							sendFeedBack.setVisibility(View.GONE);
							
					  }

					}

					if (bean.isPassed()) {

						setAlarm.setVisibility(View.GONE);

					} else {

						setAlarm.setVisibility(View.VISIBLE);

					}

					ableTochat = true;
					
				}

			
				hideProgress();

			} else {
				
				event_name.setText(bean.getEvent_title());
				event_date.setText(bean.getEvent_date());
				event_time.setText(bean.getEvent_time());
				event_type.setText(bean.getEvent_type());
				event_createdby.setText(bean.getEvent_createdby());
				event_address.setText(bean.getEvent_Address());
				event_owner.setText(bean.getEvent_createdby());
				event_description.setText(bean.getEvent_description());
				if (bean.getEvent_type().equalsIgnoreCase("1")) {
				
					event_type.setText("Public Event");
			
				} else {
					
					event_type.setText("Private Event");
				}
				
				if (bean.getCollect_money_from_participants().equalsIgnoreCase("1")) {
					sendMoneyBtn.setVisibility(View.VISIBLE);
					sendMoneyBtn.setText("$" + bean.getAmount() + " Entry Fee");
				} else if (bean.getCollect_money_from_participants().equalsIgnoreCase("0")) {
					sendMoneyBtn.setVisibility(View.GONE);
				}
				
				if (bean.getParticipantsList() != null) {
					int count = 0;
					String accept_status = "";
					String transaction_status = "";
					for (int i = 0; i < bean.getParticipantsList().size(); i++) {
						ParticipantsBean myB = bean.getParticipantsList().get(i);
						String status = myB.getStatus();
						if (status.equalsIgnoreCase("1")) {
							count++;
						}
						if (Utill.getUserPreferance(mContext).getUser_id().equalsIgnoreCase(myB.getUser_id())) {
							accept_status = myB.getStatus();
							if (accept_status.equalsIgnoreCase("1")) {
								ableTochat = true;
	//							reject_btn.setVisibility(View.VISIBLE);
		//						accept_btn.setVisibility(View.GONE);
							} else if (accept_status.equalsIgnoreCase("2")) {
								ableTochat = false;
				//				accept_btn.setVisibility(View.VISIBLE);
					//			reject_btn.setVisibility(View.GONE);
							}

							transaction_status = myB.getEvent_transaction_status();
							if (bean.getCollect_money_from_participants().equalsIgnoreCase("1") && accept_status.equalsIgnoreCase("1")) {
								sendMoneyBtn.setVisibility(View.VISIBLE);
							} else {
								sendMoneyBtn.setVisibility(View.GONE);
							}
							if (transaction_status.equalsIgnoreCase("1")) {

								sendMoneyBtn.setVisibility(View.GONE);

							}
						}
					}
					
					try{
					
					  total.setText("Participants : " + count + "/" + bean.getParticipantsList().size());
					  
					}catch(Exception e){
						
						e.printStackTrace();
						
					}

				}
								

				hideProgress();


				if (true)
					return;

				UserBean user = Utill.getUserPreferance(mContext);
				 {

					if (bean.isPassed()) {
						accept_reject_btn.setVisibility(View.GONE);
						accept_btn.setVisibility(View.GONE);
						reject_btn.setVisibility(View.GONE);
						sendFeedBack.setText("View Feedback");
						sendFeedBack.setVisibility(View.VISIBLE);
					} else {
						accept_btn.setVisibility(View.VISIBLE);
						if (bean.getEvent_type().equalsIgnoreCase("1"))
							reject_btn.setVisibility(View.GONE);
						else
							reject_btn.setVisibility(View.VISIBLE);
						accept_reject_btn.setVisibility(View.VISIBLE);
					}

					editBT.setVisibility(View.GONE);
					deleteBT.setVisibility(View.GONE);
				}

				// for setting number of participant who are comming in event
				// and
				// total number of participants.8/10
				if (bean.getParticipantsList() != null) {
					int count = 0;
					String accept_status = "";
					String transaction_status = "";
					for (int i = 0; i < bean.getParticipantsList().size(); i++) {
						ParticipantsBean myB = bean.getParticipantsList().get(i);
						String status = myB.getStatus();
						if (status.equalsIgnoreCase("1")) {
							count++;
						}
						if (Utill.getUserPreferance(mContext).getUser_id().equalsIgnoreCase(myB.getUser_id())) {
							accept_status = myB.getStatus();
							if (accept_status.equalsIgnoreCase("1")) {
								ableTochat = true;
	//							reject_btn.setVisibility(View.VISIBLE);
		//						accept_btn.setVisibility(View.GONE);
							} else if (accept_status.equalsIgnoreCase("2")) {
								ableTochat = false;
				//				accept_btn.setVisibility(View.VISIBLE);
					//			reject_btn.setVisibility(View.GONE);
							}

							transaction_status = myB.getEvent_transaction_status();
							/*
							 * if (accept_status.equalsIgnoreCase("1")) {
							 * accept_reject_btn.setVisibility(View.GONE); }
							 */
							if (bean.getCollect_money_from_participants().equalsIgnoreCase("1") && accept_status.equalsIgnoreCase("1")) {
								sendMoneyBtn.setVisibility(View.VISIBLE);
							} else {
								sendMoneyBtn.setVisibility(View.GONE);
							}
							if (transaction_status.equalsIgnoreCase("1")) {

								sendMoneyBtn.setVisibility(View.GONE);

							}
						}
					}
					
				      try{					
					
					    total.setText("Participants : " + count + "/" + bean.getParticipantsList().size());
					    
				      }catch(Exception e){
					   
					    e.printStackTrace();
					    
				      }

				} else {

					total.setText("Participants : " + "0/0");

				}
				
				if (bean.isPassed()) {
					
					reminderBTN.setVisibility(View.GONE);
					
				} else {
					
					reminderBTN.setVisibility(View.VISIBLE);
					
				}
						
				return;
			}

			// if (bean.isMyEvent())
			{

				ableTochat = false;
				eventBean = bean;
				event_name.setText(bean.getEvent_title());
				event_date.setText(bean.getEvent_date());
				event_time.setText(bean.getEvent_time());
				event_type.setText(bean.getEvent_type());
				event_createdby.setText(bean.getEvent_createdby());
				event_address.setText(bean.getEvent_Address());
				event_owner.setText(bean.getEvent_createdby());
				event_description.setText(bean.getEvent_description());
				if (bean.getEvent_type().equalsIgnoreCase("1")) {
					event_type.setText("Public Event");
					// reject_btn.setVisibility(View.GONE);
				} else {
					event_type.setText("Private Event");
				}
				if (bean.getCollect_money_from_participants().equalsIgnoreCase("1")) {
					sendMoneyBtn.setVisibility(View.VISIBLE);
					sendMoneyBtn.setText("$" + bean.getAmount() + " Entry Fee");
				} else if (bean.getCollect_money_from_participants().equalsIgnoreCase("0")) {
					sendMoneyBtn.setVisibility(View.GONE);
				}

				hideProgress();
				// event_description.setText(bean.getEvent_description());

				if (true)
					return;

				// event_description.setText(bean.getEvent_description());

				if (bean.getCollect_money_from_participants().equalsIgnoreCase("1")) {
					sendMoneyBtn.setVisibility(View.VISIBLE);
				} else if (bean.getCollect_money_from_participants().equalsIgnoreCase("0")) {
					sendMoneyBtn.setVisibility(View.GONE);
				}

				if (bean.getEvent_type().equalsIgnoreCase("1")) {
					event_type.setText("Public");
					// reject_btn.setVisibility(View.GONE);
				} else {
					event_type.setText("Private");
				}

				UserBean user = Utill.getUserPreferance(mContext);

				if (bean.getEvent_owner_id().equalsIgnoreCase(user.getUser_id())) {
					accept_reject_btn.setVisibility(View.GONE);
					accept_btn.setVisibility(View.GONE);
					reject_btn.setVisibility(View.GONE);
					editBT.setVisibility(View.VISIBLE);
					deleteBT.setVisibility(View.VISIBLE);
					sendMoneyBtn.setVisibility(View.GONE);

					// enable/disable of option for send review and feedBack.
					if (bean.isPassed()) {
						sendFeedBack.setVisibility(View.VISIBLE);
					} else {
						sendFeedBack.setVisibility(View.GONE);
					}

				} else {

					if (bean.isPassed()) {
						accept_reject_btn.setVisibility(View.GONE);
						accept_btn.setVisibility(View.GONE);
						reject_btn.setVisibility(View.GONE);
						sendFeedBack.setText("View Feedback");
						sendFeedBack.setVisibility(View.VISIBLE);
					} else {
						accept_btn.setVisibility(View.VISIBLE);
						if (bean.getEvent_type().equalsIgnoreCase("1"))
							reject_btn.setVisibility(View.GONE);
						else
							reject_btn.setVisibility(View.VISIBLE);
						accept_reject_btn.setVisibility(View.VISIBLE);
					}

					editBT.setVisibility(View.GONE);
					deleteBT.setVisibility(View.GONE);
				}

				// for setting number of participant who are comming in event
				// and
				// total number of participants.8/10
				if (bean.getParticipantsList() != null) {
					
					int count = 0;
					try{
					 total.setText("0/" + bean.getParticipantsList().size());
					}catch(Exception e){
						e.printStackTrace();
					}
						
						
					String accept_status = "";
					String transaction_status = "";
					for (int i = 0; i < bean.getParticipantsList().size(); i++) {
						ParticipantsBean myB = bean.getParticipantsList().get(i);
						String status = myB.getStatus();
						if (status.equalsIgnoreCase("1")) {
							count++;
						}
						if (Utill.getUserPreferance(mContext).getUser_id().equalsIgnoreCase(myB.getUser_id())) {
							accept_status = myB.getStatus();
							if (accept_status.equalsIgnoreCase("1")) {

								ableTochat = true;
								reject_btn.setVisibility(View.VISIBLE);
								accept_btn.setVisibility(View.GONE);
							} else if (accept_status.equalsIgnoreCase("2")) {
								ableTochat = false;
								accept_btn.setVisibility(View.VISIBLE);
								reject_btn.setVisibility(View.GONE);
							}

							transaction_status = myB.getEvent_transaction_status();
							/*
							 * if (accept_status.equalsIgnoreCase("1")) {
							 * accept_reject_btn.setVisibility(View.GONE); }
							 */
							if (bean.getCollect_money_from_participants().equalsIgnoreCase("1") && accept_status.equalsIgnoreCase("1")) {
								sendMoneyBtn.setVisibility(View.VISIBLE);
							} else {
								sendMoneyBtn.setVisibility(View.GONE);
							}
							if (transaction_status.equalsIgnoreCase("1")) {

								sendMoneyBtn.setVisibility(View.GONE);

							}
							
						}
						
					}
					
					
					/*
					 * if (Utill.getUserPreferance(mContext).getUser_id().
					 * equalsIgnoreCase (eventBean.getEvetn_owner_id())) {
					 * ableTochat = true; }
					 */

					try{
					  total.setText("Participants : " + count + "/" + bean.getParticipantsList().size());
					}catch(Exception e){
						e.printStackTrace();
					}

				} else {

					total.setText("Participants : " + "0/0");

				}
				if (bean.isPassed()) {

					setAlarm.setVisibility(View.GONE);

				} else {

					setAlarm.setVisibility(View.VISIBLE);

				}
				if (Utill.getUserPreferance(mContext).getUser_id().equalsIgnoreCase(eventBean.getEvent_owner_id())) {

					ableTochat = true;

				}
			}
	
			
			hideProgress();
			

		}

		public void onError(String msg) {
			
//			dashBoard.swithFragment(DashBoard.FRAGMENT_HOME);
			try{
			 
				if(mFragmentManager != null)
			     mFragmentManager.popBackStackImmediate();
				
							
			}catch(Exception e){
				
				e.printStackTrace();
				
			}
			 
			hideProgress();
			
									
		}
	}

	public boolean isRefreshview = false;
	public class AcceptRejectListner {
		
		public void onSuccess(String msg) {
		
			isRefreshview = true;
//			hideProgress();
			getEventDetail();
			isRefreshview = false;
			Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
			
		}

		public void onError(String msg) {
			
			isRefreshview = false;
			Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
//			hideProgress();
			
		}

	}

	// This is the call back method.
	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("Event Details",-2);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
			DashBoard.rightButton.setImageResource(R.drawable.participants_icon);
			DashBoard.rightButton.setVisibility(View.GONE);
			// DashBoard.chatIcon.setVisibility(View.GONE);
		}
		super.onStart();
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}

	ProgressDialog progress;

	// This method is used for showing progress bar.
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

	// This method is used for hiding progressbar.
	public void hideProgress() {
		if (progress != null) {
			
			progress.dismiss();
			
		}
		
	}

	// this is participants dialogue
	void showParticipants() {
		
		try {
			
			final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
			LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View vi = li.inflate(R.layout.new_guestofowner, null, false); 
			dialog.setContentView(vi);
			LinearLayout linearBack = (LinearLayout) vi.findViewById(R.id.backLinear);
						
			LinearLayout layout_guesttype = (LinearLayout) vi.findViewById(R.id.layout_guesttype);
			
			ImageView guest_ownerimg = (ImageView)vi.findViewById(R.id.guest_ownerimg);
			
			TextView guest_ownername = (TextView)vi.findViewById(R.id.guest_ownername);
					
			TextView txt_owner  = (TextView)vi.findViewById(R.id.txt_owner);
			
			
			final TextView allguest = (TextView)vi.findViewById(R.id.allguest);
			final TextView participantguest = (TextView)vi.findViewById(R.id.participantguest);
			
			ProgressBar guestownerprogress = (ProgressBar)vi.findViewById(R.id.guestownerprogress);
				
			
			//opt = UniversalImageLoaderHelper.setImageOptions();
			DisplayImageOptions opt = UniversalImageLoaderHelper.setImageOptions();
			ImageLoader image_load = ImageLoader.getInstance();
			
			Utill.loadImage(mContext, opt, image_load, guestownerprogress,guest_ownerimg,eventBean.getEvent_owner_profile_url());
			
					
//			Utill.uploadImage(vi,guest_ownerimg.getId(),eventBean.getEvent_owner_profile_url(),
//					R.drawable.icon);
			
			guest_ownername.setText(eventBean.getEvent_createdby());			
						
			final ListView listView = (ListView) vi.findViewById(R.id.list);
			
			final GridView gridviewlist = (GridView) vi.findViewById(R.id.gridviewlist);
			
						
			boolean payable = false;
			
			if (eventBean.getCollect_money_from_participants().equalsIgnoreCase("1")) {
				
				payable = true;
				
			}
						
			// ParticipantAdapter adapter = new ParticipantAdapter(mContext,
			// eventBean.getParticipantsList(), payable);

			/* chnaged this adapter constructor */
			final ParticipantAdapter adapter = new ParticipantAdapter(mContext, eventBean, payable);
			
			
			UserBean user = Utill.getUserPreferance(mContext);
			if(eventBean.getEvent_owner_id().equals(user.getUser_id())){
				
				listView.setVisibility(View.VISIBLE);
				gridviewlist.setVisibility(View.GONE);
				listView.setAdapter(adapter);
				layout_guesttype.setVisibility(View.VISIBLE);
				
			}else{
				
				listView.setVisibility(View.GONE);
				gridviewlist.setVisibility(View.VISIBLE);
				gridviewlist.setAdapter(adapter);
				layout_guesttype.setVisibility(View.GONE);
				txt_owner.setText("EVENT FOUNDER");
			}
								
		
			linearBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			
            allguest.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					allguest.setBackground(mContext.getResources().getDrawable(R.drawable.selected_border_bg));
					allguest.setTextColor(mContext.getResources().getColor(R.color.white));
					participantguest.setBackground(mContext.getResources().getDrawable(R.drawable.unselect_border_bg));
					adapter.guesttype = 1;
//					adapter.notifyDataSetChanged();
					
					if(listView.getVisibility() == View.VISIBLE){
					  
						listView.setAdapter(null);
					    listView.setAdapter(adapter);
					    
					}else{
						
						gridviewlist.setAdapter(null);
						gridviewlist.setAdapter(adapter);
						
					}
					
				}
			});
			
			participantguest.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					participantguest.setBackground(mContext.getResources().getDrawable(R.drawable.selected_border_bg));
					allguest.setTextColor(mContext.getResources().getColor(R.color.chat_bg));
					allguest.setBackground(mContext.getResources().getDrawable(R.drawable.unselect_border_bg));
					adapter.guesttype = 2;
//					adapter.notifyDataSetChanged();
										
					if(listView.getVisibility() == View.VISIBLE){
					  listView.setAdapter(null);
					  listView.setAdapter(adapter);
					}else{
						
						gridviewlist.setAdapter(null);
						gridviewlist.setAdapter(adapter);
					}
					
				}
			});
			
					
			
			dialog.show();
		} catch (Exception e) {
			e.printStackTrace();

		}

	}

	public static final int OFF = 0;
	public static final int FIFTEEN_MIN = 1;
	public static final int THIRTEE_MIN = 2;
	public static final int TWO_HR = 3;

	public static final long MILLI_15MIN = 60 * 1000 * 15;// 5 * 1000;//
	public static final long MILLI_30MIN = 60 * 1000 * 30;// 20 * 1000;//
	public static final long MILLI_2HR = 60 * 1000 * 60 * 2;// 30 * 1000;//

	// by using this method we can set Alarm.
	public void scheduleAlarm(View V, int id, long interval) {
		// time at which alarm will be scheduled here alarm is scheduled at 1
		// day from current time,
		// we fetch the current time in milliseconds and added 1 day time
		// i.e. 24*60*60*1000= 86,400,000 milliseconds in a day
		Long time = new GregorianCalendar().getTimeInMillis() + 10 * 1000;

		// create an Intent and set the class which will execute when Alarm
		// triggers, here we have
		// given AlarmReciever in the Intent, the onRecieve() method of this
		// class will execute when
		// alarm triggers and
		Intent intentAlarm = new Intent(dashBoard, AlarmReciever.class);
		String eveTitle = eventBean.getEvent_title();
		String eveId = eventBean.getEvent_id();
		intentAlarm.putExtra("event_time", eventBean.getEvent_Date_Object());
		intentAlarm.putExtra("eid", eveId);
		intentAlarm.putExtra("etitle", eveTitle);
		intentAlarm.putExtra("userid", Utill.getUserPreferance(mContext).getUser_id());
		intentAlarm.putExtra("type", AppConstants.NOTIFY_DETAIL);
		PendingIntent pI = PendingIntent.getBroadcast(dashBoard, Integer.parseInt(eveId), intentAlarm, PendingIntent.FLAG_UPDATE_CURRENT);

		// create the object
		AlarmManager alarmManager = (AlarmManager) dashBoard.getSystemService(Context.ALARM_SERVICE);
		if (id == OFF) {
			// alarmManager.set(AlarmManager.RTC_WAKEUP, time, pI);
			alarmManager.cancel(pI);
		} else {
			// alarmManager.set(AlarmManager.RTC_WAKEUP, time, pI);
			alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, time, interval, pI);
		}

	}

	void openCalendar() {
		Calendar cal = new GregorianCalendar();

		Date d = eventBean.getEvent_Date_Object();
		cal.setTimeInMillis(d.getTime());
		if (d != null)
			cal.setTime(d);
		else
			cal.setTime(cal.getTime());                                                                              

		// cal.add(Calendar.MONTH, 2);

		Intent intent = new Intent(Intent.ACTION_INSERT);
		intent.setData(Events.CONTENT_URI);
		intent.putExtra(Events.TITLE, eventBean.getEvent_title());
		intent.putExtra(EventsEntity.EVENT_LOCATION, eventBean.getEvent_Address());

		// intent.putExtra(Events., eventBean.getEvent_title());
		// eventBean.getEvent_Address()

		intent.putExtra(Events.ALL_DAY, true);
		intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, cal.getTime().getTime());

		intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, cal.getTime().getTime() + 600000);
		startActivity(intent);
	}

	// this method shows reminder dialog.
	private void SetReminderPopUp() {

		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.alarm);
		dialog.setTitle("Set Reminder");
		Button logout_yes = (Button) dialog.findViewById(R.id.Set);
		Button logout_no = (Button) dialog.findViewById(R.id.Cancel);
		LinearLayout offL = (LinearLayout) dialog.findViewById(R.id.off_linear);
		LinearLayout before15L = (LinearLayout) dialog.findViewById(R.id.fifteenminlinear);
		LinearLayout before30L = (LinearLayout) dialog.findViewById(R.id.thirty_minlinear);
		LinearLayout before2HrL = (LinearLayout) dialog.findViewById(R.id.two_hrlinear);
		final CheckBox offC = (CheckBox) dialog.findViewById(R.id.off_cheack);
		final CheckBox before15C = (CheckBox) dialog.findViewById(R.id.fifteenmincheackss);
		final CheckBox before30C = (CheckBox) dialog.findViewById(R.id.thirty_mincheackss);
		final CheckBox before2HrC = (CheckBox) dialog.findViewById(R.id.two_hrcheackss);

		OnClickListener onClick = new OnClickListener() {
			@Override
			public void onClick(View v) {
				offC.setChecked(false);
				before15C.setChecked(false);
				before30C.setChecked(false);
				before2HrC.setChecked(false);
				int id = v.getId();
				switch (id) {
				case R.id.off_linear:
					offC.setChecked(true);

					break;
				case R.id.fifteenminlinear:
					before15C.setChecked(true);
					break;
				case R.id.thirty_minlinear:
					before30C.setChecked(true);
					break;
				case R.id.two_hrlinear:
					before2HrC.setChecked(true);
					break;
				default:
					break;
				}
				
			}
			
		};
		
		offL.setOnClickListener(onClick);
		before15L.setOnClickListener(onClick);
		before30L.setOnClickListener(onClick);
		before2HrL.setOnClickListener(onClick);

		logout_yes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				dialog.dismiss();
				if (offC.isChecked()) {
					scheduleAlarm(null, OFF, 0);
				} else if (before15C.isChecked()) {
					scheduleAlarm(null, FIFTEEN_MIN, MILLI_15MIN);
				} else if (before30C.isChecked()) {
					scheduleAlarm(null, FIFTEEN_MIN, MILLI_30MIN);
				} else if (before2HrC.isChecked()) {
					scheduleAlarm(null, FIFTEEN_MIN, MILLI_2HR);
				}
			}
		});

		logout_no.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				dialog.dismiss();
			}
		});

		dialog.show();
	}
	
	@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			// TODO Auto-generated method stub
			super.onActivityResult(requestCode, resultCode, data);
			
			
		}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
	
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
		
	}

	// this is participants dialogue
	void showRatting() {
		
		try {
			
			final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
			LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View vi = li.inflate(R.layout.participants, null, false);
			dialog.setContentView(vi);
			LinearLayout linearBack = (LinearLayout) vi.findViewById(R.id.backLinear);
			ListView listView = (ListView) vi.findViewById(R.id.list);
			TextView header = (TextView) vi.findViewById(R.id.header);
			header.setText("Rating");
			RattingAdapter.rattingArray = new float[eventBean.getParticipantsList().size()];
			for (int i = 0; i < eventBean.getAcceptedParticipantsList().size(); i++) {
				String ratStr = eventBean.getAcceptedParticipantsList().get(i).getRating();
				if (!Utill.isStringNullOrBlank(ratStr)) {
					float rat = Float.parseFloat(ratStr);
					RattingAdapter.rattingArray[i] = rat;
				}
			}

			RattingAdapter adapter = new RattingAdapter(mContext, eventBean.getAcceptedParticipantsList(), 1);

			listView.setAdapter(adapter);
			linearBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					dialog.dismiss();
					
				}
				
			});
			dialog.show();
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}

	@Override
	public void onRefreshDetail() {
		getEventDetail();
	}

}
