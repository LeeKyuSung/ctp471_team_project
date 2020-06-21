package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import db.User;

public class CollectCollegeNum {
	public static void main(String[] args) {
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String date = dayTime.format(new Date());
		System.out.println("CollectCollegeNum Start : " + date);

		HashMap<String, String> tmp = User.getInstance().getCollegeUser();

		for (String userID : tmp.keySet()) {

			System.out.println(userID + " : " + tmp.get(userID));
			String userInfo = tmp.get(userID);

			String college = "";
			
			String[] info = userInfo.split("\\|");
			for (int i = 0; i < info.length; i++) {
				
				if (info[i].contains("KAIST") || info[i].contains("한국과학기술원") || info[i].contains("카이스트")) {
					if (! (info[i].contains("한국과학영재학교") || info[i].contains("Korea Science Academy"))) {
						college = "KAIST";
						break;
					}
				} else if (info[i].contains("서울대학교") || info[i].contains("Seoul National University") || info[i].contains("SNU")) {
					college = "Seoul";
					break;
				} else if (info[i].contains("서강대")) {
					college = "서강대";
					break;
				} else if (info[i].contains("부산대")) {
					college = "부산대";
					break;
				} else if (info[i].contains("명지대")) {
					college = "명지대";
					break;
				} else if (info[i].contains("단국대")) {
					college = "단국대";
					break;
				} else if (info[i].contains("고려대")) {
					college = "Korea University";
					break;
				} else if (info[i].contains("이화여")) {
					college = "Ewha University";
					break;
				} else if (info[i].contains("한양대")) {
					college = "한양대";
					break;
				} else if (info[i].contains("홍익대")) {
					college = "홍익대";
					break;
				} else if (info[i].contains("연세대") || info[i].contains("Yonsei")) {
					college = "Yonsei";
					break;
				} else if (info[i].contains("중앙대학교")) {
					college = "중앙대";
					break;
				} else if (info[i].contains("충남대학교")) {
					college = "충남대";
					break;
				} else if (info[i].contains("인하대")) {
					college = "인하대";
					break;
				} else if (info[i].contains("낙성대")) {
					college = "낙성대";
					break;
				} else if (info[i].contains("충북대학교")) {
					college = "충북대";
					break;
				} else if (info[i].contains("경희대학교")) {
					college = "경희대";
					break;
				} else if (info[i].contains("공주교육대")) {
					college = "공주교육대";
					break;
				} else if (info[i].contains("숭실대")) {
					college = "숭실대";
					break;
				} else if (info[i].contains("성균관대") || info[i].contains("Sungkyunkwan University")) {
					college = "성균관대";
					break;
				} else if (info[i].contains("서울여자")) {
					college = "서울여자대";
					break;
				} 
				
			}
			
			if (college != "") {
				User.getInstance().setCollege(userID, college);
			}
		}

		System.out.println("UpdateKAISTFriendsListTask end.");
	}
}
