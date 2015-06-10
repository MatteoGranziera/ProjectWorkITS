package com.tsac.projectwork.dataanalyser.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Pattern;

import com.tsac.projectwork.dataanalyser.DbWriter;
import com.tsac.projectwork.dataanalyser.data.Tweet;


public class ConfigManager {
	
	//path of the configs files
	private static String CONFIG_FILE = "properties.config.remote";
	private static String JSON_FILE = "JSONKeys.config";
	
	//properties instances
	private static Properties properties = new Properties();
	private static Properties jsonkeys = new Properties();
	private static Map<String, String[]> langs = new HashMap<String, String[]>();
	
	//RegEx util
	private static String REGEX_HEAD = "(\\W|^)(";
	private static String REGEX_BOTTOM = ")(\\W|$)";
	private static String REGEX_SPACE = "\\s";
	private static String REGEX_SEPARATOR = "|";
	
	public static class Names{
		//DbWriter names
		public static final String DBWRITER_ADDRESS = "dbw_address"; 
		public static final String DBWRITER_USER = "dbw_user"; 
		public static final String DBWRITER_PASSWORD = "dbw_passwd"; 
		
		//DbReader names
		public static final String DBREADER_ADDRESS_ = "dbr_address"; 
		public static final String DBREADER_QUEUE_NAME = "dbr_queue_name";
		public static final String DBREADER_WAIT_IF_EMPTY = "wait_if_empty";
		
		//JSON tag names
		public static final String JSON_KEY_TEXT = "key_text";
		public static final String JSON_KEY_CREATION = "key_creation"; 
		public static final String JSON_KEY_RETWEETED = "key_retweeted"; 
		public static final String JSON_KEY_ENTITIES = "key_entities"; 
		public static final String JSON_KEY_RETWEET_COUNT = "key_retweet_count"; 
		public static final String JSON_KEY_COUNTRY = "key_country"; 
		
		//Generic names
		public static final String NUM_TWEETS_THREAD = "num_tweets_thread";
		public static final String RETWEET_MULTIPLIER = "retweet_multiplier";
		public static final String THREAD_NUMBER = "thread_number";
		public static final String CONSOLE_LOG = "console_log";
	}
	
	public static void LoadConfiguration() throws FileNotFoundException, IOException{
		properties.load(new FileInputStream(CONFIG_FILE));
		jsonkeys.load(new FileInputStream(JSON_FILE));
		Tweet.Keys.LoadKeys();
		
		//Load languages map
		
		try(DbWriter db = new DbWriter();){
			db.Connect();
			
			langs = db.Getlanguages();
			
			db.Disconnect();
			
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
	}
	
	//keynames are stored in ConfigManager.Names
	public static String getConfig(String keyname){
		return properties.getProperty(keyname);
	}
	
	public static String getJSONkey(String keyname){
		return jsonkeys.getProperty(keyname);
	}
	
	public static Map<String,String> getLanguagesRegEx(){
		Map<String, String> regexsLang = new HashMap<String,String>();
		
		for(String key:langs.keySet()){
			StringBuilder rx = new StringBuilder();
			rx.append(REGEX_HEAD);
			
			for(String value : langs.get(key)){
				rx.append(value);
				rx.append(REGEX_SEPARATOR);
			}
			
			rx.replace(rx.length() - 1, rx.length(), "");
			rx.append(REGEX_BOTTOM);
			String result = rx.toString().replaceAll("\\s", REGEX_SPACE);
			regexsLang.put(key, result);
		}
		return regexsLang;
	}
}
