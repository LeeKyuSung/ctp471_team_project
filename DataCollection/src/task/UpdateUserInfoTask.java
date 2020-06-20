package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;

import conf.Config;
import crawling.Crawling;
import db.User;

public class UpdateUserInfoTask {
	public void updateUserInfo(String userID) {
		System.out.println("[updateUserInfo][" + userID + "][START]");

		String[] userInfo = Crawling.getInstance().getUserInfo(userID);

		if (userInfo == null) {
			System.out.println("fail!");
			return;
		}

		// check college student and make userInfoStr
		boolean isCollege = false;
		String userInfoStr = "";
		for (int i = 0; i < userInfo.length; i++) {
			if (userInfo[i].trim().equals(""))
				continue;

			userInfoStr += "|" + userInfo[i];

			HashMap<String, String> collegeName = User.getInstance().getCollegeName();
			for (String college : collegeName.keySet()) {
				if (userInfo[i].contains(college)) {
					boolean isExcept = false;
					String[] exception = collegeName.get(college).split(",");
					for (int j = 0; j < exception.length; j++) {
						if (userInfo[i].contains(exception[j])) {
							isExcept = true;
							break;
						}
					}

					if (!isExcept) {
						isCollege = true;
						break;
					}
				}
			}
		}
		if (userInfo.length != 0)
			userInfoStr = userInfoStr.substring(1);

		if (isCollege) {
			User.getInstance().updateUserInfo(userID, "Y", userInfoStr);
		} else {
			User.getInstance().updateUserInfo(userID, "N", userInfoStr);
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
		int updatedCnt = task.updateUserInfo(20);

		System.out.println(updatedCnt + " users updated. UpdateUserInfoTask end.");

		if (updatedCnt != 0)
			Crawling.getInstance().close();
	}
}
