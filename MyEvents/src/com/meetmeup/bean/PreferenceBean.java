package com.meetmeup.bean;

public class PreferenceBean {

	String category_id;
	String category_name;
	String status;
	String category_image;
	
	public PreferenceBean(String category_id, String category_name,
			String status, String category_image) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
		this.status = status;
		this.category_image = category_image;
	}
	public String getCategory_id() {
		return category_id;
	}
	public String getCategory_name() {
		return category_name;
	}
	public String getStatus() {
		return status;
	}
	public String getCategory_image() {
		return category_image;
	}

//	public PreferenceBean(String category_id, String category_name,
//			String status) {
//		super();
//		this.category_id = category_id;
//		this.category_name = category_name;
//		this.status = status;
//	}
	
	
	
	
}
