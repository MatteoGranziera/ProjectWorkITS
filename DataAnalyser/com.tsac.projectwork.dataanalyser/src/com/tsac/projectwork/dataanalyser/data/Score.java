package com.tsac.projectwork.dataanalyser.data;

import java.sql.Date;
import java.util.Calendar;

public class Score {
	private int id;
	private String pLanguage;
	private String country;
	private Date month;
	private int valScore;

	public Score(String pLanguage, String country, Date date, int valScore) {
		this.pLanguage = pLanguage;
		this.country = country;
		this.month = date;
		this.valScore = valScore;
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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
	
	public boolean equals(Score sc){
		if(sc.getCountry().equals(getCountry()) && 
				sc.getpLanguage().equals(getpLanguage()) && 
				(Integer.compare(sc.getMonth().getMonth(),getMonth().getMonth())==0)){
			return true;
		}
		else
			return false;
	}
	
	
	
}
