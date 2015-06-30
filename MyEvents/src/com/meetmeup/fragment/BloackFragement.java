package com.meetmeup.fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.LoginActivity;
import com.meetmeup.activity.R;
import com.meetmeup.helper.Utill;

//This class is used for sending money to the event owner.
public class BloackFragement extends Fragment {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	static DashBoard dashBoard;
	TextView bloackTV;
	public static String bloackMsg ="You Are Bloacked.";
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		dashBoard = (DashBoard) mContext;
		mFragmentManager = fm; 
		if (mfFragment == null) {
			mfFragment = new BloackFragement();
		}
		return mfFragment;
	}
	
	@Override
	public void onStart() {
		if(DashBoard.actionBar!=null){
			DashBoard.resetActionBarTitle("Bloack",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.setting_icon);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		bloackTV.setText(bloackMsg);
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.activity_bloack, container, false);
		initializeView(view);
		setOnClickeListeners();
		return view;
	}
	
	void initializeView(View view){
		bloackTV = (TextView) view.findViewById(R.id.bloack_msg);
		
	}

	public void setOnClickeListeners() {
		
		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutFun();
			}
		});
	}
	private void logoutFun() {
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.final_logout);
		dialog.setTitle("Do you want to logout?");
		Button logout_yes = (Button) dialog.findViewById(R.id.logout_yes);
		Button logout_no = (Button) dialog.findViewById(R.id.logout_no);
		logout_yes.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				/*
				 * SessionManager.clearUserLogin(mContext); ((Activity)
				 * mContext).finish();
				 */
				onLogout();
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
	// when clicked on logout.all data from shared preference will be cleared.
	public void onLogout() {
		Utill.removeUserPreference(mContext);
		LoginActivity.callFacebookLogout(mContext);
		dashBoard.finish();
		/*Intent intent = new Intent(mActivity,LoginActivity.class);
		startActivity(intent);*/
	}

	
}
