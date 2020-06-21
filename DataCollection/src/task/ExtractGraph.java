package task;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import db.User;

public class ExtractGraph {
	public static void main(String[] args) {
		SimpleDateFormat dayTime = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
		String date = dayTime.format(new Date());
		System.out.println("ExtractGraph Start : " + date);

		HashMap<String, String> tmp = User.getInstance().getFriendsList("");
		int n = tmp.size();
		int[][] G = new int[n][n];
		for (int i = 0; i < n; i++)
			for (int j = 0; j < n; j++)
				G[i][j] = 0;

		int[] seq = new int[n];
		int index = 0;
		for (String key : tmp.keySet())
			seq[index++] = Integer.parseInt(key);

		for (String key : tmp.keySet()) {
			if (tmp.get(key) == null || tmp.get(key).trim().length() == 0)
				continue;
			String[] friendsList = tmp.get(key).split("\\|");

			for (int i = 0; i < friendsList.length; i++) {
				int sequence = Integer.parseInt(friendsList[i]);
				for (int j = 0; j < seq.length; j++) {
					if (sequence == seq[j]) {
						G[Integer.parseInt(key)][j] = 1;
						G[j][Integer.parseInt(key)] = 1;
					}
				}
			}
		}

		File file = new File("result.txt");

		System.out.println("Start");
		try {
			FileWriter fw = new FileWriter(file);

			for (int i = 0; i < n; i++) {
				for (int j = 0; j < n; j++) {
					fw.write(G[i][j] + " ");
				}
				fw.write("\n");
				System.out.println("one line complite");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("ExtractGraph end.");
	}
}
