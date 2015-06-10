package com.tsac.projectwork.dataanalyser.log;

import java.sql.Date;

public class LogEntity {
	private String message;
	private String type;
	private Date datetime;
	private String traceback;
	
	public LogEntity(String message, String type, Date datetime, String traceback) {
		super();
		this.message = message;
		this.type = type;
		this.datetime = datetime;
		this.traceback = traceback;
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public Date getDatetime() {
		return datetime;
	}
	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}
	public String getTraceback() {
		return traceback;
	}
	public void setTraceback(String traceback) {
		this.traceback = traceback;
	}
	
}
