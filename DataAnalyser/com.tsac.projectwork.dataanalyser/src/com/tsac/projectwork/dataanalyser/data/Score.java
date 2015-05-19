package com.tsac.projectwork.dataanalyser.data;

import java.util.Date;

public class Score {
	
	private String pLanguage;
	private String country;
	private Date month;
	private int valScore;

	public Score(String pLanguage, String country, Date month, int valScore) {
		this.pLanguage = pLanguage;
		this.country = country;
		this.month = month;
		this.valScore = valScore;
	}
	
	public String getpLanguage() {
		return pLanguage;
	}
	public void setpLanguage(String pLanguage) {
		this.pLanguage = pLanguage;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public Date getMonth() {
		return month;
	}
	public void setMonth(Date month) {
		this.month = month;
	}
	public int getValScore() {
		return valScore;
	}
	public void setValScore(int valScore) {
		this.valScore = valScore;
	}
	
	
	
}
