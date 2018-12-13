package com.tbds.model.eo;

import com.jfinal.plugin.activerecord.Model;

public class ValueEvent extends Model<ValueEvent> {
	
	private static final long serialVersionUID = -3249757092919242935L;
	
	
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
	
	public void setValue1(java.lang.String value1) {
		set("value1", value1);
	}
	
	public java.lang.String getValue1() {
		return getStr("value1");
	}
	
	public void setValue2(java.lang.String value2) {
		set("value2", value2);
	}
	
	public java.lang.String getValue2() {
		return getStr("value2");
	}
	
	public void setValue3(java.lang.String value3) {
		set("value3", value3);
	}
	
	public java.lang.String getValue3() {
		return getStr("value3");
	}
	
}
