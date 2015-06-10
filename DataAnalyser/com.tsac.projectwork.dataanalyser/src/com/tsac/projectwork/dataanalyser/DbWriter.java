package com.tsac.projectwork.dataanalyser;

import java.sql.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import com.tsac.projectwork.dataanalyser.config.ConfigManager;
import com.tsac.projectwork.dataanalyser.data.Score;
import com.tsac.projectwork.dataanalyser.log.Log;
import com.tsac.projectwork.dataanalyser.log.LogEntity;
/**
 * 
 * @author Matteo Granziera
 *
 */
public class DbWriter implements AutoCloseable {
	//Connection variables
	private String address = "";
	private String username = "";
	private String password = "";
	private Connection db = null;
	private boolean auto_commit = false;
	
	/**
	 * Load variables from ConfigManager
	 */
	private void LoadVariable(){
		address = ConfigManager.getConfig(ConfigManager.Names.DBWRITER_ADDRESS);
		username = ConfigManager.getConfig(ConfigManager.Names.DBWRITER_USER);
		password = ConfigManager.getConfig(ConfigManager.Names.DBWRITER_PASSWORD);
	}
	
	/**
	 * Connet to postgres database
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public void Connect() throws ClassNotFoundException, SQLException{
			LoadVariable();
			Class.forName("org.postgresql.Driver");
			db = DriverManager.getConnection(address, username , password);
			db.setAutoCommit(auto_commit);
	}
	
	/**
	 * Add a score on database. If row doesn't exists it create
	 * @param sc com.tsac.projectwork.data.Score to add
	 * @throws SQLException
	 */
	public void AddScore(Score sc) throws SQLException
	{
		String query = "SELECT addscore(?, ? , ?, ?)";
		PreparedStatement st = db.prepareStatement(query);
		st.setString(1, sc.getpLanguage());
		st.setString(2, sc.getCountry());
		st.setInt(3, sc.getValScore());
		st.setDate(4, (Date)sc.getMonth());
		ResultSet rs = st.executeQuery();
		rs.next();
		if(!rs.getBoolean(1)){
			Log.LogInfo("Row not updated");
		}
		st.close();
	}
	
	/**
	 * Do all commits to the database
	 * @throws SQLException
	 */
	public void DoCommit() throws SQLException {
		db.commit();
	}
	
	/**
	 * Disconnect form database
	 * @throws SQLException
	 */
	public void Disconnect() throws SQLException{
		if(db != null){
			db.close();
		}
	}
	
	/**
	 * Get languages on database
	 * @return Map of languages (composed with language, and List of tags of the language)
	 * @throws ClassNotFoundException
	 * @throws SQLException
	 */
	public Map<String, String[]> Getlanguages() throws ClassNotFoundException, SQLException{
		Map<String, String[]> langs = new HashMap<String, String[]>();
		
		String query = "SELECT L.name, L.tags FROM languages L";
		Statement st = db.createStatement();
		ResultSet rs = st.executeQuery(query);
		
		while(rs.next()){
			langs.put(rs.getString("name"), rs.getString("tags").split(","));
		}
		
		return langs;
	}
	
	public void WriteLog(LogEntity log) throws SQLException{
		String query = "INSERT INTO logs(datetime, type, text, trace) VALUES(?, ?, ?, ?)";
		PreparedStatement st = db.prepareStatement(query);
		st.setDate(1, log.getDatetime());
		st.setString(2, log.getType());
		st.setString(3, log.getMessage());
		st.setString(4, log.getTraceback());
		st.execute();
		st.close();
		DoCommit();
	}

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		Disconnect();
	}
	
	
}
