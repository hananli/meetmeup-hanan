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
import com.meetmeup.activity.R.color;
import com.meetmeup.bean.FriendBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//This class is used for showing Facebook friend list and user can select to frinds for an event.
public class FeedBackAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	static FeedBackAdapter adapter;
	ArrayList<ParticipantsBean> list;
	DisplayImageOptions opt;
	ImageLoader image_load;
	public static boolean cheackStatus[]; 

	

	public FeedBackAdapter(Context context, ArrayList<ParticipantsBean> l) {
		this.mContext = context;
		this.inflator = LayoutInflater.from(mContext);
		this.adapter = this;
		this.list = l;
		this.cheackStatus = new boolean[list.size()];
		this.adapter = this;
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
			view.setBackgroundColor(mContext.getResources().getColor(color.bgcolor));
			mHolder = new ViewHolder();
			mHolder.name = (TextView) view.findViewById(R.id.name);
			mHolder.cheackBox = (CheckBox) view.findViewById(R.id.select);
			mHolder.image = (ImageView) view.findViewById(R.id.image);
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
		ParticipantsBean bean = list.get(position);
		mHolder.name.setText(bean.getUser_fname()+" "+bean.getUser_lname());
		mHolder.cheackBox.setTag(position);
		
		
		//opt = UniversalImageLoaderHelper.setImageOptions();
		opt = UniversalImageLoaderHelper.setImageOptionsForRoundedCorner();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		image_load.displayImage(bean.getImage(),mHolder.image, opt, new CustomImageLoader(mHolder.progress, mContext));
		return view;
	}

	public class ViewHolder {
		public TextView name;
		public CheckBox cheackBox;
		public ImageView image;
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
