package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
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
			String query = "SELECT UserID FROM USER WHERE isCollege=\"Y\" AND isFriendsCollected=\"N\" ORDER BY rand() LIMIT " + limit + ";";
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

		int insertedCnt = 0;
		for (int i = 0; i < user.length; i++) {
			try {
				String query = "SELECT UserID FROM USER WHERE UserID=\"" + user[i] + "\";";
				ResultSet rs = state.executeQuery(query);
				if (rs.next()) {
					// if exist continue
					continue;
				}

				String sql = "INSERT INTO USER (`UserID`) VALUES (?);";
				try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
					preparedStatement.setString(1, user[i]);

					preparedStatement.executeUpdate();
				}

				insertedCnt++;

			} catch (Exception e) {
				System.out.println("[ERROR][User][insertUsers][" + i + "][" + user[i] + "] " + e.getMessage());
				e.printStackTrace();
			}
		}

		System.out.println("[User][inserUsers] " + insertedCnt + " inserted from " + user.length + " input.");
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

			String updateQuery = "UPDATE USER SET FriendsList=?, isFriendsCollected=\"Y\", FriendsCnt=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, friendsListStr);
				preparedStatement.setInt(2, friend.length);
				preparedStatement.setString(3, userID);

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
			String query = "SELECT UserID FROM USER WHERE isUserInfoUpdated=\"N\" ORDER BY rand() LIMIT " + limit + ";";
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

	public void updateUserInfo(String userID, String isCollege, String userInfoStr) {
		try {
			String sql = "UPDATE USER SET isUserInfoUpdated=?, isCollege=?, UserInfo=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, "Y");
				preparedStatement.setString(2, isCollege);
				preparedStatement.setString(3, userInfoStr);
				preparedStatement.setString(4, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateUserInfo] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getUserToUpdateKAISTFriends(int limit) {
		HashMap<String, String> userMap = null;

		try {
			String query = "SELECT UserID, FriendsList FROM USER WHERE isFriendsCollected=\"Y\" AND FriendsList IS NOT NULL ORDER BY CheckedFriendsPercentage ASC LIMIT " + limit + ";";
			ResultSet rs = state.executeQuery(query);

			userMap = new HashMap<String, String>();
			while (rs.next()) {
				String userID = rs.getString("UserID");
				String friendsList = rs.getString("FriendsList");
				userMap.put(userID, friendsList);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getUserToUpdateKAISTFriends] " + e.getMessage());
			e.printStackTrace();
		}

		return userMap;
	}

	public void updateCheckedFriendsPercentage(String userID, Double checkedFriendsPercentage) {
		try {
			String sql = "UPDATE USER SET CheckedFriendsPercentage=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setDouble(1, checkedFriendsPercentage);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateNonKAISTUserInfo] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getStatusBySeq(int seq) {
		HashMap<String, String> result = null;

		try {
			String query = "SELECT UserID, isFriendsCollected, isUserInfoUpdated, isKAIST FROM USER WHERE Seq=" + seq + ";";
			ResultSet rs = state.executeQuery(query);

			if (rs.next()) {
				result = new HashMap<String, String>();
				result.put("userID", rs.getString("UserID"));
				result.put("isFriendsCollected", rs.getString("isFriendsCollected"));
				result.put("isUserInfoUpdated", rs.getString("isUserInfoUpdated"));
				result.put("isKAIST", rs.getString("isKAIST"));
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getStatusBySeq] " + e.getMessage());
			e.printStackTrace();
		}

		return result;
	}
	
	public void updateKAISTFriendsList(String userID, String KAISTFriendsListStr, int KAISTFriendsNum, double checkedFriendsPercentage) {
		try {
			String sql = "UPDATE USER SET KAISTFriendsList=?, KAISTFriendsNum=?, CheckedFriendsPercentage=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, KAISTFriendsListStr);
				preparedStatement.setInt(2, KAISTFriendsNum);
				preparedStatement.setDouble(3, checkedFriendsPercentage);
				preparedStatement.setString(4, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][updateKAISTFriendsList] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getCollegeName() {
		HashMap<String, String> retMap = null;

		try {
			String query = "SELECT CollegeName, Exception from COLLEGE_NAME;";
			ResultSet rs = state.executeQuery(query);

			retMap = new HashMap<String, String>();
			while (rs.next()) {
				String collegeName = rs.getString("CollegeName");
				String exception = rs.getString("Exception");
				retMap.put(collegeName, exception);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getCollegeName] " + e.getMessage());
			e.printStackTrace();
		}

		return retMap;
	}

	public HashMap<String, String> getCollegeUser() {
		HashMap<String, String> retMap = null;

		try {
			String query = "SELECT UserID, UserInfo from USER where isCollege=\"Y\" and CollegeName is NULL;";
			ResultSet rs = state.executeQuery(query);

			retMap = new HashMap<String, String>();
			while (rs.next()) {
				String UserID = rs.getString("UserID");
				String UserInfo = rs.getString("UserInfo");
				retMap.put(UserID, UserInfo);
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][getCollegeUser] " + e.getMessage());
			e.printStackTrace();
		}

		return retMap;
	}
	
	public void setCollege(String userID, String college) {
		try {
			String sql = "UPDATE USER SET CollegeName=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
				preparedStatement.setString(1, college);
				preparedStatement.setString(2,  userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("[ERROR][User][setCollege] " + e.getMessage());
			e.printStackTrace();
		}
	}
}
