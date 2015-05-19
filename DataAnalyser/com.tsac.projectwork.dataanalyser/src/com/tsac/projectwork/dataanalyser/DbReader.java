package com.tsac.projectwork.dataanalyser;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class DbReader {
	Jedis conn = null;
	String source = "";

	public void connect(String source) {
		if (conn == null) {
			this.source = source;
			try{
			conn = new Jedis(source);
			conn.connect();
			System.out.println("Connected");
			}catch(JedisException e){
				System.out.println(e.getMessage());
			}
		}

	}
	
	public String getNextTweet(){
		if(conn!= null && conn.isConnected()){
			return conn.get("TweetTest");
		}
		else
			return "NaN";
	}
	
	public void disconnect() {
		if (conn != null) {
			conn.close();
		}

	}
}
