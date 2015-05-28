package com.tsac.projectwork.dataanalyser.data;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	//JSON KEYS
	private String KEY_TEXT = "text";
	private String KEY_CREATION = "created_at";
	private String KEY_RETWITTED = "retweeted";
	private String KEY_ENTITIES = "hashtags";
	private String KEY_RETWEET_COUNT = "retweet_count";
	private String KEY_COUNTRY = "country";
	
	//Utils Strings
	private String NULL_STRING = "NaN";
	private String TWITTER_DATE="EEE, dd MMM yyyy HH:mm:ss ZZZZZ";
	
	private JSONObject jsonObj;
	
	public Tweet(String json) throws JSONException{
		ParseJSON(json);
	}
	
	public void ParseJSON(String json) throws JSONException{
		jsonObj = new JSONObject(json);
	}
	
	public String getText() throws JSONException{
		if(jsonObj != null){
			return jsonObj.getString(KEY_TEXT);
		}
		return NULL_STRING;
	}
	
	public Date getCreation() throws JSONException, ParseException{
		if(jsonObj != null){
			SimpleDateFormat sf = new SimpleDateFormat(TWITTER_DATE);
			return new Date(sf.parse(jsonObj.getString(KEY_CREATION)).getTime());
		}
		return null;
	}
	
	public boolean getRetweeted() throws JSONException{
		if(jsonObj != null){
			return  jsonObj.getBoolean(KEY_RETWITTED);
		}
		return false;
	}
	
	public int getNumRetweet() throws JSONException{
		if(jsonObj != null){
			return jsonObj.getInt(KEY_RETWEET_COUNT);
		}
		return -1;
	}
	
	public List<String> getEntities() throws JSONException{
		if(jsonObj != null){
			//  jsonObj.getString(KEY_RETWITTED);
			return null;
		}
		return null;
	}
	
	public String getCountry() throws JSONException{
		if(jsonObj != null){
			return  jsonObj.getString(KEY_COUNTRY);
		}
		return NULL_STRING;
	}
}
