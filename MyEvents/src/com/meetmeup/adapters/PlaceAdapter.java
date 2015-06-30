package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.bean.Place;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;


//This class is used for showing List of the places.
public class PlaceAdapter extends BaseAdapter{
	
	DisplayImageOptions opt;
	ImageLoader image_load;
	Context mContext;
	ArrayList<Place> nearbyList;
	
	public PlaceAdapter(Context mContext,ArrayList<Place> nearbyList) {
		
		this.mContext = mContext;
		this.nearbyList = nearbyList;
		
	}

	@Override
	public int getCount() {
		return nearbyList.size();
	}

	@Override
	public Object getItem(int position) {
		return position;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		ViewHolderItem holder;
		
		if(convertView==null){
			
			holder = new ViewHolderItem();
			LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.location_item_xml, parent,false); 

			holder.formatted_address = (TextView) convertView.findViewById(R.id.loc_formatted_address);
			holder.lat = (TextView) convertView.findViewById(R.id.loc_lat);
			holder.lng = (TextView) convertView.findViewById(R.id.loc_lon);
			holder.formatted_address = (TextView) convertView.findViewById(R.id.loc_formatted_address);
			holder.name = (TextView) convertView.findViewById(R.id.loc_name);
			holder.icon = (ImageView) convertView.findViewById(R.id.location_icon);
			holder.location_progressbar = (ProgressBar) convertView.findViewById(R.id.location_progressbar);
			convertView.setTag(holder);
			
		}
		else{
			
			holder = (ViewHolderItem) convertView.getTag();
			
		}

		holder.formatted_address.setText(nearbyList.get(position).getFormattendAddress());
	//	holder.lat.setText("Lat:"+nearbyList.get(position).getLat());
	//	holder.lng.setText("Lng"+nearbyList.get(position).getLonge());
		holder.name.setText(nearbyList.get(position).getName());
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		image_load.displayImage(nearbyList.get(position).getIconUrl(), holder.icon, opt, new CustomImageLoader(holder.location_progressbar, mContext));

		/*aq = new AQuery(convertView);
		aq.id(R.id.location_icon).progress(R.id.location_progressbar).image(nearbyList.get(position).getIcon(),
				true, true, 0, R.drawable.ic_launcher, null,
				AQuery.CACHE_DEFAULT,0.0f).visible();
*/
		return convertView;
	}

	public static class ViewHolderItem{
		TextView formatted_address;
		TextView lat;
		TextView lng;
		ImageView icon;
		TextView name;
		TextView id;
		ProgressBar location_progressbar;
	}
}
