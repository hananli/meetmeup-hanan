package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.sql.Time;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.meetmeup.activity.ChooseLocationActivity;
import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.activity.SelectAddressBYTagActivity;
import com.meetmeup.adapters.EventCategoryTypeAdapter;
import com.meetmeup.adapters.FriendAdapter;
import com.meetmeup.adapters.NearByAdapter;
import com.meetmeup.asynch.EditEventAsync;
import com.meetmeup.asynch.GetFriendListAsync;
import com.meetmeup.asynch.GetNearByAsync;
import com.meetmeup.asynch.GetFriendListAsync.GetFriendListListener;
import com.meetmeup.asynch.GetNearByAsync.GetNearByListListener;
import com.meetmeup.bean.EventCategoryList;
import com.meetmeup.bean.EventTypeCategory;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.FriendBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.fragment.CreateEventFragement.CustomTextWatcher;
//import com.meetmeup.fragment.CreateEventFragement.GetFriendListListener;
//import com.meetmeup.fragment.CreateEventFragement.GetNearByListListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;

//This class is used for Edit an event.
@SuppressLint("NewApi")
public class EditEventFragement extends Fragment {
	
	DashBoard dashBoard;
	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	static EditText eventNameEt, evetnDescEt, eventAddress, evetnDate,
			eventTime, MinparticipantsEt,MaxparticipantsEt,amountET,paypalID , eventType;
//	Button submitButton;
	
	TextView submitButton;
	
	public static Activity mActivity;
	TextView collectMoneyText;
	RadioGroup collectMoneyRadioGroup, eventTypeRadioGroup;

	static boolean isClearFields = false;
	public static String EventAddress;//, eventLat, eventLong;
	ArrayList<FriendBean> fbFriendList = null;
	ArrayList<FriendBean> nearByFriendList = null;
	
	ArrayList<FriendBean> filteredFriendList = null;
	ArrayList<FriendBean> filteredList = null;	

	String startTimeText;
	public static final int DATE = 0;
	public static final int TIME = 1;
	ImageView /*AddParticipantsIV,*/ ViewParticipantsIV;
	
	TextView AddParticipantsIV;
	
	TextView publicText,privateText;
	
	public static EventsBean dataBean;
	
	ImageView homeIV, settingIV, createEventIV, myEventsIV, nearByEventsIV;
	
	String eventTypeCategId;//used to save the eventtypecategid for sending to server as a selected event type by user
	

	// This method is used for instatiating current class object.
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			
			mfFragment = new EditEventFragement();
			
		}
		isClearFields = true;
		return mfFragment;
	}

	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			
			DashBoard.resetActionBarTitle("Edit Event",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
//			DashBoard.chatIcon.setVisibility(View.GONE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
			
		}
		if (isClearFields) {
			
			clearFields();
			isClearFields = false;
			
		}
		setDataTOView();
		
		super.onStart();
	}
	
	
	

	// This method is used whenever this view will be on front.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		dashBoard = (DashBoard) mActivity;
		View view = inflater.inflate(R.layout.create_event, container, false);
		initializeView(view);
		setOnClickeListeners();
		
		participantsFbIds = "";
		INVITE_PERSON = FACEBOOK;
		// fbFriendList = null;
		// nearByFriendList = null;
		FriendAdapter.cheackStatus = null;
		NearByAdapter.cheackStatus = null;

		fbFriendList = AppConstants.getFacebookFriendList();
		nearByFriendList = AppConstants.getNearByList();
		setDataTOView();
		return view;
	}
	
	
	//This method will set data to the view,which we got on event detail page.
	void setDataTOView(){
				
		try{
				 
		     ArrayList<EventTypeCategory> evtCategList = EventCategoryList.getEventCategoryList();
		 
		     for(int i=0;i<evtCategList.size();i++){
			 
			   if(evtCategList.get(i).getCategory_id().equals(dataBean.getEvent_category())){
				 
				   eventTypeCategId = evtCategList.get(i).getCategory_id();
				   eventType.setText(evtCategList.get(i).getCategory_name());
				 
				   break;
				 
			   }
			 
		     }
		 		 
		    eventNameEt.setText(dataBean.getEvent_title());
						
			 evetnDescEt.setText(dataBean.getEvent_description());
			
			 eventAddress.setText(dataBean.getEvent_Address());
			 evetnDate.setText(dataBean.getEvent_date());
			 eventTime.setText(dataBean.getEvent_time());
			 MinparticipantsEt.setText(dataBean.getMin_participants());
			 MaxparticipantsEt.setText(dataBean.getMax_participants());
			
			 String eventTypeTemp = dataBean.getEvent_type();			
					
			 if(eventTypeTemp.equals("0")){
				
				publicPrivateString = "0";
				publicText.setBackground(getResources().getDrawable(R.drawable.unselect_bg));
				privateText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
				publicText.setTextColor(getResources().getColor(R.color.black_text));
				privateText.setTextColor(getResources().getColor(R.color.white));
			
			 }else{
			
				publicPrivateString = "1";
				privateText.setBackground(getResources().getDrawable(R.drawable.unselect_bg));
				publicText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
				privateText.setTextColor(getResources().getColor(R.color.black_text));
				publicText.setTextColor(getResources().getColor(R.color.white));
								
			 }		
			 
			 if(Utill.isStringNullOrBlank(dataBean.getAmount())
					 && Utill.isStringNullOrBlank(dataBean.getPaypalId())){
				 	
				 
				 
 			 }else if(dataBean.getAmount().equalsIgnoreCase("0")
					 && dataBean.getPaypalId().equalsIgnoreCase("")){
				
				 
			 }else{
				 
				 collectMoneyString = "1";
				 collectMoneyText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
				 collectMoneyText.setTextColor(getResources().getColor(R.color.white));
				 amountET.setVisibility(View.VISIBLE);
				 paypalID.setVisibility(View.VISIBLE);
				 
			 }
			 
			 
//			if(dataBean.getCollect_money_from_participants() != null){
//			
//			if(dataBean.getCollect_money_from_participants().equalsIgnoreCase("0")){
				
//				collectMoneyString = "1";
//				collectMoneyText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
//				collectMoneyText.setTextColor(getResources().getColor(R.color.white));
					
//			 }else {
//				
//				collectMoneyString = "0";
//				collectMoneyText.setBackground(getResources().getDrawable(R.drawable.unselect_bg));
//				collectMoneyText.setTextColor(getResources().getColor(R.color.black));
//								
//			 } 
//			 
//		   }
							
			amountET.setText("");
			paypalID.setText("");
				
			 if(!Utill.isStringNullOrBlank(dataBean.getAmount())){
				
			
				amountET.setText(""+dataBean.getAmount());
			
		     }
		
		     if(!Utill.isStringNullOrBlank(dataBean.getPaypalId())){
				
		    	 paypalID.setText(""+dataBean.getPaypalId());
				
			 }
			
			
			//uncomment it
		/*	if(eventTypeTemp.equalsIgnoreCase("1")){
				eventTypeRadioGroup.check(R.id.publicc);
			}else{
				eventTypeRadioGroup.check(R.id.privatee);
				collectMoneyText.setVisibility(View.VISIBLE);
				collectMoneyRadioGroup.setVisibility(View.VISIBLE);
				String collectMoney = dataBean.getCollect_money_from_participants();
				if(collectMoney.equalsIgnoreCase("1")){
					collectMoneyRadioGroup.check(R.id.yes);
					amountET.setText(dataBean.getAmount());
					paypalID.setText(dataBean.getPaypalId());
				}else{
					collectMoneyRadioGroup.check(R.id.no);
				}
			}*/
		 
		 
	   }catch(Exception e){
		   
		   e.printStackTrace();
		   
	   }		
		
	}

	// This method is used for initailzing Views.
	void initializeView(View view) {
		

		publicText = (TextView) view.findViewById(R.id.public_event_text);
		privateText = (TextView) view.findViewById(R.id.private_event_text);
		publicText.setOnClickListener(publicPrivate);
		privateText.setOnClickListener(publicPrivate);
				
		eventNameEt = (EditText) view.findViewById(R.id.event_name);
		
				
		evetnDescEt = (EditText) view.findViewById(R.id.event_description);
		eventAddress = (EditText) view.findViewById(R.id.event_add);
		evetnDate = (EditText) view.findViewById(R.id.event_date);
		eventTime = (EditText) view.findViewById(R.id.event_time);

		MinparticipantsEt = (EditText) view
				.findViewById(R.id.no_of_participants);
		
//		submitButton = (Button) view.findViewById(R.id.submit);
		submitButton = (TextView) view.findViewById(R.id.submit);
		submitButton.setText("Save");
		
		collectMoneyText = (TextView) view
				.findViewById(R.id.collect_money_text);
		
		collectMoneyRadioGroup = (RadioGroup) view
				.findViewById(R.id.collect_money_radio_group);
		
		//uncomment it
		/*eventTypeRadioGroup = (RadioGroup) view
				.findViewById(R.id.event_type_radio_group);*/

//		AddParticipantsIV = (ImageView) view.findViewById(R.id.add_participants);
		AddParticipantsIV = (TextView) view.findViewById(R.id.add_participants);
		
//		AddParticipantsIV.setVisibility(View.GONE);
		ViewParticipantsIV = (ImageView) view.findViewById(R.id.b);
		
		
		MaxparticipantsEt = (EditText) view.findViewById(R.id.no_of_participants_max);
		amountET = (EditText) view.findViewById(R.id.amount);
		paypalID = (EditText)view.findViewById(R.id.paypal_id);
		
		eventType = (EditText) view.findViewById(R.id.event_type);
		
		
		homeIV = (ImageView) view.findViewById(R.id.homeIV);
		settingIV = (ImageView) view.findViewById(R.id.settingIV);
		createEventIV = (ImageView) view.findViewById(R.id.createEventIV);
		
		createEventIV.setImageResource(R.drawable.create_event_deactive_btn);
		
		myEventsIV = (ImageView) view.findViewById(R.id.myEventsIV);
		nearByEventsIV = (ImageView) view.findViewById(R.id.nearByEventsIV);

		homeIV.setOnClickListener(bottomClickListener);
		settingIV.setOnClickListener(bottomClickListener);
		createEventIV.setOnClickListener(bottomClickListener);
		myEventsIV.setOnClickListener(bottomClickListener);
		nearByEventsIV.setOnClickListener(bottomClickListener);
		
	}

	OnClickListener bottomClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			switch (id) {
			case R.id.homeIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_HOME);
				break;
			case R.id.settingIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_SETTING);
				break;
			case R.id.createEventIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_CREATE_EVENT);
				break;
			case R.id.myEventsIV:
//				if (filteredCategoryList != null) {
//
//					if (!filteredCategoryList.isEmpty()) {
//
//						filteredCategoryList.clear();
//					}
//
//					UserBean user = Utill.getUserPreferance(mContext);
//
//					if (mainList != null) {
//						for (int i = 0; i < mainList.size(); i++) {
//							EventsBean evtbean = mainList.get(i);
//							if (evtbean.getEvent_owner_id().equals(user.getUser_id()) || evtbean.getEventAcceptStatus().equalsIgnoreCase("1")) {
//								filteredCategoryList.add(evtbean);
//							}
//						}
//
//						setEventAdapter(filteredCategoryList);
//
//					}
//
//				}
				
				break;
			case R.id.nearByEventsIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT);
				break;

			default:
				break;
			}

		}
	};
	
	
	
	// This method is used for setting Click listeners for all needed fields.ex.
	// on address,time.
	public void setOnClickeListeners() {
		

		collectMoneyText.setOnClickListener(collectMoneyListener);
		
		eventType.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
//				showCategory();
				
//				getEventTypeCategory();
				
				showCategory(EventCategoryList.getEventCategoryList());
				
			}
		});
		
		
		collectMoneyRadioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				if(checkedId == R.id.yes){
					amountET.setVisibility(View.VISIBLE);
					paypalID.setVisibility(View.VISIBLE);
				}else if(checkedId==R.id.no){
					amountET.setVisibility(View.GONE);
					paypalID.setVisibility(View.GONE);
				}
			}
		});
		
		
		eventNameEt.setOnFocusChangeListener(new OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					if(dataBean!=null){
						dataBean.setEvent_title(eventNameEt.getText().toString());
					}
				}
			}
		});
		
//		evetnDescEt.setOnFocusChangeListener(new OnFocusChangeListener() {
//			@Override
//			public void onFocusChange(View v, boolean hasFocus) {
//				if(!hasFocus){
//					if(dataBean!=null){
//						dataBean.setEvent_description(evetnDescEt.getText().toString());
//					}
//				}
//			}
//		});
		
		AddParticipantsIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showFriendsList();
			}
		});
		ViewParticipantsIV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Utill.showToast(mContext, "Show Selected User");
				// showFriendsList();
			}
		});

		eventAddress.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {
			
				if(dataBean!=null){
                
			    /*EventAddress = "";
				  Intent intent = new Intent(mActivity, ChooseLocationActivity.class);
				  intent.putExtra("lat", dataBean.getEvent_lat());
				  intent.putExtra("lon", dataBean.getEvent_eventLong());
				  intent.putExtra("name",dataBean.getEvent_Address());
				  startActivityForResult(intent,0);*/
					
				  SelectOption();	
				
				}
//9039851920
			
			/*	dashBoard.swithFragment(DashBoard.FRAGMENT_LOCATION);
				EventAddress = "";*/
			}
		});
		
	/*	eventTypeRadioGroup
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {
					@Override
					public void onCheckedChanged(RadioGroup group, int checkedId) {
						if (checkedId == R.id.privatee) {
							collectMoneyText.setVisibility(View.VISIBLE);
							collectMoneyRadioGroup.setVisibility(View.VISIBLE);
							// AddParticipants.setVisibility(View.VISIBLE);
						} else {
							collectMoneyText.setVisibility(View.GONE);
							collectMoneyRadioGroup.setVisibility(View.GONE);
							// AddParticipants.setVisibility(View.GONE);
						}
					}
				});*/
		

		evetnDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				
				showDatePopup(DATE);
				
			}
		});
		
		eventTime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				/*String date = evetnDate.getText().toString().trim();
				if(Utill.isStringNullOrBlank(date)){
					Utill.showToast(mContext, "Please select date first.");
				}else*/
				{
				
					showDatePopup(TIME);	
					
				}
				
			}
		});

		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentManager.getBackStackEntryCount() > 0) {
					mFragmentManager.popBackStack();
				}
				dashBoard.hideSOftKey();
			}
		});
		
		
		submitButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//Utill.showToast(mContext,"Under developement.  ");
				cheackValidation();
			}
		});
		
	}
	
	
	String publicPrivateString = "";
	 String collectMoneyString = "0";
	
	 OnClickListener publicPrivate = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(v.getId() == R.id.public_event_text){
				publicPrivateString = "1";
				privateText.setBackground(getResources().getDrawable(R.drawable.unselect_bg));
				publicText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
				privateText.setTextColor(getResources().getColor(R.color.black_text));
				publicText.setTextColor(getResources().getColor(R.color.white));
			}else if(v.getId() == R.id.private_event_text){
				publicPrivateString = "0";
				publicText.setBackground(getResources().getDrawable(R.drawable.unselect_bg));
				privateText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
				publicText.setTextColor(getResources().getColor(R.color.black_text));
				privateText.setTextColor(getResources().getColor(R.color.white));
			} 
			
		}
	};
	
	OnClickListener collectMoneyListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			if(collectMoneyString.equalsIgnoreCase("0")){
			
				collectMoneyString = "1";
				collectMoneyText.setBackground(getResources().getDrawable(R.drawable.selected_bg));
				collectMoneyText.setTextColor(getResources().getColor(R.color.white));
				amountET.setVisibility(View.VISIBLE);
				paypalID.setVisibility(View.VISIBLE);
			
			}else if(collectMoneyString.equalsIgnoreCase("1")){
				
				collectMoneyString = "0";
				collectMoneyText.setBackground(getResources().getDrawable(R.drawable.unselect_bg));
				collectMoneyText.setTextColor(getResources().getColor(R.color.black));
				amountET.setVisibility(View.GONE);
				paypalID.setVisibility(View.GONE);
				
			} 
		}
	};
	
	
	private void SelectOption() {
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.final_logout);
		dialog.setTitle("Select Adress By :");
		Button byMap = (Button) dialog.findViewById(R.id.logout_yes);
		byMap.setText("Google Map");
		Button byAddress = (Button) dialog.findViewById(R.id.logout_no);
		byAddress.setText("Entering Add");

		byMap.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, ChooseLocationActivity.class);
				intent.putExtra("lat", AppConstants.getLattitude());
				intent.putExtra("lon", AppConstants.getLogitude());
				intent.putExtra("name", "Address :");
				startActivityForResult(intent, 5);
				dialog.dismiss();
			}
		});
		byAddress.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intent = new Intent(mActivity, SelectAddressBYTagActivity.class);
				startActivityForResult(intent, 10);
				dialog.dismiss();
			}
		});
		dialog.show();
	}
	
	
	public void showCategory(final ArrayList<EventTypeCategory> eventList) {

		eventTypeCategId = null;

		String array[] = new String[eventList.size()];
		
		for(int i=0;i<eventList.size();i++){
			
		    array[i] =  eventList.get(i).getCategory_name();	
			
		}				
		
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
		EventCategoryTypeAdapter evtcategAdapter = new EventCategoryTypeAdapter(mContext,eventList);
		listView.setAdapter(evtcategAdapter);
		
//		ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,R.layout.simpletextview,array);
//		listView.setAdapter(adapter);
		
		listView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				eventType.setText(eventList.get(position).getCategory_name());
				
				eventTypeCategId = eventList.get(position).getCategory_id();			
								
				/*if(position ==0)
					eventType.setText("");*/
				
				System.out.println("eventTypeCategId===  "+eventTypeCategId+"   "+eventList.get(position).getCategory_name());
				
			    alertDialog.dismiss();
			   			   
			}
		});
		
		alertDialog.setView(v);
		alertDialog.show();
	}

	// This method is used for validating form data and send event data to the
	// server.
	void cheackValidation() {
		
		try{
		
		String eventName = eventNameEt.getText().toString().trim();

		String eventDesc = evetnDescEt.getText().toString().trim();
		String eventAdd = eventAddress.getText().toString().trim();
		String eventDate = evetnDate.getText().toString().trim();
		String eventTim = eventTime.getText().toString().trim();
		String minNoOfPart = MinparticipantsEt.getText().toString()
				.trim();
		String maxNoOfPart = MaxparticipantsEt.getText().toString()
		.trim();
		
		String eventCagetoryStr = eventType.getText().toString().trim();
		
		if(Utill.isStringNullOrBlank(eventCagetoryStr)){
			eventCagetoryStr = "other";
		}else{
		//	eventCagetoryStr = ""+getCategoryIndex(eventCagetoryStr);
		}
		
		String eventtype = "";
		String amount = "";
		String payPalId = "";
		
		eventtype = publicPrivateString;
		

	/*	if (eventTypeRadioGroup.getCheckedRadioButtonId() == R.id.publicc) {
			eventtype = "1";
		} else {
			eventtype = "0";
		}*/
		if(dataBean==null || dataBean.getEvent_id()==null){
			Utill.showToast(mContext, "Error.");
			return;
	
		}else if (Utill.isStringNullOrBlank(eventTypeCategId)) {
		
			Utill.showToast(mContext, "Please Select Event Type.");
			
		} 

		if (Utill.isStringNullOrBlank(eventName)) {
			Utill.showToast(mContext, "Please enter event name");
		} 
//		else if (Utill.isStringNullOrBlank(eventDesc)) {
//			Utill.showToast(mContext, "Please enter event description");
//		} 
//		else if (Utill.isStringNullOrBlank(eventAdd)) {
//			Utill.showToast(mContext, "Please enter event address");
//		} 
		
		else if (Utill.isStringNullOrBlank(eventDate)) {
			Utill.showToast(mContext, "Please select date");
		} else if (Utill.isStringNullOrBlank(eventTim)) {
			Utill.showToast(mContext, "Please select time");
		}		
		else if(!Utill.isCorrectDateAndTime(eventDate,eventTim)){
			Utill.showToast(mContext,"Time expired.");
		}
		else if (Utill.isStringNullOrBlank(minNoOfPart)) {
			
			Utill.showToast(mContext, "Please enter minimum no. of participants.");
	
		}
//		else if (Utill.isStringNullOrBlank(eventTypeCategId)) {
//		
//			Utill.showToast(mContext, "Please Select Event Type.");
//			
//		}  
		
		/*else if (Utill.isStringNullOrBlank(participantsFbIds)) {
			Utill.showToast(mContext, "Please select at least one participant.");
		}*/ else {
			
			if (Utill.isNetworkAvailable(mContext)) {
				
				MultipartEntity multipart = new MultipartEntity();
				
				try {
					
					/*if(collectMoneyRadioGroup.getVisibility() == View.VISIBLE && collectMoneyRadioGroup.getCheckedRadioButtonId() == R.id.yes){
						amount = amountET.getText().toString();
						payPalId = paypalID.getText().toString();
						if(Utill.isStringNullOrBlank(amount)){
							Utill.showToast(mContext, "Please Enter amount.");
							return;
						}else if(Utill.isStringNullOrBlank(payPalId)){
							Utill.showToast(mContext, "Please Enter paypal id");
							return;
						}
						multipart.addPart("amount", new StringBody(amount));
						multipart.addPart("paypalid", new StringBody(payPalId));
					}*/
					
					
					if(collectMoneyString.equalsIgnoreCase("1")){
					
						amount = amountET.getText().toString();
						payPalId = paypalID.getText().toString();
						if(Utill.isStringNullOrBlank(amount)){
							Utill.showToast(mContext, "Please Enter amount.");
							return;
						}else if(Utill.isStringNullOrBlank(payPalId)){
							Utill.showToast(mContext, "Please Enter paypal id");
							return;
						}
						
						multipart.addPart("amount", new StringBody(amount));
												
						multipart.addPart("paypalid", new StringBody(Utill.getUTF(payPalId),Charset.forName("UTF-8")));
					}
					
					UserBean user = Utill.getUserPreferance(mContext);
					multipart.addPart("event_id",new StringBody(dataBean.getEvent_id()));
					multipart.addPart("user_id",
							new StringBody(user.getUser_id()));
										
					
					multipart.addPart("event_title", new StringBody(Utill.getUTF(eventName),Charset.forName("UTF-8")));					
//					multipart.addPart("event_title", new StringBody(eventName));
					
					multipart.addPart("address", new StringBody(Utill.getUTF(eventAdd),Charset.forName("UTF-8")));
//					multipart.addPart("address", new StringBody(eventAdd));
					
					multipart.addPart("event_description", new StringBody(Utill.getUTF(eventDesc),Charset.forName("UTF-8")));
//					multipart.addPart("event_description", new StringBody(
//							eventDesc));
					
					
					multipart.addPart("event_type", new StringBody(Utill.getUTF(eventtype),Charset.forName("UTF-8")));
//					multipart.addPart("event_type", new StringBody(eventtype));
					
					
					if(dataBean.getEvent_lat() != null && dataBean.getEvent_eventLong() != null){
					
						multipart.addPart("lat", new StringBody(dataBean.getEvent_lat()));
					    multipart.addPart("long", new StringBody(dataBean.getEvent_eventLong()));
					     
					}
					else{
						
						multipart.addPart("lat", new StringBody("0.0"));
					    multipart.addPart("long", new StringBody("0.0"));
						
					}
					
					
					multipart.addPart("date", new StringBody(Utill.getUTF(eventDate),Charset.forName("UTF-8")));
//					multipart.addPart("date", new StringBody(eventDate));
					
					multipart.addPart("time", new StringBody(Utill.getUTF(eventTim),Charset.forName("UTF-8")));
//					multipart.addPart("time", new StringBody(eventTim));

//					multipart.addPart("max_participants", new StringBody(
//							minNoOfPart));
					
					multipart.addPart("max_participants", new StringBody(maxNoOfPart));
					multipart.addPart("min_participants", new StringBody(minNoOfPart));
					
//					multipart.addPart("min_participants", new StringBody(
//							minNoOfPart));
					
					String CollectMoney = "0";
					if (collectMoneyRadioGroup.getCheckedRadioButtonId() == R.id.yes)
						CollectMoney = "1";

					multipart.addPart("collect_money_from_participants",
							new StringBody(CollectMoney));
					Log.e("Participants fb id", participantsFbIds);

			/*		multipart.addPart("participants_fb_id", new StringBody(
							participantsFbIds));*/

					if(participantsFbIds != null && !participantsFbIds.isEmpty()){
					 
						multipart.addPart("participants_fb_id", new StringBody(participantsFbIds));
						
					}										
					
					multipart.addPart("event_category", new StringBody(eventTypeCategId));
					
					System.out.println("eventTypeCategId===  "+eventTypeCategId+"   "+eventType.getText().toString());
					
					
					showProgress();
					new EditEventAsync(mContext, new EditEventListener(), multipart).execute();
					/*new CreateEventAsync(mContext, new CreateEventListener(),
							multipart).execute();*/
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
					
				}
			
			} else {
				
				Utill.showNetworkError(mContext);
				
			}
			
		}
		
		}catch(Exception e){
			
			e.printStackTrace();
			
		}
		
	}

	// This method is used for initializing activity instance
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

	// This method is used for hiding progress bar
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

	// This is the Listener Class which will be used for notify about an event
	// is successfully Edited or not.
	public class CreateEventListener {
		public void onSuccess(String msg) {
			hideProgress();
			clearFields();
			Utill.showToast(mContext, "Event Created Successfully.");
			mFragmentManager.popBackStack(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}

		public void onError(String msg) {
			Utill.showToast(mContext, msg);
			hideProgress();
		}
	}

	// This method is used for Clearing All text Fields of the Create event
	// Form.
	public static void clearFields() {
		try {
			eventNameEt.setText("");
			evetnDescEt.setText("");
			eventAddress.setText("");
			evetnDate.setText("");
			eventTime.setText("");
			MinparticipantsEt.setText("");
		} catch (Exception e) {

		}
		EditEventFragement.EventAddress = "";
	}

	// This method is used for getting formatted time.
	private String getTime(int hr, int min) {
		Time tme = new java.sql.Time(hr, min, 0);
		Format formatter;
		formatter = new SimpleDateFormat("h:mm a",Locale.ENGLISH);
		return formatter.format(tme);
	}

	// This method is used for showing Date and Time Dialogue Box according to
	// id and Fill date and time according to selection in text Fields.
	public void showDatePopup(final int id) {
		final Dialog dialog = new Dialog(mContext);
		LayoutInflater li = (LayoutInflater) mContext.getApplicationContext()
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View vi = li.inflate(R.layout.popup_date_time, null, false);
		final DatePicker datePick = (DatePicker) vi
				.findViewById(R.id.date_selector_wheel);
		final TimePicker timePick = (TimePicker) vi
				.findViewById(R.id.start_time);
		if (id == DATE) {
			datePick.setVisibility(View.VISIBLE);
			timePick.setVisibility(View.GONE);
		} else {
			datePick.setVisibility(View.GONE);
			timePick.setVisibility(View.VISIBLE);
		}

		final Calendar cal = new GregorianCalendar();
		Button subBtn = (Button) vi.findViewById(R.id.set);
		subBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int day = datePick.getDayOfMonth();
				int year = datePick.getYear();
				int month = datePick.getMonth() + 1;
				String date = day + "-" + month + "-" + year;

				startTimeText = timePick.getCurrentHour() + ":"
						+ timePick.getCurrentMinute();
				startTimeText = getTime(timePick.getCurrentHour(),
						timePick.getCurrentMinute());
				if (id == DATE)
					evetnDate.setText(date);
				else {
					String currentDate = evetnDate.getText().toString().trim();
					if(!Utill.isStringNullOrBlank(currentDate)){
						
						String tdArray[] = currentDate.split("-");
						
					}
					
					eventTime.setText(startTimeText);
				}
				
				dialog.dismiss();
				
			}
		});

		timePick.setOnTimeChangedListener(new OnTimeChangedListener() {
			@Override
			public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
				startTimeText = hourOfDay + ":" + minute;
				Log.e("time",startTimeText);
			}
		});

		long currentTime = cal.getTimeInMillis() - 1000;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			datePick.setMinDate(currentTime);
		} else {
			final int minYear = cal.get(Calendar.YEAR);
			final int minMonth = cal.get(Calendar.MONTH);
			final int minDay = cal.get(Calendar.DAY_OF_MONTH);
			datePick.init(minYear, minMonth, minDay,
					new OnDateChangedListener() {
						public void onDateChanged(DatePicker view, int year,
								int month, int day) {
							Calendar newDate = Calendar.getInstance();
							newDate.set(year, month, day);
							if (cal.after(newDate)) {
								view.init(minYear, minMonth, minDay, this);
							}
						}
					});
		}
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(vi);
		dialog.show();

	}

	// This method is used for showing Friends List and near by people On Dialogue,in this user can person's to invite.
	ListView fbFriendListView;

	public int INVITE_PERSON = -1;
	public static final int FACEBOOK = 0;
	public static final int NEARBY = 1;

	public String participantsFbIds = "";
	TextView facebookBtn;//,mapButton;

//	void showFriendsList() {
//		try {
//			/*participantsFbIds = "";
//			INVITE_PERSON = FACEBOOK;
//			fbFriendList = null;
//			nearByFriendList = null;
//			FriendAdapter.cheackStatus = null;
//			NearByAdapter.cheackStatus = null;*/
//
//			final Dialog dialog = new Dialog(mContext,
//					android.R.style.Theme_Black_NoTitleBar);
//			LayoutInflater li = (LayoutInflater) getActivity()
//					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//			View vi = li.inflate(R.layout.filter_popup, null, false);
//			dialog.setContentView(vi);
//	//		mapButton = (TextView) vi.findViewById(R.id.map);
//			TextView selectAll = (TextView) vi.findViewById(R.id.selectall);
//			TextView clearAll = (TextView) vi.findViewById(R.id.clearAll);
//			facebookBtn = (TextView) vi.findViewById(R.id.fb_friend_button);
//			final TextView nearByBtn = (TextView) vi
//					.findViewById(R.id.near_by_button);
//			ImageView filterImage = (ImageView) vi
//					.findViewById(R.id.filterbutton);
//			ImageView doneButton = (ImageView) vi.findViewById(R.id.done_btn);
//			fbFriendListView = (ListView) vi.findViewById(R.id.list);
//			
//		/*	mapButton.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if(nearByFriendList!=null && nearByFriendList.size()>0){
//						Intent intent = new Intent(mActivity, MapActivity.class);
//						startActivity(intent);
//					}else{
//						Utill.showToast(mContext,"Currently no user available.");
//					}
//				}
//			});*/
//			
//			doneButton.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//					getIds();
//				}
//			});
//
//			filterImage.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//
//			selectAll.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (INVITE_PERSON == FACEBOOK)
//						FriendAdapter.selectAll();
//					else if (INVITE_PERSON == NEARBY)
//						NearByAdapter.selectAll();
//				}
//			});
//
//			clearAll.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					if (INVITE_PERSON == FACEBOOK)
//						FriendAdapter.clearAll();
//					else if (INVITE_PERSON == NEARBY)
//						NearByAdapter.clearAll();
//				}
//			});
//			
//			facebookBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//		//			mapButton.setVisibility(View.GONE);
//					fbFriendListView.setVisibility(View.GONE);
//					INVITE_PERSON = FACEBOOK;
//					nearByBtn.setBackgroundColor(getResources().getColor(
//							R.color.unselectedd));
//					facebookBtn.setBackgroundColor(getResources().getColor(
//							R.color.selectedd));
//					if (fbFriendList == null || fbFriendList.size() == 0) {
//						showProgress();
//						MultipartEntity multipart= new MultipartEntity();
//						UserBean user = Utill.getUserPreferance(mContext);						try {
//							multipart.addPart("user_id",new StringBody(user.getUser_id()));
//						} catch (UnsupportedEncodingException e) {
//							// TODO Auto-generated catch block
//							e.printStackTrace();
//						}
//						/*new GetFriendListAsync(mContext,
//								new GetFriendListListener(), multipart).execute();*/
//					} else {
//						FriendAdapter adapter = new FriendAdapter(mContext,
//								fbFriendList);
//						fbFriendListView.setAdapter(adapter);
//						fbFriendListView.setVisibility(View.VISIBLE);
//					}
//				}
//			});
//			nearByBtn.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//		//			mapButton.setVisibility(View.GONE);
//					fbFriendListView.setVisibility(View.GONE);
//					INVITE_PERSON = NEARBY;
//					nearByBtn.setBackgroundColor(getResources().getColor(
//							R.color.selectedd));
//					facebookBtn.setBackgroundColor(getResources().getColor(
//							R.color.unselectedd));
//					if (nearByFriendList == null
//							|| nearByFriendList.size() == 0) {
//						MultipartEntity multipart = new MultipartEntity();
//						try {
//							UserBean user = Utill.getUserPreferance(mContext);
//							multipart.addPart("user_id",
//									new StringBody(user.getUser_id()));
//							multipart.addPart("lat", new StringBody(
//									AppConstants.getLattitude()));
//							multipart.addPart("long", new StringBody(
//									AppConstants.getLogitude()));
//							multipart.addPart("radius", new StringBody(Utill.getRadius(mContext)));
//						} catch (UnsupportedEncodingException e) {
//							e.printStackTrace();
//						}
//						showProgress();
//					/*	new GetNearByAsync(mContext,
//								new GetNearByListListener(), multipart)
//								.execute();*/
//					} else {
//						NearByAdapter adapter = new NearByAdapter(mContext,
//								nearByFriendList);
//						fbFriendListView.setAdapter(adapter);
//						fbFriendListView.setVisibility(View.VISIBLE);
//			//			mapButton.setVisibility(View.VISIBLE);
//					}
//				}
//			});
//
//			LinearLayout linearBack = (LinearLayout) vi
//					.findViewById(R.id.backLinear);
//			linearBack.setOnClickListener(new OnClickListener() {
//				@Override
//				public void onClick(View v) {
//					dialog.dismiss();
//				}
//			});
//			
//			dialog.show();
//			facebookBtn.performClick();
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		}
//
//	}
	
	
  void showFriendsList() {
	
	  
		try {
			/*
			 * participantsFbIds = ""; INVITE_PERSON = FACEBOOK; // fbFriendList
			 * = null; // nearByFriendList = null; FriendAdapter.cheackStatus =
			 * null; NearByAdapter.cheackStatus = null;
			 */

//			String country[] = {"canada","delhi","mumbai","indore"};
//			
//		   ArrayAdapter adapter = new ArrayAdapter
//					   (mContext,android.R.layout.simple_list_item_1,country);
			
			
			final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar);
			LayoutInflater li = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			View vi = li.inflate(R.layout.filter_popup, null, false);
			dialog.setContentView(vi);
			// mapButton = (TextView) vi.findViewById(R.id.map);
			TextView selectAll = (TextView) vi.findViewById(R.id.selectall);
			TextView clearAll = (TextView) vi.findViewById(R.id.clearAll);
			facebookBtn = (TextView) vi.findViewById(R.id.fb_friend_button);
			final TextView nearByBtn = (TextView) vi.findViewById(R.id.near_by_button);
			ImageView filterImage = (ImageView) vi.findViewById(R.id.filterbutton);
			ImageView doneButton = (ImageView) vi.findViewById(R.id.done_btn);
			
			final EditText edittxt_enter_friend = (EditText) vi.findViewById(R.id.edittxt_enter_friend);
			EditText edittxt_search = (EditText) vi.findViewById(R.id.edittxt_search);
						
			edittxt_enter_friend.addTextChangedListener(new CustomTextWatcher()); 		
			
			edittxt_search.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {					

					searchInvites(edittxt_enter_friend.getText().toString());					
					
				}
			});
			
			
			fbFriendListView = (ListView) vi.findViewById(R.id.list);

			/*
			 * mapButton.setOnClickListener(new OnClickListener() {
			 * 
			 * @Override public void onClick(View v) { if(nearByFriendList!=null
			 * && nearByFriendList.size()>0){ Intent intent = new
			 * Intent(mActivity, MapActivity.class); startActivity(intent);
			 * }else{ Utill.showToast(mContext,"Currently no user available.");
			 * } } });
			 */

			doneButton.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				//	getIds();
					getUniquFBids(null);				
					
				}
				
			});

			filterImage.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(INVITE_PERSON == FACEBOOK){
						showProgress();
						MultipartEntity multipart = new MultipartEntity();
						UserBean user = Utill.getUserPreferance(mContext);
						try {
							multipart.addPart("user_id", new StringBody(user.getUser_id()));
						} catch (UnsupportedEncodingException e) {
						
							e.printStackTrace();
						}
						new GetFriendListAsync(mContext, new GetFriendListListener(), multipart).execute();
						
					}else if(INVITE_PERSON == NEARBY){

						MultipartEntity multipart = new MultipartEntity();
						try {
							UserBean user = Utill.getUserPreferance(mContext);
							multipart.addPart("user_id", new StringBody(user.getUser_id()));
							multipart.addPart("lat", new StringBody(AppConstants.getLattitude()));
							multipart.addPart("long", new StringBody(AppConstants.getLogitude()));
							multipart.addPart("radius", new StringBody(Utill.getRadius(mContext)));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						showProgress();
						new GetNearByAsync(mContext, new GetNearByListListener(), multipart).execute();
					
					}					
					
					//dialog.dismiss();
				}
			});

			selectAll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (INVITE_PERSON == FACEBOOK)
						FriendAdapter.selectAll();
					else if (INVITE_PERSON == NEARBY)
						NearByAdapter.selectAll();
				}
			});

			clearAll.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (INVITE_PERSON == FACEBOOK)
						FriendAdapter.clearAll();
					else if (INVITE_PERSON == NEARBY)
						NearByAdapter.clearAll();
				}
			});
			facebookBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// mapButton.setVisibility(View.GONE);
					fbFriendListView.setVisibility(View.GONE);
					INVITE_PERSON = FACEBOOK;
					nearByBtn.setBackgroundColor(getResources().getColor(R.color.unselectedd));
					facebookBtn.setBackgroundColor(getResources().getColor(R.color.selectedd));
					if (fbFriendList == null || fbFriendList.size() == 0) {
						showProgress();
						MultipartEntity multipart = new MultipartEntity();
						UserBean user = Utill.getUserPreferance(mContext);
					
						try {
							
							multipart.addPart("user_id", new StringBody(user.getUser_id()));
							
						} catch (UnsupportedEncodingException e) {
						
							e.printStackTrace();
							
						}
						
						new GetFriendListAsync(mContext, new GetFriendListListener(), multipart).execute();
						
					} else {
						
											
						filteredFriendList = new ArrayList<FriendBean>();
					    
						 for(int i=0;i<fbFriendList.size();i++){
							 
							 FriendBean frndBean = fbFriendList.get(i);
							  
							 if(!isFriendParticipated(frndBean)){
									
								  filteredFriendList.add(frndBean);
								  
							  }
								
						  }
						 
						 FriendAdapter adapter = new FriendAdapter(mContext, filteredFriendList);
						 fbFriendListView.setAdapter(adapter);
						 fbFriendListView.setVisibility(View.VISIBLE);
						 
					}
				}
			});
			nearByBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					// mapButton.setVisibility(View.GONE);
					fbFriendListView.setVisibility(View.GONE);
					INVITE_PERSON = NEARBY;
					nearByBtn.setBackgroundColor(mContext.getResources().getColor(R.color.selectedd));
					facebookBtn.setBackgroundColor(mContext.getResources().getColor(R.color.unselectedd));
					if (nearByFriendList == null || nearByFriendList.size() == 0) {
						MultipartEntity multipart = new MultipartEntity();
						try {
							UserBean user = Utill.getUserPreferance(mContext);
							multipart.addPart("user_id", new StringBody(user.getUser_id()));
							multipart.addPart("lat", new StringBody(AppConstants.getLattitude()));
							multipart.addPart("long", new StringBody(AppConstants.getLogitude()));
							multipart.addPart("radius", new StringBody(Utill.getRadius(mContext)));
						} catch (UnsupportedEncodingException e) {
							e.printStackTrace();
						}
						showProgress();
						new GetNearByAsync(mContext, new GetNearByListListener(),multipart).execute();
						
					} else {
												
						 filteredFriendList = new ArrayList<FriendBean>();
						    
						  for(int i=0;i<nearByFriendList.size();i++){
							
							  FriendBean frndBean = nearByFriendList.get(i);
							  
							  if(!isFriendParticipated(frndBean)){
								
								  filteredFriendList.add(frndBean);
							  }
								
						  }
						  
						 NearByAdapter adapter = new NearByAdapter(mContext, filteredFriendList);
						 fbFriendListView.setAdapter(adapter);
						 fbFriendListView.setVisibility(View.VISIBLE);
						
						// mapButton.setVisibility(View.VISIBLE);
						 
					}
				}
			});

			LinearLayout linearBack = (LinearLayout) vi.findViewById(R.id.backLinear);
			linearBack.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});

			dialog.show();
			facebookBtn.performClick();
			
		} catch (Exception e) {
			e.printStackTrace();

		}

	}
	
  
  public boolean isFriendParticipated(FriendBean frndBean){
	  
	  try{
	  
		  for(int j=0;j<dataBean.getParticipantsList().size();j++){
			
			ParticipantsBean particBean = dataBean.getParticipantsList().get(j);
			if(particBean.getUser_id().equals(frndBean.getUser_id())){
				
				return true;
			
			}		
			
		}
		  
	  
	  }catch(Exception e){
		  
		  e.printStackTrace();
		  
	  }
	  
	  return false;
	  
  }

   public class CustomTextWatcher implements TextWatcher {

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count, int after) {

	}

	@Override
	public void onTextChanged(CharSequence text, int start, int before, int count) {
		String textSearch = text.toString();
		searchInvites(textSearch);
	}

	@Override
	public void afterTextChanged(Editable s) {

	}

  }


 public void searchInvites(String searchTag) {
	
	Log.e("hello", "" + searchTag);

	/*
	 * LinkUContactsDB db = new LinkUContactsDB(mcContext); myContacts =
	 * db.fetchMyContactsOnMorgan
	 * (LinkUApplication.getActiveUser().getUserId()); for (int s = 0; s <
	 * myContacts.size(); s++) { for (int t = 0; t < invitedList.size();
	 * t++) { if (myContacts.get(s).getUserId() ==
	 * invitedList.get(t).getUserId()) { myContacts.get(s).setInvited(true);
	 * break; } else { myContacts.get(s).setInvited(false); } } }
	 */

	if (filteredFriendList != null && filteredFriendList.size() > 0) {
		// ArrayList<EventsBean>
		filteredList = new ArrayList<FriendBean>();
		if (!Utill.isStringNullOrBlank(searchTag)) {
			for (FriendBean event : filteredFriendList) {
				if (event.getUser_fname().toLowerCase().contains(searchTag.toLowerCase())) {
					filteredList.add(event);
				}
			}
		} else {
			
			filteredList = new ArrayList<FriendBean>();
			
			for (FriendBean event : filteredFriendList) {
			
				filteredList.add(event);
			
			}
			
		}

		if (filteredList.size() > 0) {
			fbFriendListView.setVisibility(View.VISIBLE);
//			noEventTextView.setVisibility(View.GONE);
			
			FriendAdapter adapter = new FriendAdapter(mContext,filteredList);
			fbFriendListView.setAdapter(adapter);
			fbFriendListView.setVisibility(View.VISIBLE);
			
		} else {
			fbFriendListView.setVisibility(View.GONE);
//			noEventTextView.setVisibility(View.VISIBLE);
//			noEventTextView.setText("No Result found for " + searchTag);
		}
	} else {
		fbFriendListView.setVisibility(View.GONE);
//		noEventTextView.setVisibility(View.VISIBLE);
	}
	
}
	
	
	
	//This is the listener class for notify,is facebook friends list succefully got or not.
	public class GetFriendListListener implements com.meetmeup.asynch.GetFriendListAsync.GetFriendListListener{
		public void onSuccess(ArrayList<FriendBean> list, String msg) {
		
			fbFriendList = list;
 			
            filteredFriendList = new ArrayList<FriendBean>();
		    
            for(int i=0;i<fbFriendList.size();i++){
				 
				 FriendBean frndBean = fbFriendList.get(i);
				
				 if(!isFriendParticipated(frndBean)){
						
					  filteredFriendList.add(frndBean);
				  
				 }
					
			  }
			
			FriendAdapter adapter = new FriendAdapter(mContext, filteredFriendList);
			fbFriendListView.setAdapter(adapter);
			fbFriendListView.setVisibility(View.VISIBLE);
			hideProgress();
		}

		public void onError(String msg) {
			hideProgress();
			Utill.showToast(mContext,msg);
			fbFriendListView.setVisibility(View.GONE);
		}
	}

	
	//This is the listener class for notify,is nearby people succefully got or not.
	public class GetNearByListListener implements com.meetmeup.asynch.GetNearByAsync.GetNearByListListener{
		
		public void onSuccess(ArrayList<FriendBean> list, String msg) {
			nearByFriendList = list;
			
			 filteredFriendList = new ArrayList<FriendBean>();
			    
			 for(int i=0;i<nearByFriendList.size();i++){
				 
				 FriendBean frndBean = nearByFriendList.get(i);
				 if(!isFriendParticipated(frndBean)){
						
					  filteredFriendList.add(frndBean);
				
				 }
					
			  }
			
			NearByAdapter adapter = new NearByAdapter(mContext, filteredFriendList);
			fbFriendListView.setAdapter(adapter);
			fbFriendListView.setVisibility(View.VISIBLE);
			
			
			
			
		//	mapButton.setVisibility(View.VISIBLE);
			hideProgress();
			
		}

		public void onError(String msg) {
			fbFriendListView.setVisibility(View.GONE);
			hideProgress();
		//	mapButton.setVisibility(View.GONE);
		}
		
	}

	
	
	//This method is used for getting all the selected people who are selected from the list of facebook and nearby user list.
	String getIds() {
		String ids = "";
		if (NearByAdapter.cheackStatus != null) {
			for (int i = 0; i < NearByAdapter.cheackStatus.length; i++) {
				if (NearByAdapter.cheackStatus[i])
					ids = nearByFriendList.get(i).getFriend_fb_id() + "," + ids;
			}
		}
		if (FriendAdapter.cheackStatus != null) {
			for (int i = 0; i < FriendAdapter.cheackStatus.length; i++) {
				if (FriendAdapter.cheackStatus[i])
					ids = fbFriendList.get(i).getFriend_fb_id() + "," + ids;
			}
		}
		Log.e("Id", ids);
		if (ids.length() > 1) {
			ids = ids.substring(0, ids.length() - 1);
		}
		//Utill.showToast(mContext, ids);
		participantsFbIds = ids;
		return ids;
	}
	
	//This is the listener class used for edit an event.
	public class EditEventListener{
		public void onSuccess(String msg){
			hideProgress();
//			Utill.showToast(mContext, "Successfully updated");
			mFragmentManager.popBackStack(null,
					FragmentManager.POP_BACK_STACK_INCLUSIVE);
		}
		public void onError(String msg){
			hideProgress();
			Utill.showToast(mContext, msg);
		}
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == Activity.RESULT_OK){
			String add = data.getStringExtra("add");
			eventAddress.setText(add);
			EventAddress = add;
			
			if(dataBean!=null){
				
				dataBean.setEvent_lat(""+data.getDoubleExtra("lat",0));
				dataBean.setEvent_eventLong(""+data.getDoubleExtra("long",0));
				dataBean.setEvent_Address(EventAddress);
				
			}
//			eventLat = ""+data.getDoubleExtra("lat",0);
//			eventLong = ""+data.getDoubleExtra("long",0);
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	private HashSet<String> reportTypeName;

	public void getUniquFBids(ArrayList<FriendBean> mainList) {
	
		String ids = "";
		mainList = new ArrayList<FriendBean>();
		if (NearByAdapter.cheackStatus != null) {
			for (int i = 0; i < NearByAdapter.cheackStatus.length; i++) {
				if (NearByAdapter.cheackStatus[i]) {
					mainList.add(nearByFriendList.get(i));
				}
			}
		}
		if (FriendAdapter.cheackStatus != null) {
			for (int i = 0; i < FriendAdapter.cheackStatus.length; i++) {
				if (FriendAdapter.cheackStatus[i]) {
					mainList.add(fbFriendList.get(i));
				}
			}
		}

		
		reportTypeName = new HashSet<String>();
		for (int i = 0; i < mainList.size(); i++) {
			String report = mainList.get(i).getFriend_fb_id();
			reportTypeName.add(report);
		}
		Iterator<String> it = reportTypeName.iterator();
		while (it.hasNext()) {
			String name = it.next() + ",";
			ids = ids + name;
		}
		Log.e("Id", ids);
		if (ids.length() > 1) {
			ids = ids.substring(0, ids.length() - 1);
		}
		participantsFbIds = ids;
//		Utill.showToast(mContext, ""+participantsFbIds);
	}

}
