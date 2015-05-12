package com.its.projectwork.dataanalyser.tweetdbreader;

import java.io.IOException;
import java.io.PrintWriter;

import redis.clients.jedis.Jedis;

;

public class DbReader {
	private String source = "192.168.56.101:6379";
	Jedis jedis = null;

	public void connect() {
		if (jedis == null) {
			
			jedis = new Jedis(source);
			jedis.connect();
			
			System.out.println("Connection to server sucessfully");
			// check whether server is running or not
			System.out.println("Server is running: " + jedis.ping());
		}
	}
	
	public String getNextTweet(){
		return jedis.blpop("TweetTest").get(0);
	}
	
	public void disconnect(){
		if (jedis != null) {
			jedis.close();
			jedis = null;

			System.out.println("Connection to server sucessfully");
			// check whether server is running or not
			System.out.println("Server is running: " + jedis.ping());
		}
	}
}
