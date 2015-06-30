package com.meetmeup.helper;

import android.os.Handler;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

public class UniversalImageLoaderHelper {

	public static DisplayImageOptions setImageOptions()
	
	{
		
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	//	.showImageOnLoading(R.drawable.country_flag) 
	//    .showImageForEmptyUri(R.drawable.country_flag) // resource or drawable
	//    .showImageOnFail(R.drawable.country_flag) //
		.resetViewBeforeLoading(true)  // default
        .cacheInMemory(true) // default
        .cacheOnDisc(false) // default
        .considerExifParams(false) // default
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .displayer(new SimpleBitmapDisplayer()) // default
        .handler(new Handler()) // default
        .build();
		return options;
	}
	
	public static DisplayImageOptions setImageOptionsForRoundedCorner()
	
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
     //   .showImageOnLoading(R.drawable.country_flag) // resource or drawable
    //    .showImageForEmptyUri(R.drawable.country_flag) // resource or drawable
   //     .showImageOnFail(R.drawable.country_flag) // resource or drawable
        .resetViewBeforeLoading(true)  // default
        .delayBeforeLoading(0)
        .cacheInMemory(true) // default
        .cacheOnDisc(true) // default
        .considerExifParams(false) // default
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .displayer(new RoundedBitmapDisplayer(100)) // default
        .handler(new Handler()) // default
        .build();
		return options;
	}
/*	
	public static DisplayImageOptions setVideoThumbOptions()
	
	{
		DisplayImageOptions options = new DisplayImageOptions.Builder()
	//	.showImageOnLoading(R.drawable.country_flag) // resource or drawable
	//    .showImageForEmptyUri(R.drawable.country_flag) // resource or drawable
	//    .showImageOnFail(R.drawable.country_flag) 
        .resetViewBeforeLoading(true)  // default
        .cacheInMemory(true) // default
        .cacheOnDisc(true) // default
        .considerExifParams(false) // default
        .imageScaleType(ImageScaleType.IN_SAMPLE_POWER_OF_2) // default
        .displayer(new SimpleBitmapDisplayer()) // default
        .handler(new Handler()) // default
        .build();
		return options;
	}*/
}
