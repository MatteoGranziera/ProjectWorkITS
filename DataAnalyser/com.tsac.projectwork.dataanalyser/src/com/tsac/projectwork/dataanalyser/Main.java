package com.tsac.projectwork.dataanalyser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Date;


import com.tsac.projectwork.dataanalyser.analyser.Analyser;
import com.tsac.projectwork.dataanalyser.config.ConfigManager;
import com.tsac.projectwork.dataanalyser.data.Score;

public class Main {
	
	public static void main(String[] args) {
		
		ConfigManager.log.info("Data analyser started");
		//Enter application
		try {
			//Load configs files
			ConfigManager.LoadConfiguration();
			ConfigManager.log.info("Config files loaded");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			ConfigManager.log.warn(e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			ConfigManager.log.warn(e.getMessage());
			e.printStackTrace();
		}
		
		//Start Analyser
		Analyser an = new Analyser();
		an.StartWorker();
		System.exit(0);
	}

}
