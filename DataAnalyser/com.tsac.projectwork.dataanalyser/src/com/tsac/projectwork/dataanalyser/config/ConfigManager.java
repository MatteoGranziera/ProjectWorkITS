package com.tsac.projectwork.dataanalyser.config;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Properties;


public class ConfigManager {
	
	//path of the configs files
	private static String CONFIG_FILE = "properties.config";
	private static String JSON_FILE = "JSONKeys.config";
	
	//properties instances
	private static Properties properties = new Properties();
	private static Properties jsonkeys = new Properties();
	
	public static class Names{
		//DbWriter names
		public static final String DBWRITER_ADDRESS = "dbw_address"; 
		public static final String DBWRITER_USER = "dbw_user"; 
		public static final String DBWRITER_PASSWORD = "dbw_passwd"; 
		
		//DbReader names
		public static final String DBREADER_ADDRESS_ = "dbr_address"; 
		public static final String DBREADER_QUEUE_NAME = "dbr_queue_name"; 
		
		//JSON tag names
		public static final String JSON_KEY_TEXT = "key_text";
		public static final String JSON_KEY_CREATION = "key_creation"; 
		public static final String JSON_KEY_RETWITTED = "key_retwitted"; 
		public static final String JSON_KEY_ENTITIES = "key_entities"; 
		public static final String JSON_KEY_RETWEET_COUNT = "key_retweet_count"; 
		public static final String JSON_KEY_COUNTRY = "key_country"; 
		
		//Generic names
		public static final String NUM_TWEETS_THREAD = "num_tweets_thread";
		public static final String LANGUAGES = "num_tweets_thread";
	}
	
	public static void LoadConfiguration() throws FileNotFoundException, IOException{
		properties.load(new FileInputStream(CONFIG_FILE));
		jsonkeys.load(new FileInputStream(JSON_FILE));
	}
	
	//keynames are stored in ConfigManager.Names
	public static String getConfig(String keyname){
		return properties.getProperty(keyname);
	}
	
	public static String[] getLanguages(){
		return properties.getProperty(Names.LANGUAGES).split(";");
	}
}
