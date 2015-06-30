package com.meetmeup.activity;

import java.io.UnsupportedEncodingException;




import java.util.Currency;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceFragment;
import android.sax.StartElementListener;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.FacebookException;
import com.facebook.FacebookOperationCanceledException;
import com.facebook.Session;
import com.facebook.widget.WebDialog;
import com.facebook.widget.WebDialog.OnCompleteListener;
import com.meetmeup.asynch.CheckUserBloack;
import com.meetmeup.asynch.GetFriendListAsync;
import com.meetmeup.asynch.GetNearByAsync;
import com.meetmeup.bean.ChatBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.fragment.BloackFragement;
import com.meetmeup.fragment.ChatFragement;
import com.meetmeup.fragment.CreateEventFragement;
import com.meetmeup.fragment.EditEventFragement;
import com.meetmeup.fragment.EventDetailFragement;
import com.meetmeup.fragment.HomeFragment;
import com.meetmeup.fragment.MapFragement;
import com.meetmeup.fragment.MyProfileFragement;
import com.meetmeup.fragment.NearByEventDetailFragement;
import com.meetmeup.fragment.NearByEventsFragement;
import com.meetmeup.fragment.ParticipantsFragement;
import com.meetmeup.fragment.PreferencesFragment;
import com.meetmeup.fragment.RatingParticipantsFragement;
import com.meetmeup.fragment.SendFeedBackFragement;
import com.meetmeup.fragment.SendMoneyFragement;
import com.meetmeup.fragment.SettingFragement;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;

//this is the DashBoard Activity on which we are managing all the screens.by using switchFragment method.  
public class DashBoard extends ActionBarActivity {
	
	public static Context mContext;
	public static ActionBar actionBar;
	public static FragmentManager fm;
	public static FragmentTransaction ft;
	public static Activity mActivity;

	// these are the screen id's for all available screen's.
	public static final int FRAGMENT_CREATE_EVENT = 0;
	public static final int FRAGMENT_HOME = 1;
	public static final int FRAGMENT_EVENT_DETAIL = 2;
	public static final int FRAGMENT_SEND_MONEY = 3;
	public static final int FRAGMENT_PARTICIPANTS = 4;
	public static final int FRAGMENT_RATING = 5;
	public static final int FRAGMENT_FEEDBACK = 6;
	public static final int FRAGMENT_LOCATION = 7;
	public static final int FRAGMENT_SETTING = 8;
	public static final int FRAGMENT_EDIT_EVENT = 9;
	public static final int FRAGMENT_NEAR_EVENT = 10;
	public static final int FRAGMENT_NEAR_EVENT_DETAIL = 11;
	public static final int FRAGMENT_MY_PROFILE = 12;
	public static final int FRAGMENT_BLOACK = 13;
	public static final int FRAGMENT_CHAT = 14;  
	public static final int FRAGMENT_OWENER_PROFILE = 15;
	public static final int FRAGMENT_PREFERENCE = 16;
	
	public static int currentstate = -1;

	public static ImageButton leftButton, rightButton /*,chatIcon*/;
	public static RelativeLayout mainActionRelative;
	public static TextView searchIcon; 
	TextView Refresh;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
	    super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_dash_board);
		Refresh = (TextView) findViewById(R.id.refresh);
		Refresh.setOnClickListener(refreshListener);
		mContext = this;
		mActivity = this;
		fm = getSupportFragmentManager();
		getSupportActionBar();
		setActionBarCustom();
		updateTitle("DashBoard");
		checkUserStatus();
		getFBFriends();
		getNearByPeople();
				
	}

	// this is the listener for refresh button,it will enable any error occur
	// when we starting application.
	OnClickListener refreshListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			
			checkUserStatus();
		
		}
	};

	// this method is used for checking user status (block or not )
	void checkUserStatus() {
		if (Utill.isNetworkAvailable(mContext)) {
			
			MultipartEntity multi = new MultipartEntity();
			
			try {
				
				UserBean user = Utill.getUserPreferance(mContext);
				multi.addPart("user_id", new StringBody(user.getUser_id()));
				showProgress();
				new CheckUserBloack(mContext, new CheckUserListener(), multi).execute();
				
				
			} catch (UnsupportedEncodingException e) {
				
				e.printStackTrace();
				
			}
		
		} else {
			
			Utill.showNetworkError(mContext);
			Refresh.setVisibility(View.VISIBLE);
			
		}

	}

	// this method is used for initializing action bar
	public void setActionBarCustom() {
	
		actionBar = getSupportActionBar();
		actionBar.setDisplayShowCustomEnabled(true);
		actionBar.setDisplayUseLogoEnabled(false);
		actionBar.setDisplayShowHomeEnabled(false);
		actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		actionBar.setCustomView(getLayoutInflater().inflate(R.layout.actionbar_bg, null), new ActionBar.LayoutParams(
				ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER));
		mainActionRelative = (RelativeLayout) findViewById(R.id.mainActionBar);
		leftButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.left_logo);
		rightButton = (ImageButton) actionBar.getCustomView().findViewById(R.id.right_logo);
		
		searchIcon = (TextView) actionBar.getCustomView().findViewById(R.id.search_icon);
		
//		chatIcon = (ImageButton) actionBar.getCustomView().findViewById(R.id.group_chat_icon);
//		chatIcon.setVisibility(View.GONE);
	
	}

	// this method is used for updating Title of all the screen's.
	public static void updateTitle(String title) {
		
		actionBar.setTitle(title);

	}

	// this mehtod is used for updating title of screen from all other screen's
	public static void resetActionBarTitle(String title,int state) {
	
		if(state == FRAGMENT_HOME){
			
			((TextView) actionBar.getCustomView().
					findViewById(R.id.title)).setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.logo_small));
		
			((TextView) actionBar.getCustomView().findViewById(R.id.title)).setText("");
			
			
		}else{
			
			((TextView) actionBar.getCustomView().
					findViewById(R.id.title)).setBackgroundDrawable(null);
			
			((TextView) actionBar.getCustomView().findViewById(R.id.title)).setText(title);
			
		}
		
//		((TextView) actionBar.getCustomView().findViewById(R.id.title)).setText(title);
		
//		((TextView) actionBar.getCustomView().findViewById(R.id.title)).setBackground(background);;
		
		
	}

	
	// This mehtod is used for switching from one screen to another screen.
	 public void swithFragment(int type) {
		 
	    currentstate = -1; 
		 
	   try{ 
		 
		 hideSOftKey();
		 Fragment mFragment = null;
		 ft = fm.beginTransaction();
		
	    switch (type) {
		
		
		case FRAGMENT_HOME:
			mFragment = HomeFragment.getInstance(mContext, fm);
			ft.replace(R.id.container, mFragment, "home");			
		
			currentstate = FRAGMENT_HOME;
			
			break;
	
		case FRAGMENT_CREATE_EVENT:
			CreateEventFragement.clearFields();

			mFragment = CreateEventFragement.getInstance(mContext, fm);
			ft.replace(R.id.container, mFragment, "");
			ft.addToBackStack(null);
			break;
	
		case FRAGMENT_EVENT_DETAIL:
			Fragment myFragment = (Fragment) fm.findFragmentByTag("detail");
			if (myFragment != null && myFragment.isVisible()) {
				
				String s = myFragment.getTag();
				EventDetailFragement ev = (EventDetailFragement)myFragment;
				ev.onRefreshDetail();
				break;				
			}
			
			mFragment = EventDetailFragement.getInstance(mContext, fm);
			String backStateName = mFragment.getClass().getName();
			boolean fragmentPopped = fm.popBackStackImmediate(backStateName, 0);
			if (!fragmentPopped) {
				ft.replace(R.id.container, mFragment, "detail");
			}
			ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
			ft.addToBackStack(backStateName);
//			break;

		/*
		 * try { mFragment = EventDetailFragement.getInstance(mContext, fm);
		 * ft.replace(R.id.container, mFragment, "detail");
		 * ft.addToBackStack(null); } catch (Exception e) { e.printStackTrace();
		 * }
		 */

		break;
		
	case FRAGMENT_PARTICIPANTS:
		mFragment = ParticipantsFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_FEEDBACK:
		mFragment = SendFeedBackFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_RATING:
		mFragment = RatingParticipantsFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_SETTING:
		mFragment = SettingFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_EDIT_EVENT:
		mFragment = EditEventFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_SEND_MONEY:
		mFragment = SendMoneyFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_LOCATION:
		mFragment = MapFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_NEAR_EVENT:
		mFragment = NearByEventsFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		
//		Intent intent = new Intent(DashBoard.this,ShowAllNearByUsersActivity.class);
//		startActivity(intent);
		
		break;
		
	case FRAGMENT_NEAR_EVENT_DETAIL:
		mFragment = NearByEventDetailFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);		
		break;
	
	case FRAGMENT_MY_PROFILE:
		
		 UserBean user = Utill.getUserPreferance(mContext); 		
		 mFragment = MyProfileFragement.getInstance(mContext, fm , user.getUser_id());
		 ft.replace(R.id.container, mFragment, "");
		 ft.addToBackStack(null);
		 break;
	
  
	case FRAGMENT_BLOACK:
		mFragment = BloackFragement.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "");
		ft.addToBackStack(null);
		break;
		
		
	case FRAGMENT_PREFERENCE:
		
		mFragment = PreferencesFragment.getInstance(mContext, fm);
		ft.replace(R.id.container, mFragment, "pfer");
		ft.addToBackStack(null);
		break;
		
	case FRAGMENT_CHAT:
				
		mFragment = ChatFragement.getInstance(mContext, fm);
		String backStackName = mFragment.getClass().getName();
		boolean fragmentPoppedUP = fm.popBackStackImmediate(backStackName, 0);
		
		if (!fragmentPoppedUP) {
			
			ft.replace(R.id.container, mFragment, "chat");
			
		}
		
		ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
		ft.addToBackStack(backStackName);
		break;
		
		
	/*
	 * case SETTING: Fragment myFragment = (Fragment)
	 * fm.findFragmentByTag("SETTING1"); if (myFragment != null &&
	 * myFragment.isVisible()) { String s = myFragment.getTag();
	 * Log.e("fragment Name", s); break; } mFragment =
	 * SettingFragment.getInstance(mContext, fm); String backStateName =
	 * mFragment.getClass().getName(); boolean fragmentPopped =
	 * fm.popBackStackImmediate(backStateName, 0); if (!fragmentPopped) {
	 * ft.replace(R.id.container, mFragment, "SETTING1"); }
	 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	 * ft.addToBackStack(backStateName); break;
	 * 
	 * case GALLERY: mFragment = GalleryHomeFragment.getInstance(mContext, fm);
	 * ft.replace(R.id.container, mFragment, ""); ft.addToBackStack(null);
	 * break; case CAMERA: mFragment = GalleryHomeFragment.getInstance(mContext,
	 * fm); ft.replace(R.id.container, mFragment, ""); ft.addToBackStack(null);
	 * break;
	 * 
	 * case NOTIFICATION: Fragment myNotFragment = (Fragment)
	 * fm.findFragmentByTag("NOTIFICATION"); if (myNotFragment != null &&
	 * myNotFragment.isVisible()) { String s = myNotFragment.getTag();
	 * Log.e("fragment Name", s); break; } mFragment =
	 * NotificationFragment.getInstance(mContext, fm); String notificationName =
	 * mFragment.getClass().getName(); boolean mfragmentPopped =
	 * fm.popBackStackImmediate(notificationName, 0); if (!mfragmentPopped) {
	 * ft.replace(R.id.container, mFragment, "NOTIFICATION"); }
	 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	 * ft.addToBackStack(notificationName); break; case EMERGENCY: Fragment
	 * emergencyFragment = (Fragment) fm.findFragmentByTag("EMERGENCY"); if
	 * (emergencyFragment != null && emergencyFragment.isVisible()) { String s =
	 * emergencyFragment.getTag(); Log.e("fragment Name", s); break; } mFragment
	 * = EmergencyFragment.getInstance(mContext, fm); String emergencyName =
	 * mFragment.getClass().getName(); boolean emerfragmentPopped =
	 * fm.popBackStackImmediate(emergencyName, 0); if (!emerfragmentPopped) {
	 * ft.replace(R.id.container, mFragment, "EMERGENCY"); }
	 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	 * ft.addToBackStack(emergencyName);
	 * 
	 * break; case TIMELINE: Fragment timelineFragment = (Fragment)
	 * fm.findFragmentByTag("TIMELINE"); if (timelineFragment != null &&
	 * timelineFragment.isVisible()) { String s = timelineFragment.getTag();
	 * Log.e("fragment Name", s); break; } mFragment =
	 * TimeLineFragment.getInstance(mContext, fm); String timelineName =
	 * mFragment.getClass().getName(); boolean timeLinePopUP =
	 * fm.popBackStackImmediate(timelineName, 0); if (!timeLinePopUP) {
	 * ft.replace(R.id.container, mFragment, "TIMELINE"); }
	 * ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
	 * ft.addToBackStack(timelineName);
	 * 
	 * break; case MY_PROFILE: mFragment = MyAccount.getInstance(mContext, fm);
	 * ft.replace(R.id.container, mFragment, ""); ft.addToBackStack(null);
	 * break; case LAB: mFragment = LabRecieptFragment.getInstance(mContext,
	 * fm); ft.replace(R.id.container, mFragment, ""); ft.addToBackStack(null);
	 * break; case OPTICAL: mFragment = opticalFragment.getInstance(mContext,
	 * fm); ft.replace(R.id.container, mFragment, ""); ft.addToBackStack(null);
	 * break; // ft.commit(); default: break;
	 */
	  
      }
		
	  ft.commit();
	
    }catch(Exception e){
	 
        e.printStackTrace();
	   
    }
	   	
   }		      
		 
	 	 
	 @Override
	    public boolean dispatchTouchEvent(MotionEvent ev) {
	       
		    try {
		    	
//	        	swithFragment(FRAGMENT_HOME);	
		  		
	            return super.dispatchTouchEvent(ev);
	            
	          	       
	        } catch (Exception e) {
	        	
	            Log.w("StreetViewPanoramaBasicDemo", "Caught unhandled exception.", e);
	            
	        }
	        
	        return true;
	    
	 }
	  
	
	public void showOwnerProfile(String user_id){
		 		
		 Fragment mFragment = null;
		 ft = fm.beginTransaction();
		
		 mFragment = MyProfileFragement.getInstance(mContext, fm ,user_id);
		 ft.replace(R.id.container, mFragment, "");
		 ft.addToBackStack(null);
		 ft.commit();
		
	}
	
	
	// this method is used for removing all the screen's which are availale on
	// stack.
	public static void removeAllFromBackStack() {
		HomeFragment.loadAgain = true;
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	// this method is used for go back from one to another screen.
	public static void backStackPress() {
		fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
	}

	// this method called when default back button pressed.
	@Override
	public void onBackPressed() {
	
		try{

			hideSOftKey();
			Fragment myFragment = fm.findFragmentByTag("home");
		
			if (myFragment != null && myFragment.isVisible()) {
				
				ExitDialogue();
				return;
				
			} else {
				
				Fragment bloackFragment = fm.findFragmentByTag("block");
				if (bloackFragment != null && bloackFragment.isVisible()) {
					ExitDialogue();
					return;
				}
				
			}

		} catch (Exception e) {
			
			e.printStackTrace();

		}
		
		super.onBackPressed();
		
	}

	// this method show's Exit Dialgue to ask for Do you want to Exit or not.
	private void ExitDialogue() {
	
		final Dialog dialog = new Dialog(mContext);
		dialog.setContentView(R.layout.final_logout);
		dialog.setTitle("Do you want to Exit?");
		Button logout_yes = (Button) dialog.findViewById(R.id.logout_yes);
		Button logout_no = (Button) dialog.findViewById(R.id.logout_no);
		
		logout_yes.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				finish();
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

	// this is call back method,it is called when startActivityForResult()
	// finished.
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.container);
		fragment.onActivityResult(requestCode, resultCode, data);
		
		
		
		super.onActivityResult(requestCode, resultCode, data);
		
	}

	// this method is used for hiding keyboard whenever new fragment is opened.
	public void hideSOftKey() {
		try {
			InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	// this is the listener class for checking user is blocked or not,if user is
	// not bloaked than send to home page,if bloack than send to bloack page and
	// if any error occure than onError() will be called
	public class CheckUserListener {
		public void onSuccess(String msg) {
			swithFragment(DashBoard.FRAGMENT_HOME);
			hideProgress();
			Refresh.setVisibility(View.GONE);
		}

		public void onBloack(String msg, String date) {
			
			hideProgress();
			BloackFragement.bloackMsg = "Sorry You Are Blocked." + msg + ".You will be Activate on " + date;
			swithFragment(FRAGMENT_BLOACK);
			Refresh.setVisibility(View.GONE);
			
		}

		public void onError(String msg) {
			
//			swithFragment(DashBoard.FRAGMENT_HOME);
			hideProgress();
			Refresh.setVisibility(View.VISIBLE);
//			Refresh.setVisibility(View.GONE);
			
		}
	}
	

	ProgressDialog progress;

	// This method will be used for showing progress bar.
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

	// This method will show for hiding progressBar.
	public void hideProgress() {
		if (progress != null) {
			progress.dismiss();
		}
	}

	// Extras
	public void publishFeedDialog() {
		Bundle params = new Bundle();
		params.putString("name", "Facebook SDK for Android");
		params.putString("caption", "Build great social apps and get more installs.");
		params.putString("description", "The Facebook SDK for Android makes it easier and faster to develop Facebook integrated Android apps.");
		params.putString("link", "https://developers.facebook.com/android");
		params.putString("picture", "https://raw.github.com/fbsamples/ios-3.x-howtos/master/Images/iossdk_logo.png");

		WebDialog feedDialog = (new WebDialog.FeedDialogBuilder(this, Session.getActiveSession(), params)).setOnCompleteListener(
				new OnCompleteListener() {

					@Override
					public void onComplete(Bundle values, FacebookException error) {
						if (error == null) {
							// When the story is posted, echo the success
							// and the post Id.
							final String postId = values.getString("post_id");
							if (postId != null) {
								Toast.makeText(mContext, "Posted story, id: " + postId, Toast.LENGTH_SHORT).show();
							} else {
								// User clicked the Cancel button
								Toast.makeText(getApplicationContext(), "Publish cancelled", Toast.LENGTH_SHORT).show();
							}
						} else if (error instanceof FacebookOperationCanceledException) {
							// User clicked the "x" button
							Toast.makeText(mContext, "Publish cancelled", Toast.LENGTH_SHORT).show();
						} else {
							// Generic, ex: network error
							Toast.makeText(getApplicationContext(), "Error posting story", Toast.LENGTH_SHORT).show();
						}
					}

				}).build();
		feedDialog.show();
	}

	// Extras
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

	public void popFromBackStack() {
		if (fm != null)
			if (fm.getBackStackEntryCount() > 0) {
				fm.popBackStack();
			}
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// setIntent(intent);
		UserBean mUser = Utill.getUserPreferance(this);
	//	String extraString = getIntent().getStringExtra("notificationId");
		Bundle extra = intent.getExtras();
		if (extra != null) {
	//		String temp1 = extra.getString("shahid");
			int notificationId = extra.getInt("notificationId");
	//		String screen_id = extra.getString("screen_id");
			if (mUser.getUser_id() != null) {
				
				if (mUser.getUser_id() != null && notificationId != 0) {
					
					String type = extra.getString("type");
					String eid = extra.getString("eid");
					
					String event_title = extra.getString("event_title");
					
		//			String uid = extra.getString("uid");
					// String temp = extra.getString("shahid");
					if (type != null && type.equalsIgnoreCase("detail")) {
					//	removeAllFromBackStack();
						EventDetailFragement.eventID = eid;
						swithFragment(FRAGMENT_EVENT_DETAIL);
					}else if (type != null && type.equalsIgnoreCase("chat")) {
				//		removeAllFromBackStack();
						
						ChatFragement.eventID = eid;
						
						ChatFragement.eventName = event_title;
						
						swithFragment(FRAGMENT_CHAT);
					}else {
						
						removeAllFromBackStack();
						
					}
					
				}
//				else {
//					
//					removeAllFromBackStack();
//					
//				}
				
			} else {
				
				finish();
				Intent intent1 = new Intent(DashBoard.this, LoginActivity.class);
				startActivity(intent1);
				
			}
			
		}
		super.onNewIntent(intent);
	}

	@Override
	protected void onResume() {
		UserBean mUser = Utill.getUserPreferance(this);
	//	String extraString = getIntent().getStringExtra("notificationId");
		Bundle extra = getIntent().getExtras();
		if (extra != null) {
	//		String temp1 = extra.getString("shahid");
			int notificationId = extra.getInt("notificationId");
	//		String screen_id = extra.getString("screen_id");
			if (mUser.getUser_id() != null && notificationId != 0) {
				String type = extra.getString("type");
				String eid = extra.getString("eid");
				
				String event_title = extra.getString("event_title");
				
		//		String uid = extra.getString("uid");
		//		String temp = extra.getString("shahid");
				if (type != null && type.equalsIgnoreCase("detail")) {
				//	removeAllFromBackStack();
					EventDetailFragement.eventID = eid;
					
					ChatFragement.eventName = event_title;
					
					swithFragment(FRAGMENT_EVENT_DETAIL);
					
				}else if (type != null && type.equalsIgnoreCase("chat")) {
		//			removeAllFromBackStack();
					ChatFragement.eventID = eid;
					swithFragment(FRAGMENT_CHAT);
				} 
				else {
					
					removeAllFromBackStack();
					
				}

			} else {
				finish();
				Intent intent1 = new Intent(DashBoard.this, LoginActivity.class);
				startActivity(intent1);
			}
		}

		super.onResume();
	}
	
		
}
