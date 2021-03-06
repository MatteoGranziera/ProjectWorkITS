package com.tsac.projectwork.dataanalyser.data;
import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import com.tsac.projectwork.dataanalyser.config.ConfigManager;

public class Tweet {
	//JSON KEYS
	public static class Keys{
		private static String KEY_TEXT = "";
		private static String KEY_CREATION = "";
		private static String KEY_RETWEETED = "";
		private static String KEY_ENTITIES = "";
		private static String KEY_RETWEET_COUNT = "";
		private static String KEY_COUNTRY = "";
		
		public static void LoadKeys(){
			KEY_TEXT = ConfigManager.getJSONkey(ConfigManager.Names.JSON_KEY_TEXT);
			KEY_CREATION = ConfigManager.getJSONkey(ConfigManager.Names.JSON_KEY_CREATION);
			KEY_RETWEETED = ConfigManager.getJSONkey(ConfigManager.Names.JSON_KEY_RETWEETED);
			KEY_ENTITIES = ConfigManager.getJSONkey(ConfigManager.Names.JSON_KEY_ENTITIES);
			KEY_RETWEET_COUNT = ConfigManager.getJSONkey(ConfigManager.Names.JSON_KEY_RETWEET_COUNT);
			KEY_COUNTRY = ConfigManager.getJSONkey(ConfigManager.Names.JSON_KEY_COUNTRY);
		}
	}
	
	
	//Utils Strings
	private String NULL_STRING = "NaN";
	private String TWITTER_DATE="EEE MMM dd HH:mm:ss ZZZZZ yyyy";
	private Calendar calendar = GregorianCalendar.getInstance();
	
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
			SimpleDateFormat sf = new SimpleDateFormat(TWITTER_DATE, Locale.ENGLISH);
			sf.setLenient(true);
			calendar.setTime(sf.parse(jsonObj.getString(Keys.KEY_CREATION)));
			calendar.add(Calendar.DAY_OF_MONTH , -1 * ( calendar.get(Calendar.DAY_OF_MONTH) - 1 ));
			return new Date(calendar.getTime().getTime());
		}
		return null;
	}
	
	public boolean getRetweeted() throws JSONException{
		if(jsonObj != null){
			return  jsonObj.getBoolean(Keys.KEY_RETWEETED);
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
