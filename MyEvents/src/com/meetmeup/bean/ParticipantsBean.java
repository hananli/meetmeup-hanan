package com.meetmeup.bean;

public class ParticipantsBean {
	 private String user_id;
	 private String user_fname;
	 private String user_lname;
	 private String image;
	 private String status;
	 private String rating;
	 private String event_transaction_status;
	 
	 
	 
	public String getEvent_transaction_status() {
		return event_transaction_status;
	}
	public void setEvent_transaction_status(String event_transaction_status) {
		this.event_transaction_status = event_transaction_status;
	}
	public String getRating() {
		return rating;
	}
	public void setRating(String rating) {
		this.rating = rating;
	}
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
	public String getUser_lname() {
		return user_lname;
	}
	public void setUser_lname(String user_lname) {
		this.user_lname = user_lname;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	 
}
