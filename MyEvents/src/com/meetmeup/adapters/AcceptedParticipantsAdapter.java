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
public class AcceptedParticipantsAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	static AcceptedParticipantsAdapter adapter;
	ArrayList<ParticipantsBean> list;
	DisplayImageOptions opt;
	ImageLoader image_load;

	

	public AcceptedParticipantsAdapter(Context context, ArrayList<ParticipantsBean> l) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
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
			view = inflator.inflate(R.layout.accepted_participants, parent, false);
			view.setBackgroundColor(mContext.getResources().getColor(color.grey_line_color));
			mHolder = new ViewHolder();
			mHolder.image = (ImageView) view.findViewById(R.id.image);
			mHolder.progress = (ProgressBar) view.findViewById(R.id.location_progressbar);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
	
		ParticipantsBean bean = list.get(position);
		//opt = UniversalImageLoaderHelper.setImageOptions();
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		image_load.displayImage(bean.getImage(),mHolder.image, opt, new CustomImageLoader(mHolder.progress, mContext));
		return view;
	}

	public class ViewHolder {
		public ImageView image;
		ProgressBar progress;
	}
	
	
}
