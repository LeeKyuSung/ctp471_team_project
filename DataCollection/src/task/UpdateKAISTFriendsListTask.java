package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import db.User;

public class UpdateKAISTFriendsListTask {
	private void updateKAISTFriendsList(String userID, String friendsListStr) {
		System.out.println("[updateKAISTFriendsList][" + userID + "][START]");

		String[] friendsList = friendsListStr.split("\\|");

		int totalFriendsNum = friendsList.length;
		int checkedFriendsNum = 0;
		int KAISTFriendsNum = 0;

		String KAISTFriendsListStr = "";

		for (int i = 0; i < friendsList.length; i++) {
			int seq = Integer.parseInt(friendsList[i]);
			HashMap<String, String> status = User.getInstance().getStatusBySeq(seq);

			if ("Y".equals(status.get("isUserInfoUpdated"))) {
				checkedFriendsNum++;

				if ("Y".equals(status.get("isKAIST"))) {
					KAISTFriendsListStr += "|" + seq;
					KAISTFriendsNum++;
				} else {
					continue;
				}
			} else if ("N".equals(status.get("isUserInfoUpdated"))) {
				continue;
			} else { // INVALID case
				checkedFriendsNum++;
			}
		}

		if (!"".equals(KAISTFriendsListStr))
			KAISTFriendsListStr = KAISTFriendsListStr.substring(1);

		double checkedFriendsPercentage = ((double) checkedFriendsNum) * 100.0 / ((double) totalFriendsNum);
		
		User.getInstance().updateKAISTFriendsList(userID, KAISTFriendsListStr, KAISTFriendsNum, checkedFriendsPercentage);

		System.out.println("[updateKAISTFriendsList][" + userID + "][END]");
	}

	public int updateKAISTFriendsList(int limit) {
		System.out.println("[updateKAISTFriendsList][limit : " + limit + "][START]");

		HashMap<String, String> targetUserMap = User.getInstance().getUserToUpdateKAISTFriends(limit);
		for (String userID : targetUserMap.keySet()) {
			User.getInstance().updateCheckedFriendsPercentage(userID, 100.0);
		}

		for (String userID : targetUserMap.keySet()) {
			updateKAISTFriendsList(userID, targetUserMap.get(userID));
		}

		System.out.println("[updateKAISTFriendsList][limit : " + limit + "][END]");

		return targetUserMap.size();
	}

	public static void main(String[] args) {
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String date = dayTime.format(new Date());
		System.out.println("UpdateKAISTFriendsListTask Start : " + date);

		// step 1 : find users to update and get FriendsList from db (isFriendsCollected=Y, lower CheckedFriendsPercentage)
		// step 2 : update CheckedFriendsPercentage = 100
		// step 3 : check all of friends (friends are userInfoUpdated?, friends are KAIST?)
		// step 4 : save KAISTfreindsList, KAISTFriendsNum, CheckedFriendsPercentage

		UpdateKAISTFriendsListTask task = new UpdateKAISTFriendsListTask();
		int updatedCnt = task.updateKAISTFriendsList(3);

		System.out.println(updatedCnt + " users updated. UpdateKAISTFriendsListTask end.");
	}
}
