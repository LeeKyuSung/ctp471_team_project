package task;

import crawling.Crawling;
import db.User;

public class DataCollectionTask {

	private Crawling crawling = null;

	public DataCollectionTask() {
		crawling = new Crawling();
	}

	public void findAndInsertFriends(String userID) {
		crawling.getFriendsList(userID);
		
		db.insertOrUpdateUser(userID, crawling.getUserInfo(userID));
		db.plusCnt(userID);
		String[] friend = crawling.getFriendsList(userID);
		db.addFriendsList(userID, friend);
		
		for (int i = 0; i < friend.length; i++) {
			System.out.println(i + " : " + friend[i]);
			
			db.insertOrUpdateUser(friend[i], crawling.getUserInfo(friend[i]));
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
		DataCollectionTask task = new DataCollectionTask();
		//task.findAndInsertFriends("ks5050577");
		task.selectUserFromDBAndInsertFriends(31);
	}
}
