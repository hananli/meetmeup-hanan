package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.adapters.EventAdapter.EventItemClickListener;
import com.meetmeup.adapters.EventAdapter.ViewHolder;
import com.meetmeup.bean.EventTypeCategory;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

public class EventCategoryTypeAdapter extends BaseAdapter {
	
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	EventCategoryTypeAdapter adapter;
	DisplayImageOptions opt;
	ImageLoader image_load;
	ArrayList<EventTypeCategory> list;
	
	EventItemClickListener eventItemClickListener;
	

	public EventCategoryTypeAdapter(Context context,ArrayList<EventTypeCategory> eventL) {
		
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = eventL;
		
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
	
		
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
			view = inflator.inflate(R.layout.event_category_layout, parent, false);
						
			 mHolder = new ViewHolder();			
			
			 mHolder.txt_eventcategoryname = (TextView) view.findViewById(R.id.txt_eventcategoryname);
			 mHolder.img_eventcategory = (ImageView) view.findViewById(R.id.img_eventcategory);
			 
			 view.setTag(mHolder);
			
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
				
		final EventTypeCategory bean = list.get(position);
		
		image_load.displayImage(bean.getCategory_image(),mHolder.img_eventcategory, opt, new CustomImageLoader(new ProgressBar(mContext), mContext));
			
		mHolder.txt_eventcategoryname.setText(bean.getCategory_name());
						
		return view;
		
	}

	public class ViewHolder {
		
		public TextView txt_eventcategoryname;
		public ImageView img_eventcategory; 
		
	}
	
	
	public interface EventItemClickListener{
		
		public void onClickProfilePic(String user_id);
		
		
	}
	
	
}
