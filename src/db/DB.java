package db;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DB {
	
	private static Connection connection = null;
	

	//load properties from database.
	private static Properties loadProperties(){
		try (FileInputStream fs = new FileInputStream("db.properties")){
			Properties props = new Properties();
			props.load(fs);
			return props;
		}catch(IOException e) {
			throw new DbExceptions(e.getMessage());
		}
	}
	
	//start database connection
	public static Connection getConnection() {
		if(connection == null) {
			try {
				Properties props = loadProperties();
				String url = props.getProperty("dburl");
				connection = DriverManager.getConnection(url, props);				
			}catch(SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
		return connection;
	}
	
	//close database connection
	public static void closeConnection() {
		if (connection != null) {
			try {
				connection.close();
			}catch(SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
	}
	
	public static void closeStatement(Statement statement) {
		if(statement != null) {
			try {
				statement.close();
			} catch (SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
	}
	
	public static void closeResultSet(ResultSet result) {
		if(result != null) {
			try {
				result.close();
			} catch (SQLException e) {
				throw new DbExceptions(e.getMessage());
			}
		}
	}
	
}
