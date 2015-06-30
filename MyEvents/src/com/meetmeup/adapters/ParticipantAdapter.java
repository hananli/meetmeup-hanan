package com.meetmeup.adapters;

import java.util.ArrayList;





import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.meetmeup.activity.R;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.bean.ParticipantsBean;
import com.meetmeup.bean.UserBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

//This class is used for showing List All the event's of current user.
public class ParticipantAdapter extends BaseAdapter {
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	ParticipantAdapter adapter;
	ArrayList<ParticipantsBean> list;
	DisplayImageOptions opt;
	ImageLoader image_load;
	boolean isPayable = false;
	
	public static int ALL_GUEST = 1;
	public static int PARTICIPANT_GUEST = 2;
	
	public int guesttype = 0;
	
	
	EventsBean eventBean;

	public ParticipantAdapter(Context context,EventsBean evBean,boolean isPay) {
		
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
//		list = l;
		
		list = evBean.getParticipantsList();//changed this one also
		
		isPayable = isPay;
	
		this.eventBean = evBean;
		
//		opt = UniversalImageLoaderHelper.setImageOptionsForRoundedCorner();
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		
		
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
			
			view = inflator.inflate(R.layout.new_guestofowner_view, parent, false);
			mHolder = new ViewHolder();
			mHolder.name = (TextView) view.findViewById(R.id.name);
			mHolder.status = (TextView) view.findViewById(R.id.status);
			mHolder.image = (ImageView) view.findViewById(R.id.image);
			mHolder.amountStatus = (TextView) view.findViewById(R.id.amount_status);
			mHolder.layoutmainguest = (LinearLayout)view.findViewById(R.id.layoutmainguest);	
			
			mHolder.guestownerprogress = (ProgressBar)view.findViewById(R.id.guestprogress);
						
			mHolder.image_status = (ImageView) view.findViewById(R.id.image_status);
			
			/*if(isPayable)
				mHolder.amountStatus.setVisibility(View.VISIBLE);
			else
				mHolder.amountStatus.setVisibility(View.GONE);*/
						
			view.setTag(mHolder);
			
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
				
				
		ParticipantsBean bean = list.get(position);
		
		mHolder.name.setText(bean.getUser_fname()+" "+bean.getUser_lname());
		String Status  = bean.getStatus();
	
		/*	if(Status.equalsIgnoreCase("1"))
			mHolder.status.setText("Accept");
		else if(Status.equalsIgnoreCase("2"))
			mHolder.status.setText("Reject");
		else 
			mHolder.status.setText("Pending");*/
		
		if(guesttype == 2 && !(Status.equalsIgnoreCase("1"))){// || Status.equalsIgnoreCase("0"))){
			
			mHolder.layoutmainguest.setVisibility(View.GONE);
			
		}else{
			
			mHolder.layoutmainguest.setVisibility(View.VISIBLE);
		}
		
		
		if(Status.equalsIgnoreCase("1")){
			
			mHolder.status.setText("Join");
			mHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.selected_bg));
			mHolder.image_status.setImageResource(R.drawable.joined);
		
		}else if(Status.equalsIgnoreCase("2")){
			
			mHolder.status.setText("Decline");
			mHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.red_selected_bg));
			mHolder.image_status.setImageResource(R.drawable.decline);
		
		}else{ 
			
			mHolder.status.setText("Pending");
			mHolder.status.setBackground(mContext.getResources().getDrawable(R.drawable.grey_selected_bg));
			mHolder.image_status.setImageResource(R.drawable.pending);
		}
		
		
		Utill.loadImage(mContext, opt, image_load,mHolder.guestownerprogress,
				mHolder.image,bean.getImage());
							
//		Utill.uploadImage(view,mHolder.image.getId(),bean.getImage(),
//				R.drawable.icon);
		
		
//		image_load.displayImage(bean.getImage(),mHolder.image, 
//				opt, new CustomImageLoader(new ProgressBar(mContext),mContext));
		
		
//		image_load.displayImage(bean.getImage(),mHolder.image, opt, new CustomImageLoader(new ProgressBar(mContext), mContext));
		
		
		UserBean user = Utill.getUserPreferance(mContext);
		if(eventBean.getEvent_owner_id().equals(user.getUser_id())){
			
			mHolder.status.setVisibility(View.VISIBLE);		
			
		}else{
			
			mHolder.status.setVisibility(View.GONE);		
			if(!Status.equalsIgnoreCase("1")){
			  
				mHolder.layoutmainguest.setVisibility(View.GONE);
				
			}			
			
		}
			
		
		
	
		/*if(eventBean.getEvent_owner_id().equals(user.getUser_id())){
			
			mHolder.amountStatus.setVisibility(View.VISIBLE);			
			
		}else{
			
			mHolder.amountStatus.setVisibility(View.GONE);
			
		}*/
		
		
		if(isPayable){
		
			String amountStatus = bean.getEvent_transaction_status();
				
		    if(amountStatus.equalsIgnoreCase("1"))
				mHolder.amountStatus.setText("(Paid)");
			else
				mHolder.amountStatus.setText("(Not Paid)");
		}
		
		
		
		return view;
	}

	public class ViewHolder {
		public LinearLayout layoutmainguest;
		public TextView name, status,amountStatus;
		public ImageView image,image_status ;
		
		ProgressBar guestownerprogress;
	}
}
