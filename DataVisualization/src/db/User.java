package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;

import conf.Config;

public class User {
	private static User instance = new User();

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://" + Config.DB_HOST + "/" + Config.DB_DBNAME + "?serverTimezone=UTC&characterEncoding=utf8";

	private Connection conn = null;
	private Statement state = null;

	private User() {
		try {
			Class.forName(JDBC_DRIVER);
			System.out.println(DB_URL);
			conn = DriverManager.getConnection(DB_URL, Config.DB_USER_NAME, Config.DB_PASSWORD);
			state = conn.createStatement();
		} catch (Exception e) {
			System.out.println("[ERROR][User] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public static User getInstance() {
		return instance;
	}

	public HashMap<Integer, String> getUserMap() {

		HashMap<Integer, String> userMap = null;

		try {
			String query = "SELECT Seq, KAISTFriendsList FROM USER WHERE isFriendsCollected=\"Y\" AND KAISTFriendsNum IS NOT NULL;";
			ResultSet rs = state.executeQuery(query);

			userMap = new HashMap<Integer, String>();
			while (rs.next()) {
				int seq = rs.getInt("Seq");
				String friendsList = rs.getString("KAISTFriendsList");

				userMap.put(seq, friendsList);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getUserList] " + e.getMessage());
			e.printStackTrace();
		}

		return userMap;
	}

	public HashMap<Integer, String> getAllUserMap() {

		HashMap<Integer, String> userMap = null;

		try {
			String query = "SELECT Seq, FriendsList FROM USER WHERE isFriendsCollected=\"Y\";";
			ResultSet rs = state.executeQuery(query);

			userMap = new HashMap<Integer, String>();
			while (rs.next()) {
				int seq = rs.getInt("Seq");
				String friendsList = rs.getString("FriendsList");

				userMap.put(seq, friendsList);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getUserList] " + e.getMessage());
			e.printStackTrace();
		}

		return userMap;
	}

}
