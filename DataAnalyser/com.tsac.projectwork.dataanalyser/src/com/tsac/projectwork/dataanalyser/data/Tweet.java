package com.tsac.projectwork.dataanalyser.data;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Dictionary;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsac.projectwork.dataanalyser.config.ConfigManager;

public class Tweet {
	//JSON KEYS
	public static class Keys{
		private static String KEY_TEXT = "";
		private static String KEY_CREATION = "";
		private static String KEY_RETWITTED = "";
		private static String KEY_ENTITIES = "";
		private static String KEY_RETWEET_COUNT = "";
		private static String KEY_COUNTRY = "";
		
		public static void LoadKeys(){
			KEY_TEXT = ConfigManager.getConfig(ConfigManager.Names.JSON_KEY_TEXT);
			KEY_CREATION = ConfigManager.getConfig(ConfigManager.Names.JSON_KEY_CREATION);
			KEY_RETWITTED = ConfigManager.getConfig(ConfigManager.Names.JSON_KEY_RETWITTED);
			KEY_ENTITIES = ConfigManager.getConfig(ConfigManager.Names.JSON_KEY_ENTITIES);
			KEY_RETWEET_COUNT = ConfigManager.getConfig(ConfigManager.Names.JSON_KEY_RETWEET_COUNT);
			KEY_COUNTRY = ConfigManager.getConfig(ConfigManager.Names.JSON_KEY_COUNTRY);
		}
	}
	
	
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
			return jsonObj.getString(Keys.KEY_TEXT);
		}
		return NULL_STRING;
	}
	
	public Date getCreation() throws JSONException, ParseException{
		if(jsonObj != null){
			SimpleDateFormat sf = new SimpleDateFormat(TWITTER_DATE);
			return new Date(sf.parse(jsonObj.getString(Keys.KEY_CREATION)).getTime());
		}
		return null;
	}
	
	public boolean getRetweeted() throws JSONException{
		if(jsonObj != null){
			return  jsonObj.getBoolean(Keys.KEY_RETWITTED);
		}
		return false;
	}
	
	public int getNumRetweet() throws JSONException{
		if(jsonObj != null){
			return jsonObj.getInt(Keys.KEY_RETWEET_COUNT);
		}
		return -1;
	}
	
	public String[] getEntities() throws JSONException{
		if(jsonObj != null){
			return jsonObj.getString(Keys.KEY_ENTITIES).split(",");
		}
		return null;
	}
	
	public String getCountry() throws JSONException{
		if(jsonObj != null){
			return  jsonObj.getString(Keys.KEY_COUNTRY);
		}
		return NULL_STRING;
	}
}
