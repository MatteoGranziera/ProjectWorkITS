package com.tsac.projectwork.dataanalyser.analyser;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.json.JSONException;

import com.tsac.projectwork.dataanalyser.DbReader;
import com.tsac.projectwork.dataanalyser.DbWriter;
import com.tsac.projectwork.dataanalyser.config.ConfigManager;
import com.tsac.projectwork.dataanalyser.data.*;

public class Analyser implements Runnable{
	private int num_tweets = 10;
	private int retw_mult = 1;
	private List<Score> scoreList = new ArrayList<Score>();
	private Map<String, String> languages = null;
	private int threadNumber = 0;
	
	public Analyser(int th){
		threadNumber = th;
	}
	
	public void StartWorker(){
		num_tweets = Integer.parseInt(ConfigManager.getConfig(ConfigManager.Names.NUM_TWEETS_THREAD));
		retw_mult = Integer.parseInt(ConfigManager.getConfig(ConfigManager.Names.RETWEET_MULTIPLIER));
		languages = ConfigManager.getLanguagesRegEx();
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
			
			try(DbWriter dbw = new DbWriter();){
				dbw.Connect();
				for(Score sc : scoreList){
					dbw.AddScore(sc);
				}
				System.out.println("Thread N." + threadNumber + " : Updated: " + scoreList.size() + " rows on DB");
				dbw.DoCommit();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private List<Tweet> ReadTweets(){
		try(DbReader dbr = new DbReader()){
			
			dbr.connect();
			
			List<Tweet> tweets = new ArrayList<Tweet>();
			Tweet extract = null;
			String jsontweet;
			
			for(int i = 0;i < num_tweets; i++){
				
				jsontweet = dbr.getNextTweet().replaceAll("[\\\\].", "");
				
				try{
					extract = new Tweet(jsontweet);
					tweets.add(extract);
				}catch(JSONException je){
					je.printStackTrace();
				}
			}
			
			return tweets;
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
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
			if(!t.getRetweeted()){
				if(Pattern.compile(languages.get(lang), Pattern.CASE_INSENSITIVE).matcher(t.getText()).find()){
					try {
						addScoreToList(scoreList, new Score(lang, t.getCountry(), t.getCreation(), 1 + (retw_mult * t.getNumRetweet())));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		StartWorker();
		
	}
}
