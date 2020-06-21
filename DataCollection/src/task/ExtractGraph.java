package task;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.jgrapht.alg.scoring.AlphaCentrality;
import org.jgrapht.alg.scoring.ClosenessCentrality;
import org.jgrapht.alg.scoring.HarmonicCentrality;
import org.jgrapht.alg.scoring.ClusteringCoefficient;
import org.jgrapht.alg.scoring.Coreness;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

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

		SimpleGraph<String, DefaultEdge> g = new SimpleGraph<>(DefaultEdge.class);
		for (int i = 0; i < 50000; i++) {
			g.addVertex(i + "");
		}
		for (String key : tmp.keySet()) {
			String fl = tmp.get(key);
			if (fl == null || fl.trim().length() == 0)
				continue;
			String[] friendsList = fl.split("\\|");

			for (int i = 0; i < friendsList.length; i++) {
				String asdf = friendsList[i].trim();
				if (g.containsVertex(key) && g.containsVertex(asdf) && !g.containsEdge(key, asdf) && !key.equals(asdf)) {
					g.addEdge(key, asdf);
					g.addEdge(asdf, key);
				} else {
					System.out.print("Wrong!");
				}
			}
		}
		System.out.println();
		System.out.println("ExtractGraph end.");

		String[] college = { "KAIST", "Seoul", "Korea University", "Yonsei", "성균관대", "한양대", "경희대", "충북대", "홍익대", "Ewha University" };
		
		System.out.println("Clustering Coefficient");
		ClusteringCoefficient c = new ClusteringCoefficient(g);
		for (int i = 0; i < college.length; i++) {
			int cnt = 0;
			double sum = 0;
			HashMap<String, String> user = User.getInstance().getFriendsList(college[i]);
			for (String key : user.keySet()) {
				cnt++;
				sum += c.getVertexScore(key);
			}
			System.out.println(college[i] + " : " + sum / cnt);
		}
		
		System.out.println("Coreness");
		Coreness c2 = new Coreness(g);
		for (int i = 0; i < college.length; i++) {
			int cnt = 0;
			double sum = 0;
			HashMap<String, String> user = User.getInstance().getFriendsList(college[i]);
			for (String key : user.keySet()) {
				cnt++;
				sum += c2.getVertexScore(key);
			}
			System.out.println(college[i] + " : " + sum / cnt);
		}
		
		System.out.println("AlphaCentrality");
		AlphaCentrality c3 = new AlphaCentrality(g);
		for (int i = 0; i < college.length; i++) {
			int cnt = 0;
			double sum = 0;
			HashMap<String, String> user = User.getInstance().getFriendsList(college[i]);
			for (String key : user.keySet()) {
				cnt++;
				sum += c3.getVertexScore(key);
			}
			System.out.println(college[i] + " : " + sum / cnt);
		}
		
	}
}
