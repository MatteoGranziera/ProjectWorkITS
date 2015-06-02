package com.tsac.projectwork.dataanalyser;

import java.sql.*;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

import com.tsac.projectwork.dataanalyser.config.ConfigManager;
import com.tsac.projectwork.dataanalyser.data.Score;
public class DbWriter implements AutoCloseable {
	//Connection variables
	String address = "";
	String username = "";
	String password = "";
	
	Connection db = null;
	boolean auto_commit = false;
	
	private void LoadVariable(){

		address = ConfigManager.getConfig(ConfigManager.Names.DBWRITER_ADDRESS);
		username = ConfigManager.getConfig(ConfigManager.Names.DBWRITER_USER);
		password = ConfigManager.getConfig(ConfigManager.Names.DBWRITER_PASSWORD);
	}
	
	public void Connect() throws ClassNotFoundException, SQLException{
			LoadVariable();
			Class.forName("org.postgresql.Driver");
			
			db = DriverManager.getConnection(address, username , password);
			db.setAutoCommit(auto_commit);
			System.out.println(db.isClosed());
	}
	
	private int CheckIfExist(Score sc) throws SQLException{
		int index = -1;
		String query = "SELECT S.id, L.name, C.name, S.month, S.score FROM scores S "
				+ "LEFT JOIN languages L ON L.id = S.id_language "
				+ "LEFT JOIN countries C ON C.id=S.id_country "
				+ "WHERE C.name = ? AND L.name = ?";
		PreparedStatement st = (PreparedStatement) db.prepareStatement(query);
		st.setString(1, sc.getCountry());
		st.setString(2, sc.getpLanguage());
		ResultSet rs = st.executeQuery();
		
		if(rs.next()){
			index = rs.getInt(1);
		}else{
			query = "INSERT INTO scores(id_country, id_language, score, month) VALUES ( "
					+ "(SELECT id FROM countries WHERE name = ? ) , "
					+ "(SELECT id FROM languages WHERE name = ? ) , 0, ? ) RETURNING id";
			st = db.prepareStatement(query);
			st.setString(1, sc.getCountry());
			st.setString(2, sc.getpLanguage());
			st.setDate(3, (Date) sc.getMonth());
			rs = st.executeQuery();
			rs.next();
			index = rs.getInt(1);
		}
		
		rs.close();
		st.close();
		return index;

	}
	
	public void AddScore(Score sc) throws SQLException
	{
		int index = CheckIfExist(sc);
		String query = "UPDATE scores "
				+ "SET score = score + "+ sc.getValScore() +" "
				+ "WHERE id = " + index;
		Statement st = db.createStatement();
		st.execute(query);
		st.close();
	}
	
	public void DoCommit() throws SQLException {
		db.commit();
	}
	
	public void Disconnect() throws SQLException{
		if(db != null){
			db.close();
		}
	}
	
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

	@Override
	public void close() throws Exception {
		// TODO Auto-generated method stub
		Disconnect();
	}
	
	
}
