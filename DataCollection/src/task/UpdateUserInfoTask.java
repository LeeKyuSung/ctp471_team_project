package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import conf.Config;
import crawling.Crawling;
import db.User;

public class UpdateUserInfoTask {
	public void updateUserInfo(String userID) {
		System.out.println("[updateUserInfo][" + userID + "][START]");

		String[] userInfo = Crawling.getInstance().getUserInfo(userID);
		String userInfoStr = "";

		// check KAIST
		boolean isKAIST = false;
		for (int i = 0; i < userInfo.length; i++) {

			userInfoStr += "|" + userInfo[i];

			for (int j = 0; j < Config.KAIST_NAME.length; j++) {
				if (userInfo[i].contains(Config.KAIST_NAME[j]) && !userInfo[i].contains(Config.KAIST_NAME_EXCEPT)) {
					isKAIST = true;
					break;
				}
			}

			if (isKAIST) {
				User.getInstance().updateKAISTUserInfo(userID, userInfoStr.substring(1));
				break;
			}
		}

		System.out.println("[updateUserInfo][" + userID + "][END]");
	}

	public int updateUserInfo(int limit) {
		System.out.println("[updateUserInfo][limit : " + limit + "][START]");

		HashSet<String> targetUserSet = User.getInstance().getUserToUpdateUserInfo(limit);
		for (String targetUser : targetUserSet) {
			User.getInstance().updateIsUserUpdated(targetUser, "Y");
		}

		for (String targetUser : targetUserSet) {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			updateUserInfo(targetUser);
		}

		System.out.println("[updateUserInfo][limit : " + limit + "][END]");
		return targetUserSet.size();
	}

	public static void main(String[] args) {
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String date = dayTime.format(new Date());
		System.out.println("UpdateUserInfoTask Start : " + date);

		// step 1 : find users to update (isUserInfoUpdate=N)
		// step 2 : change isUserInfoUpdate=Y
		// step 3 : crawl userInformation
		// step 4 (if KAIST) : change isUserInfoUpdate=Y, isKAIST=Y, update UserInfo to db

		UpdateUserInfoTask task = new UpdateUserInfoTask();
		int updatedCnt = task.updateUserInfo(10);

		System.out.println(updatedCnt + " users updated. UpdateUserInfoTask end.");

		if (updatedCnt != 0)
			Crawling.getInstance().close();
	}
}
