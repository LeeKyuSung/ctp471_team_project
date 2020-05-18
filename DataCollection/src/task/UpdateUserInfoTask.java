package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;

import conf.Config;
import crawling.Crawling;
import db.User;

public class UpdateUserInfoTask {
	public void updateUserInfo(String userID) {
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

			if (isKAIST)
				break;
		}

		if (isKAIST)
			User.getInstance().updateKAISTUserInfo(userID, userInfoStr.substring(1));
		else
			User.getInstance().updateNonKAISTUserInfo(userID);
	}

	public int updateUserInfo(int limit) {
		HashSet<String> targetUserSet = User.getInstance().getUserToUpdateUserInfo(limit);

		for (String targetUser : targetUserSet) {
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			System.out.println("[TARGET][" + targetUser + "]");
			updateUserInfo(targetUser);
		}

		return targetUserSet.size();
	}

	public static void main(String[] args) {
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String date = dayTime.format(new Date());
		System.out.println("UpdateUserInfoTask Task Start : " + date);

		// step 1 : find users to update (isUserInfoUpdate=N)
		// step 2 : crawl userInformation
		// step 3 (if KAIST) : change isUserInfoUpdate=Y, isKAIST=Y, update UserInfo to db
		// step 3 (if not KAIST) : change isUserInfoUpdate=Y

		UpdateUserInfoTask task = new UpdateUserInfoTask();
		int updatedCnt = task.updateUserInfo(3);

		System.out.println(updatedCnt + " users updated. UpdateUserInfoTask end.");

		if (updatedCnt != 0)
			Crawling.getInstance().close();
	}
}
