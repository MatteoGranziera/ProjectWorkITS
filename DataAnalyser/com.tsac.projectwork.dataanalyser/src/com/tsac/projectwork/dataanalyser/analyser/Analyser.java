package com.tsac.projectwork.dataanalyser.analyser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONException;

import com.tsac.projectwork.dataanalyser.DbReader;
import com.tsac.projectwork.dataanalyser.config.ConfigManager;
import com.tsac.projectwork.dataanalyser.data.*;

public class Analyser{
	private int num_tweets = 10;
	private List<Score> scoreList = new ArrayList<Score>();
	private Map<String, String[]> languages = null;
	
	public void StartWorker(){
		num_tweets = Integer.parseInt(ConfigManager.getConfig(ConfigManager.Names.NUM_TWEETS_THREAD));
		languages = ConfigManager.getLanguages();
		while(true){
			List<Tweet> tweets = ReadTweets();
			
			for(Tweet t : tweets){
				try {
					Analyse(t);
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	private List<Tweet> ReadTweets(){
		try(DbReader dbr = new DbReader()){
			
			dbr.connect();
			
			List<Tweet> tweets = new ArrayList<Tweet>();
			Tweet extract = null;
			for(int i = 0; i < num_tweets && (extract = new Tweet(dbr.getNextTweet())) != null ; i++){
				tweets.add(extract);
			}
			
			return tweets;
				
			}catch(Exception e){
				return null;
				//Exception
			}
		
	}
	
	private void addScoreToList(List<Score> lst , Score sc){
		boolean ok = false;
		for(Score s : lst){
			if(s.equals(sc)){
				ok = true;
				s.setValScore(s.getValScore() + sc.getValScore());
			}
		}
		if(!ok){
			lst.add(sc);
		}
	}
	
	private void Analyse(Tweet t) throws JSONException{
		for(String lang: languages.keySet()){
		
			for(String tag:  languages.get(lang)){
				
				
				addScoreToList(scoreList, null);
				
			}
		
		}
		
	}
}
