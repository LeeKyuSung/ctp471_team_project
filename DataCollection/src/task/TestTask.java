package task;

public class TestTask {
	public static void main(String[] args) {
		UserCollectionTask task1 = new UserCollectionTask();
		UpdateUserInfoTask task2 = new UpdateUserInfoTask();

		// case 1. find friends of one user
		task1.findAndInsertFriends("ks5050577");

		// case 2. find users to search from db and find friends of them
		// task1.selectUserFromDBAndInsertFriends(3);

		// case 3. update user information to db
		// task2.updateUserInfo("ks5050577");

		// case 4. update all user information to db
		// task2.updateUserInfo();
	}
}
