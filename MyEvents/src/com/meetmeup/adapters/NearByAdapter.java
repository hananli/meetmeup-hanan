package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.bean.FriendBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.RoundedImageView;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//This class is used for showing near by people list and user can select to people for an event.
public class NearByAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	static NearByAdapter adapter;
	ArrayList<FriendBean> list;
	DisplayImageOptions opt;
	ImageLoader image_load;
	public static boolean cheackStatus[]; 

	

	public NearByAdapter(Context context, ArrayList<FriendBean> l) {
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
			
			view = inflator.inflate(R.layout.invite_person_view, parent, false);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) view.findViewById(R.id.name);
			mHolder.cheackBox = (CheckBox) view.findViewById(R.id.select);
			mHolder.image = (RoundedImageView) view.findViewById(R.id.image);
			mHolder.mainLayout = (LinearLayout) view.findViewById(R.id.main);
			mHolder.progress = (ProgressBar) view.findViewById(R.id.location_progressbar);
			mHolder.mainLayout.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

					ViewHolder jHolder = (ViewHolder) v.getTag();
					CheckBox cb = jHolder.cheackBox;
					if (cb.isChecked()) {
						cheackStatus[(Integer) cb.getTag()] = false;
						jHolder.cheackBox.setChecked(false);
					} else {
						cheackStatus[(Integer) cb.getTag()] = true;
						jHolder.cheackBox.setChecked(true);
					}
				
					
				}
			});
			
			view.setTag(mHolder);
			
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
		if (cheackStatus[position]) {
			
			mHolder.cheackBox.setChecked(true);
			
		} else {
			
			mHolder.cheackBox.setChecked(false);
			
		}
	
		FriendBean bean = list.get(position);
		mHolder.name.setText(bean.getUser_fname()+" "+bean.getUser_lname());
		mHolder.cheackBox.setTag(position);
		
		
		//opt = UniversalImageLoaderHelper.setImageOptions();
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		image_load.displayImage(bean.getImage_url(),mHolder.image, opt, new CustomImageLoader(mHolder.progress, mContext));
		return view;
		
	}

	public class ViewHolder {
		public TextView name;
		public CheckBox cheackBox;
		public RoundedImageView image;
		public LinearLayout mainLayout;
		ProgressBar progress;
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
