package com.tsac.projectwork.dataanalyser;

import java.sql.*;
public class DbWriter {
	//Environment variables names
	String ENV_ADDRESS = "PW_ADDRESS";
	String ENV_PASSWD = "PW_PASSWD";
	String ENV_USER = "PW_USERNAME";
	
	//Connection variables
	String address = "";
	String username = "";
	String password = "";
	
	Connection db = null;
	
	private void LoadEnvVariable(){
		address = System.getenv(ENV_ADDRESS);
		username = System.getenv(ENV_USER);
		password = System.getenv(ENV_PASSWD);
	}
	
	public void Connect() throws ClassNotFoundException, SQLException{
			LoadEnvVariable();
			Class.forName("org.postgresql.Driver");
			db = DriverManager.getConnection(address, username , password);
			System.out.println(db.isClosed());
	}
	
	public void AddScore(int scoreToAdd, String country, String language) throws SQLException
	{
		String query = "SELECT ";
		Statement st = db.createStatement();
		st.execute(query);
	}
	
	public void Disconnect() throws SQLException{
		if(db != null){
			db.close();
		}
	}
	
	
}
