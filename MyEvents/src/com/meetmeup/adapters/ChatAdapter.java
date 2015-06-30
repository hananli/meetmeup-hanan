package com.meetmeup.adapters;

import java.util.ArrayList;





import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.content.Context;
import android.graphics.Paint;
import android.text.Html;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.meetmeup.activity.R;
import com.meetmeup.bean.ChatBean;
import com.meetmeup.helper.CustomImageLoader;
import com.meetmeup.helper.UniversalImageLoaderHelper;
import com.meetmeup.helper.Utill;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

//This class is used for showing List of the Chat according to a perticular event.
public class ChatAdapter extends BaseAdapter {
	
	ViewHolder mHolder;
	LayoutInflater inflator;
	View view;
	Context mContext;
	ListView listView;
	static ChatAdapter adapter;
	ArrayList<ChatBean> list;
	DisplayImageOptions opt;
	ImageLoader image_load;

	public ChatAdapter(Context context, ArrayList<ChatBean> l) {
		mContext = context;
		inflator = LayoutInflater.from(mContext);
		adapter = this;
		list = l;
		adapter = this;
	}

	 @Override
     public int getViewTypeCount() {
         return 2;
     }

	 @Override
     public int getItemViewType(int position) {
         return list.get(position).getType();
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
		if(list.get(position).getType() ==0){
		
			view = inflator.inflate(R.layout.right_chat_item, parent, false);
		}else if(list.get(position).getType()==1){
		
			view = inflator.inflate(R.layout.left_chat_item, parent, false);
		
		}
		
		mHolder = new ViewHolder();
		mHolder.image = (ImageView) view.findViewById(R.id.image);
		mHolder.msg = (TextView) view.findViewById(R.id.text);
		mHolder.name = (TextView) view.findViewById(R.id.name);
		
		mHolder.date = (TextView) view.findViewById(R.id.date);
	
//		mHolder.layout_date_seen = (LinearLayout) view.findViewById(R.id.layout_date_seen);	
//		mHolder.layoutchat = (LinearLayout) view.findViewById(R.id.layoutchat);	
		
		
		
		mHolder.sentImage = (ImageView) view.findViewById(R.id.sent);
			view.setTag(mHolder);
		} else {
			mHolder = (ViewHolder) view.getTag();
		}
		
		ChatBean bean = list.get(position);
		
		mHolder.date.setText(bean.getMsgTime());
		
		if(Utill.isStringNullOrBlank(bean.getChatId())){
			
			mHolder.sentImage.setImageResource(R.drawable.seen_icon);
		
		}else{
			
			mHolder.sentImage.setImageResource(R.drawable.delivered);
			
		}
		
		//mHolder.msg.setText(bean.getMsg()+bean.getChatId());
		mHolder.msg.setText(bean.getMsg());
			
				
		mHolder.name.setPaintFlags(mHolder.name.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
		mHolder.name.setText(bean.getSenderName());
				
		opt = UniversalImageLoaderHelper.setImageOptionsForRoundedCorner();
		image_load = ImageLoader.getInstance();
		image_load.init(ImageLoaderConfiguration.createDefault(mContext));
		image_load.displayImage(bean.getImageUrl(),mHolder.image, opt, new CustomImageLoader(null, mContext));
		
//		mHolder.layout_date_seen.setY(y);
		
		return view;
	}

	public class ViewHolder {
		
		public ImageView image;
		public TextView msg,name;
		public ImageView sentImage;
		public TextView date;
		
//		public LinearLayout layout_date_seen;		
//		public LinearLayout layoutchat;
	}
	
}









