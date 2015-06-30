package com.meetmeup.bean;
//This class is used for holding user,s data.
public class UserBean {
	
	public boolean isRegistered() {
		return isRegistered;
	}
	public void setRegistered(boolean isRegistered) {
		this.isRegistered = isRegistered;
	}
	
	private boolean isRegistered;
	private String fb_id;
	private String email;
	private String f_name;
	private String l_name;
	private String lattitude;
	private String longitute;
	private String profile_pic_url;
	private String fb_access_token;
	private String fb_token_expire;
	private String user_id;
	
	
	public String getUser_id() {
		return user_id;
	}
	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}
	public String getFb_id() {
		return fb_id;
	}
	public void setFb_id(String fb_id) {
		this.fb_id = fb_id;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getF_name() {
		return f_name;
	}
	public void setF_name(String f_name) {
		this.f_name = f_name;
	}
	public String getL_name() {
		return l_name;
	}
	public void setL_name(String l_name) {
		this.l_name = l_name;
	}
	public String getLattitude() {
		return lattitude;
	}
	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}
	public String getLongitute() {
		return longitute;
	}
	public void setLongitute(String longitute) {
		this.longitute = longitute;
	}
	public String getProfile_pic_url() {
		return profile_pic_url;
	}
	public void setProfile_pic_url(String profile_pic_url) {
		this.profile_pic_url = profile_pic_url;
	}
	public String getFb_access_token() {
		return fb_access_token;
 	}
	public void setFb_access_token(String fb_access_token) {
		this.fb_access_token = fb_access_token;
	}
	public String getFb_token_expire() {
		return fb_token_expire;
	}
	public void setFb_token_expire(String fb_token_expire) {
		this.fb_token_expire = fb_token_expire;
	}
	//9039851920
	
}
