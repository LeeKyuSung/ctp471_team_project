package crawling;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Coordinates;
import org.openqa.selenium.interactions.Locatable;

import conf.Config;

public class Crawling {
	private static Crawling instance = new Crawling();

	private final String baseUrl = Config.CRAWLING_TARGET;
	 private WebDriver driver; // 1. chrome (at window local test)
	//private FirefoxDriver driver; // 2. firefox (at linux test server)

	private Crawling() {
		// 1. chrome (at window local test)
		 System.setProperty("webdriver.chrome.driver", Config.CRAWLING_CHROME);
		 driver = new ChromeDriver();

		// 2. firefox (at linux test server)
		//System.setProperty("webdriver.gecko.driver", Config.CRAWLING_FIREFOX);
		//driver = new FirefoxDriver();

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
		try {
			Thread.sleep(5000);
		} catch (Exception e) {
			e.printStackTrace();
		}

		long startTime = System.currentTimeMillis();
		// start finding friends
		List<WebElement> friends = driver.findElements(By.xpath("//*[@class='bp9cbjyn ue3kfks5 pw54ja7n uo3d90p7 l82x9zwi n1f8r23x rq0escxv j83agx80 bi6gxh9e discj3wi hv4rvrfc ihqw7lf3 dati1w0a gfomwglr']"));
		int found = friends.size();
		
		while (true) {
			// scroll down
			try {
				Thread.sleep(500);
			} catch (Exception e) {
				e.printStackTrace();
			}
			boolean failFlag = false;
			try {
				Coordinates coordinate = ((Locatable) friends.get(found - 1)).getCoordinates();
				coordinate.onPage();
				coordinate.inViewPort();
			} catch (Exception e) {
				failFlag = true;
			}
			friends = driver.findElements(By.xpath("//*[@class='bp9cbjyn ue3kfks5 pw54ja7n uo3d90p7 l82x9zwi n1f8r23x rq0escxv j83agx80 bi6gxh9e discj3wi hv4rvrfc ihqw7lf3 dati1w0a gfomwglr']"));

			if (friends.size() >= 500) {
				// more then 500 friends collected, stop
				break;
			} else if (friends.size() > found) {
				found = friends.size();
				startTime = System.currentTimeMillis();
			} else if (System.currentTimeMillis() - startTime > 10000) {
				// if cannot find more friends for 10 seconds, stop loop
				break;
			} else if (failFlag) {
				found = friends.size();
			}
		}

		found = friends.size();
		System.out.println("[getFriendsList][" + userID + "] crawled : " + found);

		if (found == 0)
			return null;

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
		
		List<WebElement> tmpInfo;
		while (true) {
			tmpInfo = driver.findElements(By.xpath("//*[@class='fjf4s8hc tu1s4ah4 f7vcsfb0 k3eq2f2k']"));
			if (tmpInfo==null || tmpInfo.size()==0) {
				continue;
			}
			break;
		}
		List<WebElement> informations = tmpInfo.get(0).findElements(By.xpath("//*[@class='oi732d6d ik7dh3pa d2edcug0 qv66sw1b c1et5uql a8c37x1j muag1w35 enqfppq2 jq4qci2q a3bd9o3v knj5qynh oo9gr5id hzawbc8m']"));

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
