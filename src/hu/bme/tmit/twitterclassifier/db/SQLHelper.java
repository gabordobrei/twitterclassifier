package hu.bme.tmit.twitterclassifier.db;

import hu.bme.tmit.twitterclassifier.logger.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import twitter4j.TweetEntity;

public class SQLHelper {
	private static Logger log = new Logger(SQLHelper.class);

	private Connection conn = null;

	public SQLHelper() {
		try {

			conn = DatabaseConnector.getConnection();

		} catch (Exception e) {
			log.e(e.getMessage());
		}
	}

	public void insertTweet(TweetEntity tweet) {

		PreparedStatement statement = null;
		ResultSet resultado = null;
		String query = "SELECT * FROM empleados";

		try {
			statement = conn.prepareStatement(query);
			resultado = statement.executeQuery();

			while (resultado.next()) {
				System.out.println(resultado.getString(1) + "\t"
						+ resultado.getString(2) + "\t"
						+ resultado.getString(3) + "\t");
			}

		} catch (SQLException e) {
			log.e(e.getMessage());
		}

	}

	public void test() {
		PreparedStatement statement;
		try {
			statement = conn
					.prepareStatement("INSERT INTO `hashtags`(`tag`) VALUES (\"asdf\")");

			statement.execute();
		} catch (SQLException e) {
			log.e(e.getMessage());
		}

		statement = null;
		ResultSet resultado = null;
		String query = "SELECT * FROM hashtags";

		try {

			statement = conn.prepareStatement(query);
			resultado = statement.executeQuery();

			while (resultado.next()) {
				System.out.println(resultado.getString(1) + "\t"
						+ resultado.getString(2) + "\t");
			}
		} catch (Exception e) {
			log.e(e.getMessage());
		}

	}

}
