package com.meetmeup.adapters;

import java.util.ArrayList;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.UserBean;
//import com.meetmeup.fragment.HomeFragment.EventItemClickListener;
//import com.meetmeup.fragment.HomeFragment.EventItemClickListener;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

//This class is used for showing List All the event's of current user.
public class EventAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	EventAdapter adapter;
	DisplayImageOptions opt;
	ImageLoader image_load;
	ArrayList<EventsBean> list;
	
	EventItemClickListener eventItemClickListener;
	

	public EventAdapter(Context context, ArrayList<EventsBean> l) {
		
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
		
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		
//		this.eventClickListener =  evtclk;
	}
	
	public void setEventItemClickListener(EventItemClickListener evtclck){
		
		this.eventItemClickListener = evtclck;
		
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
			view = inflator.inflate(R.layout.listview_itemview, parent, false);
			
			
			mHolder = new ViewHolder();
			
			
			mHolder.eventName = (TextView) view.findViewById(R.id.event_name);
			mHolder.imgviewEventType = (ImageView) view.findViewById(R.id.imgview_event_type);
			
			/*mHolder.createdBy = (TextView) view.findViewById(R.id.created_by);
			mHolder.datetime = (TextView) view.findViewById(R.id.datetime);
			mHolder.new_old_event = (ImageView) view.findViewById(R.id.newevent);
			mHolder.myEvent = (ImageView) view.findViewById(R.id.my_event);
			
			mHolder.layout_imageowner = (LinearLayout)view.findViewById(R.id.layout_imageowner);
			mHolder.imgviewRoundOwner = (ImageView)view.findViewById(R.id.imgviewRoundOwner);*/
			
			view.setTag(mHolder);
			
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
		
		
		final EventsBean bean = list.get(position);
		
		image_load.displayImage(bean.getCategory_image(),mHolder.imgviewEventType, opt, new CustomImageLoader(new ProgressBar(mContext), mContext));
	
		
		/*image_load.displayImage(bean.getEvent_owner_profile_url(),mHolder.imgviewRoundOwner, 
				opt, new CustomImageLoader(new ProgressBar(mContext),mContext));*/
				
		
//		CustomImageLoader.uploadImage(view,mHolder.imgviewRoundOwner.getId(),
//				bean.getEvent_owner_profile_url(),
//				R.drawable.default_user);
		
		
		/*here we click on owner profile pic to open profile page of specific user*/	
//		 mHolder.layout_imageowner.setOnClickListener(new OnClickListener() {
//			
//			@Override
//			public void onClick(View v) {
//
//				String ownerid = bean.getEvent_owner_id();
//				
////				Toast.makeText(mContext,"click on image",Toast.LENGTH_LONG).show();
//				if(EventAdapter.this.eventItemClickListener != null)
//					EventAdapter.this.eventItemClickListener.onClickProfilePic(ownerid);
//								
//				System.out.println("helloooooo == "+EventAdapter.this.eventItemClickListener);
//				
//			}
//		});
		
		
		mHolder.eventName.setText((position+1)+". "+bean.getEvent_title());
		
				
//		mHolder.createdBy.setText("By " + bean.getEvent_owner_name());
//		mHolder.datetime.setText(bean.getEvent_date() + " "
//				+ bean.getEvent_time());
//		if(bean.isPassed()){
//			mHolder.new_old_event.setImageResource(R.drawable.old_event_icon);
//			
//		}else{
//			mHolder.new_old_event.setImageResource(R.drawable.new_event_icon);
//		}
				
		UserBean user = Utill.getUserPreferance(mContext);
//		if(user.getUser_id().equalsIgnoreCase(bean.getEvent_owner_id())){
//			mHolder.myEvent.setVisibility(View.VISIBLE);
//		}else{
//			mHolder.myEvent.setVisibility(View.GONE);
//		}
		
		return view;
	}

	public class ViewHolder {
		
		public TextView eventName;
		public ImageView imgviewEventType; 
		
		/*public TextView eventName, createdBy, datetime;
		public ImageView new_old_event,myEvent; 
		public LinearLayout layout_imageowner;
		public ImageView imgviewRoundOwner;*/
		
	}
	
	
	public interface EventItemClickListener{
		
		public void onClickProfilePic(String user_id);
		
		
	}
	
	
}
