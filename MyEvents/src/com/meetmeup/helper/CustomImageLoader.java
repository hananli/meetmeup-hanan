package com.meetmeup.helper;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class CustomImageLoader implements ImageLoadingListener{

	ProgressBar progress;
	Context mcContext;
	Drawable onFailDrawable;
	public CustomImageLoader(ProgressBar progressBar,Context ct){
		this.progress = progressBar;
		mcContext = ct;
	}
	public CustomImageLoader(ProgressBar progressBar,Context ct,Drawable d){
		this.progress = progressBar;
		mcContext = ct;
		onFailDrawable  = d;
	}
	
	public CustomImageLoader(ProgressBar progressBar,Context ct,TextView t){
		this.progress = progressBar;
		mcContext = ct;
	}
	@Override
	public void onLoadingStarted(String imageUri, View view) {
		if(progress!=null){
			progress.setVisibility(View.VISIBLE);
		}
	//	view.setBackgroundDrawable(mcContext.getResources().getDrawable(R.drawable.default_user));
	}

	@Override
	public void onLoadingFailed(String imageUri, View view,
			FailReason failReason) {
		
		//UtilMethod.showToast("unable to download image !!"+failReason.getCause(),mcContext );
		if(progress!=null){
			progress.setVisibility(View.GONE);
		}
		if(onFailDrawable!=null){
			view.setBackgroundDrawable(onFailDrawable);	
		}
	}

	@Override
	public void onLoadingComplete(String imageUri, View postImage,
			Bitmap loadedImage) {
		if(progress!=null){
			progress.setVisibility(View.GONE);
		}
		postImage.setBackgroundDrawable(null);
	}

	@Override
	public void onLoadingCancelled(String imageUri, View view) {
			if(progress!=null){
				progress.setVisibility(View.GONE);
			}
	}
}