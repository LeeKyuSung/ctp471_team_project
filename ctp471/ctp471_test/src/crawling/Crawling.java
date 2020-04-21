package crawling;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

import conf.Config;
import util.HTTPConnection;

public class Crawling {

	private final String preUrl = Config.CRAWLING_TARGET;
	private final String ignoreID = "settings|notifications|photo.php|friends|profile.php|pages|friendship";
	// TODO if fail, change Valid to N at DB

	public Crawling() {
		// TODO options... etc
	}

	public String[] getFriendsList(String userID) {
		String targetUrl = preUrl + userID + "/friends_all";
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("accept", "text/html");
		options.put("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
		options.put("cookie",
				"sb=iWx2XvF303mNba4Prq1B17jY; datr=iWx2Xk3Ux86nYmSvpHF-HN6D; c_user=100001957833452; xs=15%3AR1xxUkZf3kmO0A%3A2%3A1584819339%3A16696%3A10142; spin=r.1002005796_b.trunk_t.1587126366_s.1_v.2_; _fbp=fb.1.1587171989782.149943900; act=1587173707634%2F2; presence=EDvF3EtimeF1587181498EuserFA21B01957833452A2EstateFDutF1587181498621CEchF_7bCC; ; fr=002thBpV7SnQrR6GY.AWU4GlioikiZlSPvENgh6RL105I.Bedigb.mR.F6U.0.0.BemoOE.AWU_Y7Jc; wd=150x620");
		// TODO check cookie need? change cookie when failed

		String responseStr = HTTPConnection.sendGet(targetUrl, options);

		HashSet<String> result = new HashSet<String>();
		int fromIndex = 0;
		while (true) {
			int index = responseStr.indexOf("https://www.facebook.com/", fromIndex);

			if (index == -1)
				break;
			fromIndex = index + 20;

			String tmp = responseStr.substring(index + 25, responseStr.indexOf("\"", index));
			if (tmp.contains("?"))
				tmp = tmp.substring(0, tmp.indexOf('?'));
			if (tmp.contains("/"))
				tmp = tmp.substring(0, tmp.indexOf('/'));

			if (tmp.equals(userID))
				continue;
			else if (tmp.equals(""))
				continue;
			else if (ignoreID.contains(tmp))
				continue;
			// TODO move ignore to config or anywhere

			if (!result.contains(tmp))
				result.add(tmp);
		}

		String[] ret = new String[result.size()];
		int cnt = 0;
		Iterator<String> iter = result.iterator();
		while(iter.hasNext()) {
			ret[cnt++] = iter.next();
		}
		return ret;
	}

	public String getUserInfo(String userID) {
		String targetUrl = preUrl + userID + "/about";
		HashMap<String, String> options = new HashMap<String, String>();
		options.put("accept", "text/html");
		options.put("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
		options.put("cookie",
				"sb=iWx2XvF303mNba4Prq1B17jY; datr=iWx2Xk3Ux86nYmSvpHF-HN6D; c_user=100001957833452; xs=15%3AR1xxUkZf3kmO0A%3A2%3A1584819339%3A16696%3A10142; spin=r.1002005796_b.trunk_t.1587126366_s.1_v.2_; _fbp=fb.1.1587171989782.149943900; act=1587173707634%2F2; presence=EDvF3EtimeF1587181498EuserFA21B01957833452A2EstateFDutF1587181498621CEchF_7bCC; ; fr=002thBpV7SnQrR6GY.AWU4GlioikiZlSPvENgh6RL105I.Bedigb.mR.F6U.0.0.BemoOE.AWU_Y7Jc; wd=150x620");
		// TODO check cookie need? change cookie when failed

		String responseStr = HTTPConnection.sendGet(targetUrl, options);
		if ("".equals(responseStr) || null==responseStr)
			return "ERROR";

		int index = responseStr.indexOf("script type=\"application/ld+json\"");
		String tmp = responseStr.substring(index + 34, responseStr.indexOf("</script>", index));
		// TODO change korean, add json parser

		return tmp;
	}
}
