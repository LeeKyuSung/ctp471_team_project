package task;

import crawling.Crawling;
import db.DB;

public class Main {

	private DB db = null;
	private Crawling crawling = null;

	public Main() {
		db = new DB();
		crawling = new Crawling();
	}

	public void findAndInsertFriends(String userID) {
		db.insertOrUpdateUser(userID, crawling.getUserInfo(userID));
		db.plusCnt(userID);
		String[] friend = crawling.getFriendsList(userID);
		db.addFriendsList(userID, friend);
		
		for (int i = 0; i < friend.length; i++) {
			System.out.println(i + " : " + friend[i]);
			
			db.insertOrUpdateUser(friend[i], crawling.getUserInfo(friend[i]));

			try {
				Thread.sleep(1);
			} catch (Exception e) {
				System.out.println("[ERROR][Main][findAndInsertFriends] " + e.getMessage());
				e.printStackTrace();
			}
		}
	}
	public void selectUserFromDBAndInsertFriends(int limit) {
		String[] targetUser = db.findUser(limit);
		for (int i=0; i<targetUser.length; i++) {
			System.out.println("[TARGET][" + targetUser[i] + "]");
			findAndInsertFriends(targetUser[i]);
		}
	}

	public static void main(String[] args) {
		Main m = new Main();
		//m.findAndInsertFriends("ks5050577");
		m.selectUserFromDBAndInsertFriends(31);
	}
}
