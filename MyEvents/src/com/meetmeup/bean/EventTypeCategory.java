package com.meetmeup.bean;

public class EventTypeCategory {
	
	String category_id;
	String category_name;
	String category_image;
	
	public EventTypeCategory(String category_id, String category_name) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
	}
	
	public EventTypeCategory(String category_id, String category_name,String categoryimg) {
		super();
		this.category_id = category_id;
		this.category_name = category_name;
		this.category_image = categoryimg;
		
	}
	

	public String getCategory_id() {
		return category_id;
	}

	public String getCategory_name() {
		return category_name;
	}

	public String getCategory_image() {
		return category_image;
	}
	
}
