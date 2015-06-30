package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;

import java.util.ArrayList;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.widget.FacebookDialog;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.LoginActivity;
import com.meetmeup.activity.R;
import com.meetmeup.adapters.FriendAdapter;
import com.meetmeup.adapters.NearByAdapter;
import com.meetmeup.adapters.PrefAdapter;
import com.meetmeup.adapters.PreferenceAdapter;
import com.meetmeup.asynch.GetFBFriendListAsync;
import com.meetmeup.asynch.GetFriendListAsync;
import com.meetmeup.asynch.GetNearByAsync;
import com.meetmeup.bean.FriendBean;
import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.fragment.CreateEventFragement.CustomTextWatcher;
import com.meetmeup.fragment.CreateEventFragement.GetFriendListListener;
import com.meetmeup.fragment.CreateEventFragement.GetNearByListListener;
import com.meetmeup.fragment.PreferencesFragment.CheckStatus;
import com.meetmeup.fragment.PreferencesFragment.CheckedListener;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;

//This class is used for All settings for the app like,logut,radius,shareapp on facebook,notification on off, etc.
public class SettingFragement extends Fragment {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	TextView logoutTV, radiusTV, shareOnFB ,shareOnWall, facebookFriends, profilTV /*, txtview_preference*/;
	LinearLayout notificationL;
//	CheckBox notificationC;
	TextView notificationTextView , sort_option;
	DashBoard dashBoard;
	private UiLifecycleHelper uiHelper;

	ImageView homeIV, settingIV, createEventIV, myEventsIV, nearByEventsIV;	
	
	
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new SettingFragement();
		}
		return mfFragment;
	}

	// creating uiHelper object for facebook sharing.
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
		uiHelper = new UiLifecycleHelper(mActivity, callback);
		uiHelper.onCreate(savedInstanceState);
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		uiHelper.onActivityResult(requestCode, resultCode, data, new FacebookDialog.Callback() {
			@Override
			public void onError(FacebookDialog.PendingCall pendingCall, Exception error, Bundle data) {
				Log.e("Activity", String.format("Error: %s", error.toString()));
			}

			@Override
			public void onComplete(FacebookDialog.PendingCall pendingCall, Bundle data) {
				Log.i("Activity", "Success!");
			}
		});
	}

	// this is call back method in this we are setting title to the screen and
	// setting all the navigation button visibilities.
	@Override
	public void onStart() {
		if (DashBoard.actionBar != null) {
			DashBoard.resetActionBarTitle("Settings",-2);
			DashBoard.rightButton.setVisibility(View.GONE);
			DashBoard.leftButton.setVisibility(View.VISIBLE);
			DashBoard.leftButton.setImageResource(R.drawable.back_btn);
//			DashBoard.chatIcon.setVisibility(View.GONE);
		}
		
//		if (Utill.getNotification(mContext)){
////			notificationC.setChecked(true);
////			notificationTextView.setText("Disable Notifications");
//			Utill.setNotification(mContext, true);
//		}
//		else{
////			notificationC.setChecked(false);
////			notificationTextView.setText("Enable Notifications");
//			Utill.setNotification(mContext, false);
//		}
		
		if (Utill.getNotification(mContext)) {
		
			 notificationTextView.setText("Disable Notifications");
			
		} else {
		
			notificationTextView.setText("Enable Notifications");
		
			
		}
				
		super.onStart();
		
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.setting, container, false);
		dashBoard = (DashBoard) mActivity;
		initializeView(view);
		setOnClickeListeners();
		return view;
		
	}

	static Activity mActivity;

	
	@Override
	public void onAttach(Activity activity) {
	
		mActivity = activity;
		super.onAttach(activity);
		
	}
	

	void initializeView(View view) {
		
		notificationTextView = (TextView) view.findViewById(R.id.notification_tv);
		
		sort_option = (TextView) view.findViewById(R.id.sort_option);
//		sort_option.setVisibility(View.GONE);
		
		
		
		logoutTV = (TextView) view.findViewById(R.id.logout);
		radiusTV = (TextView) view.findViewById(R.id.Radius);
//		notificationC = (CheckBox) view.findViewById(R.id.checkboxC);
		notificationL = (LinearLayout) view.findViewById(R.id.notificationL);
		shareOnFB = (TextView) view.findViewById(R.id.share_on_fb);
		facebookFriends = (TextView) view.findViewById(R.id.facebook_friends);
		profilTV = (TextView) view.findViewById(R.id.profile);
		
//		txtview_preference = (TextView) view.findViewById(R.id.txtview_preference);
		shareOnWall = (TextView) view.findViewById(R.id.share_on_wall);
		
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
				dashBoard.swithFragment(DashBoard.FRAGMENT_HOME);
				break;
			case R.id.settingIV:
//				dashBoard.swithFragment(DashBoard.FRAGMENT_SETTING);
				break;
				
			case R.id.createEventIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_CREATE_EVENT);
				break;
				
			case R.id.myEventsIV:
				HomeFragment.isClickedOnMyEvents = true; 
				dashBoard.swithFragment(DashBoard.FRAGMENT_HOME);
		    	break;
		    	
			case R.id.nearByEventsIV:
				dashBoard.swithFragment(DashBoard.FRAGMENT_NEAR_EVENT);
				break;

			default:
				break;
			}

		}
	};
	
	// logout function dialog
	private void logoutDialog() {
		
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

	// This method is used for updating radius.
	private void radiusDialog() {
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.radius_dialogue);
		dialog.setTitle("radius for near by app,in Km.");
		Button ok = (Button) dialog.findViewById(R.id.ok);
		final EditText radiusET = (EditText) dialog.findViewById(R.id.radiusET);

		radiusET.setText(Utill.getRadius(mContext));
		ok.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String radius = radiusET.getText().toString();
				Utill.setRadius(mContext, radius);
				getNearByPeople();
				dialog.dismiss();
			}
		});
		dialog.show();
	}



	// when clicked on logout.all data from shared preference will be cleared.
	public void onLogout() {
		
		Utill.removeUserPreference(mContext);
		LoginActivity.callFacebookLogout(mContext);
		logout();
		getActivity().finish();	
						
	}

	public void logout() {

		try {

			Intent intent1 = new Intent(mContext,
					LoginActivity.class);

			intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
					| Intent.FLAG_ACTIVITY_CLEAR_TASK);

			getActivity().startActivity(intent1);
			

		} catch (Exception e) {

			e.printStackTrace();

		}

	}
	
	
	
	//in this method we are setting ClickLister to all the View's.
	public void setOnClickeListeners() {
		profilTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dashBoard.swithFragment(DashBoard.FRAGMENT_MY_PROFILE);
			}
		});

		facebookFriends.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				MultipartEntity multi = new MultipartEntity();
				try {
					multi.addPart("access_token", new StringBody(Utill.getUserPreferance(mContext).getFb_access_token()));
					new GetFBFriendListAsync(mContext, null, Utill.getUserPreferance(mContext).getFb_access_token()).execute();
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		});
		shareOnFB.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					sendNotification(null);
					//publishFeedDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		shareOnWall.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					//sendNotification(null);
					publishFeedDialog();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		

		/*notificationL.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (notificationC.isChecked()) {
					notificationC.setChecked(false);
					Utill.setNotification(mContext, false);
					notificationTextView.setText("Disable Notifications");
				} else {
					notificationC.setChecked(true);
					Utill.setNotification(mContext, true);
					notificationTextView.setText("Enable Notifications");
				}
			}
		});*/
		
		
		
		notificationL.setOnClickListener(new OnClickListener() {
			@Override 
			public void onClick(View v) {
				
				if (Utill.getNotification(mContext)){
//					notificationC.setChecked(true);
					notificationTextView.setText("Enable Notifications");
					Utill.setNotification(mContext, false);
				}
				else{
//					notificationC.setChecked(false);
				
					notificationTextView.setText("Disable Notifications");
					Utill.setNotification(mContext, true);
				}
				
							
			}
		});

		
		
		sort_option.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			
				dashBoard.swithFragment(DashBoard.FRAGMENT_PREFERENCE);
				
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
		logoutTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				logoutDialog();
			}
		});
		radiusTV.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				radiusDialog();
			}
		});
		
		
//		txtview_preference.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//								
//				dashBoard.swithFragment(DashBoard.FRAGMENT_PREFERENCE);
//				
//			}
//		});
		
	}

	@Override
	public void onResume() {
		super.onResume();
		uiHelper.onResume();
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	@Override
	public void onPause() {
		super.onPause();
		uiHelper.onPause();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		uiHelper.onDestroy();
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			if (session != null) {
				boolean b = session.isOpened();
			}
		}
	};
	
	

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

	// By this method we can post on facebook.
	private void publishFeedDialog() {
		 Session session = Session.getActiveSession();
		 //if(session==null)
		 {                      
			    // try to restore from cache
			    session = Session.openActiveSessionFromCache(dashBoard);
			}
		Bundle params = new Bundle();
		params.putString("name", "MeetMeUp");
		params.putString("caption", "Discover events nearby, easily create events and invite your friends!");
		params.putString("description", "Finding, creating and sharing activities you’re passionate about are made easy with MeetMeUp. Whether you’re organizing a small gathering with friends, looking for a neighborhood football game or going to a local music concert, MeetMeUp makes it happen.");
		params.putString("link", "https://play.google.com/store/apps/details?id=com.meetmeup.activity");
		params.putString("picture", "http://72.167.41.165/meetmeup/webservices/images/facebook_splash.png");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(getActivity(),session, params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(getActivity(), "Posted story, id: " + postId, Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(getActivity().getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(getActivity().getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(getActivity().getApplicationContext(), "Error posting story", Toast.LENGTH_SHORT).show();
						}
					}

				}).build();
		feedDialog.show();
	}
	
	private void sendRequestDialog(String msg) {
	
		String json = Utill.loadJSONFromAsset(mContext);
		Bundle params = new Bundle();
		params.putString("message", msg);
		params.putString("data", json);
		WebDialog requestsDialog = (new WebDialog.RequestsDialogBuilder(mContext, Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error) {

						if (error != null) {
							if (error instanceof FacebookOperationCanceledException) {
								Toast.makeText(mContext, "Request cancelled", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mContext, "Network Error", Toast.LENGTH_SHORT).show();
							}
						} else {
							final String requestId = values.getString("request");
							if (requestId != null) {
								Toast.makeText(mContext, "Request sent", Toast.LENGTH_SHORT).show();
							} else {
								Toast.makeText(mContext, "Request cancelled", Toast.LENGTH_SHORT).show();
							}
						}
					}

				}).build();
		requestsDialog.show();
	}
	
	void getFBFriends() {
		if (Utill.isNetworkAvailable(mContext)) {
			MultipartEntity multipart = new MultipartEntity();
			UserBean user = Utill.getUserPreferance(mContext);
			try {
				multipart.addPart("user_id", new StringBody(user.getUser_id()));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			new GetFriendListAsync(mContext, null, multipart).execute();
		}
	}

	void getNearByPeople() {
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
		new GetNearByAsync(mContext, null, multipart).execute();
	}
	
   
}
