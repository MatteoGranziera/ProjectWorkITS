package com.tsac.projectwork.dataanalyser.analyser;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.tsac.projectwork.dataanalyser.DbReader;
import com.tsac.projectwork.dataanalyser.data.*;

public class Analyser{
	private int DEFAULT_NUM_TWEETS = 10;
	private int num_tweets = 10;
	
	public void StartWorker(){
		while(true){
			List<Tweet> tweets = ReadTweets();
			
			for(Tweet t : tweets){
				
			}
			
		}
	}
	
	private List<Tweet> ReadTweets(){
		try(DbReader dbr = new DbReader()){
			
			dbr.connect("192.168.56.101");
			
			List<Tweet> tweets = new ArrayList<Tweet>();
			for(int i = 0; i < num_tweets ; i++){
				tweets.add(new Tweet(dbr.getNextTweet()));
			}
			
			return tweets;
				
			}catch(Exception e){
				return null;
				//Exception
			}
		
	}
	
	private static Score Analyse(Tweet t) throws JSONException{
		int scorePoint = 0;
		
		
		
		return new Score(null, t.getCountry(), null, scorePoint);
		
	}
}
