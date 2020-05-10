package crawling;

import java.util.HashSet;
import java.util.Iterator;

import conf.Config;

public class Crawling {
	private final String preUrl = Config.CRAWLING_TARGET;
	private CrawlingUtil crawlingUtil = CrawlingUtil.getInstance();

	public String[] getFriendsList(String userID) {
		String targetUrl = preUrl + userID + "/friends_all";
		String responseStr = crawlingUtil.getResponse(targetUrl);
		
		// start parsing response
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
			else if (crawlingUtil.isInvalidID(tmp))
				continue;

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
		String responseStr = crawlingUtil.getResponse(targetUrl);
		if ("".equals(responseStr) || null==responseStr)
			return "ERROR";

		int index = responseStr.indexOf("script type=\"application/ld+json\"");
		String tmp = responseStr.substring(index + 34, responseStr.indexOf("</script>", index));
		// TODO change korean, add json parser

		return tmp;
	}
}
