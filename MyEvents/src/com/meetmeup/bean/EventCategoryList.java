package com.meetmeup.bean;

import java.util.ArrayList;

public class EventCategoryList {

	
	static ArrayList<EventTypeCategory> eventList;
	
	public static void setEventCategoryList(ArrayList<EventTypeCategory> eventL){	
		
		eventList = eventL;
		
	}
	
	public static ArrayList<EventTypeCategory> getEventCategoryList(){
		
		return eventList;
		
	}
	
}
