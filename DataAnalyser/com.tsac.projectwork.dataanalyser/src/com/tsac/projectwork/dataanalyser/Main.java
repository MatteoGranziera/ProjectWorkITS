package com.tsac.projectwork.dataanalyser;

public class Main {
	
	public static void main(String[] args) {
		DbReader db = new DbReader();
		db.connect("192.168.56.101");
		
		System.out.println("Connecting to redis...");

		System.out.println("Tweet: " + db.getNextTweet());
		
		db.disconnect();
	}

}
