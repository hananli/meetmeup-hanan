package com.meetmeup.bean;

import java.util.ArrayList;
import java.util.Date;


//This class is used for holding Events data.
public class EventsBean {
	 
	private String event_id;
	private String event_title;
	private String event_date;
	private String event_description;
	private String event_createdby;
	private String event_Address;
	private String event_lat;
	private String event_eventLong;
	private String collect_money_from_participants;
	private String evetn_owner_id;
	private String event_category;
	private String evetn_owner_profile_pic;
	private String eventAcceptStatus = "3";
	private ArrayList<ParticipantsBean> pariticipantsList;
	private String joindEvent = "0";
	
	private String category_image;
	
	private String event_chat_mode;
	
	public ArrayList<ParticipantsBean> getPariticipantsList() {
		return pariticipantsList;
	}
	public String getJoindEvent() {
		return joindEvent;
	}
	public void setJoindEvent(String joindEvent) {
		this.joindEvent = joindEvent;
	}
	public void setPariticipantsList(ArrayList<ParticipantsBean> pariticipantsList) {
		this.pariticipantsList = pariticipantsList;
	}
	public String getEventAcceptStatus() {
		return eventAcceptStatus;
	}
	public void setEventAcceptStatus(String eventAcceptStatus) {
		this.eventAcceptStatus = eventAcceptStatus;
	}
	public String getEvetn_owner_profile_pic() {
		return evetn_owner_profile_pic;
	}
	public void setEvetn_owner_profile_pic(String evetn_owner_profile_pic) {
		this.evetn_owner_profile_pic = evetn_owner_profile_pic;
	}
	String event_owner_profile_url;
	
	public String getEvent_category() {
		return event_category;
	}
	public void setEvent_category(String event_category) {
		this.event_category = event_category;
	}
	private String amount;
	private String paypalId;
	private boolean isMyEvent = false;
	
	
	
	public boolean isMyEvent() {
		return isMyEvent;
	}
	public void setMyEvent(boolean isMyEvent) {
		this.isMyEvent = isMyEvent;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getPaypalId() {
		return paypalId;
	}
	public void setPaypalId(String paypalId) {
		this.paypalId = paypalId;
	}
	public String getFeedBackStatus() {
		return feedBackStatus;
	}
	public void setFeedBackStatus(String feedBackStatus) {
		this.feedBackStatus = feedBackStatus;
	}
	private String min_participants;
	private String max_participants;
	private boolean passed;
	private String feedBackStatus;
	
	
	public boolean isPassed() {
		return passed;
	}
	public void setPassed(boolean passed) {
		this.passed = passed;
	}
	public String getMin_participants() {
		return min_participants;
	}
	public void setMin_participants(String min_participants) {
		this.min_participants = min_participants;
	}
	public String getMax_participants() {
		return max_participants;
	}
	public void setMax_participants(String max_participants) {
		this.max_participants = max_participants;
	}
	ArrayList<ParticipantsBean> participantsList;
	ArrayList<ParticipantsBean> acceptedParticipantsList;
	
	public ArrayList<ParticipantsBean> getAcceptedParticipantsList() {
		return acceptedParticipantsList;
	}
	public void setAcceptedParticipantsList(ArrayList<ParticipantsBean> acceptedParticipantsList) {
		this.acceptedParticipantsList = acceptedParticipantsList;
	}
	public ArrayList<ParticipantsBean> getParticipantsList() {
		return participantsList;
	}
	public void setParticipantsList(ArrayList<ParticipantsBean> participantsList) {
		this.participantsList = participantsList;
	}
	public String getEvetn_owner_id() {
		
		return evetn_owner_id;
		
	}
	public void setEvetn_owner_id(String evetn_owner_id) {
		
		this.evetn_owner_id = evetn_owner_id;
	}
	public String getCollect_money_from_participants() {
		return collect_money_from_participants;
	}
	public void setCollect_money_from_participants(
			String collect_money_from_participants) {
		this.collect_money_from_participants = collect_money_from_participants;
	}
	private Date event_Date_Object;
	
	public Date getEvent_Date_Object() {
		return event_Date_Object;
	}
	public void setEvent_Date_Object(Date event_Date_Object) {
		this.event_Date_Object = event_Date_Object;
	}
	public String getEvent_Address() {
		return event_Address;
	}
	public void setEvent_Address(String event_Address) {
		this.event_Address = event_Address;
	}
	public String getEvent_lat() {
		return event_lat;
	}
	public void setEvent_lat(String event_lat) {
		this.event_lat = event_lat;
	}
	public String getEvent_eventLong() {
		return event_eventLong;
	}
	public void setEvent_eventLong(String event_eventLong) {
		this.event_eventLong = event_eventLong;
	}
	public String getEvent_createdby() {
		return event_createdby;
	}
	public void setEvent_createdby(String event_createdby) {
		this.event_createdby = event_createdby;
	}
	private String event_type;
	
	
	public String getEvent_description() {
		return event_description;
	}
	public void setEvent_description(String event_description) {
		this.event_description = event_description;
	}
	public String getEvent_type() {
		return event_type;
	}
	public void setEvent_type(String event_type) {
		this.event_type = event_type;
	}
	private String event_time;
	private String event_owner_name;
	private String event_owner_id;
	
	public String getEvent_id() {
		
		return event_id;
		
	}
	
	public void setEvent_id(String event_id) {
		
		this.event_id = event_id;
		
	}
	
	public String getEvent_title() {
		
		return event_title;
		
	}
	
	public void setEvent_title(String event_title) {
		
		this.event_title = event_title;
		
	}
	
	public String getEvent_date() {
		
		return event_date;
		
	}
	
	public void setEvent_date(String event_date) {
		
		this.event_date = event_date;
		
	}
	
	public String getEvent_time() {
		
		return event_time;
		
	}
	
	public void setEvent_time(String event_time) {
		
		this.event_time = event_time;
		
	}
	
	public String getEvent_owner_name() {
		
		return event_owner_name;
		
	}
	
	public void setEvent_owner_name(String event_owner_name) {
		
		this.event_owner_name = event_owner_name;
		
	}
	
	public String getEvent_owner_id() {
		
		return event_owner_id;
		
	}
	public void setEvent_owner_id(String event_owner_id) {
		
		this.event_owner_id = event_owner_id;
		
	}
	public String getEvent_owner_profile_url() {
		
		return event_owner_profile_url;
		
	}
	
	public void setEvent_owner_profile_url(String event_owner_profile_url) {
		
		this.event_owner_profile_url = event_owner_profile_url;
		
	}
	
	public String getCategory_image() {
		
		return category_image;
		
	}
	
	public void setCategory_image(String category_image) {
		
		this.category_image = category_image;
		
	}
	
	
	public String getEvent_chat_mode() {
		
		return event_chat_mode;
		
	}
	public void setEvent_chat_mode(String event_chat_mode) {
		
		this.event_chat_mode = event_chat_mode;
		
	}
	
	
	
	
}
