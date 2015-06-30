package com.meetmeup.bean;

//This class is used for holding Facebook friend data.
public class FriendBean {
	private String user_id;
	private String user_fname;
	private String friend_fb_id;
	private String user_lname;
	private String image_url;
	private String is_app_user;
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getUser_fname() {
		return user_fname;
	}
	public void setUser_fname(String user_fname) {
		this.user_fname = user_fname;
	}
	public String getFriend_fb_id() {
		return friend_fb_id;
	}
	public void setFriend_fb_id(String friend_fb_id) {
		this.friend_fb_id = friend_fb_id;
	}
	public String getUser_lname() {
		return user_lname;
	}
	public void setUser_lname(String user_lname) {
		this.user_lname = user_lname;
	}
	public String getImage_url() {
		return image_url;
	}
	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	public String getIs_app_user() {
		return is_app_user;
	}
	public void setIs_app_user(String is_app_user) {
		this.is_app_user = is_app_user;
	}
}
