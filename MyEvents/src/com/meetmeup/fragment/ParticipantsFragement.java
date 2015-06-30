package com.meetmeup.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;

//This class is used for showing list of all the participants.
public class ParticipantsFragement extends Fragment {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new ParticipantsFragement();
		}
		return mfFragment;
	}
	
	@Override
	public void onStart() {
		if(DashBoard.actionBar!=null){
			DashBoard.resetActionBarTitle("Participants",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		super.onStart();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.participants_list_view, container, false);
		initializeView(view);
		setOnClickeListeners();
		return view;
	}
	
	void initializeView(View view){
		
	}

	public void setOnClickeListeners() {
		
		DashBoard.leftButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (mFragmentManager.getBackStackEntryCount() > 0) {
					mFragmentManager.popBackStack();
				}
			}
		});
	}

	
}
