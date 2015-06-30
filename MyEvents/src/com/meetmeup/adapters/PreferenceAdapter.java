package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.adapters.EventAdapter.EventItemClickListener;
import com.meetmeup.adapters.EventAdapter.ViewHolder;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.fragment.PreferencesFragment.CheckedListener;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


public class PreferenceAdapter extends BaseAdapter {
	
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	DisplayImageOptions opt;
	ImageLoader image_load;
	ArrayList<PreferenceBean> list;
	CheckedListener listener;
	public static boolean cheackStatus[]; 
	
	static PreferenceAdapter adapter;
	
	public boolean isAnimatedAll = false; 
	int mLastPosition;
    int prevPosition;
	
	
//	EventItemClickListener eventItemClickListener;	

	public PreferenceAdapter(Context context, ArrayList<PreferenceBean> l,CheckedListener liste) {
		
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		list = l;
				
		adapter = this;
		cheackStatus = new boolean[list.size()];
		
		for(int i=0;i<list.size();i++){
			
			PreferenceBean prefbean = list.get(i);
			
			if(prefbean.getStatus().equals("1")){
				
				cheackStatus[i] = true;
				
			}
							
		}
		
		
//		opt = UniversalImageLoaderHelper.setImageOptionsForRoundedCorner();
		
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		
//		this.eventClickListener =  evtclk;
		this.listener = liste;
		
	}
	
//	public void setEventItemClickListener(EventItemClickListener evtclck){
//		
//		this.eventItemClickListener = evtclck;
//		
//	}
	

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
			
		final PreferenceBean prefBean = list.get(position); 
		
		if (view == null) {
		
			view = inflator.inflate(R.layout.preference_view, parent, false);			
			mHolder = new ViewHolder();		
			
			mHolder.layout_preference = (LinearLayout)view.findViewById(R.id.layout_preference);
			mHolder.textview_evtype_pref = (TextView)view.findViewById(R.id.textview_evtype_pref);
			mHolder.checkbox_evtype_pref = (CheckBox)view.findViewById(R.id.checkbox_evtype_pref);
			
			mHolder.img_evtype_pref = (ImageView)view.findViewById(R.id.img_evtype_pref);
						
//			mHolder.checkbox_evtype_pref.setTag(mHolder);
			mHolder.layout_preference.setTag(mHolder);
			
			view.setTag(mHolder);
								
			
		}else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
					
		
		mHolder.layout_preference.setOnClickListener(new OnClickListener() {
		
			@Override
			public void onClick(View v) {

				ViewHolder jHolder = (ViewHolder) v.getTag();
				
				if(jHolder != null){
				
				    CheckBox cb = jHolder.checkbox_evtype_pref;
				  
				   if (cb.isChecked()) {
					   
						cheackStatus[(Integer) cb.getTag()] = false;
						jHolder.checkbox_evtype_pref.setChecked(false);
					    listener.onChecked(position,prefBean.getCategory_id(),"2");
					
					} else {
						
						cheackStatus[(Integer) cb.getTag()] = true;
						jHolder.checkbox_evtype_pref.setChecked(true);
						listener.onChecked(position,prefBean.getCategory_id(),"1");
						
					}
				   
				}	
				
			}
		});
	
		
//		BitmapDrawable drawable = (BitmapDrawable)mHolder.img_evtype_pref.getDrawable();
//		Bitmap bitmap = drawable.getBitmap();
		
//		Utill.uploadImage(v, id, url,defaultImgId);
		
		image_load.displayImage(prefBean.getCategory_image(),mHolder.img_evtype_pref, opt,
				new CustomImageLoader(new ProgressBar(mContext), mContext));
		
		
		if (cheackStatus[position]) {

			mHolder.checkbox_evtype_pref.setChecked(true);
//			listener.onChecked(position, prefBean.getCategory_id(),
//					prefBean.getStatus());
			
			listener.onChecked(position, prefBean.getCategory_id(),
					"1");
//			cheackStatus[position] = true;

		} else {

//			listener.onChecked(position, prefBean.getCategory_id(),
//					prefBean.getStatus());                                                         
			mHolder.checkbox_evtype_pref.setChecked(false);
			listener.onChecked(position, prefBean.getCategory_id(),
					"2");
//			cheackStatus[position] = false;
			
		}
				
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
     
     public static void selectAll(){
 		if(cheackStatus!=null){
 			for(int i = 0;i<cheackStatus.length;i++){
 				cheackStatus[i] = true;
 			}
 			if(adapter!=null)
 				adapter.notifyDataSetChanged();
 		}
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
     
     
}