package com.meetmeup.fragment;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.ArrayList;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.meetmeup.activity.DashBoard;
import com.meetmeup.activity.R;
import com.meetmeup.activity.RangeSeekBar;
import com.meetmeup.activity.RangeSeekBar.OnRangeSeekBarChangeListener;
import com.meetmeup.adapters.EventAdapter;
import com.meetmeup.adapters.PrefAdapter;
import com.meetmeup.adapters.PreferenceAdapter;
import com.meetmeup.adapters.SwipeviewAdapter;
import com.meetmeup.adapters.EventAdapter.EventItemClickListener;
import com.meetmeup.adapters.SwipeviewAdapter.SwipeEventItemClickListener;
import com.meetmeup.asynch.GetEventsAsync;
import com.meetmeup.asynch.GetPreferencesListAsyTask;
import com.meetmeup.asynch.SetPreferencesAsyTask;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.fragment.HomeFragment.CustomTextWatcher;
import com.meetmeup.fragment.HomeFragment.GetEventListener;
import com.meetmeup.helper.Utill;


/*this fragment shows the list of all preference list*/
public class PreferencesFragment extends Fragment {

	static Context mContext;
	static FragmentManager mFragmentManager;
	static Fragment mfFragment;
	public static Activity mActivity;
	DashBoard dashBoard;

	ListView listView;		
	TextView txtview_update_pref;
	
	CheckBox select_all_pref;

	// This method is used for instantiating class object.
	public static Fragment getInstance(Context ct, FragmentManager fm) {
		mContext = ct;
		mFragmentManager = fm;
		if (mfFragment == null) {
			mfFragment = new PreferencesFragment();
		}
		return mfFragment;
	}

	// This method is used for getting list of all Preferences.
	void getPreferenceList() {

		listView.setVisibility(View.GONE);
		UserBean user = Utill.getUserPreferance(mContext);
		if (user.getUser_id() == null) {

			Utill.showToast(mContext, "Error");
			return;

		}

		if (Utill.isNetworkAvailable(mContext)) {
			MultipartEntity multipart = new MultipartEntity();
			
			try {
				
				showProgress();
				multipart.addPart("user_id", new StringBody(user.getUser_id()));
				new GetPreferencesListAsyTask(mContext, new GetPreferencesListener(), multipart).execute();
				
			} catch (UnsupportedEncodingException e) {
			
				e.printStackTrace();
				
			}
			
		} else {
			
			Utill.showNetworkError(mContext);
			
		}

	}

	public class GetPreferencesListener{

		public void onSuccess(ArrayList<PreferenceBean> list, String user_id) {

			hideProgress();

			if(list.isEmpty()){

				return;

			}
						
			setPreferenceAdapter(list);				

		}

		public void onError(String msg) {

			hideProgress();
			Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();

		}

		
	}

	// This is the call back method it will be called whenever current screen
	// will be on front.
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		setHasOptionsMenu(true);
		View view = inflater.inflate(R.layout.preference_listview, container, false);
		dashBoard = (DashBoard) mActivity;

		if (DashBoard.actionBar != null) {
		
			DashBoard.resetActionBarTitle("Sort Option",-2);
			
		}
		
		initializeView(view);
		setOnClickeListeners();
		getPreferenceList();

		return view;

	}


	// This method is used for intializing all the view.
	void initializeView(View view) {

		listView = (ListView) view.findViewById(R.id.listview_preference);

		txtview_update_pref = (TextView)view.findViewById(R.id.txtview_update_pref);

		select_all_pref = (CheckBox) view.findViewById(R.id.select_all_pref);
		
				
	}

	// This method is used for setting click listener to update pref.
	void setOnClickeListeners() {

		txtview_update_pref.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {


				UserBean user = Utill.getUserPreferance(mContext);
			
				if (user.getUser_id() == null) {

					Utill.showToast(mContext, "Error");
					return;

				}

				if (Utill.isNetworkAvailable(mContext)) {

					MultipartEntity multipart = new MultipartEntity();
					//MultipartEntity multipart = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE,null, Charset.forName(HTTP.UTF_8));

					try {

						showProgress();


						JSONArray datjsonArray = new JSONArray();
						JSONObject obj;
						JSONObject jsonArray = new JSONObject();
						
						try {
							jsonArray.put("user_id",user.getUser_id());
						} catch (JSONException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
											
						
						for(int i = 0; i<checkList.size(); i++){
							obj = new JSONObject();
							try {
								obj.put("status", checkList.get(i).isChecked);
								
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							try {
								
								obj.put("category_id", checkList.get(i).checkId);
							} catch (JSONException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							datjsonArray.put(obj);

						}

						try {

							jsonArray.put("category_list", datjsonArray);
						

						} catch (JSONException e) {
						
							// TODO Auto-generated catch block
							e.printStackTrace();
							
						}


						multipart.addPart("user_detail",new StringBody(jsonArray.toString()));   

						new SetPreferencesAsyTask(mContext, new SetPreferencesListener(), multipart).execute();

					} catch (UnsupportedEncodingException e) {
						e.printStackTrace();
						Log.e("unsupportedEncodingException", e.toString());
					}

				} else {

					Utill.showNetworkError(mContext);

				}

			}
		});
		
		
		select_all_pref.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				
				if(isChecked){
					
					PreferenceAdapter.selectAll();	
					
				}else{					
					
					PreferenceAdapter.clearAll(); 
					
				}
				
			}
		});
		
		
		

	};

	
	public ArrayList<PreferencesFragment.CheckStatus> checkList = new ArrayList<PreferencesFragment.CheckStatus>();

	public class CheckStatus{

		public String checkId;
		public String isChecked;			

	}

	
	public class SetPreferencesListener{

		public void onError(String msg) {


			hideProgress();

		}

		public void onSuccess(String msg) {

			Toast.makeText(mContext,""+msg,Toast.LENGTH_LONG).show();
			hideProgress();

		}

	}

	
	
	

	public void setPreferenceAdapter(ArrayList<PreferenceBean> list){

		listView.setAdapter(null);

		if(!checkList.isEmpty()){
			
			checkList.clear();
			
		}
		
		boolean isAllSelect = true;
		
		for(int i=0;i<list.size();i++){

			CheckStatus chkst = new CheckStatus();
			chkst.checkId = list.get(i).getCategory_id();
			chkst.isChecked = list.get(i).getStatus();	
			checkList.add(chkst);
			
			if(chkst.isChecked.equalsIgnoreCase("2") && isAllSelect){
				
				isAllSelect = false;
			}
			
		}
						
		PreferenceAdapter adapter = new PreferenceAdapter(mContext,list,new CheckedListener());
            	
		
		listView.setAdapter(adapter);
		listView.setVisibility(View.VISIBLE);	
		txtview_update_pref.setVisibility(View.VISIBLE);
		
		 if(isAllSelect){
				
	        select_all_pref.setChecked(true);			
	        
		}else{
			
			select_all_pref.setChecked(false);
		}
		
//		PrefAdapter adapter = new PrefAdapter(mContext,list,new CheckedListener());
//		listView.setAdapter(adapter);
//		listView.setVisibility(View.VISIBLE);	
//		txtview_update_pref.setVisibility(View.VISIBLE);

	}

	public class CheckedListener{

		public void onChecked(int pos,String checkId,String isChecked){

			if(!checkList.isEmpty()){
				
//				CheckStatus chkst = new CheckStatus();
				CheckStatus chkst = checkList.get(pos);
				chkst.checkId = checkId;
				chkst.isChecked = isChecked;				
				checkList.set(pos,chkst);
				
			}
		}
	}

	@Override
	public void onAttach(Activity activity) {
		mActivity = activity;
		super.onAttach(activity);
	}
	
	
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

}
