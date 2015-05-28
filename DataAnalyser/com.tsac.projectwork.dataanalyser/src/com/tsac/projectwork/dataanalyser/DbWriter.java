package com.tsac.projectwork.dataanalyser;

import java.sql.*;

import com.tsac.projectwork.dataanalyser.data.Score;
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
	
	private int CheckIfExist(Score sc) throws SQLException{
		int index = -1;
		String query = "SELECT S.id, L.nome, C.nome, S.month, S.score FROM score S "
				+ "LEFT JOIN languages L ON L.id = S.id_language "
				+ "LEFT JOIN country C ON C.id=S.id_country "
				+ "WHERE C.nome = ? AND L.nome = ?";
		PreparedStatement st = (PreparedStatement) db.prepareStatement(query);
		st.setString(1, sc.getCountry());
		st.setString(2, sc.getpLanguage());
		ResultSet rs = st.executeQuery();
		
		if(rs.next()){
			index = rs.getInt(1);
		}else{
			query = "INSERT INTO score(id_country, id_language, score, month) VALUES ( "
					+ "(SELECT id FROM country WHERE nome = ? ) , "
					+ "(SELECT id FROM languages WHERE nome = ? ) , 0, ? ) RETURNING id";
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
		String query = "UPDATE score "
				+ "SET score = score + "+ sc.getValScore() +" "
				+ "WHERE id = " + index;
		Statement st = db.createStatement();
		st.execute(query);
		st.close();
	}
	
	public void Disconnect() throws SQLException{
		if(db != null){
			db.close();
		}
	}
	
	
}
