import java.util.HashMap;

import db.User;
import processing.core.PApplet;

public class ProcessingTest extends PApplet {
	private int width = 1200;
	private int height = 800;

	private int maxFriendsNum = 0;
	Node[] node;
	
	// The argument passed to main must match the class name
	public static void main(String[] args) {
		PApplet.main("ProcessingTest");
	}

	// method used only for setting the size of the window
	public void settings() {
		size(width, height);
	}

	// identical use to setup in Processing IDE except for size()
	public void setup() {
		background(0);

		HashMap<Integer, String> userMap = User.getInstance().getUserMap();

		node = new Node[userMap.size()];

		for (int i = 0; i < node.length; i++) {
			node[i] = new Node();
			node[i].x = 100 + (int) ((width - 200) * Math.random());
			node[i].y = 100 + (int) ((height - 200) * Math.random());
		}

		int cnt = 0;
		for (int key : userMap.keySet()) {
			node[cnt].seq = key;
			node[cnt++].friendsStr = userMap.get(key);
		}

		for (int i = 0; i < node.length; i++) {
			
			String[] friendsList = node[i].friendsStr.split("\\|");
			Node[] friends = new Node[friendsList.length];
			node[i].friendsNum = friendsList.length;

			if (maxFriendsNum < friendsList.length)
				maxFriendsNum = friendsList.length;

			for (int j=0; j<friends.length; j++) {
				for (int k=0 ;k<node.length; k++) {
					if (friendsList[j].equals(node[k].seq + "")) {
						friends[j] = node[k];
					}
				}
			}
			
			node[i].friends = friends;
		}


		stroke(40);
		for (int i=0; i<node.length; i++) {
			Node[] friend = node[i].friends;
			for (int j = 0; j < friend.length; j++) {
				if (friend[j] != null)
					line(node[i].x, node[i].y, friend[j].x, friend[j].y);
			}
		}
	}

	// identical use to draw in Prcessing IDE
	public void draw() {
		noStroke();
		for (int i = 0; i < node.length; i++) {
			float gray = (float) (255.0 * ((float)node[i].friendsNum / (float)maxFriendsNum));
			fill(gray);
			circle(node[i].x, node[i].y, 10);
		}
	}
}
