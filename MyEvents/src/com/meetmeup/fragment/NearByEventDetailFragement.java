package com.meetmeup.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.MapActivity;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.EventAdapter;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.helper.Utill;

//This class is used for showing event detail near by.
public class NearByEventDetailFragement extends Fragment {
	public static String eventID;
	public static int index;
	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	public static Activity mActivity;
	DashBoard dashBoard;
	EventAdapter adapter;
	TextView event_name, event_address, event_date, event_time;
	TextView event_description, event_createdby, event_type/*, accept_btn, reject_btn, total, setAlarm*/;
	View view;
	String currentDateTimeString;
//	RelativeLayout accept_reject_btn;
//	Button sendMoneyBtn;
//	ImageButton editBT, deleteBT, participantsBT, chatBT;
	public static EventsBean bean;

	// This method is used for instantiating current class object.
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new NearByEventDetailFragement();
		}
		// eventID = null;
		return mfFragment;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		view = inflater.inflate(R.layout.new_nearbyevent_detail, container, false);
		dashBoard = (DashBoard) mActivity;
		initializeView();
		setOnClickeListeners();
		/*
		 * Calendar cal = Calendar.getInstance(); currentDateTimeString =
		 * DateFormat.getDateTimeInstance().format(new Date());
		 */
		setdataToView();
		return view;
	}

	// This method is used for initializing Views.
	void initializeView() {
		event_name = (TextView) view.findViewById(R.id.event_name);
		event_address = (TextView) view.findViewById(R.id.event_address);
		event_date = (TextView) view.findViewById(R.id.date);
		event_time = (TextView) view.findViewById(R.id.time);
		event_description = (TextView) view.findViewById(R.id.event_description);
		event_createdby = (TextView) view.findViewById(R.id.created_by);
		event_type = (TextView) view.findViewById(R.id.event_type);
//		accept_btn = (TextView) view.findViewById(R.id.accept);
//		accept_btn.setVisibility(View.GONE);
//		reject_btn = (TextView) view.findViewById(R.id.reject);
//		reject_btn.setVisibility(View.GONE);
//		sendMoneyBtn = (Button) view.findViewById(R.id.send_money);
//		sendMoneyBtn.setVisibility(View.GONE);
//		accept_reject_btn = (RelativeLayout) view.findViewById(R.id.accept_reject);
//		accept_reject_btn.setVisibility(View.GONE);
//		total = (TextView) view.findViewById(R.id.total);
//		total.setVisibility(View.GONE);
//		editBT = (ImageButton) view.findViewById(R.id.edit);
//		editBT.setVisibility(View.GONE);
//		deleteBT = (ImageButton) view.findViewById(R.id.delete);
//		deleteBT.setVisibility(View.GONE);
//		participantsBT = (ImageButton) view.findViewById(R.id.participants);
//		participantsBT.setVisibility(View.GONE);
//		chatBT = (ImageButton) view.findViewById(R.id.chat);
//		chatBT.setVisibility(View.GONE);
//		setAlarm = (TextView) view.findViewById(R.id.set_alarm);
//		setAlarm.setVisibility(View.GONE);

	}

	// This method is used for set Click listenrs on the views.
	void setOnClickeListeners() {

		event_address.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (bean != null) {
					Intent intent = new Intent(mActivity, MapActivity.class);
					intent.putExtra("lat", bean.getEvent_lat());
					intent.putExtra("lon", bean.getEvent_eventLong());
					intent.putExtra("name", bean.getEvent_title());
					startActivity(intent);
				} else {
					Utill.showToast(mContext, "Some Error in Address.");
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

	
	// This is the call back method.
	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("Event Neary Detail",-2);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
			DashBoard.rightButton.setImageResource(R.drawable.participants_icon);
			DashBoard.rightButton.setVisibility(View.GONE);
//			DashBoard.chatIcon.setVisibility(View.GONE);
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
     
	
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		final InputMethodManager imm = (InputMethodManager) mActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(getView().getWindowToken(), 0);
	}

	
	//set data to view which we have already geto on nearBy Evetns Fragment.
	void setdataToView() {
		event_name.setText(bean.getEvent_title());
		event_date.setText(bean.getEvent_date());
		event_time.setText(bean.getEvent_time());
		event_description.setText(bean.getEvent_description());
		event_type.setText(bean.getEvent_type());
		event_createdby.setText(bean.getEvent_owner_name());
		event_address.setText(bean.getEvent_Address());
		if (bean.getEvent_type().equalsIgnoreCase("1")) {
			event_type.setText("Public");
		} else {
			event_type.setText("Private");
		}
	}

}
