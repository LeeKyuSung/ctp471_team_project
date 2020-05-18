package conf;

public class Config {
	public static final String CRAWLING_TARGET = "https://www.facebook.com/";
	public static final int CRAWLING_INTERVAL_TIME = 10000; // 10 seconds between requests to facebook

	public static final String DB_HOST = "211.231.80.215";
	public static final String DB_DBNAME = "CTP471";
	public static final String DB_USER_NAME = "ctp471";
	public static final String DB_PASSWORD = "ctp471";
	
	public static final String[] KAIST_NAME = {"KAIST", "한국과학기술원"};
	public static final String KAIST_NAME_EXCEPT = "한국과학영재학교";
}
