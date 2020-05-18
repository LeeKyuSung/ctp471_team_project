package crawling_deprecated;

import java.util.HashMap;
import java.util.HashSet;

import conf.Config;
import db.InvalidID;

public class CrawlingUtil {
	private static CrawlingUtil instance = new CrawlingUtil();

	private HashMap<String, String> httpOptions;
	private HashSet<String> invalidIDSet;
	private int sleepTime = Config.CRAWLING_INTERVAL_TIME;

	private CrawlingUtil() {
		httpOptions = new HashMap<String, String>();
		httpOptions.put("accept", "text/html");
		httpOptions.put("user-agent",
				"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/80.0.3987.163 Safari/537.36");
		httpOptions.put("cookie",
				"sb=iWx2XvF303mNba4Prq1B17jY; datr=iWx2Xk3Ux86nYmSvpHF-HN6D; c_user=100001957833452; xs=15%3AR1xxUkZf3kmO0A%3A2%3A1584819339%3A16696%3A10142; spin=r.1002005796_b.trunk_t.1587126366_s.1_v.2_; _fbp=fb.1.1587171989782.149943900; act=1587173707634%2F2; presence=EDvF3EtimeF1587181498EuserFA21B01957833452A2EstateFDutF1587181498621CEchF_7bCC; ; fr=002thBpV7SnQrR6GY.AWU4GlioikiZlSPvENgh6RL105I.Bedigb.mR.F6U.0.0.BemoOE.AWU_Y7Jc; wd=150x620");
		// TODO check cookie need? change cookie when failed

		invalidIDSet = InvalidID.getInstance().getInvalidIDSet();

		sleepTime = 0;
	}

	public static CrawlingUtil getInstance() {
		return instance;
	}

	public String getResponse(String targetUrl) {
		try {
			Thread.sleep(sleepTime);
		} catch (Exception e) {
			System.out.println("[ERROR][CrawlingUtil][getResponse] " + e.getMessage());
			e.printStackTrace();
		}

		return HTTPUtil.sendGet(targetUrl, httpOptions);
	}

	public boolean isInvalidID(String userID) {
		
		return invalidIDSet.contains(userID);
	}
}
