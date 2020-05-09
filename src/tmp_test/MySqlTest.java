package tmp_test;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class MySqlTest {

	private final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://192.168.0.7/TestDB?serverTimezone=UTC";

	private final String USER_NAME = "kslksks";
	private final String PASSWORD = "ichon514!";

	public MySqlTest() {
		Connection conn = null;
		Statement state = null;

		try {
			Class.forName(JDBC_DRIVER);
			conn = DriverManager.getConnection(DB_URL, USER_NAME, PASSWORD);
			System.out.println("[ MySQL Connection ]\n");
			state = conn.createStatement();

			String sql;
			sql = "SELECT * FROM User";
			ResultSet rs = state.executeQuery(sql);

			while (rs.next()) {
				String seq = rs.getString("SEQ");
				String name = rs.getString("USERNAME");
				String list = rs.getString("FRIENDSLIST");

				System.out.println(seq + " : " + name + " : " + list);
			}

			rs.close();
			state.close();
			conn.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		} finally {

		}

		System.out.println("Mysql Close");
	}

	public static void main(String[] args) {

		MySqlTest test = new MySqlTest();
	}
}
