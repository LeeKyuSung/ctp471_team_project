package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

import conf.Config;

public class InvalidID {
	private static InvalidID instance = new InvalidID();

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://" + Config.DB_HOST + "/" + Config.DB_DBNAME + "?serverTimezone=UTC";

	private Connection conn = null;
	private Statement state = null;

	private InvalidID() {
		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, Config.DB_USER_NAME, Config.DB_PASSWORD);
			state = conn.createStatement();
		} catch (Exception e) {
			System.out.println("[ERROR][User] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static InvalidID getInstance() {
		return instance;
	}

	public HashSet<String> getInvalidIDSet() {

		HashSet<String> idSet = null;

		try {
			String query = "SELECT UserID from INVALID_ID;";
			ResultSet rs = state.executeQuery(query);

			idSet = new HashSet<String>();
			while (rs.next()) {
				String userID = rs.getString("UserID");
				idSet.add(userID);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][InvalidID][getInvalidIDSet] " + e.getMessage());
			e.printStackTrace();
		}

		return idSet;
	}

	public boolean insertInvalidID(String id) {
		// TODO
		return true;
	}
}
