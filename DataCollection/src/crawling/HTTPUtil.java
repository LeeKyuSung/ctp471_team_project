package crawling;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

public class HTTPUtil {

	public static String sendGet(String targetUrl, HashMap<String, String> options) {
		try {
			URL url = new URL(targetUrl);
			HttpURLConnection con = (HttpURLConnection) url.openConnection();

			con.setRequestMethod("GET");

			for (String key : options.keySet()) {
				con.setRequestProperty(key, options.get(key));
			}

			int responseCode = con.getResponseCode();
			if (responseCode != 200) {
				System.out.println("[ERROR][HTTPConnection][sendGet] responseCode not 200 : " + responseCode);
				return null;
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			return response.toString();
		} catch (Exception e) {
			System.out.println("[ERROR][HTTPConnection][sendGet] " + e.getMessage());
			e.printStackTrace();
		}

		return null;
	}
}
