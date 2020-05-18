package task;

import crawling_deprecated.Crawling;
import db.User;

public class UpdateUserInfoTask {
	private Crawling crawling = null;

	public UpdateUserInfoTask() {
		crawling = new Crawling();
	}

	public void updateUserInfo(String userID) {
		String userInfo = crawling.getUserInfo(userID);

		User.getInstance().insertOrUpdateUser(userID, userInfo);
	}

	public void updateUserInfo() {
		// TODO
	}

	public static void main(String[] args) {
		UpdateUserInfoTask task = new UpdateUserInfoTask();

		// case 1. update user information to db
		// task.updateUserInfo("ks5050577");

		// case 2. update all user information to db
		// task.updateUserInfo();
	}
}
