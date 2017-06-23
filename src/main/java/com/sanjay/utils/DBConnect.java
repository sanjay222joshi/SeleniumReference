package com.sanjay.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * To change this template use Options | File Templates.
 */
public class DBConnect {

	protected Log log = LogFactory.getLog(getClass());
	/**
	 * Connects through JDBC
	 */
	private static Connection jdbcConnectDB() throws SQLException {

		final String dbURL = PropertiesUtil.getProperty("database-url");
		final String dbUserName = PropertiesUtil.getProperty("database-userID");
		final String dbPassword = PropertiesUtil.getProperty("database-pwd");
		final String dataBaseDriver = PropertiesUtil
				.getProperty("database-driver");

		Connection conn = null;
		try {
			Class.forName(dataBaseDriver);
			// Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
			conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

		}
		return conn;
		/**
		 * Other DBs
		 */

		// String dbUserName = FromProperties.getProperty("database-userID");
		// String dbPassword = FromProperties.getProperty("database-pwd");
		// String dbURL = FromProperties.getProperty("database-url");
		// Connection conn = null;
		// try {
		// Class.forName("oracle.jdbc.driver.OracleDriver");
		// conn = DriverManager.getConnection(dbURL, dbUserName, dbPassword);
		// } catch (ClassNotFoundException cnfe) {
		// }
		// return conn;
	}

	public static Connection getConnection() throws SQLException {
		return jdbcConnectDB();
	}
}
