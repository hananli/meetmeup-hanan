package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.asynch.GetUserProfileAsync;
import com.meetmeup.bean.ProfileBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.RoundedImageView;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

//This class is used for sending money to the event owner.
public class MyProfileFragement extends Fragment {

	static Context mContext;
	static Activity mActivity;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;

  ImageView profile_pic;
	TextView name, yellowCard,acceptedEvents,percentTV;
	RatingBar ratting;
	DisplayImageOptions opt;
	ImageLoader image_load;
	ProgressDialog progress;
	ProgressBar progressOnProfilePic;

	static String user_id;
	
	
	public static Fragment getInstance(Context ct, FragmentManager fm,String userid) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new MyProfileFragement();
		}
		
		user_id = userid;
		
		return mfFragment;
	}

	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("My Profile",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		super.onStart();
	}
	@Override
	public void onResume() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("My Profile",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		super.onResume();
	}

	void getUserDetail() {
		if (Utill.isNetworkAvailable(mContext)) {
			MultipartEntity multi = new MultipartEntity();
			try {
//				UserBean user = Utill.getUserPreferance(mContext);
				multi.addPart("user_id", new StringBody(user_id));
				showProgress();
				new GetUserProfileAsync(mContext, new ProfileListener(), multi).execute();
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
		} else {
			Utill.showNetworkError(mContext);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.my_profile, container, false);
		initializeView(view);
		setOnClickeListeners();
		getUserDetail();
	//	setData();
		return view;
	}

	public class ProfileListener {
		public void onSuccess(ProfileBean bean) {
			setData(bean);
			hideProgress();
		}

		public void onError(String msg) {
			hideProgress();
		}
	}

	void initializeView(View view) {
		profile_pic = (ImageView) view.findViewById(R.id.profile);
		progressOnProfilePic = (ProgressBar) view.findViewById(R.id.progressbar);
		name = (TextView) view.findViewById(R.id.name);
	
		yellowCard = (TextView) view.findViewById(R.id.yellow_card);
		acceptedEvents =  (TextView) view.findViewById(R.id.accepted_event);
		percentTV = (TextView) view.findViewById(R.id.percent);
		ratting = (RatingBar)view.findViewById(R.id.ratting);
		ratting.setIsIndicator(true);
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

	public void setData(ProfileBean bean) {
		UserBean user = Utill.getUserPreferance(mContext);
		name.setText(bean.getUser_fname()+" "+bean.getUser_lname());
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		String p = bean.getImage_url();
		image_load.displayImage(p, profile_pic, opt, new CustomImageLoader(progressOnProfilePic, mContext));
		yellowCard.setText("Yellow Cards : "+bean.getYellow_card_status());
		acceptedEvents.setText("Accepted Events : "+bean.getAccept_event());
		String ratStr = bean.getTotal_user_ratting();
		if(Utill.isStringNullOrBlank(ratStr)){
			ratting.setRating(0f);
			percentTV.setText("0 %");
		}else{ 
			float rat = Float.parseFloat(ratStr);
			ratting.setRating(rat);
			float ratPer = rat * 20;
			percentTV.setText(ratPer+" %");
		}
	}

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
	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}

	// This method is used for hiding progress bar.
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

}
