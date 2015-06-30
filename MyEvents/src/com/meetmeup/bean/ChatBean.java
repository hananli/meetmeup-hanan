package com.meetmeup.bean;

import java.util.Date;

/**
 * @author administrator
 *
 */
public class ChatBean {
/*	 "chat_id": "6",
     "event_id": "72",
     "sender_id": "85",
     "f_name": "Rabia Khan",
     "image": "http://graph.facebook.com/1380272668951177/picture?type=large",
     "msg_date_time": "2015-02-16",
     "chat_message": "bdhf"*/
	
	public Date getChatTime() {
		return chatTime;
	}
	public void setChatTime(Date chatTime) {
		this.chatTime = chatTime;
	}
	private Date chatTime;
	private String chatId;
	public String getMsgTime() {
		return msgTime;
	}
	public void setMsgTime(String msgTime) {
		this.msgTime = msgTime;
	}
	private String senderId;
	private String imageUrl;
	private String senderName;
	private String msg;
	private String msgTime;
	private int type;
	
	
	public String getChatId() {
		return chatId;
	}
	public void setChatId(String chatId) {
		this.chatId = chatId;
	}
	public String getSenderId() {
		return senderId;
	}
	public void setSenderId(String senderId) {
		this.senderId = senderId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getSenderName() {
		return senderName;
	}
	public void setSenderName(String senderName) {
		this.senderName = senderName;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
}
