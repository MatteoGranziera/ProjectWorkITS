package com.tsac.projectwork.dataanalyser;

import com.tsac.projectwork.dataanalyser.config.ConfigManager;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;

public class DbReader implements AutoCloseable{
	Jedis conn = null;

	/**
	 * Connect to redis db (it takes config from ConfigManager)
	 */
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
	
	/**
	 * Get one tweet from redis
	 * @return json string of tweet
	 */
	public String getNextTweet(){
		while(true)
		{
			if(conn!= null && conn.isConnected()){
				String res = conn.lpop(ConfigManager.getConfig(ConfigManager.Names.DBREADER_QUEUE_NAME));
				if(res != null){
					return res;
				}
				System.out.println("Queue is empty... Retry in 10 seconds");
				try {
					Thread.sleep(Integer.parseInt(ConfigManager.getConfig(ConfigManager.Names.DBREADER_WAIT_IF_EMPTY)));
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				//return "{'text':'#Python, is java beacuse python is #Java','created_at':'Wed Aug 27 13:08:45 +0000 2015','retweeted':'False','hashtags':[],'retweet_count':'0','state':'Italy'}";
			}
		}
	}
	
	/**
	 * disconnect from redis 
	 */
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
