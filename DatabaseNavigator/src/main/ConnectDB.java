package main;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
	private static String username = null;
	private static String password = null;
	private static String conString = null;
	private static String driverString = null;
	private static Connection con = null;

	static void setValues(String dbserver) {
		if (dbserver == "MySQL") {
			username = "root";
			password = "admin";
			conString = "jdbc:mysql://localhost:3306";
			driverString = "com.mysql.cj.jdbc.Driver";
		}
		if (dbserver == "MongoDB") {
			username = "root";
			password = "admin";
			conString = "jdbc:mysql://localhost:3306";
			driverString = "com.mysql.cj.jdbc.Driver";
		}
	}

	public static Connection getSqlCon(String dbserver) throws ClassNotFoundException, SQLException{
		setValues(dbserver);
		Class.forName(driverString);
		con = DriverManager.getConnection(conString, username, password);
		return con;
	}
	
	public static Connection getNoSqlCon(String dbserver) {
		return null;
	}
}
