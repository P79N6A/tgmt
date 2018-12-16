package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

public class ErrorEvent extends Model<ErrorEvent> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3064113955984824852L;
	
	public static final ErrorEvent dao = new ErrorEvent().dao();
	
	public void setId(java.lang.Long id) {
		set("id", id);
	}
	
	public java.lang.Long getId() {
		return getLong("id");
	}
	
	public void setCpm(java.lang.String cpm) {
		set("cpm", cpm);
	}
	
	public java.lang.String getCpm() {
		return getStr("cpm");
	}
	
	public void setEventDateTime(java.util.Date eventDateTime) {
		set("event_datetime", eventDateTime);
	}
	
	public java.util.Date getEventDateTime() {
		return get("event_datetime");
	}
		
	public void setEventDate(java.util.Date eventDate) {
		set("event_date", eventDate);
	}
	
	public java.util.Date getEventDate() {
		return get("event_date");
	}
	
	public void setEventTime(java.util.Date eventTime) {
		set("event_time", eventTime);
	}
	
	public java.util.Date getEventTime() {
		return get("event_time");
	}
	
	public void setTrain(java.lang.String train) {
		set("train", train);
	}
	
	public java.lang.String getTrain() {
		return getStr("train");
	}
	
	public void setObcu(java.lang.String obcu) {
		set("obcu", obcu);
	}
	
	public java.lang.String getObcu() {
		return getStr("obcu");
	}
	
	public void setType(java.lang.String type) {
		set("type", type);
	}
	
	public java.lang.String getType() {
		return getStr("type");
	}
	
	public void setItem(java.lang.String item) {
		set("item", item);
	}
	
	public java.lang.String getItem() {
		return getStr("item");
	}
	
	public void setElement(java.lang.String element) {
		set("element", element);
	}
	
	public java.lang.String getElement() {
		return getStr("element");
	}
	
	public void setErrorType(java.lang.String errorType) {
		set("error_type", errorType);
	}
	
	public java.lang.String getErrorType() {
		return getStr("error_type");
	}
	
	public void setErrorDetail(java.lang.String errorDetail) {
		set("error_detail", errorDetail);
	}
	
	public java.lang.String getErrorDetail() {
		return getStr("error_detail");
	}
	
	public void setExtInfo(java.lang.String extInfo) {
		set("ext_info", extInfo);
	}
	
	public java.lang.String getExtInfo() {
		return getStr("ext_info");
	}

}
