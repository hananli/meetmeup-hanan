package com.meetmeup.bean;

import java.io.Serializable;

//This class is used for holding Place  Data.
public class Place implements Serializable {
private Double lat;
private Double longe;
private String name;
private String formattendAddress;
private String id;
private String iconUrl;

private String placeId;


public String getPlaceId() {
	return placeId;
}
public void setPlaceId(String placeId) {
	this.placeId = placeId;
}
public String getIconUrl() {
	return iconUrl;
}
public void setIconUrl(String iconUrl) {
	this.iconUrl = iconUrl;
}
public String getId() {
	return id;
}
public void setId(String id) {
	this.id = id;
}
public String getName() {
	return name;
}
public Double getLat() {
	return lat;
}
public void setLat(Double lat) {
	this.lat = lat;
}
public Double getLonge() {
	return longe;
}
public void setLonge(Double longe) {
	this.longe = longe;
}
public void setName(String name) {
	this.name = name;
}
public String getFormattendAddress() {
	return formattendAddress;
}
public void setFormattendAddress(String formattendAddress) {
	this.formattendAddress = formattendAddress;
}

}
