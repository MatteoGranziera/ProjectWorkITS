package com.tsac.projectwork.dataanalyser.data;
import java.sql.Date;

import org.json.JSONException;
import org.json.JSONObject;

public class Tweet {
	//JSON KEYS
	private String KEY_TEXT = "text";
	private String KEY_CREATION = "created_at";
	private String KEY_RETWITTED = "retweeted";
	private String KEY_ENTITIES = "hashtags";
	private String KEY_RETWEET_COUNT = "retweet_count";
	
	private String NULL_STRING = "NaN";
	
	private JSONObject jsonObj;
	
	public void ParseJSON(String json) throws JSONException{
		jsonObj = new JSONObject(json);
	}
	
	public String getText() throws JSONException{
		if(jsonObj != null){
			return jsonObj.getString(KEY_TEXT);
		}
		return NULL_STRING;
	}
	
	public Date getCreation() throws JSONException{
		if(jsonObj != null){
			return jsonObj.get(KEY_TEXT);
		}
		return NULL_STRING;
	}
}
