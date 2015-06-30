package com.meetmeup.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Paint.Join;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.meetmeup.activity.R;
import com.meetmeup.adapters.PreferenceAdapter.ViewHolder;
import com.meetmeup.bean.EventsBean;
import com.meetmeup.fragment.HomeFragment.setOnJoinClickListener;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.RoundedImageView;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
 

public class SwipeviewAdapter extends BaseAdapter {
	
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	SwipeviewAdapter adapter;
	DisplayImageOptions opt;
	ImageLoader image_load;
	ArrayList<EventsBean> list;
	
	public static boolean cheackStatus[]; 
	
	SwipeEventItemClickListener eventItemClickListener;
	setOnJoinClickListener joinClickListener;

	public SwipeviewAdapter(Context context, ArrayList<EventsBean> l,setOnJoinClickListener listener) {
		
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
		
		opt = UniversalImageLoaderHelper.setImageOptions();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		joinClickListener = listener;
		
		cheackStatus = new boolean[list.size()];
		
//		this.eventClickListener =  evtclk;
	}
	
	public void setEventItemClickListener(SwipeEventItemClickListener evtclck){
		
		this.eventItemClickListener = evtclck;
		
	}
	

	@Override
	public int getCount() {
		
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
			view = inflator.inflate(R.layout.new_list_item, parent, false);
			
			view.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
				
					return false;
					
				}
				
			});
			
			mHolder = new ViewHolder();			
			mHolder.eventDate = (TextView) view.findViewById(R.id.event_date);
			mHolder.eventTime = (TextView) view.findViewById(R.id.event_time);
			mHolder.eventName = (TextView) view.findViewById(R.id.event_name);
			mHolder.createdBy = (TextView) view.findViewById(R.id.event_owner_name);
			mHolder.eventAddress = (TextView) view.findViewById(R.id.event_Address);
			mHolder.acceptedParticipantsCount = (TextView) view.findViewById(R.id.participants_count);
			mHolder.eventType = (TextView) view.findViewById(R.id.event_type);
			mHolder.joinIV = (RoundedImageView) view.findViewById(R.id.join_image);
			mHolder.joinTV = (TextView) view.findViewById(R.id.join_text);
			mHolder.joinLL = (LinearLayout) view.findViewById(R.id.join_linear);
			mHolder.pariticipantsGrid = (Gallery) view.findViewById(R.id.participant_view);
			
			
	//		mHolder.datetime = (TextView) view.findViewById(R.id.datetime);
		//	mHolder.new_old_event = (ImageView) view.findViewById(R.id.newevent);
		//	mHolder.myEvent = (ImageView) view.findViewById(R.id.my_event);
			
		//	mHolder.layout_imageowner = (LinearLayout)view.findViewById(R.id.layout_imageowner);
			mHolder.imgviewRoundOwner = (ImageView)view.findViewById(R.id.event_owner_image);
						
			mHolder.buttonEdit = (TextView)view.findViewById(R.id.editButton);		
			mHolder.buttonInviteFrnd = (TextView)view.findViewById(R.id.swipe_button1);		
			mHolder.buttonDeleteEvent = (TextView)view.findViewById(R.id.swipe_button2);			
			
			
			mHolder.editEvent=(TextView)view.findViewById(R.id.editButton);
			
//			mHolder.joinTV.setTag(mHolder);
//			mHolder.joinIV.setTag(mHolder);
			
			view.setTag(mHolder);
			
			
		} else {
			
			mHolder = (ViewHolder) view.getTag();
			
		}
		
		
		final EventsBean bean = list.get(position);
	
		
		image_load.displayImage(bean.getEvent_owner_profile_url(),mHolder.imgviewRoundOwner, 
				opt, new CustomImageLoader(new ProgressBar(mContext),mContext));
				
		
//		CustomImageLoader.uploadImage(view,mHolder.imgviewRoundOwner.getId(),
//				bean.getEvent_owner_profile_url(),
//				R.drawable.default_user);
		
		
		/*here we click on owner profile pic to open profile page of specific user*/	
	/*	 mHolder.layout_imageowner.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {

				String ownerid = bean.getEvent_owner_id();
				
//				Toast.makeText(mContext,"click on image",Toast.LENGTH_LONG).show();
				if(SwipeviewAdapter.this.eventItemClickListener != null)
					SwipeviewAdapter.this.eventItemClickListener.onClickProfilePic(ownerid);
								
				System.out.println("helloooooo == "+SwipeviewAdapter.this.eventItemClickListener);
				
			}
		});*/
		
				
		
		 mHolder.editEvent.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Utill.showToast(mContext, "Edit event under developement.");				
				}
			});
	        
		 
		 mHolder.buttonEdit.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if (bean.isPassed()) { 
						
						Utill.showToast(mContext,
							 "Event time finished.can not Edit.");
					}else if(SwipeviewAdapter.this.eventItemClickListener != null)
						   SwipeviewAdapter.this.eventItemClickListener.onEditButtonClick(position);		
				}				
			
		 });
		   
		 
		 mHolder.buttonInviteFrnd.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					
					if(SwipeviewAdapter.this.eventItemClickListener != null)
						   SwipeviewAdapter.this.eventItemClickListener.onFirstButtonClick(position);;
				    
									
				}
			
		 });
		 
	        
		 mHolder.buttonDeleteEvent.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub
//							Toast.makeText(mContext, "Button 2 Clicked",Toast.LENGTH_SHORT).show();
							
					if(SwipeviewAdapter.this.eventItemClickListener != null)
					   SwipeviewAdapter.this.eventItemClickListener.onSecondButtonClick(position);
						
			  }		
			
		 });
		 		
		
		mHolder.eventName.setText(bean.getEvent_title());
		mHolder.createdBy.setText(bean.getEvent_owner_name());
		mHolder.eventDate.setText(""+bean.getEvent_date());
		mHolder.eventTime.setText(""+bean.getEvent_time());
		mHolder.eventAddress.setText(""+bean.getEvent_Address());
		mHolder.pariticipantsGrid.setAdapter(new AcceptedParticipantsAdapter(mContext,
				bean.getAcceptedParticipantsList()));
		
		
		if(bean.isMyEvent() && bean.isPassed()){
			
			mHolder.joinTV.setText("send\nfeedback");
			mHolder.joinIV.setImageResource(R.drawable.feedback_icon);
			mHolder.acceptedParticipantsCount.setText("YOU+"+bean.getAcceptedParticipantsList().size());
			
		}else 
			if(!bean.isMyEvent() && bean.isPassed()){
			
			mHolder.joinTV.setText("view\nfeedback");
			mHolder.joinIV.setImageResource(R.drawable.feedback_icon);
			mHolder.acceptedParticipantsCount.setText("YOU+"+bean.getAcceptedParticipantsList().size());
			
		}else 
			if(bean.getEventAcceptStatus().equalsIgnoreCase("1")){
			
			mHolder.joinTV.setText("Joined");
			mHolder.joinIV.setImageResource(R.drawable.joined_icon);
			mHolder.acceptedParticipantsCount.setText("YOU+"+bean.getAcceptedParticipantsList().size());
		
		}else{
			
			mHolder.joinTV.setText("Join");
			mHolder.joinIV.setImageResource(R.drawable.join_icon);
			mHolder.acceptedParticipantsCount.setText("+"+bean.getAcceptedParticipantsList().size());
			
		}
		
		
		try {
			
			
			if(bean.getEvent_type().equalsIgnoreCase("1")){
				
				mHolder.eventType.setText("PUBLIC EVENT");
				
			}else{
				
				mHolder.eventType.setText("PRIVATE EVENT");
				
		    }	
			
			
		} catch (Exception e) {
			
			// TODO: handle exception
			
		}
		
		mHolder.joinLL.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				/*if(list.get(position).isMyEvent()){
					Utill.showToast(mContext,"You are the event owner.");
				}else if(!list.get(position).getEventAcceptStatus().equalsIgnoreCase("1")){
					joinClickListener.onClickJoin(position);
				}else if(list.get(position).getEventAcceptStatus().equalsIgnoreCase("1")){
//					Utill.showToast(mContext,"You have already joined.");
					joinClickListener.onClickJoin(position);
				}*/
				
				
//				if(list.get(position).isMyEvent() && list.get(position).isPassed()){
				if(list.get(position).isPassed()){
					
					joinClickListener.onClickFeedback(position);
					
				
				}else if(list.get(position).isMyEvent()){
					
					Utill.showToast(mContext,"You are the event owner.");
					
				}
				
//				else if(!list.get(position).isMyEvent() && list.get(position).isPassed()){
//					
//					Utill.showToast(mContext,"You are Not the event owner.");
//					
//				}
				else {					
							
//					,mHolder.joinTV,mHolder.joinIV
					
					/*ViewHolder jHolder = (ViewHolder) v.getTag();
					
					TextView joinTV = jHolder.joinTV;
					ImageView joinIV = jHolder.joinIV;
					
					if(joinTV.getText().toString().equalsIgnoreCase("Joined")){
						
						joinTV.setText("joined");
						joinIV.setImageResource(R.drawable.joined_icon);
						
					}else{
						
						joinTV.setText("join");
						joinIV.setImageResource(R.drawable.join_icon);
						
					}*/
					
					
					joinClickListener.onClickJoin(position);	
					
					
				}			
				
			}
		});
		
		
	/*	mHolder.datetime.setText(bean.getEvent_date() + " "
				+ bean.getEvent_time());*/
	/*	if(bean.isPassed()){
//			mHolder.new_old_event.setImageResource(R.drawable.old_event_icon);
			
		}else{
			mHolder.new_old_event.setImageResource(R.drawable.new_event_icon);
		}
		UserBean user = Utill.getUserPreferance(mContext);
		if(user.getUser_id().equalsIgnoreCase(bean.getEvent_owner_id())){
			mHolder.myEvent.setVisibility(View.VISIBLE);
		}else{
			mHolder.myEvent.setVisibility(View.GONE);
		}*/
		
		
		if(bean.isMyEvent()){
			mHolder.editEvent.setVisibility(View.VISIBLE);
		}else{
			mHolder.editEvent.setVisibility(View.GONE);
		}
		return view;
	}

	public class ViewHolder {
		
		public TextView eventName, createdBy, eventDate,eventTime,eventAddress,acceptedParticipantsCount,eventType;
	//	public ImageView new_old_event,myEvent; 
	//	public LinearLayout layout_imageowner;
		public ImageView imgviewRoundOwner;
		
		public RoundedImageView joinIV;
		public TextView joinTV;
		public LinearLayout joinLL;
		public Gallery pariticipantsGrid;
		
		public TextView buttonEdit;
		public TextView buttonInviteFrnd;
		public TextView buttonDeleteEvent;
		public TextView editEvent;
		
	}
	
	
	public interface SwipeEventItemClickListener{
		
		public void onClickProfilePic(String user_id);
		
		public void onEditButtonClick(int position);
		public void onFirstButtonClick(int position);
		public void onSecondButtonClick(int position);
		
		
	}
	
	
}
