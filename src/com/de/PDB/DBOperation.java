package com.de.PDB;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

public class DBOperation {
//	private final String DELETETABLE = "DELETE FROM noduleInfo;"
//									+ "DELETE FROM noduleNum;";
	private String dbpath,imgpath;
	
	public static void main(String[] args){
		String str1 = "C:\\Users\\Foggy\\Desktop\\doctor\\thyroid.db";
		String str2 = "C:\\Users\\Foggy\\Desktop\\doctor\\img";
		DBOperation dbo = new DBOperation(str1,str2);
//		dbo.deleteRemain("noduleNum");
		long s = System.currentTimeMillis();
		dbo.mergeTable("noduleNum");
		dbo.mergeTable("noduleInfo");
		long e = System.currentTimeMillis();
		System.out.println((e-s)/1000/60);
	}
	
	
	public DBOperation(String dbpath,String imgpath){
		this.dbpath = dbpath;
		this.imgpath = imgpath;
		
	}
	
	public void deleteRemain(String table){
		StringBuilder sb = new StringBuilder();
		File file = new File(imgpath);
		String[] list = file.list();
		try {
			Connection con = DBUtils.getConnection(dbpath);
			Statement stme = con.createStatement();
			for(int i=0;i<list.length;i++){
				sb.append("'"+list[i]+"'");
				if(i < (list.length-1)){
					sb.append(",");
				}
			}
			String sql = "DELETE FROM "+table+" "
						+"WHERE name NOT IN ("+sb.toString()+")";
//			System.out.println(sql);
			stme.execute(sql);
			stme.execute(sql);
			stme.executeUpdate("VACUUM");
			stme.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBUtils.closeConnection();
		}
	}
	
	public void mergeTable(String table){
		ResultSet rs = null;
		File file = new File(imgpath);
		String[] list = file.list();
		String[] str = clearTable(table);
		String sql1 = "INSERT INTO "+table+"("+str[0]+")"
					+" values("+str[1]+")";
		String sql2 = "SELECT * FROM "+table
					+" WHERE name=?";
		File[] dblist = new File(new File(dbpath).getParent()+"/db").listFiles(new MyFilter(".db"));
		try {
//			Files.copy(dblist[0].toPath(), new File(dbpath).toPath());
			Connection con1 = DBUtils.getConnection(dbpath);
			con1.setAutoCommit(false);
			PreparedStatement ps = con1.prepareStatement(sql1);
			for(int k=0;k<dblist.length;k++){
			try{
				Connection con = DBUtils.getConnection(dblist[k].getAbsolutePath());
				PreparedStatement ps2 = con.prepareStatement(sql2);
				for(int i=0;i<list.length;i++){
					ps2.setString(1, list[i]);
					rs = ps2.executeQuery();
					while(rs.next()){
						ResultSetMetaData rsmd = rs.getMetaData();
						for(int j=1;j<=rsmd.getColumnCount();j++){
							ps.setObject(j, rs.getObject(j), rsmd.getColumnType(j));
						}
						ps.addBatch();
					}
				}
				ps.executeBatch();
				con1.commit();
				rs.close();
				ps2.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally{
				DBUtils.closeConnection();
			}
			}
			ps.close();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBUtils.closeConnection();
		}
	}
	
	private String[] clearTable(String table){
		StringBuilder keys = new StringBuilder(),values = new StringBuilder();
		String[] str = new String[2];
		try {
			Connection con = DBUtils.getConnection(dbpath);
			Statement stme = con.createStatement();
			String sql = "DELETE FROM "+table;
			stme.execute(sql);
			stme.executeUpdate("VACUUM");
			ResultSet rs = stme.executeQuery("SELECT * FROM "+table);
			ResultSetMetaData rsmd = rs.getMetaData();
			for(int j=1;j<=rsmd.getColumnCount();j++){
				String columnName = rsmd.getColumnName(j);
				keys.append(columnName+",");
				values.append("?,");
			}
			rs.close();
			stme.close();
			str[0] =keys.substring(0, keys.length()-1).toString();
			str[1] =values.substring(0,values.length()-1).toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally{
			DBUtils.closeConnection();
		}
		return str;
	}
}
