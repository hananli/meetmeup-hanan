package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.adapters.FriendAdapter.ViewHolder;
import com.meetmeup.bean.FriendBean;
import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.fragment.PreferencesFragment.CheckedListener;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.RoundedImageView;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class PrefAdapter extends BaseAdapter {
	
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	static PrefAdapter adapter;
	
	
	
	ArrayList<PreferenceBean> list;
	
	CheckedListener listener;
	public static boolean cheackStatus[]; 
	
	DisplayImageOptions opt;
	ImageLoader image_load;
	

	public PrefAdapter(Context context, ArrayList<PreferenceBean> l) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
//		if(cheackStatus == null)
		cheackStatus = new boolean[list.size()];
		adapter = this;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		view = convertView;
		
		if (view == null) {
			
			view = inflator.inflate(R.layout.preference_view, parent, false);		
			
			mHolder = new ViewHolder();
			
			mHolder.layout_preference = (LinearLayout)view.findViewById(R.id.layout_preference);
			mHolder.textview_evtype_pref = (TextView)view.findViewById(R.id.textview_evtype_pref);
			mHolder.checkbox_evtype_pref = (CheckBox)view.findViewById(R.id.checkbox_evtype_pref);
			
			mHolder.img_evtype_pref = (ImageView)view.findViewById(R.id.img_evtype_pref);
			
			mHolder.layout_preference.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					ViewHolder jHolder = (ViewHolder) v.getTag();
					CheckBox cb = jHolder.checkbox_evtype_pref;
					
					if (cb.isChecked()) {
						
						cheackStatus[(Integer) cb.getTag()] = false;
						jHolder.checkbox_evtype_pref.setChecked(false);
					
					} else {
						
						cheackStatus[(Integer) cb.getTag()] = true;
						jHolder.checkbox_evtype_pref.setChecked(true);
						
					}
									
				}
			});
			
			mHolder.layout_preference.setTag(mHolder);
			
			view.setTag(mHolder);
			
			
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
		
				
		if (cheackStatus[position]) {
			
			mHolder.checkbox_evtype_pref.setChecked(true);
			
		} else {
			
			mHolder.checkbox_evtype_pref.setChecked(false);
			
		}
		
		
		
		final PreferenceBean prefBean = list.get(position);
		
		mHolder.textview_evtype_pref.setText(""+prefBean.getCategory_name());
		
		mHolder.checkbox_evtype_pref.setTag(position);
			
		return view;
		
	}

	 public class ViewHolder {
			
	    	public LinearLayout layout_preference; 
			public TextView textview_evtype_pref;
			public CheckBox checkbox_evtype_pref;
			public ImageView img_evtype_pref;
				
	 }
	
	 
	public static void clearAll(){
		if(cheackStatus!=null){
			for(int i = 0;i<cheackStatus.length;i++){
				cheackStatus[i] = false;
			}
			if(adapter!=null)
				adapter.notifyDataSetChanged();
		}
		
		
	}
	public static void selectAll(){
		if(cheackStatus!=null){
			for(int i = 0;i<cheackStatus.length;i++){
				cheackStatus[i] = true;
			}
			if(adapter!=null)
				adapter.notifyDataSetChanged();
		}
	}
}
