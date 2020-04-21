package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Iterator;

import conf.Config;

public class DB {

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://" + Config.DB_HOST + "/" + Config.DB_DBNAME + "?serverTimezone=UTC";

	private Connection conn = null;
	private Statement state = null;

	public DB() {

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, Config.DB_USER_NAME, Config.DB_PASSWORD);
			System.out.println("[DB][START]\n");
			state = conn.createStatement();
		} catch (Exception e) {
			System.out.println("[ERROR][DB] " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void addFriendsList(String userID, String[] friend) {
		// find friends by name at DB and insert into friendsList field with seq
		try {

			String selectQuery = "SELECT FriendsList FROM User WHERE UserID=\"" + userID + "\";";
			ResultSet rs = state.executeQuery(selectQuery);
			if (!rs.next()) {
				System.out.println("[ERROR][DB][addFriendsList] No userID found.");
				return;
			}
			String friendsList = rs.getString("FriendsList");

			for (int i = 0; i < friend.length; i++) {

				selectQuery = "SELECT Seq FROM User WHERE UserID=\"" + friend[i] + "\";";
				rs = state.executeQuery(selectQuery);
				if (!rs.next())
					continue;
				friendsList += "|" + rs.getString("Seq");
			}
			if ('|' == friendsList.charAt(0))
				friendsList = friendsList.substring(1);

			String updateQuery = "UPDATE User SET FriendsList=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
				preparedStatement.setString(1, friendsList);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void insertOrUpdateUser(String userID, String userInfo) {
		try {
			String sql = "INSERT INTO User (`UserID`, `UserInfo`) VALUES ((?), (?)) ON DUPLICATE KEY UPDATE `UserID`=(?), `Userinfo`=(?);";
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

	public void plusCnt(String userID) {
		try {
			String selectQuery = "SELECT SearchCnt FROM User WHERE UserID=\"" + userID + "\";";
			ResultSet rs = state.executeQuery(selectQuery);
			if (!rs.next()) {
				System.out.println("[ERROR][DB][addFriendsList] No userID found.");
				return;
			}
			int searchCnt = rs.getInt("SearchCnt");

			String updateQuery = "UPDATE User SET SearchCnt=? WHERE UserID=?;";
			try (PreparedStatement preparedStatement = conn.prepareStatement(updateQuery)) {
				preparedStatement.setInt(1, searchCnt+1);
				preparedStatement.setString(2, userID);

				preparedStatement.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public String[] findUser(int limit) {
		try {
			HashSet<String> userSet = new HashSet<String>();

			String selectQuery = "SELECT UserID FROM User WHERE Valid=\"Y\" ORDER BY SearchCnt ASC LIMIT " + limit
					+ ";";
			ResultSet rs = state.executeQuery(selectQuery);
			while (rs.next()) {
				String userID = rs.getString("UserID");
				userSet.add(userID);
			}

			String[] ret = new String[userSet.size()];
			int cnt = 0;
			Iterator<String> iter = userSet.iterator();
			while (iter.hasNext()) {
				ret[cnt++] = iter.next();
			}
			return ret;
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		return null;
	}
}
