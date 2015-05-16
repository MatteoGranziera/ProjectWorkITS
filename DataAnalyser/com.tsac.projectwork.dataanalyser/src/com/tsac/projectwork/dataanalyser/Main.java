package com.tsac.projectwork.dataanalyser;

import java.sql.SQLException;

public class Main {
	
	public static void main(String[] args) {
		DbReader db = new DbReader();
		db.connect("192.168.56.101");
		
		System.out.println("Connecting to redis...");

		System.out.println("Tweet: " + db.getNextTweet());
		
		db.disconnect();
		
		DbWriter dbw = new DbWriter();
		try {
			dbw.Connect();
			
			dbw.Disconnect();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
