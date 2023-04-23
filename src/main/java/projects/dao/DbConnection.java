package projects.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import projects.exception.DbException;

public class DbConnection {
	private static String HOST = "localhost";
	private static String PASSWORD = "Password1";
	private static int PORT = 3306;
	private static String SCHEMA="projects";
	private static String USER="projects";
	
	
		
	public static Connection getConnection() {
		String uri = String.format("jdbc:mysql://%s:%d/%s?user=%s&password=%s", HOST,PORT,SCHEMA,USER,PASSWORD);
		Connection myConn;
		try{
			myConn=	DriverManager.getConnection(uri);
			System.out.println("Connection to schema '"+SCHEMA+"' is sucessful.");
		
		}catch(SQLException ex) {
			throw new DbException(ex.getMessage()+uri);
		}
		
		return myConn;
	}
}
