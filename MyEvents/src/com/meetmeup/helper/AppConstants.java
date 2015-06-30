package com.meetmeup.helper;

import java.util.ArrayList;

import com.meetmeup.bean.FriendBean;

public class AppConstants {
	public static final String NOTIFY_HOME = "home";
	public static final String NOTIFY_DETAIL = "detail";
	public static final String networkError = "Please Cheack internate connectivity.";
	private static String lattitude = "0";
	private static  String logitude = "0";
	private static ArrayList<FriendBean> facebookFriendList;
	private static ArrayList<FriendBean> nearByList;
	
	public static String COUNTRY = "in";
	
	private static double currentLatitude,currentLongitude;
	
	public static String getLattitude() {
		return lattitude;
	}
	public static void setLattitude(String lattitude) {
		AppConstants.lattitude = lattitude;
	}
	public static String getLogitude() {
		return logitude;
	}
	public static ArrayList<FriendBean> getFacebookFriendList() {
		return facebookFriendList;
	}
	public static void setFacebookFriendList(ArrayList<FriendBean> facebookFriendList) {
		AppConstants.facebookFriendList = facebookFriendList;
	}
	public static ArrayList<FriendBean> getNearByList() {
		return nearByList;
	}
	public static void setNearByList(ArrayList<FriendBean> nearByList) {
		AppConstants.nearByList = nearByList;
	}
	public static void setLogitude(String logitude) {
		AppConstants.logitude = logitude;
	}
	public static double getCurrentLatitude() {
		return currentLatitude;
	}
	public static void setCurrentLatitude(double currentLatitude) {
		AppConstants.currentLatitude = currentLatitude;
	}
	public static double getCurrentLongitude() {
		return currentLongitude;
	}
	public static void setCurrentLongitude(double currentLongitude) {
		AppConstants.currentLongitude = currentLongitude;
	}
		
	
	
}
