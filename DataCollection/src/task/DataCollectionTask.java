package task;

import java.util.HashSet;
import java.util.Iterator;

import crawling.Crawling;
import db.User;

public class DataCollectionTask {

	private Crawling crawling = null;

	public DataCollectionTask() {
		crawling = new Crawling();
	}

	public void findAndInsertFriends(String userID) {
		String[] friend = crawling.getFriendsList(userID);
		String userInfo = crawling.getUserInfo(userID);
		
		User.getInstance().plusSearchCnt(userID);
		User.getInstance().insertOrUpdateUser(userID, userInfo);
		User.getInstance().addFriendsList(userID, friend);
		
		for (int i = 0; i < friend.length; i++) {
			System.out.println(i + " : " + friend[i]);

			User.getInstance().insertOrUpdateUser(friend[i], crawling.getUserInfo(friend[i]));
		}
	}

	public void selectUserFromDBAndInsertFriends(int limit) {
		HashSet<String> targetUserSet = User.getInstance().getUserToSearch(limit);
		for (String targetUser : targetUserSet) {
			System.out.println("[TARGET][" + targetUser + "]");
			findAndInsertFriends(targetUser);
		}
	}

	public static void main(String[] args) {
		DataCollectionTask task = new DataCollectionTask();
		//task.findAndInsertFriends("ks5050577");
		task.selectUserFromDBAndInsertFriends(3);
	}
}
