package com.tsac.projectwork.dataanalyser;

import com.tsac.projectwork.dataanalyser.config.ConfigManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class DbReader implements AutoCloseable{
	Jedis conn = null;

	public void connect() {
		if (conn == null) {
			try{
			conn = new Jedis(ConfigManager.getConfig(ConfigManager.Names.DBREADER_ADDRESS_));
			conn.connect();
			}catch(JedisException e){
				System.out.println(e.getMessage());
			}
		}

	}
	
	public String getNextTweet(){
		if(conn!= null && conn.isConnected()){
			return conn.lpop(ConfigManager.getConfig(ConfigManager.Names.DBREADER_QUEUE_NAME));
		}
		else
			return "NaN";
	}
	
	public void disconnect() {
		if (conn != null) {
			conn.close();
		}

	}

	@Override
	public void close() throws Exception {
		disconnect();
		
	}
}
