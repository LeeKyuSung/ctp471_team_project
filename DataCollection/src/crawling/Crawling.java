package crawling;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;

import conf.Config;

public class Crawling {
	private static Crawling instance = new Crawling();

	private final String baseUrl = Config.CRAWLING_TARGET;
	// private WebDriver driver; // 1. chrome (at window local test)
	private FirefoxDriver driver; // 2. firefox (at linux test server)

	private Crawling() {
		// 1. chrome (at window local test)
		// System.setProperty("webdriver.chrome.driver", Config.CRAWLING_CHROME);
		// driver = new ChromeDriver();

		// 2. firefox (at linux test server)
		System.setProperty("webdriver.gecko.driver", Config.CRAWLING_FIREFOX);
		driver = new FirefoxDriver();

		// login to facebook
		driver.get(baseUrl);
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// login if not logined
		if (!"Facebook".equals(driver.getTitle())) {
			WebElement id = driver.findElement(By.id("email"));
			WebElement pwd = driver.findElement(By.id("pass"));
			id.sendKeys(Config.CRAWLING_FACEBOOK_ID);
			pwd.sendKeys(Config.CRAWLING_FACEBOOK_PWD);
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}

			pwd.sendKeys(Keys.RETURN);
			try {
				Thread.sleep(5000);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Crawling getInstance() {
		return instance;
	}

	public String[] getFriendsList(String userID) {
		System.out.println("[getFriendsList][" + userID + "][START]");

		String targetUrl = baseUrl + userID + "/friends_all";
		driver.get(targetUrl);

		WebElement ele = driver.findElements(By.xpath("//*[@class='_3c_ _3s-']")).get(0);
		if (!"모든 친구".equals(ele.getAttribute("name"))) {
			// case that the user don't open friends list
			return null;
		}

		long startTime = System.currentTimeMillis();
		// start finding friends
		List<WebElement> friends = driver.findElements(By.xpath("//*[@class='fsl fwb fcb']"));
		int found = friends.size();
		while (true) {
			// scroll down
			Coordinates coordinate = ((Locatable) friends.get(found - 1)).getCoordinates();
			coordinate.onPage();
			coordinate.inViewPort();
			friends = driver.findElements(By.xpath("//*[@class='fsl fwb fcb']"));

			if (friends.size() > found) {
				found = friends.size();
				startTime = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - startTime > 10000) {
				// if cannot find more friends for 10 seconds, stop loop
				break;
			}
		}

		found = friends.size();
		System.out.println("[getFriendsList][" + userID + "] crawled : " + found);

		HashSet<String> friendsSet = new HashSet<String>();
		for (int i = 0; i < found; i++) {
			String friendID = friends.get(i).getAttribute("innerHTML");

			int index = friendID.indexOf("https://www.facebook.com/");
			if (index == -1)
				continue;

			friendID = friendID.substring(index + 25, friendID.indexOf("\"", index));
			if (friendID.contains("?"))
				friendID = friendID.substring(0, friendID.indexOf('?'));
			if (friendID.contains("/"))
				friendID = friendID.substring(0, friendID.indexOf('/'));

			if (friendID.contentEquals("profile.php") || friendID.equals(""))
				continue;
			if (friendsSet.contains(friendID))
				continue;

			friendsSet.add(friendID);
		}

		String[] ret = new String[friendsSet.size()];
		int cnt = 0;
		Iterator<String> iter = friendsSet.iterator();
		while (iter.hasNext()) {
			ret[cnt++] = iter.next();
		}

		System.out.println("[getFriendsList][" + userID + "] final friends num : " + friendsSet.size());
		System.out.println("[getFriendsList][" + userID + "][END]");
		return ret;
	}

	public String[] getUserInfo(String userID) {
		System.out.println("[getUserInfo][" + userID + "][START]");

		String targetUrl = baseUrl + userID + "/about";
		driver.get(targetUrl);

		List<WebElement> informations = driver.findElements(By.xpath("//*[@class='_c24 _50f4']"));

		int found = informations.size();
		String[] ret = new String[found];
		for (int i = 0; i < found; i++) {
			ret[i] = informations.get(i).getText();
		}

		System.out.println("[getUserInfo][" + userID + "][END]");
		return ret;
	}

	public void close() {
		driver.close();
	}
}
