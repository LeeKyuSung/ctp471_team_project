package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import crawling.Crawling;
import db.User;

public class CollectFriendsTask {
	public void collectFriends(String userID) {
		System.out.println("[collectFriends][" + userID + "][START]");

		String[] friend = Crawling.getInstance().getFriendsList(userID);
		
		if (friend == null) {
			User.getInstance().updateIsFriendsCollected(userID, "INVALID");
		} else {
			User.getInstance().insertUsers(friend);
			User.getInstance().updateFriendsList(userID, friend);
		}

		System.out.println("[collectFriends][" + userID + "][END]");
	}

	public int collectFriends(int limit) {
		System.out.println("[collectFriends][limit : " + limit + "][START]");

		HashSet<String> targetUserSet = User.getInstance().getUserToCollectFriends(limit);
		for (String targetUser : targetUserSet) {
			User.getInstance().updateIsFriendsCollected(targetUser, "Y");
		}

		for (String targetUser : targetUserSet) {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			collectFriends(targetUser);
		}

		System.out.println("[collectFriends][limit : " + limit + "][END]");
		return targetUserSet.size();
	}

	public static void main(String[] args) {
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String date = dayTime.format(new Date());
		System.out.println("CollectFriendsTask Start : " + date);

		// step 1 : find users to search from db (isKAIST=Y, isFriendsCollected=N)
		// step 2 : change isFriendsCollected=Y
		// for each users {
		// step 3 : find friends by crawling
		// if friends collected {
		// step 4 : insert all friends to db
		// step 5 : update friends list
		// } else {
		// step 4 : change isFriendsCollected=INVALID
		// }
		// }

		CollectFriendsTask task = new CollectFriendsTask();
		int collectedCnt = task.collectFriends(3);

		System.out.println(collectedCnt + " users' friends collected. CollectFriendsTask end.");

		if (collectedCnt != 0)
			Crawling.getInstance().close();
	}
}
