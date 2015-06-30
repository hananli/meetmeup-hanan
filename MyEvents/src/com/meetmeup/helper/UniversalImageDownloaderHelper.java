package com.meetmeup.helper;

import android.graphics.Bitmap;

import com.meetmeup.activity.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
public class UniversalImageDownloaderHelper {
	
	static DisplayImageOptions options;
	
	public static DisplayImageOptions initRoundedThumbConfig(){
	    options = new DisplayImageOptions.Builder()
			.showImageOnLoading(R.drawable.launcher)
			.showImageForEmptyUri(R.drawable.launcher)
			.showImageOnFail(R.drawable.launcher)
			.cacheInMemory(true)
			.cacheOnDisc(true)
			.considerExifParams(true)
			.displayer(new RoundedBitmapDisplayer(100))
			.bitmapConfig(Bitmap.Config.RGB_565)
			.build();
	    return options;
	}
	
	public static DisplayImageOptions initLargeImageConfig(){
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.launcher)
			.showImageOnFail(R.drawable.launcher)
			.resetViewBeforeLoading(true)
			.cacheOnDisc(true)
			.imageScaleType(ImageScaleType.EXACTLY)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.considerExifParams(true)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
		return options;
	}
}