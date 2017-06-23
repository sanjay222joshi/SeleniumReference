package com.sanjay.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataLoader {

	private static DataLoader dataloader;
	private Connection connection;

	private DataLoader() {
	}

	public static DataLoader getInstance() { 
		if (dataloader == null) {
			dataloader = new DataLoader();
			dataloader.connectDB();
		}
		return dataloader;
	}

	private void connectDB() {
		if (this.connection == null) {
			try {
				this.connection = DBConnect.getConnection();
				this.connection.setAutoCommit(false);
			} catch (Exception e) {
				e.printStackTrace();
				closeConnection();
				throw new RuntimeException("Unable to connect to db.....", e);
			}
		}
	}

	public void executeSQLStatement(File sqlFile) {
		if (!sqlFile.exists()) {
			throw new RuntimeException("Script file not found, Path of the file:" + sqlFile.getAbsolutePath());
		}
		Statement st = null;
		try {
			st = this.connection.createStatement();
			readScriptAndAddToBatchStmt(sqlFile, st);
			st.executeBatch();
			st.close();
			this.connection.commit();
		} catch (Exception se) {
			closeStatement(st);
			rollback();
			se.printStackTrace();
			throw new RuntimeException("Oops got SQL exception ", se);
		}
	}

	public static boolean readScriptAndAddToBatchStmt(File script, Statement st) {
		try {
			BufferedReader fr = new BufferedReader(new FileReader(script));
			String eachSql;
			while ((eachSql = fr.readLine()) != null) {
				if (!eachSql.trim().equals("") && !eachSql.trim().startsWith("#")) {
					st.addBatch(eachSql);
				}
			}
			if (fr != null) {
				fr.close();
			}
		} catch (SQLException e) {
			throw new RuntimeException("Not able to add ", e);
		} catch (IOException ioe) {
			throw new RuntimeException("Unable to do file operation ", ioe);
		}
		return true;
	}

	private void rollback() {
		try {
			this.connection.rollback();
		} catch (SQLException sqe) {

			throw new RuntimeException("Unable to rollback ", sqe);
		}
	}

	public void closeConnection() {
		if (this.connection != null) {
			try {
				this.connection.close();
				this.connection = null;
				dataloader = null;
			} catch (SQLException se) {
				throw new RuntimeException(se);
			}
		}
	}

	private void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException sqe) {
				throw new RuntimeException("Unable to rollback ", sqe);
			}
		}
	}

	public String executeSQLStatement(String query, String columnName) {
		Statement statement = null;
		String result = null;
		try {
			statement = this.connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			if (resultSet.next()) {
				result = resultSet.getObject(columnName).toString();
			}
			this.connection.commit();
			statement.close();
			return result;
		} catch (Exception se) {
			System.out.println("Se Exception" + se);
			closeStatement(statement);
			rollback();
			se.printStackTrace();
			throw new RuntimeException("Oops got SQL exception ", se);
		}
	}

	public int executeSQLStatement(String query) {
		Statement statement = null;
		try {
			statement = this.connection.createStatement();
			int count = statement.executeUpdate(query);
			this.connection.commit();
			statement.close();
			return count;
		} catch (Exception se) {
			closeStatement(statement);
			rollback();
			se.printStackTrace();
			throw new RuntimeException("Not able to Execute ", se);
		}
	}

	public List<String> executeSQLQuery(String query, String columnName) {
		Statement statement = null;
		List<String> resultList = null;
		try {
			statement = this.connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			resultList = new ArrayList<String>();
			if (resultSet.next()) {
				do {
					Object object = resultSet.getObject(columnName);
					if (object != null) {
						resultList.add(object.toString());
					}
				} while (resultSet.next());
			}
			this.connection.commit();
			statement.close();
			return resultList;

		} catch (Exception e) {
			closeStatement(statement);
			e.printStackTrace();
			rollback();
			throw new RuntimeException("Not able to Execute", e);
		}

	}

	// TODO - Needs to test this method.
	public Map<Integer, List<String>> executeSQLQuery(String query) {
		Statement statement = null;
		Map<Integer, List<String>> resultData = new HashMap<Integer, List<String>>();

		try {
			statement = this.connection.createStatement();
			ResultSet resultSet = statement.executeQuery(query);
			ResultSetMetaData rsMetaData = resultSet.getMetaData();
			int numberOfColumns = rsMetaData.getColumnCount();

			int rowId = 1;

			while (resultSet.next()) {
				List<String> rowData = new ArrayList<String>();

				for (int i = 1; i <= numberOfColumns; i++) {
					rowData.add(resultSet.getString(i));
				}
				resultData.put(rowId, rowData);
				rowId++;
			}

			this.connection.commit();
			statement.close();
			return resultData;

		} catch (Exception e) {
			closeStatement(statement);
			e.printStackTrace();
			rollback();
			throw new RuntimeException("Not able to Execute Query", e);
		}

	}

	public Connection getConnection() {
		return this.connection;
	}

}
