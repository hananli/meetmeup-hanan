package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.bean.PreferenceBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//This class is used for showing participants for giving ratting.
public class RattingAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	static RattingAdapter adapter;
	ArrayList<ParticipantsBean> list;
	DisplayImageOptions opt;
	ImageLoader image_load;
	public static float rattingArray[]; 
	int SCREEN_ID=-1;
	 


	public RattingAdapter(Context context, ArrayList<ParticipantsBean> l,int id) {
		
		this.mContext = context;
		this.inflator = LayoutInflater.from(mContext);
		this.adapter = this;
		this.list = l;
//		adapter = this;
		this.SCREEN_ID = id;
		if(SCREEN_ID == 0 && this.rattingArray == null)
			this.rattingArray = new float[list.size()];
		
 
		for(int i=0;i<list.size();i++){
			
			ParticipantsBean pbean = list.get(i);
			
			if(pbean.getRating() != null){
				
				try{
				
					 rattingArray[i] = Float.parseFloat(pbean.getRating());
				
				}catch(Exception e){
					
					e.printStackTrace();
				}
				
			}
			
		}
		
	}

	@Override
	public int getCount() {
		
		if(list == null)
			return 0 ;
		else					
		  return list.size();
		
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		
		view = convertView;
		
		if (view == null) {
			
			view = inflator.inflate(R.layout.ratting_view, parent, false);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) view.findViewById(R.id.name);
			mHolder.image = (ImageView) view.findViewById(R.id.image);
			mHolder.percentTV = (TextView) view.findViewById(R.id.percent);
			mHolder.rattingBar = (RatingBar) view.findViewById(R.id.rattingbar);
			
			if(SCREEN_ID == 1)
			 mHolder.rattingBar.setIsIndicator(true);
			
			/*mHolder.rattingBar.setTag(mHolder);
			
			view.setTag(mHolder);*/
			
			
			view.setTag(mHolder);
	
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
		
				
		mHolder.rattingBar.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			
			@Override
			public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
			
				
				if(fromUser){
					rattingArray[(Integer)ratingBar.getTag()] =  rating;
					float per = rating*20;
					if(per>50.0){
						mHolder.percentTV.setTextColor(mContext.getResources().getColor(R.color.green));
					}else if(per<50.0){
						mHolder.percentTV.setTextColor(mContext.getResources().getColor(R.color.red));
					}else if(per ==50.0){
						mHolder.percentTV.setTextColor(mContext.getResources().getColor(R.color.blue));
					}
					
					
					
					
//					LayerDrawable layerDrawable = (LayerDrawable) ratingBar.getProgressDrawable();
//					DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(0)), Color.RED);   // Empty star
//					DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(1)), Color.GREEN); // Partial star
//					DrawableCompat.setTint(DrawableCompat.wrap(layerDrawable.getDrawable(2)), Color.BLUE);  // Full star
					
					LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
			        stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
			        stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
			        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
									
//			        mHolder.percentTV.setText(per+" %");
			        
			        mHolder.percentTV.setText(per+" %");
			        
				}
			}
		});
		
		
		mHolder.rattingBar.setRating(rattingArray[position]);
		
		float per = rattingArray[position]*20;
		
		if(per>50.0){
			mHolder.percentTV.setTextColor(mContext.getResources().getColor(R.color.green));
		}else if(per<50.0){
			mHolder.percentTV.setTextColor(mContext.getResources().getColor(R.color.red));
		}else if(per ==50.0){
			mHolder.percentTV.setTextColor(mContext.getResources().getColor(R.color.blue));
		}
		
		mHolder.percentTV.setText(per+" %");
	
		
		LayerDrawable stars = (LayerDrawable) mHolder.rattingBar.getProgressDrawable();
        stars.getDrawable(2).setColorFilter(Color.GREEN, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(1).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
        stars.getDrawable(0).setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_ATOP);
		
		
    	mHolder.rattingBar.setTag(position);
    	
		
		ParticipantsBean bean = list.get(position);
		 mHolder.name.setText(bean.getUser_fname()+" "+bean.getUser_lname());
		//opt = UniversalImageLoaderHelper.setImageOptions();
		opt = UniversalImageLoaderHelper.setImageOptionsForRoundedCorner();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		image_load.displayImage(bean.getImage(),mHolder.image, opt, new CustomImageLoader(null, mContext));
		return view;
	}

	public class ViewHolder {
		public TextView name,percentTV;
		public ImageView image;
		public RatingBar rattingBar;
		
	}
	
	
}
