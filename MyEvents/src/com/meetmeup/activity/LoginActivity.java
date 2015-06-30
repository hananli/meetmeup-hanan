package com.meetmeup.activity;

import static com.meetmeup.helper.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.meetmeup.helper.CommonUtilities.EXTRA_MESSAGE;
import static com.meetmeup.helper.CommonUtilities.SENDER_ID;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.Signature;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.android.Facebook;
import com.facebook.model.GraphMultiResult;
import com.facebook.model.GraphObject;
import com.facebook.model.GraphObjectList;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.google.android.gcm.GCMRegistrar;
import com.meetmeup.asynch.LoginAsync;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.AppConstants;
import com.meetmeup.helper.Utill;
import com.meetmeup.helper.WakeLocker;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

//This class is used for Login with facebook.
public class LoginActivity extends Activity {
	
	public static final String USER_PROFILE1 = "http://graph.facebook.com/";// 1420370681588319+
	public static final String USER_PROFILE2 = "/picture?type=large";
	LoginButton loginButton;
	String TAG = "tag";
	UiLifecycleHelper uiHelper;
	Context mContext; 
	Activity mActivity;
	String gcm_id;
	String APP_ID;
	Facebook fb;
	boolean flag = false;
	public static boolean isFbDataReceived = false, isServiceCalled = false;
	String accessToken;
	Date accessExpire;
	public static boolean isFbclicked = false;
	ArrayList<String> friends_list;
	

	// public static AsyncFacebookRunner mAsyncRunner = null;

	// This method will be called whenever this screen will be open.
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		mContext = this;
		mActivity = this;
		uiHelper = new UiLifecycleHelper(this, callback);
		uiHelper.onCreate(savedInstanceState);
		loginButton = (LoginButton) findViewById(R.id.authButton);

		// loginButton.setBackgroundResource(R.drawable.facebook_icon);

		// loginButton.setCompoundDrawablesWithIntrinsicBounds(null, null, null,
		// null);

		/*
		 * loginButton.setReadPermissions(Arrays.asList("user_likes",
		 * "user_status","public_profile","email"));
		 */
		loginButton.setReadPermissions(Arrays.asList("user_likes", "user_friends", "user_status", "public_profile", "email", 
				 "read_friendlists"));
		/*
		 * user_checkins friends_checkins read_friendlists manage_friendlists
		 * publish_checkins
		 */

		APP_ID = getString(R.string.facebook_app_id);
		fb = new Facebook(APP_ID);
		/*
		 * mAsyncRunner = new AsyncFacebookRunner(fb); friends_list = new
		 * ArrayList<String>();
		 */
		

		loginButton.setSessionStatusCallback(new Session.StatusCallback() {

			@Override
			public void call(Session session, SessionState state, Exception exception) {
				/*
				 * try {
				 * Session.getActiveSession().closeAndClearTokenInformation(); }
				 * catch (Throwable e) { e.printStackTrace(); }
				 */
				// TODO Auto-generated method stub
				boolean b = session.isOpened();
				if (session.isOpened()) {

					fetchUserDetails(session);
					// requestMyAppFacebookFriends(session);
				} else {
					Log.v("session is", "closed");
				}
			}
		});

	
		
		getHashCode();
		getDeviceGcmId();

	}

	private void fetchUserDetails(final Session session) {
		Log.i("Session is", "Access Token" + session.getAccessToken());

		Request.newMeRequest(session, new Request.GraphUserCallback() {
			@Override
			public void onCompleted(GraphUser user, Response response) {
				// some code
				getUserFacebookData(user);

			}
		}).executeAsync();
	}

	@Override
	public void onStart() {
		super.onStart();
		flag = false;
		isServiceCalled = false;
		isFbDataReceived = false;
		Session session = Session.getActiveSession();
	}

	@Override
	public void onStop() {
		super.onStop();

	}

	@SuppressWarnings("deprecation")
	private void onSessionStateChange(final Session session, SessionState state, Exception exception) {
		flag = true;

		if (state.isOpened()) {
			accessToken = session.getAccessToken();
			accessExpire = session.getExpirationDate();

			// showProgress();

			Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
				public void onCompleted(GraphUser user, Response response) {
					Log.v("come here", " " + session);
					session.close();
				}
			});
		} else if (state.isClosed()) {
		}
	}

	private Session.StatusCallback callback = new Session.StatusCallback() {
		@Override
		public void call(Session session, SessionState state, Exception exception) {
			onSessionStateChange(session, state, exception);
		}
	};

	@SuppressWarnings("deprecation")
	@Override
	public void onResume() {
		super.onResume();
		final Session session = Session.getActiveSession();
		if (fb.isSessionValid()) {
			accessToken = session.getAccessToken();
			accessExpire = session.getExpirationDate();

			if (Session.getActiveSession().isOpened()) {

				showProgress();
				Request.executeMeRequestAsync(session, new Request.GraphUserCallback() {
					public void onCompleted(GraphUser user, Response response) {
						Log.v("come here", " " + session);
						/* session.close(); */

						getUserFacebookData(user);
					}
				});
			}
		}

		else if (session != null && (session.isOpened() || session.isClosed())) {
			onSessionStateChange(session, session.getState(), null);
		} else {
			if (flag) {
				Session s = new Session(LoginActivity.this);
				Session.setActiveSession(s);
				s.openForRead(new Session.OpenRequest(this).setCallback(callback).setPermissions(Arrays.asList("public_profile", "email")));
			}

		}
		uiHelper.onResume();
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		uiHelper.onActivityResult(requestCode, resultCode, data);
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

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		uiHelper.onSaveInstanceState(outState);
	}

	public class LoginListener {
		public void onSuccess(String msg, String userid) {
			AppConstants.setFacebookFriendList(null);
			AppConstants.setNearByList(null);
			Intent intent = new Intent(LoginActivity.this, DashBoard.class);
			startActivity(intent);
			UserBean user = Utill.getUserPreferance(mContext);
			user.setUser_id(userid);
			Utill.createUserPreference(user, mContext);
			// callFacebookLogout(mContext);
			hideProgress();
			finish();
		}

		public void onError(String msg) {
		
			hideProgress();
			Utill.removeUserPreference(mContext);
			
		}
		
	}

	@SuppressWarnings("deprecation")
	private void getUserFacebookData(GraphUser user) {
		
		if (user != null) {
			
			try {
				
				getDeviceGcmId();
				String userName = (String) user.getProperty("username");
				/*
				 * String profilePic = "http://graph.facebook.com/" + userName +
				 * "/picture?type=large";
				 */
				String firstName = user.getFirstName();
				String lastName = user.getLastName();
				String gender = user.getProperty("gender").toString();
				String email = user.getProperty("email").toString();
				String fb_id = user.getId();

				if (Utill.isNetworkAvailable(mContext)) {
					
					showProgress();
					
					MultipartEntity multipart = new MultipartEntity();
					multipart.addPart("fb_id", new StringBody(fb_id));
					multipart.addPart("email", new StringBody(email));
					multipart.addPart("f_name", new StringBody(firstName));
					multipart.addPart("l_name", new StringBody(lastName));
					multipart.addPart("lat", new StringBody(AppConstants.getLattitude()));
					multipart.addPart("long", new StringBody(AppConstants.getLogitude()));
					multipart.addPart("profile_pic_url", new StringBody(USER_PROFILE1 + fb_id + USER_PROFILE2));
					multipart.addPart("fb_access_token", new StringBody(accessToken));
					multipart.addPart("fb_token_expire", new StringBody("" + accessExpire));
					multipart.addPart("gcm_id", new StringBody(gcm_id));
					// Toast.makeText(mContext, "gcm = "+gcm_id,
					// Toast.LENGTH_LONG).show();

					UserBean userObj = new UserBean();
					userObj.setFb_id(fb_id);
					userObj.setEmail(email);
					userObj.setF_name(firstName);
					userObj.setL_name(lastName);
					userObj.setLattitude(AppConstants.getLattitude());
					userObj.setLongitute(AppConstants.getLogitude());
					userObj.setProfile_pic_url(USER_PROFILE1 + fb_id + USER_PROFILE2);
					userObj.setFb_access_token(accessToken);
					userObj.setFb_token_expire("" + accessExpire);
					Utill.createUserPreference(userObj, mContext);

					// mAsyncRunner.request("me/friends", new
					// FriendsRequestListener());

					new LoginAsync(mContext, new LoginListener(), multipart).execute();
					
					uiHelper.onDestroy();
					
				} else {
					Utill.showNetworkError(mContext);
				}
			} catch (Exception e) {

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
		
	}

	// This code used for generating SHA1 key which is used in facebook app
	// project.
	void getHashCode() {
		try {
			
			PackageInfo info = getPackageManager().getPackageInfo("com.meetmeup.activity", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
				MessageDigest md = MessageDigest.getInstance("SHA");
				md.update(signature.toByteArray());
				
				String hashval = Base64.encodeToString(md.digest(), Base64.DEFAULT);
				
				Log.d("Your Tag SHA1 key for Facebook.",hashval);
			}
			
		} catch (NameNotFoundException e) {

		} catch (NoSuchAlgorithmException e) {

		}

	}
	
	void getHasNew(){
		try {
			PackageInfo info = getPackageManager().getPackageInfo("com.meetmeup.activity", PackageManager.GET_SIGNATURES);
			for (Signature signature : info.signatures) {
			MessageDigest md = MessageDigest.getInstance("SHA");
			md.update(signature.toByteArray());
			String sign=Base64.encodeToString(md.digest(), Base64.DEFAULT);
			Log.e("MY KEY HASH:", sign);
			Toast.makeText(getApplicationContext(),sign, Toast.LENGTH_LONG).show();
			}
			} catch (NameNotFoundException e) {
			} catch (NoSuchAlgorithmException e) {
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

	public static void callFacebookLogout(Context context) {
		Session session = Session.getActiveSession();
		if (session != null) {

			if (!session.isClosed()) {
				session.closeAndClearTokenInformation();
				// clear your preferences if saved

			}
		} else {

			session = new Session(context);
			Session.setActiveSession(session);
			session.closeAndClearTokenInformation();
			// clear your preferences if saved
		}
		// Session.setActiveSession(null);
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
			// Waking up mobile if it is sleeping
			WakeLocker.acquire(getApplicationContext());

			/**
			 * Take appropriate action on this message depending upon your app
			 * requirement For now i am just displaying it on the screen
			 * */

			// Showing received message
			// lblMessage.append(newMessage + "\n");;
			// Toast.makeText(getApplicationContext(), "New Message: " +
			// newMessage, Toast.LENGTH_LONG).show();

			// Releasing wake lock
			WakeLocker.release();
		}
	};

	public void getDeviceGcmId() {
		// accessing device gcm id
		GCMRegistrar.checkDevice(LoginActivity.this);
		// Make sure the manifest was properly set - comment out this line
		// while developing the app, then uncomment it when it's ready.
		GCMRegistrar.checkManifest(LoginActivity.this);
		GCMRegistrar.register(mContext, SENDER_ID);
		registerReceiver(mHandleMessageReceiver, new IntentFilter(DISPLAY_MESSAGE_ACTION));
		gcm_id = GCMRegistrar.getRegistrationId(LoginActivity.this);
		if (gcm_id.equals("")) {
			// Registration is not present, register now with GCM
			Log.v("hello this is sender id=", SENDER_ID);
			GCMRegistrar.register(LoginActivity.this, SENDER_ID);
			Log.v("Gcm id", "Gcm Id = " + gcm_id);
		}
	}

	private void requestMyAppFacebookFriends(Session session) {
		Request friendsRequest = createRequest(session);
		friendsRequest.setCallback(new Request.Callback() {

			@Override
			public void onCompleted(Response response) { }
		});
		friendsRequest.executeAsync();
	}

	private Request createRequest(Session session) {
		Request request = Request.newGraphPathRequest(session, "me/friends", null);

		Set<String> fields = new HashSet<String>();
		String[] requiredFields = new String[] { "id", "name", "picture" };
		fields.addAll(Arrays.asList(requiredFields));

		Bundle parameters = request.getParameters();
		parameters.putString("fields", TextUtils.join(",", fields));
		request.setParameters(parameters);

		return request;
	}

	private List<GraphUser> getResults(Response response) {
		Log.e("Response = ", "Fb Response = " + response);
		GraphMultiResult multiResult = response.getGraphObjectAs(GraphMultiResult.class);
		GraphObjectList<GraphObject> data = multiResult.getData();
		return data.castToListOf(GraphUser.class);
	}
}
