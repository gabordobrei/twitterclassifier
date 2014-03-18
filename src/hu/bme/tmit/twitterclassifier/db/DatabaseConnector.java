package hu.bme.tmit.twitterclassifier.db;

import hu.bme.tmit.twitterclassifier.logger.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.google.common.base.Joiner;

public class DatabaseConnector {

	private static Logger log = new Logger(DatabaseConnector.class);

	public static Connection getConnection() throws Exception {

		final String url = "jdbc:mysql://152.66.244.83:3306/";
		final String dbName = "tehazi";
		final String driver = "com.mysql.jdbc.Driver";
		final String userName = "javaclient";
		final String password = "javaclient";

		log.d(Joiner.on(", ").join(url + dbName, userName, password));

		Class.forName(driver).newInstance();
		Connection conn = DriverManager.getConnection(url + dbName, userName,
				password);

		return conn;
	}

	public static void closeConnection(Connection conn) {

		try {
			if (!conn.isClosed() && conn != null) {
				conn.close();
			}

		} catch (SQLException e) {
			log.e(e.getMessage());
		}

	}
}
