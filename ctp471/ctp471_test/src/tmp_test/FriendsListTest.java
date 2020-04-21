package tmp_test;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class FriendsListTest {

	public static void main(String[] args) throws Exception {

		FriendsListTest http = new FriendsListTest();

		http.sendGet("https://www.facebook.com/ks5050577/friends_all");
		// http.sendGet("https://www.naver.com");
	}

	private void sendGet(String targetUrl) throws Exception {

		URL url = new URL(targetUrl);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();

		con.setRequestMethod("GET"); // optional default is GET
		// add request header
		// con.setRequestProperty("accept",
		// "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
		// con.setRequestProperty("accept-encoding", "gzip, deflate, br");

		con.setRequestProperty("accept", "text/html");
		// con.setRequestProperty("accept-encoding", "gzip");

		// con.setRequestProperty("accept-language",
		// "ko,ko-KR;q=0.9,en-US;q=0.8,en;q=0.7");
		// con.setRequestProperty("cache-control", "max-age=0");
		con.setRequestProperty("cookie",
				"sb=iWx2XvF303mNba4Prq1B17jY; datr=iWx2Xk3Ux86nYmSvpHF-HN6D; c_user=100001957833452; xs=15%3AR1xxUkZf3kmO0A%3A2%3A1584819339%3A16696%3A10142; spin=r.1002005796_b.trunk_t.1587126366_s.1_v.2_; _fbp=fb.1.1587171989782.149943900; act=1587173707634%2F2; presence=EDvF3EtimeF1587181498EuserFA21B01957833452A2EstateFDutF1587181498621CEchF_7bCC; ; fr=002thBpV7SnQrR6GY.AWU4GlioikiZlSPvENgh6RL105I.Bedigb.mR.F6U.0.0.BemoOE.AWU_Y7Jc; wd=150x620");
		// con.setRequestProperty("sec-fetch-dest", "document");
		// con.setRequestProperty("sec-fetch-mode", "navigate");
		// con.setRequestProperty("sec-fetch-site", "same-origin");
		// con.setRequestProperty("sec-fetch-user", "?1");
		// con.setRequestProperty("upgrade-insecure-requests", "1");
		con.setRequestProperty("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
		// con.setRequestProperty("viewport-width", "1920");

		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println("HTTP 응답 코드 : " + responseCode);

		String responseStr = response.toString();

		int fromIndex = 0;
		while (true) {
			int index = responseStr.indexOf("https://www.facebook.com/", fromIndex);

			if (index == -1)
				break;
			fromIndex = index + 20;

			String tmp = responseStr.substring(index, responseStr.indexOf("\"", index));

			System.out.println("[" + index + "][" + tmp + "]");
		}
	}

	private void sendPost(String targetUrl, String parameters) throws Exception {

		URL url = new URL(targetUrl);
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection();

		con.setRequestMethod("POST"); // HTTP POST 메소드 설정
		con.setRequestProperty("User-Agent", "Mozilla/5.0");
		con.setDoOutput(true); // POST 파라미터 전달을 위한 설정

		// Send post request
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(parameters);
		wr.flush();
		wr.close();
		int responseCode = con.getResponseCode();
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

		// print result
		System.out.println("HTTP 응답 코드 : " + responseCode);
		System.out.println("HTTP body : " + response.toString());
	}

}
