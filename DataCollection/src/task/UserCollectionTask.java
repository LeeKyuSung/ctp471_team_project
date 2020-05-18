package task;

import java.util.HashSet;

import crawling.Crawling;
import db.User;

public class UserCollectionTask {
	private Crawling crawling = null;

	public UserCollectionTask() {
		crawling = new Crawling();
	}

	public void findAndInsertFriends(String userID) {
		String[] friend = crawling.getFriendsList(userID);

		User.getInstance().plusSearchCnt(userID);
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
		UserCollectionTask task = new UserCollectionTask();

		// case 1. find friends of one user
		// task.findAndInsertFriends("ks5050577");

		// case 2. find users to search from db and find friends of them
		task.selectUserFromDBAndInsertFriends(3);
	}
}
