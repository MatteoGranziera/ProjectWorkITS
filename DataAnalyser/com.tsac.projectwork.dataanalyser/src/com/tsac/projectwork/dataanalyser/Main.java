package com.tsac.projectwork.dataanalyser;

import java.sql.SQLException;
import java.util.Date;

import com.tsac.projectwork.dataanalyser.data.Score;

public class Main {
	
	public static void main(String[] args) {
		/*DbReader db = new DbReader();
		db.connect("192.168.56.101");
		
		System.out.println("Connecting to redis...");

		System.out.println("Tweet: " + db.getNextTweet());
		
		db.disconnect();*/
		
		DbWriter dbw = new DbWriter();
		try {
			dbw.Connect();
			dbw.AddScore(new Score("Python", "Italia", new java.sql.Date(2015, 5, 1), 250));
			dbw.Disconnect();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.exit(0);
	}

}
