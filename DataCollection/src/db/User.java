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
	private final String DB_URL = "jdbc:mysql://" + Config.DB_HOST + "/" + Config.DB_DBNAME + "?serverTimezone=UTC";

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

	public HashSet<String> getUserToSearch(int num) {

		HashSet<String> userSet = null;

		try {
			String query = "SELECT UserID FROM USER WHERE Valid=\"Y\" ORDER BY SearchCnt ASC LIMIT " + num + ";";
			ResultSet rs = state.executeQuery(query);

			userSet = new HashSet<String>();
			while (rs.next()) {
				String userID = rs.getString("UserID");
				userSet.add(userID);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getUserToSearch] " + e.getMessage());
			e.printStackTrace();
		}

		return userSet;
	}

	public void plusSearchCnt(String userID) {

		try {
			String query = "SELECT SearchCnt FROM USER WHERE UserID=\"" + userID + "\";";
			ResultSet rs = state.executeQuery(query);

			if (!rs.next()) {
				System.out.println("[ERROR][User][plusSearchCnt] No userID found.");
				return;
			}

			int searchCnt = rs.getInt("SearchCnt");

			String updateQuery = "UPDATE USER SET SearchCnt=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
				preparedStatement.setInt(1, searchCnt + 1);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][plusSearchCnt] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void insertOrUpdateUser(String userID, String userInfo) {
		try {
			String sql = "INSERT INTO USER (`UserID`, `UserInfo`) VALUES ((?), (?)) ON DUPLICATE KEY UPDATE `UserID`=(?), `Userinfo`=(?);";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, userID);
				preparedStatement.setString(2, userInfo);
				preparedStatement.setString(3, userID);
				preparedStatement.setString(4, userInfo);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
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
}
