package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;

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

	public void addFriendsList(String userID, String[] friend) {
		// find friends by name at DB and insert into friendsList field with seq
		try {

			String selectQuery = "SELECT FriendsList FROM USER WHERE UserID=\"" + userID + "\";";
			ResultSet rs = state.executeQuery(selectQuery);
			if (!rs.next()) {
				System.out.println("[ERROR][DB][addFriendsList] No userID found.");
				return;
			}
			String friendsList = rs.getString("FriendsList");

			for (int i = 0; i < friend.length; i++) {

				selectQuery = "SELECT Seq FROM USER WHERE UserID=\"" + friend[i] + "\";";
				rs = state.executeQuery(selectQuery);
				if (!rs.next())
					continue;
				friendsList += "|" + rs.getString("Seq");
			}
			if ('|' == friendsList.charAt(0))
				friendsList = friendsList.substring(1);

			String updateQuery = "UPDATE USER SET FriendsList=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, friendsList);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public HashSet<String> getUserToCollectFriends(int limit) {
		HashSet<String> userSet = null;

		try {
			String query = "SELECT UserID FROM USER WHERE isKAIST=\"Y\" AND isFriendsCollected=\"N\" LIMIT " + limit + ";";
			ResultSet rs = state.executeQuery(query);

			userSet = new HashSet<String>();
			while (rs.next()) {
				String userID = rs.getString("UserID");
				userSet.add(userID);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getUserToCollectFriends] " + e.getMessage());
			e.printStackTrace();
		}

		return userSet;
	}

	public void updateIsFriendsCollected(String userID, String isFriendsCollected) {
		try {
			String sql = "UPDATE USER SET isFriendsCollected=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, isFriendsCollected);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateIsFriendsCollected] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void insertUsers(String[] user) {
		if (user == null || user.length == 0) {
			System.out.println("[ERROR][User][insertUsers] input user array is null or 0");
			return;
		}

		for (int i = 0; i < user.length; i++) {
			try {
				String sql = "INSERT INTO USER (`UserID`) VALUES (?);";
				try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
					preparedStatement.setString(1, user[i]);

					preparedStatement.executeUpdate();
				}
			} catch (Exception e) {
				System.out.println("[ERROR][User][insertUsers][" + i + "][" + user[i] + "] " + e.getMessage());
				e.printStackTrace();
			}
		}
	}

	public void updateFriendsList(String userID, String[] friend) {
		if (friend == null) {
			System.out.println("[ERROR][User][updateFriendsList] input friend array is null or 0");
			return;
		}
		
		try {
			// make friendsListStr : seq is combine by |
			String friendsListStr = "";
			if (friend != null && friend.length != 0) {
				String selectQuery;
				ResultSet rs;

				for (int i = 0; i < friend.length; i++) {
					selectQuery = "SELECT Seq FROM USER WHERE UserID=\"" + friend[i] + "\";";
					rs = state.executeQuery(selectQuery);
					if (!rs.next())
						continue;
					friendsListStr += "|" + rs.getString("Seq");
				}
				friendsListStr = friendsListStr.substring(1);
			}

			String updateQuery = "UPDATE USER SET FriendsList=?, isFriendsCollected=\"Y\" WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, friendsListStr);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateFriendsList] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public HashSet<String> getUserToUpdateUserInfo(int limit) {
		HashSet<String> userSet = null;

		try {
			String query = "SELECT UserID FROM USER WHERE isUserInfoUpdated=\"N\" LIMIT " + limit + ";";
			ResultSet rs = state.executeQuery(query);

			userSet = new HashSet<String>();
			while (rs.next()) {
				String userID = rs.getString("UserID");
				userSet.add(userID);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getUserToUpdateUserInfo] " + e.getMessage());
			e.printStackTrace();
		}

		return userSet;
	}

	public void updateIsUserUpdated(String userID, String isUserInfoUpdated) {
		try {
			String sql = "UPDATE USER SET isUserInfoUpdated=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, isUserInfoUpdated);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateNonKAISTUserInfo] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void updateKAISTUserInfo(String userID, String userInfoStr) {
		try {
			String sql = "UPDATE USER SET isUserInfoUpdated=?, isKAIST=?, UserInfo=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, "Y");
				preparedStatement.setString(2, "Y");
				preparedStatement.setString(3, userInfoStr);
				preparedStatement.setString(4, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateKAISTUserInfo] " + e.getMessage());
			e.printStackTrace();
		}
	}
}
