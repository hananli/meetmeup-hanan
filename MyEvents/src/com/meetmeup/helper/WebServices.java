package com.meetmeup.helper;

public class WebServices {
	
	public static final String main_Url = "http://72.167.41.165/meetmeup/webservices/";
	public static final String WEB_LOGIN = main_Url + "fb_login.php";
	public static final String WEB_GET_EVENTS_LIST = main_Url + "get_event_list.php";
	public static final String WEB_GET_EVENT_DETAIL = main_Url + "get_event_detail.php";
	public static final String WEB_CREATE_EVENT = main_Url + "create_event.php";
	public static String SERACH_NEAR_PLACES = "https://maps.googleapis.com/maps/api/place/search/json?";
	public static String SERACH_NEAR_PLACES_BYTAG = "https://maps.googleapis.com/maps/api/place/textsearch/json?";
	public static final String USER_CURRENT_LATLONG = main_Url + "set_user_current_position.php";
	public static final String GET_NEAR_BY = main_Url + "get_nearby_location.php";
	public static final String GET_Fb_Friends = main_Url + "get_fb_friend_list.php";
	public static final String Accept_Reject = main_Url + "accept_reject.php";
	public static final String DeleteEvent = main_Url + "event_delete.php";
	public static final String EditEvent = main_Url + "event_edit.php";
	public static final String GET_NEAR_EVENETS = main_Url + "get_near_by_events.php";
	public static final String WEB_RATE_PARTICIPANTS = main_Url+"event_ratting.php";
	public static final String WEB_SEND_YELLOW_CARD = main_Url+"show_yellow_card.php";
	public static final String WEB_CHECK_USER = main_Url+"check_user_active_status.php";
	public static final String WEB_USER_PROFILE = main_Url+"get_user_details.php";
	public static final String WEB_TRANSACTION = main_Url+"add_event_transaction.php";
	public static final String WEB_SEND_CHAT = main_Url+"event_chat.php";
	public static final String WEB_GET_CHAT = main_Url+"get_event_chat_detail.php";
	
	public static final String WEB_ADDRESS_TEXT = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";

	public static final String GetFBFriendsDirect = "https://graph.facebook.com/me/friends?";
		
	public static final String GET_PREFERENCES_LIST = main_Url + "get_preferences_list.php"; 
	public static final String SET_PREFERENCES_LIST = main_Url + "set_preferences.php"; 
	
	public static final String DELETE_EVENT_PARTICIPENT = main_Url + "delete_participants_fbid.php";  
	
	public static final String EVENT_TYPE_CATEGORY_LIST = main_Url + "get_event_category_list.php";  
		
//	public static final String WEB_ADDRESS_TEXT = "https://maps.googleapis.com/maps/api/geocode/json?latlng=";
	
}
