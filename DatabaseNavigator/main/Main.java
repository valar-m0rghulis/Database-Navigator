package main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

public class Main {

	private static Connection con = null;
	private static PreparedStatement ps = null;
	private static ResultSet rs = null;
	private static String query = null;

	public static int Connect(String dbserver) throws ClassNotFoundException, SQLException {
		con = ConnectDB.getSqlCon(dbserver);
		if (con != null) {
			return 1;
		}
		return 0;
	}

	public static Vector<String> showDB() throws SQLException {
		Vector<String> dbs = new Vector<String>();
		if (con != null) {
			query = "show schemas";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				dbs.add(rs.getString(1));
			}
			rs.close();
		}
		return dbs;
	}

	public static Vector<String> showTB(String dbname) throws SQLException {
		Vector<String> tbs = new Vector<String>();
		if (con != null) {
			query = "use " + dbname;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			query = "show tables";
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			while (rs.next()) {
				tbs.add(rs.getString(1));
			}
			rs.close();
		}
		return tbs;
	}

	public static ResultSet viewTable(String tablename) throws SQLException {
		if (con != null) {
			query = "select * from " + tablename;
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
		}
		return rs;
	}
}
