package com.de.PDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtils {
	private static ThreadLocal<Connection> tl = new ThreadLocal<>();
	
	public static Connection getConnection(String dbpath) throws Exception{
		try{
			Class.forName("org.sqlite.JDBC");
			Connection con = DriverManager.getConnection("jdbc:sqlite:"+dbpath);
			tl.set(con);
			return con;
		}catch(Exception e){
			e.printStackTrace();
			throw e;
		}
	}
	
	public static void closeConnection(){
		try{
			Connection con = tl.get();
			if(con != null){
				con.close();
				tl.remove();
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
