package com.tsac.projectwork.dataanalyser.log;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;

import com.tsac.projectwork.dataanalyser.DbWriter;
import com.tsac.projectwork.dataanalyser.config.ConfigManager;

public class Log {
	//Log types
	private static String TYPE_INFO = "I";
	private static String TYPE_ERROR = "E";
	private static String TYPE_WARN = "W";
	

	private static Date getCurrentDate(){
		java.util.Date utilDate = new java.util.Date();
		return new java.sql.Date(utilDate.getTime());
	}
	
	private static void Log(LogEntity entity){
		if(ConfigManager.getConfig(ConfigManager.Names.CONSOLE_LOG).equals("true")){
			System.out.println(entity.getDatetime().toString() + " | " + entity.getType() + " | " + entity.getMessage() + " | " + entity.getTraceback());
		}
		
		try(DbWriter db = new DbWriter()){
			db.Connect();
			db.WriteLog(entity);
			db.Disconnect();
			
		} catch (ClassNotFoundException | SQLException e) {
			// TODO Auto-generated catch block
			if(ConfigManager.getConfig(ConfigManager.Names.CONSOLE_LOG) == "true"){
			e.printStackTrace();
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public static void LogInfo(String message){
		Log(new LogEntity(message, TYPE_INFO, getCurrentDate(), ""));
	}
	
	public static void LogError(Exception ex){
		Log(new LogEntity(ex.getMessage(), TYPE_ERROR, getCurrentDate(), Arrays.toString(ex.getStackTrace())));

	}
}
