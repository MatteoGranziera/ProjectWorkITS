package com.tsac.projectwork.dataanalyser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.tsac.projectwork.dataanalyser.analyser.Analyser;
import com.tsac.projectwork.dataanalyser.config.ConfigManager;
import com.tsac.projectwork.dataanalyser.data.Score;
import com.tsac.projectwork.dataanalyser.log.Log;

public class Main {
	
	public static void main(String[] args) {
		
		try {
			ConfigManager.LoadConfiguration();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			Log.LogError(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			Log.LogError(e);
		}
		
		List<Analyser> analyserList = new ArrayList<Analyser>();
		
		for(int i = 0; i < Integer.parseInt(ConfigManager.getConfig(ConfigManager.Names.THREAD_NUMBER)); i++){
			analyserList.add(new Analyser(i + 1));
			Thread t = new Thread(analyserList.get(i));
			t.start();
			
		}
		
		Log.LogInfo(("Started " + ConfigManager.getConfig(ConfigManager.Names.THREAD_NUMBER) + " Threads : " + ConfigManager.getConfig(ConfigManager.Names.NUM_TWEETS_THREAD) + " tweets for thread"));
		
		while(true){
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				Log.LogError(e);
			}
		}

	}

}
