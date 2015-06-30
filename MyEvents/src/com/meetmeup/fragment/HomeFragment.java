package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.UtteranceProgressListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;
import com.google.android.gms.analytics.HitBuilders.EventBuilder;
import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.activity.RangeSeekBar;
import com.meetmeup.activity.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.meetmeup.adapters.EventCategoryTypeAdapter;
import com.meetmeup.adapters.RattingAdapter;
import com.meetmeup.adapters.SwipeviewAdapter;
import com.meetmeup.adapters.SwipeviewAdapter.SwipeEventItemClickListener;
import com.meetmeup.asynch.AcceptAsync;
import com.meetmeup.asynch.DeleteEventParticipentAsyTask;
import com.meetmeup.asynch.GetEventDetialAsync;
import com.meetmeup.asynch.GetEventsAsync;
import com.meetmeup.bean.EventTypeCategory;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.Utill;

//This is the home page.here we are getting all events list,and showing them.
public class HomeFragment extends Fragment {

	LinearLayout headerButtons;
	static Context mContext;
	static FragmentManager mFragmentManager;  
	static Fragment mfFragment;
	public static Activity mActivity;
	DashBoard dashBoard;

	com.fortysevendeg.swipelistview.SwipeListView listView;
	EditText searchET, searchBT;
	TextView noEventTextView;// , ;
	// ImageView nearbyimageView;
	public static ArrayList<EventsBean> mainEventList;
	public static ArrayList<EventsBean> filteredList;
	ArrayList<EventsBean> filterList;
	ImageView homeIV, settingIV, createEventIV, myEventsIV, nearByEventsIV;
	LinearLayout layout_fade;

	SwipeviewAdapter swipeAdapter;

	// boolean isFirstviewCreated = false;//check the view is created first time
	// for need to check dispatch touch evnt on view

	public static ArrayList<EventsBean> filteredCategoryList;

	Button All_home, sun_home, mon_home, tue_home, wed_home, thurs_home, fri_home, sat_home;
	ArrayList<EventsBean> finalList;

	TextView first_home, second_home;

	boolean referesh = true;
	public static boolean loadAgain = false;

	public static Button button_allevent, button_yourevent, button_event_type;

	RelativeLayout relative_layout_search;

	// public static ArrayList<EventsBean> filterList;
	String currentDateTimeString;

	public static boolean isClickedOnMyEvents;
	public static boolean isClickedOnAllEvents;
	
	// This method is used for instantiating class object.
	public static Fragment getInstance(Context ct, FragmentManager fm) {
	
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			
			mfFragment = new HomeFragment();
			
		}
		
		return mfFragment;
		
	}

	// This method is used for getting list of all events.
	  void getEventsList() {

//		listView.setVisibility(View.GONE);
		UserBean user = Utill.getUserPreferance(mContext);
		
		if (user.getUser_id() == null) {

			Utill.showToast(mContext, "Error");
			return;

		}

		if (Utill.isNetworkAvailable(mContext)) {

			MultipartEntity multipart = new MultipartEntity();
			
			try {
				
//				if(!refreshHomepage && !isDeleteEvent){
//				
//					showProgress();
//					
//				}
				
				if(!isRefreshnNotify){
					
					showProgress();
					
				}
								
				multipart.addPart("user_id", new StringBody(user.getUser_id()));
				new GetEventsAsync(mContext, new GetEventListener(), multipart).execute();

			} catch (UnsupportedEncodingException e) {

				e.printStackTrace();

		   }
			

		} else {

			Utill.showNetworkError(mContext);

		}
				
	 }
	

	// This is the call back method it will be called whenever current screen
	// will be on front.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.event_list_view, container, false);
		dashBoard = (DashBoard) mActivity;
		
		isRefreshnNotify = false;
		
	    mainEventList = null;
		filteredList = null;
		
		initializeView(view);
		initializeDayView(view);
		setOnClickeListeners();
//		getEventsList();
		
		
		refreshHomepage();
		
		referesh = true;
		Calendar cal = Calendar.getInstance();
		currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

		return view;

	}

	/*
	 * public class BloackListener{ public void onSuccess(String msg){
	 * dashBoard.removeAllFromBackStack(); dashBoard.finish(); Intent intent =
	 * new Intent(mContext, BloackActivity.class); startActivity(intent); }
	 * public void onError(String msg){
	 * 
	 * } }
	 */

	// This method is used for intializing all the view.

	boolean istrue;

	void initializeView(View view) {

		listView = (com.fortysevendeg.swipelistview.SwipeListView) view.findViewById(R.id.example_lv_list);
		searchET = (EditText) view.findViewById(R.id.enter_text);
		searchBT = (EditText) view.findViewById(R.id.search);
		noEventTextView = (TextView) view.findViewById(R.id.no_event_found);
		// nearbyimageView = (ImageView)
		// view.findViewById(R.id.nearbyimageView);

		button_allevent = (Button) view.findViewById(R.id.button_allevent);
		button_yourevent = (Button) view.findViewById(R.id.button_yourevent);
		button_event_type = (Button) view.findViewById(R.id.events_type);
		button_event_type.setVisibility(View.GONE);
		
		button_allevent.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
		button_allevent.setTextColor(getResources().getColor(R.color.white));
		

		relative_layout_search = (RelativeLayout) view.findViewById(R.id.relative_layout_search);

		homeIV = (ImageView) view.findViewById(R.id.homeIV);
		settingIV = (ImageView) view.findViewById(R.id.settingIV);
		createEventIV = (ImageView) view.findViewById(R.id.createEventIV);
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
				
				button_allevent.performClick();
				homeIV.setImageResource(R.drawable.home_active_btn);
				myEventsIV.setImageResource(R.drawable.my_event_deactive_btn);
								
				break;
			case R.id.settingIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_SETTING);
				
				break;
			case R.id.createEventIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_CREATE_EVENT);
				
				break;
			case R.id.myEventsIV:
				homeIV.setImageResource(R.drawable.home_deactive_btn);
				myEventsIV.setImageResource(R.drawable.my_event_active_btn);
				button_yourevent.performClick();
				
				break;
			case R.id.nearByEventsIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT);
				break;

			default:
				break;
			}

		}
	};

	// This method is used for setting click listener to all the views.
	void setOnClickeListeners() {

		All_home.setOnClickListener(clickListener);
		sun_home.setOnClickListener(clickListener);
		mon_home.setOnClickListener(clickListener);
		tue_home.setOnClickListener(clickListener);
		wed_home.setOnClickListener(clickListener);
		thurs_home.setOnClickListener(clickListener);
		fri_home.setOnClickListener(clickListener);
		sat_home.setOnClickListener(clickListener);

		button_yourevent.setOnClickListener(onButtonclickListener);

		button_allevent.setOnClickListener(onButtonclickListener);

		button_event_type.setOnClickListener(onButtonclickListener);

		/*
		 * nearByEventsIV.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT);
		 * 
		 * } });
		 */
		

		setSwipeListview();

		/*
		 * DashBoard.rightButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * dashBoard.swithFragment(DashBoard.FRAGMENT_CREATE_EVENT);
		 * 
		 * } });
		 */
		/*
		 * DashBoard.leftButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { // Utill.showToast(mContext,
		 * "Logout"); dashBoard.swithFragment(DashBoard.FRAGMENT_SETTING); //
		 * logoutFun(); } });
		 */

		// DashBoard.chatIcon.setOnClickListener(new OnClickListener() {
		//
		// @Override
		// public void onClick(View v) {
		//
		// // showCategory();
		//
		// // getEventTypeCategory();
		//
		// showCategory(EventCategoryList.getEventCategoryList());
		//
		// }
		// });
		

		DashBoard.rightButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (relative_layout_search.getVisibility() == View.VISIBLE) {

					relative_layout_search.setVisibility(View.GONE);
					DashBoard.rightButton.setBackground(getResources().getDrawable(R.drawable.button_bg_selector));

				} else {

					relative_layout_search.setVisibility(View.VISIBLE);
					DashBoard.rightButton.setBackground(getResources().getDrawable(R.drawable.button_selected_bg));

				}

			}
		});

		
		searchET.addTextChangedListener(new CustomTextWatcher());
        
		
		searchBT.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				String textSearch = searchET.getText().toString();
				if(textSearch != null && textSearch.length() > 0){
					
					searchInvites(textSearch);
					   			  
				}else{
					
					Utill.showToast(mContext,"Please Enter event Name, event owner");
					
				}
			}
		});
	}

	@Override
	public void onDestroy() {

		super.onDestroy();

	}

	// public void getEventTypeCategory(){
	//
	// UserBean user = Utill.getUserPreferance(mContext);
	// if (user.getUser_id() == null) {
	//
	// Utill.showToast(mContext, "Error");
	// return;
	//
	// }
	//
	// if (Utill.isNetworkAvailable(mContext)) {
	// MultipartEntity multipart = new MultipartEntity();
	// try {
	// showProgress();
	// // multipart.addPart("user_id", new StringBody(user.getUser_id()));
	// new GetEventTypeCategAsyTask(mContext, new GetEventTypeCategListener() {
	//
	// @Override
	// public void onSuccess(ArrayList<EventTypeCategory> eventList) {
	//
	// hideProgress();
	//
	// if(eventList.isEmpty()){
	//
	// return;
	// }
	//
	// showCategory(eventList);
	//
	// }
	//
	// @Override
	// public void onError(String msg) {
	//
	// hideProgress();
	//
	// }
	// }, multipart).execute();
	//
	// } catch (Exception e) {
	//
	// e.printStackTrace();
	//
	// }
	//
	// } else {
	//
	// Utill.showNetworkError(mContext);
	//
	// }
	//
	//
	// }

	public void setSwipeListview() {

		listView.setSwipeListViewListener(baseSwipeListener);
		// These are the swipe listview settings. you can change these
		// setting as your requirement
		// listView.setSwipeMode(SwipeListView.SWIPE_MODE_BOTH); // there are
		// five swiping modes
		// listView.setSwipeActionLeft(SwipeListView.SWIPE_ACTION_NONE); //there
		// are four swipe actions
		// listView.setSwipeActionRight(SwipeListView.SWIPE_ACTION_NONE);
		// listView.setOffsetLeft(convertDpToPixel(0f)); // left side offset
		// listView.setOffsetRight(convertDpToPixel(10f)); // right side offset
		// listView.setAnimationTime(500); // Animation time
		// listView.setSwipeOpenOnLongPress(true); // enable or disable
		// SwipeOpenOnLongPress

	}

	// This listener is used for showing event list according to day selection.
	OnClickListener onButtonclickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {

			resetBar();
			button_event_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			button_yourevent.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			button_allevent.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));

			button_event_type.setTextColor(getResources().getColor(R.color.grey_line_color));
			button_yourevent.setTextColor(getResources().getColor(R.color.grey_line_color));
			button_allevent.setTextColor(getResources().getColor(R.color.grey_line_color));

			switch (v.getId()) {
			case R.id.events_type:
			
				 button_event_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				 button_event_type.setTextColor(getResources().getColor(R.color.white));
				
//				 showCategory(EventCategoryList.getEventCategoryList());
				
				break;

			case R.id.button_yourevent:

				 isClickedOnMyEvents = true;
				
				// button_yourevent.setBackgroundColor(getResources().getColor(R.color.selectedd));
				 button_yourevent.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				 button_yourevent.setTextColor(getResources().getColor(R.color.white));

				 searchET.setText("");
				
				 if (filteredCategoryList != null) {

					if (!filteredCategoryList.isEmpty()) {

						filteredCategoryList.clear();
						
					}

					UserBean user = Utill.getUserPreferance(mContext);

					if (mainEventList != null) {
						
						for (int i = 0; i < mainEventList.size(); i++) {
							EventsBean evtbean = mainEventList.get(i);
							if (evtbean.getEvent_owner_id().equals(user.getUser_id()) || evtbean.getEventAcceptStatus().equalsIgnoreCase("1")) {
								filteredCategoryList.add(evtbean);
							}
						}

						setEventAdapter(filteredCategoryList);
						
					}

				}

				break;

			case R.id.button_allevent:
				
				 isClickedOnAllEvents = true;

				// button_allevent.setBackgroundColor(getResources().getColor(R.color.selectedd));
				button_allevent.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				button_allevent.setTextColor(getResources().getColor(R.color.white));
			
				searchET.setText("");
				
				if (filteredCategoryList != null) {

					 if (!filteredCategoryList.isEmpty()) {
					
						filteredCategoryList.clear();
					
					}					
					
					if (mainEventList != null) {

						for (int i = 0; i < mainEventList.size(); i++) {

							filteredCategoryList.add(mainEventList.get(i));

						}

						setEventAdapter(filteredCategoryList);

				   }
					
				}

				break;

			}

		}

	};
	

	BaseSwipeListViewListener baseSwipeListener = new BaseSwipeListViewListener() {
		@Override
		public void onOpened(int position, boolean toRight) {
			if (listView != null && filteredCategoryList != null) {
				for (int i = 0; i < filteredCategoryList.size(); i++) {
					if (i != position) {

						View view = listView.getChildAt(i - listView.getFirstVisiblePosition());

						if (view != null)
							listView.closeAnimate(i);

						// listView.openAnimate(position);

					}
				}
			}

			// if(listView != null){
			//
			//
			// listView.closeOpenedItems();
			//
			// }

		}

		@Override
		public void onClosed(int position, boolean fromRight) {

			
		}

		@Override
		public void onListChanged() {

			
		}

		@Override
		public void onMove(int position, float x) {

			
		}

		@Override
		public void onStartOpen(int position, int action, boolean right) {

			// Log.d("swipe", String.format("onStartOpen %d - action %d",
			// position, action));

		}

		@Override
		public void onStartClose(int position, boolean right) {

			// Log.d("swipe", String.format("onStartClose %d", position));

		}

		@Override
		public void onClickFrontView(int position) {

			Log.d("swipe", String.format("onClickFrontView %d", position));

			EventDetailFragement.index = position;
			// EventDetailFragement.eventID =
			// eventsList.get(arg2).getEvent_id();
//			EventDetailFragement.eventID = filteredList.get(position).getEvent_id();
 
			if(filteredCategoryList != null){
				
				EventDetailFragement.eventID = filteredCategoryList.get(position).getEvent_id();
				dashBoard.swithFragment(DashBoard.FRAGMENT_EVENT_DETAIL);
				
			}
			
			System.out.println("filter position ========= "+filteredList.get(position).getEvent_id());
			
//			EventDetailFragement.eventID = filteredList.get(position).getEvent_id();
			
			

			// listView.openAnimate(position); //when you touch front view it
			// will open

		}

		@Override
		public void onClickBackView(int position) {

			// Log.d("swipe", String.format("onClickBackView %d", position));

			listView.closeAnimate(position);// when you touch back view it will
											// close

		}

		@Override
		public void onDismiss(int[] reverseSortedPositions) {

		}

	};

	int min = 0;
	int max = 24;

	void initializeDayView(View view) {
		
		headerButtons = (LinearLayout) view.findViewById(R.id.layout_fade);
		
		first_home = (TextView) view.findViewById(R.id.first_home);
		second_home = (TextView) view.findViewById(R.id.second_home);

		All_home = (Button) view.findViewById(R.id.all_home);
		sun_home = (Button) view.findViewById(R.id.sun_home);
		mon_home = (Button) view.findViewById(R.id.mon_home);
		tue_home = (Button) view.findViewById(R.id.tue_home);
		wed_home = (Button) view.findViewById(R.id.wed_home);
		thurs_home = (Button) view.findViewById(R.id.thu_home);
		fri_home = (Button) view.findViewById(R.id.fri_home);
		sat_home = (Button) view.findViewById(R.id.sat_home);

		long a = 0, b = 24;
		RangeSeekBar<Long> seekBar = new RangeSeekBar<Long>(a, b, mContext);
		seekBar.setOnRangeSeekBarChangeListener(seekBarListener);
		LinearLayout layout = (LinearLayout) view.findViewById(R.id.main_home);
		layout.addView(seekBar);
		layout.setVisibility(View.GONE);

	}

	OnRangeSeekBarChangeListener seekBarListener = new OnRangeSeekBarChangeListener<Long>() {
		@Override
		public void onRangeSeekBarValuesChanged(RangeSeekBar<?> bar, Long minValue, Long maxValue) {
			// handle changed range values
			Log.e("min", "" + minValue);
			Log.e("max", "" + maxValue);
			first_home.setText(minValue + ":00");
			second_home.setText(maxValue + ":00");
			min = Integer.parseInt("" + minValue);
			max = Integer.parseInt("" + maxValue);
			finalList = new ArrayList<EventsBean>();
			
			if (filterList != null) {
				
				EventsBean bean = null;
				
				for (int i = 0; i < filterList.size(); i++) {
					
					bean = filterList.get(i);
					// int number =
					// Utill.getDayNumber(bean.getEvent_date(),bean.getEvent_time());
					int timeNumber = Utill.get24HrFormatTime(bean.getEvent_time());
					if ((timeNumber >= min && timeNumber < max)) {
						finalList.add(filterList.get(i));
					}
				}

				setEventAdapter(finalList);

				// EventAdapter adapter = new EventAdapter(mContext,finalList);
				// listView.setAdapter(adapter);

				// setEventAdapter(finalList)

			}
		}
	};

	// This listener is used for showing event list according to day selection.
	OnClickListener clickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			resetBar();
			int id = v.getId();
			int dayNumber = -1;
			getResources().getColor(R.color.unselectedd);
			All_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			sun_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			mon_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			tue_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			wed_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			thurs_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			fri_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			sat_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));

			All_home.setTextColor(getResources().getColor(R.color.red_text));
			sun_home.setTextColor(getResources().getColor(R.color.red_text));
			mon_home.setTextColor(getResources().getColor(R.color.red_text));
			tue_home.setTextColor(getResources().getColor(R.color.red_text));
			wed_home.setTextColor(getResources().getColor(R.color.red_text));
			thurs_home.setTextColor(getResources().getColor(R.color.red_text));
			fri_home.setTextColor(getResources().getColor(R.color.red_text));
			sat_home.setTextColor(getResources().getColor(R.color.red_text));

			/*
			 * All_home.setBackgroundColor(getResources().getColor(R.color.
			 * unselectedd));
			 * sun_home.setBackgroundColor(getResources().getColor
			 * (R.color.unselectedd));
			 * mon_home.setBackgroundColor(getResources()
			 * .getColor(R.color.unselectedd));
			 * tue_home.setBackgroundColor(getResources
			 * ().getColor(R.color.unselectedd));
			 * wed_home.setBackgroundColor(getResources
			 * ().getColor(R.color.unselectedd));
			 * thurs_home.setBackgroundColor(getResources
			 * ().getColor(R.color.unselectedd));
			 * fri_home.setBackgroundColor(getResources
			 * ().getColor(R.color.unselectedd));
			 * sat_home.setBackgroundColor(getResources
			 * ().getColor(R.color.unselectedd));
			 */

			switch (id) {
			case R.id.all_home:
				if (filteredCategoryList != null) {
					// EventAdapter adapter = new EventAdapter(mContext,
					// filteredCategoryList);

					setEventAdapter(filteredCategoryList);

					filterList = filteredCategoryList;
					finalList = filteredCategoryList;
					// listView.setAdapter(adapter);
				}
				// All_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				All_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				All_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.mon_home:
				dayNumber = 2;
				// mon_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				mon_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				mon_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.tue_home:
				dayNumber = 3;
				// tue_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				tue_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				tue_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.wed_home:
				dayNumber = 4;
				// wed_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				wed_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				wed_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.thu_home:
				dayNumber = 5;
				// thurs_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				thurs_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				thurs_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.fri_home:
				dayNumber = 6;
				// fri_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				fri_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				fri_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.sat_home:
				dayNumber = 7;
				// sat_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				sat_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				sat_home.setTextColor(getResources().getColor(R.color.white));
				break;
			case R.id.sun_home:
				dayNumber = 1;
				// sun_home.setBackgroundColor(getResources().getColor(R.color.selectedd));
				sun_home.setBackgroundDrawable(getResources().getDrawable(R.drawable.selected_bg));
				sun_home.setTextColor(getResources().getColor(R.color.white));
				break;

			default:
				break;
			}

			if (filteredCategoryList != null && dayNumber != -1) {
				filterList = new ArrayList<EventsBean>();
				EventsBean bean = null;
				for (int i = 0; i < filteredCategoryList.size(); i++) {
					bean = filteredCategoryList.get(i);
					int number = Utill.getDayNumber(bean.getEvent_date(), bean.getEvent_time());
					int timeNumber = Utill.get24HrFormatTime(bean.getEvent_time());
					if (number == dayNumber && (timeNumber >= min && timeNumber < max)) {
						filterList.add(filteredCategoryList.get(i));
					}
				}
				finalList = filterList;

				// EventAdapter adapter = new EventAdapter(mContext,
				// filterList);
				// listView.setAdapter(adapter);

				setEventAdapter(filterList);

			} else {
			}

		}
	};

	// for resetting the bar to its initial position.
	void resetBar() {/*
					 * long a = 0, b = 24; min = 0; max = 24;
					 * first_home.setText("00:00");
					 * second_home.setText("24:00"); RangeSeekBar<Long> seekBar
					 * = new RangeSeekBar<Long>(a, b, mContext);
					 * seekBar.setOnRangeSeekBarChangeListener(seekBarListener);
					 * LinearLayout layout = (LinearLayout)
					 * getView().findViewById(R.id.main_home);
					 * layout.removeAllViews(); layout.addView(seekBar);
					 */
	}

	
	String eventTypeCategId;//used to save the eventtypecategid for sending to server as a selected event type by user
	public void showCategory(final ArrayList<EventTypeCategory> eventList) {

		eventTypeCategId = null;
			
		final AlertDialog alertDialog = new AlertDialog.Builder(mContext).create();
		LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.listview, null);
		ListView listView = (ListView) v.findViewById(R.id.list);
			
		EventCategoryTypeAdapter evtcategAdapter = new EventCategoryTypeAdapter(mContext,eventList);
		listView.setAdapter(evtcategAdapter);
		
		
		listView.setOnItemClickListener(new OnItemClickListener() {
		
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				
				eventTypeCategId = eventList.get(position).getCategory_id();			
							
			    alertDialog.dismiss();
			    
			    button_event_type.setBackgroundDrawable(getResources().getDrawable(R.drawable.unselect_bg));
			    button_event_type.setTextColor(getResources().getColor(R.color.grey_line_color));
			   		
			    if (!filteredCategoryList.isEmpty()) {

					filteredCategoryList.clear();
					
				}
			    
				if (mainEventList != null) {
				
					for (int i = 0; i < mainEventList.size(); i++) {
					
						EventsBean evtbean = mainEventList.get(i);
						if (evtbean.getEvent_category().equalsIgnoreCase(eventTypeCategId)) {
							
							filteredCategoryList.add(evtbean);
							
						}
					}

					setEventAdapter(filteredCategoryList);

			  }
			    			
			}
			
		});
		
		
		alertDialog.setView(v);
		alertDialog.show();
	}
	
	
	// String categoryType[] = { "All", "Soccer", "Basketball", "Tennis", "Gym",
	// "Workout", "Social Event", "Party" };

	/*
	 * int getCategoryIndex(String category) {
	 * 
	 * if (category.equalsIgnoreCase("Soccer")) { return 1; } else if
	 * (category.equalsIgnoreCase("Basketball")) { return 2; } else if
	 * (category.equalsIgnoreCase("Tennis")) { return 3; } else if
	 * (category.equalsIgnoreCase("Gym")) { return 4; } else if
	 * (category.equalsIgnoreCase("Workout")) { return 5; } else if
	 * (category.equalsIgnoreCase("Social")) { return 6; } else if
	 * (category.equalsIgnoreCase("Event")) { return 7; } else if
	 * (category.equalsIgnoreCase("Party")) { return 8; }
	 * 
	 * return 0; }
	 */
	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}

	public class CustomTextWatcher implements TextWatcher {

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			if(s.toString().isEmpty())
				 searchInvites(s.toString());
		}

		@Override
		public void onTextChanged(CharSequence text, int start, int before, int count) {
			
//			if(text != null && text.length() > 0){
				
			   String textSearch = text.toString();
			   searchInvites(textSearch);
			   			  
//			}
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

		  if(isClickedOnMyEvents){
			
			  myEvent();
				
				
		  }else{
				
				allEvent();
				
	      }
		
		
		  
		  
		 if (filteredCategoryList != null && filteredCategoryList.size() > 0) {
			
			// ArrayList<EventsBean>
			filteredList = new ArrayList<EventsBean>();
		
			if (!Utill.isStringNullOrBlank(searchTag)) {
				
				for (EventsBean event : filteredCategoryList) {
					/*
					 * if
					 * (event.getEvent_title().toLowerCase().contains(searchTag
					 * .toLowerCase())) { filteredList.add(event); }
					 */
					if (event.getEvent_owner_name().toLowerCase().contains(searchTag.toLowerCase())
							|| event.getEvent_title().toLowerCase().contains(searchTag.toLowerCase())) {
					
						filteredList.add(event);
						
					}
				}
			
			} else {				
				
				for (EventsBean event : filteredCategoryList) {
					
					filteredList.add(event);
					
				}
				
//				filteredList = filteredCategoryList;
															
			}
				
			
			if (filteredList.size() > 0) {
				
				listView.setVisibility(View.VISIBLE);
				noEventTextView.setVisibility(View.GONE);
				// EventAdapter adapter = new EventAdapter(mContext,
				// filteredList);
				// listView.setAdapter(adapter);
				// adapter.notifyDataSetChanged();

				if(filteredCategoryList != null && !filteredCategoryList.isEmpty()){
					
					filteredCategoryList.clear();
					
				}else{
					
					filteredCategoryList = new ArrayList<EventsBean>();
					
				}
				
				for(int i=0;i<filteredList.size();i++){
					
					filteredCategoryList.add(filteredList.get(i));
					
				}
				
				setEventAdapter(filteredCategoryList);
				
				
			} else {
				
				listView.setVisibility(View.GONE);
				noEventTextView.setVisibility(View.VISIBLE);
				noEventTextView.setText("No Result found for " + searchTag);
				
			}
			
		} else {
			
			listView.setVisibility(View.GONE);
			noEventTextView.setVisibility(View.VISIBLE);
			
		}

	}
	

	public void myEvent(){
		
		
		 filteredCategoryList = new ArrayList<EventsBean>();
	
		 UserBean user = Utill.getUserPreferance(mContext);

		   if (mainEventList != null) {
				
				for (int i = 0; i < mainEventList.size(); i++) {
					EventsBean evtbean = mainEventList.get(i);
					if (evtbean.getEvent_owner_id().equals(user.getUser_id()) || evtbean.getEventAcceptStatus().equalsIgnoreCase("1")) {
						filteredCategoryList.add(evtbean);
					}
				}
		   }
		
	}
	
	public void allEvent(){
		
		filteredCategoryList = new ArrayList<EventsBean>();
		 
		if (mainEventList != null) {
		  for (int i = 0; i < mainEventList.size(); i++) {

		  filteredCategoryList.add(mainEventList.get(i));

		 }
	   }
		
	}
	
	
	
	boolean headerOpen;
	Animation anim;
	@Override
	public void onStart() {

		if (DashBoard.actionBar != null) {
			
			  DashBoard.mainActionRelative.setOnTouchListener(new OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
					
					if(headerButtons.getVisibility() == View.VISIBLE){
						headerOpen = false;
					}else{
						headerOpen = true;
					}
					if(headerOpen){
						anim = AnimationUtils.loadAnimation(mContext,R.anim.bottom_down);
					}else {
						anim = AnimationUtils.loadAnimation(mContext,R.anim.bottom_up);
					}
					anim.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
							headerButtons.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if(!headerOpen)
								headerButtons.setVisibility(View.GONE);
							
							DashBoard.mainActionRelative.setClickable(true);
					   }
						
					});
					
					headerButtons.startAnimation(anim);
				
					
				}
					return false;
				}
			
			});
			
			
		/*	DashBoard.mainActionRelative.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					DashBoard.mainActionRelative.setClickable(false);
					if(headerButtons.getVisibility() == View.VISIBLE){
						headerOpen = false;
					}else{
						headerOpen = true;
					}
					if(headerOpen){
						anim = AnimationUtils.loadAnimation(mContext,R.anim.bottom_down);
					}else {
						anim = AnimationUtils.loadAnimation(mContext,R.anim.bottom_up);
					}
					anim.setAnimationListener(new AnimationListener() {
						
						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub
							
							headerButtons.setVisibility(View.VISIBLE);
						}
						
						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub
							
						}
						
						@Override
						public void onAnimationEnd(Animation animation) {
							if(!headerOpen)
								headerButtons.setVisibility(View.GONE);
							DashBoard.mainActionRelative.setClickable(true);
						}
					});
					headerButtons.startAnimation(anim);
				}
				
			});*/
			
			  
			  
			DashBoard.resetActionBarTitle("Meet Me Up",1);
			DashBoard.rightButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setVisibility(View.GONE);
			DashBoard.leftButton.setImageResource(R.drawable.setting_icon);
			// DashBoard.chatIcon.setVisibility(View.VISIBLE);
			// DashBoard.chatIcon.setImageResource(R.drawable.filter_icon);

			DashBoard.rightButton.setImageResource(R.drawable.seacrh_icon);
			
			if(!searchET.getText().toString().isEmpty())
			 searchET.setText("");
			
		}
		if (referesh) {
			listView.setVisibility(View.GONE);
			referesh = false;
		}
		if (loadAgain) {
			getEventsList();
			loadAgain = false;
		}

		// isFirstviewCreated = true;

		// getEventsList();

		super.onStart();

		// listView.setSwipeListViewListener(baseSwipeListener);
		// listView.dispatchTouchEvent(null);

	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
				
		super.onStop();

	}
	
	
	// This class is used for notify weather event list got successfully or not.
	public class GetEventListener {

		public void onSuccess(ArrayList<EventsBean> list, String msg) {

			 hideProgress();
			 
			 mainEventList = list;
			 filteredList = list;
		
		     filteredCategoryList = new ArrayList<EventsBean>();
			
		     
			   for (int i = 0; i < mainEventList.size(); i++) {
 
				  filteredCategoryList.add(mainEventList.get(i));

			   }
		
				
//			  String textSearch = searchET.getText().toString();
//			
//			  if(textSearch != null && textSearch.length() > 0){
//					
//				 searchInvites(textSearch);
//					   			  
//			  }
			
			 
			isRefreshStopped = false; 
			 
			if(isClickedOnMyEvents){
				
				button_yourevent.performClick();
				isClickedOnMyEvents = false;
				return;
				
			}else if(isClickedOnAllEvents){
				
				button_allevent.performClick();
				isClickedOnAllEvents = false;
				return;
				
			}
			
			
			setEventAdapter(filteredCategoryList);
//			button_allevent.performClick();
//			Utill.showToast(mContext, msg);
						
						
		}

		public void onError(String msg) {

//			Utill.showToast(mContext, msg);
			listView.setVisibility(View.GONE);
			noEventTextView.setVisibility(View.VISIBLE);
			hideProgress();
			isRefreshStopped = false; 

		}

	}

	@Override
	public void onPause() {

		super.onPause();

		if (!getDispatchState()) {

			setDispatchState(true);

		}
	}

	@Override
	public void onResume() {

		super.onResume();

		if (getDispatchState()) {

			if (filteredCategoryList != null) {

				// listView.setSwipeListViewListener(baseSwipeListener);
				// swipeAdapter.notifyDataSetChanged();
				setEventAdapter(filteredCategoryList);

			}
		}

	}

	public class setOnJoinClickListener {
		
		final ArrayList<EventsBean> joinList;
		
		  public setOnJoinClickListener(final ArrayList<EventsBean> joinlist){
			
			this.joinList = joinlist;			
			
		  }		
				
		  public void onClickFeedback(int position){
			
			EventsBean bean =  joinList.get(position);

			if(bean.isPassed()){
				
//				Toast.makeText(mContext,"click on send feedback",Toast.LENGTH_LONG).show();
//				SendFeedBackFragement.eventBean = bean;
//				dashBoard.swithFragment(DashBoard.FRAGMENT_FEEDBACK);
//				showRatting(bean);
								 
			    getEventDetail(bean.getEvent_id(),"1",bean.isMyEvent());
										
		   }
			
		}
		
		public void onClickJoin(int position) {
			// TODO Auto-generated method stub
						
			
			UserBean user = Utill.getUserPreferance(mContext);
			
			if (Utill.isNetworkAvailable(mContext)) {
				
				MultipartEntity multipart = new MultipartEntity();
				
				try {

					Log.e("Current date", "current date " + currentDateTimeString);
					/*if(filteredList==null||filteredList.size()==0){
						return;
					}
						
					
					String event_id = filteredList.get(position).getEvent_id();*/
					
//					if(filteredCategoryList==null||filteredCategoryList.size()==0){
//						
//						return;
//						
//					}
//						
//					
//					String event_id = filteredCategoryList.get(position).getEvent_id();
					
					
//					joinList.get(position).getEventAcceptStatus();
					
					String status = "1";
					String acceptStatus = joinList.get(position).getEventAcceptStatus();
					if(acceptStatus.equalsIgnoreCase("1")){
						
						status = "2";
//						txtviewJoin.setText("Join");
//						imgviewJoin.setImageResource(R.drawable.join_icon);
					
					}else{
						
						status = "1";
//						txtviewJoin.setText("Joined");
//						imgviewJoin.setImageResource(R.drawable.joined_icon);
						
					}
										
					if(joinList == null || joinList.size()==0){
						
						return;
						
					}
					
					String event_id = joinList.get(position).getEvent_id();
					
					
					multipart.addPart("user_id", new StringBody(user.getUser_id()));
					multipart.addPart("event_id", new StringBody(event_id));
					multipart.addPart("current_date", new StringBody(currentDateTimeString));
					
					/*
					 * if (eventBean!=null &&
					 * eventBean.getEvent_type().equalsIgnoreCase("1")) { status
					 * = "2"; }else if (eventBean!=null &&
					 * eventBean.getEvent_type().equalsIgnoreCase("2")) { status
					 * = "1"; }
					 */multipart.addPart("status", new StringBody(status));

//					showProgress();
					new AcceptAsync(mContext, new AcceptRejectListnerHome(), multipart).execute();
					
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
					
				}catch (Exception e) {
					
					e.printStackTrace();
					
				}
			} else {
				Utill.showNetworkError(mContext);
			}
		}
	}
	
    
	// this is participants dialogue
		void showRatting(EventsBean eventBean) {
			
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

				  RattingAdapter adapter = new RattingAdapter(mContext, eventBean.getAcceptedParticipantsList(), 1);
				  listView.setAdapter(adapter);
				  
				}else{
				  
					Toast.makeText(mContext, "No Rating available",Toast.LENGTH_SHORT).show();
				   
			   }

			
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
	
	
	
	public class AcceptRejectListnerHome {
		
		public void onSuccess(String msg) {
			
//			hideProgress();
//			Utill.showToast(mContext, msg);
			isRefreshnNotify = true;
//			getEventsList();
			
		}

		public void onError(String msg) {
			
//			hideProgress();
			Utill.showToast(mContext, msg);
			
	    }
		
	}
	

	public void setEventAdapter(final ArrayList<EventsBean> list) {

				
		swipeAdapter = null;

//		swipeAdapter = new SwipeviewAdapter(mContext, list, new setOnJoinClickListener());
		swipeAdapter = new SwipeviewAdapter(mContext, list, new setOnJoinClickListener(list));

		swipeAdapter.setEventItemClickListener(new SwipeEventItemClickListener() {

			@Override
			public void onClickProfilePic(String user_id) {

				dashBoard.showOwnerProfile(user_id);

			}

			@Override
			public void onEditButtonClick(int position) {
				
//				Toast.makeText(mContext,"Edit event",Toast.LENGTH_LONG).show();
				
				if(list != null && !list.isEmpty()){
					
					EventsBean evtbean = list.get(position);					
					getEventDetail(evtbean.getEvent_id(),"0",false);
										
				} 
				
			}
			
						
			@Override
			public void onFirstButtonClick(int position) {

//				Toast.makeText(mContext,"invite friend ",Toast.LENGTH_LONG).show();
				sendNotification(null);
				
			}

			
			@Override
			public void onSecondButtonClick(final int position) {

				listView.closeAnimate(position);

				final Dialog dialog = new Dialog(mContext);
				dialog.setContentView(R.layout.final_logout);
				dialog.setTitle(""
						+ "Do want to delete event?");
				Button logout_yes = (Button) dialog.findViewById(R.id.logout_yes);
				Button logout_no = (Button) dialog.findViewById(R.id.logout_no);
				logout_yes.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						/*
						 * SessionManager.clearUserLogin(mContext); ((Activity)
						 * mContext).finish();
						 */
						
						 if (list != null && !list.isEmpty()) {

							deleteEventParticepents(list.get(position).getEvent_id());

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
		
		});

		listView.setAdapter(swipeAdapter);

		// listView.setAdapter(adapter);
		listView.setVisibility(View.VISIBLE);
		noEventTextView.setVisibility(View.GONE);

	}

	// This method is used for getting Detail for a perticular Event.
		void getEventDetail(String event_id,String state,boolean ismyevent) {
			UserBean user = Utill.getUserPreferance(mContext);
			if (user.getUser_id() == null) {
				Utill.showToast(mContext, "Error");
				return;
			}
			
			if (Utill.isNetworkAvailable(mContext)) {
				MultipartEntity multipart = new MultipartEntity();
				try {
//					String event_id = eventID;// HomeFragment.filteredList.get(index).getEvent_id();
					multipart.addPart("user_id", new StringBody(user.getUser_id()));
					multipart.addPart("event_id", new StringBody(event_id));
//					showProgress();
					new GetEventDetialAsync(mContext, new GetEventDetailListener(state,ismyevent), multipart).execute();
			
				} catch (UnsupportedEncodingException e) {
					
					e.printStackTrace();
					
				}
				
			} else {
				
				Utill.showNetworkError(mContext);
				
			}
		
		}
	
	
	public class GetEventDetailListener implements com.meetmeup.asynch.GetEventDetialAsync.GetEventDetailListener{

		String state;
		boolean ismyevent;
		
		public GetEventDetailListener(String stat,boolean ismyevt){
			
			this.state = stat;
			this.ismyevent = ismyevt;
			
		}
		
		public void onSuccess(EventsBean bean, String msg) {
			
			if(!state.equalsIgnoreCase("1")){
			 
			   EditEventFragement.dataBean = bean;
			   dashBoard.swithFragment(DashBoard.FRAGMENT_EDIT_EVENT);
			
			}else{
				
				  if(ismyevent ){
					  
				      SendFeedBackFragement.eventBean = bean;
			          dashBoard.swithFragment(DashBoard.FRAGMENT_FEEDBACK);
				
				  }else{
					  
					  showRatting(bean);
				 }
			   
			}
			
		}

		@Override
		public void onError(String msg) {
			
			
			
		}
	
	}
	
	
	
	public void deleteEventParticepents(String event_id) {

		UserBean user = Utill.getUserPreferance(mContext);
		
		if (user.getUser_id() == null) {

			Utill.showToast(mContext, "User not exists");
			return;

		}

		if (Utill.isNetworkAvailable(mContext)) {

			MultipartEntity multipart = new MultipartEntity();
			// MultipartEntity multipart = new
			// MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null,
			// Charset.forName(HTTP.UTF_8));

			try {

				multipart.addPart("user_id", new StringBody(user.getUser_id()));
				multipart.addPart("event_id", new StringBody(event_id));

				new DeleteEventParticipentAsyTask(mContext, new DeleteEventParticipentListener(), multipart).execute();

			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				Log.e("unsupportedEncodingException", e.toString());
			}

		} else {

			Utill.showNetworkError(mContext);

		}

	}

	public class DeleteEventParticipentListener {

		public void onSuccess(String msg) {

//			Toast.makeText(mContext, "Successfully deleted", Toast.LENGTH_SHORT).show();
			
//			isDeleteEvent = true;
			
//			if(!isRefreshnNotify){
			   
//				getEventsList();
			   
//			}
			
			isRefreshnNotify = true;
			
//		    mHandler.post(mUpdateResults);		
					
			
		}

		public void onError(String msg) {

			Toast.makeText(mContext,msg, Toast.LENGTH_SHORT).show();
			
		}

	}

	public int convertDpToPixel(float dp) {

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		float px = dp * (metrics.densityDpi / 160f);
		return (int) px;

	}

	/*
	 * this class onClickProfilePic is used for open profile detail on click of
	 * owner profile image from eventAdapter
	 */
	// public class EventItemClickListener{
	//
	// public void onClickProfilePic(String user_id){
	//
	// dashBoard.showOwnerProfile(user_id);
	//
	// }
	//
	// }

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

	// public boolean isDispatch = false;

	// @Override
	// public boolean onTouch(View v, MotionEvent event) {
	//
	//
	//
	//
	// return false;
	// }

	public void setDispatchState(boolean state) {

		try {

			SharedPreferences shpf = mContext.getSharedPreferences("dispatchState", 0);

			Editor editor = shpf.edit();
			editor.clear();
			editor.putBoolean("state", state);
			editor.commit();

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

	public boolean getDispatchState() {

		try {

			SharedPreferences shpf = mContext.getSharedPreferences("dispatchState", 0);
			return shpf.getBoolean("state", false);

		} catch (Exception e) {

			e.printStackTrace();

		}

		return false;

	}
	
	
	Handler mHandler = new Handler();
	
	Runnable mUpdateResults =  new Runnable() {
		
		public void run() {
						
			 searchET.setText("");		
						
			 getEventsList();
//			 refreshHomepage = false;
					
			 isRefreshnNotify = false;
			 
//			 isRefreshInInterval = false;
//			 isDeleteEvent = false;
			 
		}
		
	};

	
//	Timer timerRefreshInterval;
	Timer timerRefreshNotify;
//	public static boolean refreshHomepage = false;
//	public boolean isDeleteEvent = false;
	public boolean isRefreshStopped = false;	
	public static boolean isRefreshnNotify = false;	
	public long refreshTime;
	
	public void refreshHomepage(){
		
		
		// Create runnable for posting
		 
//		refreshHomepage = false;
		
//		isRefreshInInterval = true;
		
		listView.setVisibility(View.GONE);
		getEventsList();
		
//		long delay = 1000; // delay for 1 sec.
//		long period = 6000*10*2; // repeat every 6 sec.
//		timerRefreshInterval = new Timer();
//		showProgress();
//		timerRefreshInterval.scheduleAtFixedRate(new TimerTask() {
//			public void run() {
//				
//				if(!isRefreshnNotify && !isRefreshStopped && Math.abs(refreshTime - System.currentTimeMillis()) > 6000*10*2){					
//				   						
//					mHandler.post(mUpdateResults);
//				   	  			   
//				}
//			    				
//			}
//			
//		}, delay, period);
					
		
				
		long delay = 1000; // delay for 1 sec.
		long period = 1000; // repeat every 6 sec.
		timerRefreshNotify = new Timer();
		showProgress();
		timerRefreshNotify.scheduleAtFixedRate(new TimerTask() {
		    public void run() {
								
				if(isRefreshnNotify){					
					
					refreshTime = System.currentTimeMillis();
					isRefreshStopped = true;
				    mHandler.post(mUpdateResults);
				        				 				   				  
				}
				
			}
			
		}, delay, period);
					
	}
	
	public void stopRefresh(){
				
//       if(timerRefreshInterval != null){
//			
//			timerRefreshInterval.cancel();
//		}
       if(timerRefreshNotify != null){
			
    	   timerRefreshNotify.cancel();
		}
		
	}
	
	
	@Override
	public void onDestroyView() {
	
		stopRefresh();
		super.onDestroyView();
		
	}
	public HomeFragment() {
		// TODO Auto-generated constructor stub
	}
	

	void sendNotification(Session s){
		Session session = Session.getActiveSession(); 

		//if(session==null)
		{                      
		    // try to restore from cache
		    session = Session.openActiveSessionFromCache(dashBoard);
		}

		Bundle params = new Bundle();
	    params.putString("message", "Learn how to make your Android apps social");
		params.putString("name", "MeetMeUp");
		params.putString("caption", "Discover events nearby, easily create events and invite your friends!");
		params.putString("description","Finding, creating and sharing activities you’re passionate about are made easy with MeetMeUp. Whether you’re organizing a small gathering with friends, looking for a neighborhood football game or going to a local music concert, MeetMeUp makes it happen.");
		params.putString("link", "https://apps.facebook.com/946706258674050");
		params.putString("picture", "http://72.167.41.165/meetmeup/webservices/images/aaaaa-05.jpg");

		
	    WebDialog requestsDialog = (
	            new WebDialog.RequestsDialogBuilder(dashBoard,
	                    session,
	                    params))
	            .setOnCompleteListener(new WebDialog.OnCompleteListener() {

	                @Override
	                public void onComplete(Bundle values,
	                                       FacebookException error) {
	                    if (error != null) {
	                        if (error instanceof FacebookOperationCanceledException) {
	                            Toast.makeText(dashBoard,
	                                    "Request cancelled",
	                                    Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(dashBoard,
	                                    "Network Error",
	                                    Toast.LENGTH_SHORT).show();
	                        }
	                    } else {
	                        final String requestId = values.getString("request");
	                        if (requestId != null) {
	                            Toast.makeText(dashBoard,
	                                    "Request sent",
	                                    Toast.LENGTH_SHORT).show();
	                        } else {
	                            Toast.makeText(dashBoard,
	                                    "Request cancelled",
	                                    Toast.LENGTH_SHORT).show();
	                        }
	                    }
	                }

	            })
	            .build();
	    requestsDialog.show();
	}
}
